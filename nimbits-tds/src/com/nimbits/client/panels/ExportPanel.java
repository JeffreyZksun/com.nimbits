package com.nimbits.client.panels;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nimbits.client.enums.ExportType;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.Const;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.point.PointName;
import com.nimbits.client.service.datapoints.PointService;
import com.nimbits.client.service.datapoints.PointServiceAsync;

import java.util.Map;

import static com.google.gwt.user.client.Window.alert;


public class ExportPanel extends LayoutContainer {

    VerticalPanel vp;

    private Map<PointName, Point> points;

    public ExportPanel(Map<PointName, Point> points) {
        this.points = points;
    }

    @Override
    protected void onRender(final Element parent, final int index) {
        super.onRender(parent, index);
        setLayout(new FillLayout());
        FormData formdata = new FormData("-20");
        vp = new VerticalPanel();
        vp.setBorders(false);

        //vp.setSpacing(10);
        createForm();
        add(vp);
    }

    private void createForm() {
        //  String url = GWT.getModuleBaseURL() +  "export";
        final FormPanel panel = new FormPanel();
        //  panel.setAction(url);
        //  panel.setMethod(FormPanel.Method.POST);
        //   panel.setEncoding(FormPanel.Encoding.MULTIPART);
        final RadioGroup option = new RadioGroup();
        final Radio csvSeparateColumns = new Radio();
        //  final Radio descriptiveStatistics = new Radio();
        // final Radio possibleContinuation = new Radio();

        option.setFieldLabel("Report Type");
        option.setOrientation(Style.Orientation.VERTICAL);

        //  descriptiveStatistics.setBoxLabel("Descriptive Statistics (Beta)");
        // descriptiveStatistics.setValue(true);
         final Radio dataView = new Radio();
        dataView.setBoxLabel("Current Status Report");
        dataView.setValue(true);

        csvSeparateColumns.setValue(true);
        csvSeparateColumns.setBoxLabel("Export to Spreadsheet (CSV with separate columns)");
        // possibleContinuation.setBoxLabel("Calculate a Possible Continuation (beta)");
        panel.setLayout(new FitLayout());
        panel.setFrame(false);
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        //  panel.setWidth(480);
        // panel.setHeight(360);

        panel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {
                alert(be.getResultHtml());
            }
        });

        Button submit = new Button("Submit");

        submit.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                PointServiceAsync pointService = GWT.create(PointService.class);
                final MessageBox box = MessageBox.wait("Progress",
                        "Generating your report, please wait", "Thinking about it...");
                box.show();
                try {
                    final ExportType exportType;

                    if (csvSeparateColumns.getValue()) {
                        exportType = ExportType.csvSeparateColumns;
                     } else if (dataView.getValue()) {
                              exportType = ExportType.currentStatusReport;

                        //  } else if (possibleContinuation.getValue()) {
                        //      exportType = ExportType.possibleContinuation;
                    } else {
                        exportType = ExportType.csvSeparateColumns;
                    }


                    pointService.exportData(points, exportType, new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable e) {
                            GWT.log(e.getMessage(), e);
                            box.close();
                            MessageBox.alert("Error", e.getMessage(), null);
                        }

                        @Override
                        public void onSuccess(final String result) {
                            final String url = GWT.getModuleBaseURL() + "export?" + Const.PARAM_BLOB_KEY
                                    + "=" + result;
                            box.close();
                            Window.open(url, "Export", "");
                        }
                    });
                } catch (NimbitsException e) {
                    box.close();
                    MessageBox.alert("Error", e.getMessage(), null);
                    GWT.log(e.getMessage(), e);
                }

            }
        });
        String s = "<p>Use the options below to export your data into a report. Please disable any pop-up blockers for this site, as your report will open in a new window. </p>";

        if (points.size() > 1) {
            s += "<p> Please note that you have selected more than one data point to report on. Currently, the statistics reports will only use the first point to analyse.</p>";
        }

        Html h = new Html(s);

        vp.add(h);
        option.add(csvSeparateColumns);
        //  option.add(descriptiveStatistics);
        //  option.add(possibleContinuation);
        panel.add(option);
        panel.add(submit);
        vp.add(panel);
    }

}