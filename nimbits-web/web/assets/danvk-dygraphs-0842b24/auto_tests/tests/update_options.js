/*
 * Copyright (c) 2010 Nimbits Inc.
 *
 * http://www.nimbits.com
 *
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, eitherexpress or implied. See the License for the specific language governing permissions and limitations under the License.
 */

/**
 * @fileoverview Tests for the updateOptions function.
 * @author antrob@google.com (Anthony Robledo)
 */
var UpdateOptionsTestCase = TestCase("update-options");

UpdateOptionsTestCase.prototype.opts = {
  width: 480,
  height: 320,
};

UpdateOptionsTestCase.prototype.data = "X,Y1,Y2\n" +
  "2011-01-01,2,3\n" +
  "2011-02-02,5,3\n" +
  "2011-03-03,6,1\n" +
  "2011-04-04,9,5\n" +
  "2011-05-05,8,3\n";

UpdateOptionsTestCase.prototype.setUp = function() {
  document.body.innerHTML = "<div id='graph'></div><div id='labels'></div>";
};

UpdateOptionsTestCase.prototype.tearDown = function() {
};

/*
 * Tweaks the dygraph so it sets g._testDrawCalled to true when internal method
 * drawGraph_ is called. Call unWrapDrawGraph when done with this.
 */
UpdateOptionsTestCase.prototype.wrapDrawGraph = function(g) {
  g._testDrawCalled = false;
  g._oldDrawGraph = g.drawGraph_;
  g.drawGraph_ = function() {
    g._testDrawCalled = true;
    g._oldDrawGraph.call(g);
  }
}

/*
 * See wrapDrawGraph
 */
UpdateOptionsTestCase.prototype.unwrapDrawGraph = function(g) {
  g.drawGraph_ = g._oldDrawGraph;
}

UpdateOptionsTestCase.prototype.testStrokeAll = function() {
  var graphDiv = document.getElementById("graph");
  var graph = new Dygraph(graphDiv, this.data, this.opts);
  var updatedOptions = { };

  updatedOptions['strokeWidth'] = 3;

  // These options will allow us to jump to renderGraph_()
  // drawGraph_() will be skipped.
  this.wrapDrawGraph(graph);
  graph.updateOptions(updatedOptions);
  this.unwrapDrawGraph(graph);
  assertFalse(graph._testDrawCalled);
};

UpdateOptionsTestCase.prototype.testStrokeSingleSeries = function() {
  var graphDiv = document.getElementById("graph");
  var graph = new Dygraph(graphDiv, this.data, this.opts);
  var updatedOptions = { };
  var optionsForY1 = { };

  optionsForY1['strokeWidth'] = 3;
  updatedOptions['Y1'] = optionsForY1;

  // These options will allow us to jump to renderGraph_()
  // drawGraph_() will be skipped.
  this.wrapDrawGraph(graph);
  graph.updateOptions(updatedOptions);
  this.unwrapDrawGraph(graph);
  assertFalse(graph._testDrawCalled);
};

UpdateOptionsTestCase.prototype.testSingleSeriesRequiresNewPoints = function() {
  var graphDiv = document.getElementById("graph");
  var graph = new Dygraph(graphDiv, this.data, this.opts);
  var updatedOptions = { };
  var optionsForY1 = { };
  var optionsForY2 = { };

  // This will not require new points.
  optionsForY1['strokeWidth'] = 2;
  updatedOptions['Y1'] = optionsForY1;

  // This will require new points.
  optionsForY2['stepPlot'] = true;
  updatedOptions['Y2'] = optionsForY2;

  // These options will not allow us to jump to renderGraph_()
  // drawGraph_() must be called
  this.wrapDrawGraph(graph);
  graph.updateOptions(updatedOptions);
  this.unwrapDrawGraph(graph);
  assertTrue(graph._testDrawCalled);
};

UpdateOptionsTestCase.prototype.testWidthChangeNeedsNewPoints = function() {
  var graphDiv = document.getElementById("graph");
  var graph = new Dygraph(graphDiv, this.data, this.opts);
  var updatedOptions = { };

  // This will require new points.
  updatedOptions['width'] = 600;

  // These options will not allow us to jump to renderGraph_()
  // drawGraph_() must be called
  this.wrapDrawGraph(graph);
  graph.updateOptions(updatedOptions);
  this.unwrapDrawGraph(graph);
  assertTrue(graph._testDrawCalled);
};

// Test https://github.com/danvk/dygraphs/issues/87
UpdateOptionsTestCase.prototype.testUpdateLabelsDivDoesntInfiniteLoop = function() {
  var graphDiv = document.getElementById("graph");
  var labelsDiv = document.getElementById("labels");
  var graph = new Dygraph(graphDiv, this.data, this.opts);
  graph.updateOptions({labelsDiv : labelsDiv});
}

// Test https://github.com/danvk/dygraphs/issues/247
UpdateOptionsTestCase.prototype.testUpdateColors = function() {
  var graphDiv = document.getElementById("graph");
  var graph = new Dygraph(graphDiv, this.data, this.opts);

  var defaultColors = ["rgb(0,128,0)", "rgb(0,0,128)"];
  assertEquals(["rgb(0,128,0)", "rgb(0,0,128)"], graph.getColors());

  var colors1 = [ "#aaa", "#bbb" ];
  graph.updateOptions({ colors: colors1 });
  assertEquals(colors1, graph.getColors());

  // extra colors are ignored until you add additional data series.
  var colors2 = [ "#aaa", "#bbb", "#ccc" ];
  graph.updateOptions({ colors: colors2 });
  assertEquals(colors1, graph.getColors());

  graph.updateOptions({ file:
      "X,Y1,Y2,Y3\n" +
      "2011-01-01,2,3,4\n" +
      "2011-02-02,5,3,2\n"
  });
  assertEquals(colors2, graph.getColors());

  graph.updateOptions({ colors: null, file: this.data });
  assertEquals(defaultColors, graph.getColors());
}
