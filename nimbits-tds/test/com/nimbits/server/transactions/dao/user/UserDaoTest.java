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
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, eitherexpress or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.server.transactions.dao.user;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.common.CommonFactoryLocator;
import com.nimbits.client.model.email.EmailAddress;
import com.nimbits.client.model.user.User;
import com.nimbits.server.user.UserTransactionFactory;
import helper.NimbitsServletTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 4/7/12
 * Time: 4:52 PM
 */
public class UserDaoTest extends NimbitsServletTest {
    public final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());
            //new LocalTaskQueueTestConfig(),
            //new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true).setEnvEmail(email).setEnvAuthDomain("example.com");

    @Before
    public void setUp() throws NimbitsException {

        helper.setUp();
    }
    @After
    public void tearDown() {
        helper.tearDown();

    }
        @Test
    public void createUserTest() throws NimbitsException {
        EmailAddress e = CommonFactoryLocator.getInstance().createEmailAddress("bob@example.com");
        User u = UserTransactionFactory.getInstance().createNimbitsUser(e);
        assertNotNull(u);
        assertEquals(e.getValue(), u.getEmail().getValue());
        User r = UserTransactionFactory.getInstance().getUserByKey(e.getValue());
        assertNotNull(r);
            assertEquals(e.getValue(), r.getEmail().getValue());
            assertNotNull(r.getDateCreated());

    }


}