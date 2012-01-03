/*
 * Copyright (c) 2010 Tonic Solutions LLC.
 *
 * http://www.nimbits.com
 *
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 3.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.client.panels;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.nimbits.client.enums.ProtectionLevel;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.icons.Icons;
import com.nimbits.client.model.Const;
import com.nimbits.client.model.category.Category;
import com.nimbits.client.service.category.CategoryService;
import com.nimbits.client.service.category.CategoryServiceAsync;


/**
 * Created by bsautner
 * User: benjamin
 * Date: 5/31/11
 * Time: 1:05 PM
 */
class CategoryPropertyPanel extends NavigationEventProvider {

    private final Category category;

    //    private final Icons ICONS = GWT.create(Icons.class);
    private final boolean readOnly;
    private final RadioGroup radioGroup = new RadioGroup();
    private final Radio radioProtection0 = new Radio();
    private final Radio radioProtection1 = new Radio();
    private final Radio radioProtection2 = new Radio();
    private final TextArea description = new TextArea();


    CategoryPropertyPanel(final Category c, final boolean readOnly) {
        this.category = c;
        this.readOnly = readOnly;
    }

    private VerticalPanel vp;

    private FormData formData;

    @Override
    protected void onRender(final Element parent, final int index) {
        super.onRender(parent, index);
        ContentPanel p = new ContentPanel();
        p.setHeaderVisible(false);
        p.setTopComponent(mainToolBar());
        p.setFrame(false);
        p.setBodyBorder(false);
        p.setLayout(new FillLayout());
        //p.setHeight(500);

        formData = new FormData("-20");
        vp = new VerticalPanel();
        vp.setLayout(new FillLayout());

        // vp.setSpacing(10);
        createForm();

        String url = "http://" + com.google.gwt.user.client.Window.Location.getHostName() + "?" + Const.PARAM_UUID + "=" + category.getUUID();


        Html h = new Html("<p>Link:</p><br>" +
                " <A href =\"" + url + "\">" + url + "</a>");
        vp.add(h);
        p.add(vp);

        add(p);
    }

    private void createForm() {
        FormPanel simple = new FormPanel();

        simple.setHeaderVisible(false);
        simple.setFrame(false);
        simple.setBodyBorder(false);
        simple.setLayout(new FillLayout());
        //  simple.setWidth(480);


        radioProtection0.setBoxLabel("Only Me");
        radioProtection0.setValue(category.getProtectionLevel().equals(ProtectionLevel.onlyMe));


        radioProtection1.setBoxLabel("My Connections");
        radioProtection1.setValue(category.getProtectionLevel().equals(ProtectionLevel.onlyConnection));


        radioProtection2.setBoxLabel("Anyone");
        radioProtection2.setValue(category.getProtectionLevel().equals(ProtectionLevel.everyone));


        radioGroup.setFieldLabel("Who can view");

        radioGroup.add(radioProtection0);
        radioGroup.add(radioProtection1);
        radioGroup.add(radioProtection2);
        simple.add(radioGroup, formData);


        description.setPreventScrollbars(true);
        description.setValue(category.getDescription());
        description.setFieldLabel("Description");
        simple.add(description, new FormData("-20"));
        description.setSize("400", "100");

        simple.add(description);

        vp.add(simple);
    }

    ToolBar mainToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.setHeight("");


        final Button buttonSave = new Button("Save");

        buttonSave.setIcon(AbstractImagePrototype.create(Icons.INSTANCE.SaveAll()));
        buttonSave.addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent ce) {

                try {
                    save();
                } catch (NimbitsException ignored) {

                }
            }
        });


        toolBar.add(buttonSave);


        return toolBar;


    }

    private void save() throws NimbitsException {

        final CategoryServiceAsync serviceAsync = GWT.create(CategoryService.class);
        if (radioProtection0.getValue()) {
            category.setProtectionLevel(ProtectionLevel.onlyMe);
        } else if (radioProtection1.getValue()) {
            category.setProtectionLevel(ProtectionLevel.onlyConnection);
        } else if (radioProtection2.getValue()) {
            category.setProtectionLevel(ProtectionLevel.everyone);
        }

        category.setDescription(description.getValue());

        serviceAsync.updateCategory(category, new AsyncCallback<Category>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Category diagram) {
                MessageBox.info("Category Settings", "Category Updated", null);

            }
        });


    }
}