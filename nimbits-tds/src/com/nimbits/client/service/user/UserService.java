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

package com.nimbits.client.service.user;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.nimbits.client.enums.AuthLevel;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.connection.ConnectionRequest;
import com.nimbits.client.model.email.EmailAddress;
import com.nimbits.client.model.user.User;

import java.util.List;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {


    void sendConnectionRequest(final EmailAddress email) throws NimbitsException;
    List<ConnectionRequest> getPendingConnectionRequests(final EmailAddress email) throws NimbitsException;
    void connectionRequestReply(final EmailAddress targetEmail, final EmailAddress RequestorEmail, final Long key, final boolean accepted) throws NimbitsException;
    User getAppUserUsingGoogleAuth() throws NimbitsException;
   // String getSecret() throws NimbitsException;
    User getUserByKey(String key, AuthLevel authLevel) throws NimbitsException;
    List<User> getConnectionRequests(List<String> connections) throws NimbitsException;
    User login(String requestUri) throws NimbitsException;
    Integer getQuota() throws NimbitsException;
}
