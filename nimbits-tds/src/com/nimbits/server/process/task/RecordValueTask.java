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

package com.nimbits.server.process.task;


import com.nimbits.client.enums.EntityType;
import com.nimbits.client.enums.Parameters;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.entity.Entity;
import com.nimbits.client.model.entity.EntityModel;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.user.User;
import com.nimbits.client.model.user.UserModel;
import com.nimbits.client.model.value.Value;
import com.nimbits.client.model.value.impl.ValueModel;
import com.nimbits.server.admin.logging.LogHelper;
import com.nimbits.server.gson.GsonFactory;
import com.nimbits.server.transactions.service.calculation.CalculationServiceFactory;
import com.nimbits.server.transactions.service.entity.EntityServiceFactory;
import com.nimbits.server.transactions.service.intelligence.IntelligenceServiceFactory;
import com.nimbits.server.transactions.service.subscription.SubscriptionServiceFactory;
import com.nimbits.server.transactions.service.summary.SummaryServiceFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecordValueTask extends HttpServlet {


    private static final long serialVersionUID = 2L;

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) {

        processRequest(req);


    }

    protected void processRequest(ServletRequest req) {

        final String userJson = req.getParameter(Parameters.pointUser.getText());
        final String pointJson = req.getParameter(Parameters.pointJson.getText());
        final String valueJson = req.getParameter(Parameters.valueJson.getText());
        final Entity entity = GsonFactory.getInstance().fromJson(pointJson, EntityModel.class);
        final Value value = GsonFactory.getInstance().fromJson(valueJson, ValueModel.class);
        final User u = GsonFactory.getInstance().fromJson(userJson, UserModel.class);

        try {

            final Point point = entity instanceof Point
                    ? (Point) entity
                    : (Point) EntityServiceFactory.getInstance().getEntityByKey(u, entity.getKey(), EntityType.point).get(0);

            if (point.isIdleAlarmOn() && point.getIdleAlarmSent()) {
                point.setIdleAlarmSent(false);
                EntityServiceFactory.getInstance().addUpdateEntity(u,  point);
            }

            //triggers
            CalculationServiceFactory.getInstance().processCalculations(u, point, value);
            IntelligenceServiceFactory.getInstance().processIntelligence(u, point);
            SummaryServiceFactory.getInstance().processSummaries(u, point);



            SubscriptionServiceFactory.getInstance().processSubscriptions(u,  point, value);

        } catch (NimbitsException e) {
            LogHelper.logException(this.getClass(), e);
        }
    }


}