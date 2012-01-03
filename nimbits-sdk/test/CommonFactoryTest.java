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

import com.nimbits.client.model.common.CommonFactoryLocator;
import com.nimbits.client.model.email.EmailAddress;
import com.nimbits.client.model.point.PointName;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 8/6/11
 * Time: 11:13 AM
 */
public class CommonFactoryTest {

    //test to make sure email addresses are lower cased through the polymorphism

    @Test
    public void testPolyMorphism() {
        EmailAddress emailAddress = (EmailAddress) CommonFactoryLocator.getInstance().createEmailAddress("TEST");

        Assert.assertEquals("test", emailAddress.getValue());
        Assert.assertNotSame("TEST", emailAddress.getValue());

        PointName pointName = CommonFactoryLocator.getInstance().createPointName("TEST");

        Assert.assertEquals("TEST", pointName.getValue());
        Assert.assertNotSame("test", pointName.getValue());

    }


}