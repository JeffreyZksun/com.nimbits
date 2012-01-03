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

package com.nimbits.server.login;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.LoginInfo;
import com.nimbits.client.model.common.CommonFactoryLocator;
import com.nimbits.client.model.email.EmailAddress;
import com.nimbits.client.service.LoginService;
import com.nimbits.server.user.UserTransactionFactory;

import java.util.Date;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public LoginInfo login(final String requestUri) throws NimbitsException {
        final UserService userService = UserServiceFactory.getUserService();
        final User user = userService.getCurrentUser();
        final LoginInfo loginInfo = new LoginInfo();

        if (user != null) {
            final EmailAddress internetAddress = CommonFactoryLocator.getInstance().createEmailAddress(user.getEmail());

            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(internetAddress);
            loginInfo.setNickname(user.getNickname());
            loginInfo.setUserAdmin(userService.isUserAdmin());

            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
            com.nimbits.client.model.user.User u = UserTransactionFactory.getInstance().getNimbitsUser(internetAddress);

            if (u == null) {
                u = UserTransactionFactory.getInstance().createNimbitsUser(internetAddress);
            }
            UserTransactionFactory.getInstance().updateLastLoggedIn(u, new Date());
            loginInfo.setUser(u);
            // A user has logged in through google auth - this creates the user

        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
        }
        return loginInfo;
    }

}