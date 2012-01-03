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

package com.nimbits.server.task;

import com.google.gson.Gson;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.enums.ProtectionLevel;
import com.nimbits.client.model.Const;
import com.nimbits.client.model.category.Category;
import com.nimbits.client.model.point.PointModel;
import com.nimbits.client.model.user.User;
import com.nimbits.server.common.ServerInfoImpl;
import com.nimbits.server.core.CoreFactory;
import com.nimbits.server.gson.GsonFactory;
import com.nimbits.server.point.PointTransactionsFactory;
import com.nimbits.server.pointcategory.CategoryServiceFactory;
import com.nimbits.server.user.UserTransactionFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class PointMaintTask extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(PointMaintTask.class.getName());


    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

        final Gson gson = GsonFactory.getInstance();
        resp.setContentType(Const.CONTENT_TYPE_HTML);

        final String pointJson = req.getParameter(Const.PARAM_POINT);
        final PointModel p = gson.fromJson(pointJson, PointModel.class);
        // final UserContext context = gson.fromJson(jsonContext, UserContextImpl.class);


        User n = null;  // pm.getObjectById(NimbitsUser.class, p.getUserFK());//UserDAL.getNimbitsUserByID(p.getUser());
        try {
            n = UserTransactionFactory.getInstance().getNimbitsUserByID(p.getUserFK());
        } catch (Exception e) {
            log.severe(e.getMessage());
            log.severe("Error getting user with id: " + p.getUserFK());
            log.severe("I'd like to delete point: " + p.getName().getValue() + "   " + p.getId());

        }


        if (n != null) {
            PointTransactionsFactory.getInstance(null).checkPoint(req, n.getEmail(), p);

            log.info("reporting point to core:" + p.getName().getValue());
            String url = ServerInfoImpl.getFullServerURL(req);
            CoreFactory.getInstance().reportUpdateToCore(url, pointJson, EntityType.point);
            Category category = CategoryServiceFactory.getInstance().getCategory(n, p.getCatID());

            if (category.getProtectionLevel() != null && category.getProtectionLevel().equals(ProtectionLevel.everyone)) {
                String j = GsonFactory.getInstance().toJson(category);
                CoreFactory.getInstance().reportUpdateToCore(url, j, EntityType.category);

            }

        } else {
//            try {
//                // delete me context.addTrace("Deleting point" + p.getName().getValue());
//
//            //    PointServiceFactory.getInstance().deletePoint(p);
//            } catch (NimbitsException e) {
//                log.severe(e.getMessage());
//            }
            log.severe("Point Maint Task could not find user - would like to delete point :" + p.getName().getValue());


        }


    }


}
