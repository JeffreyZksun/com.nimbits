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
 * Unless required by applicable law or agreed to in writing, software distributed under the license is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.nimbits.client.model.email.impl;


import com.nimbits.client.model.common.impl.CommonIdentifierImpl;
import com.nimbits.client.model.email.EmailAddress;

import java.io.Serializable;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 8/1/11
 * Time: 9:56 AM
 */
@SuppressWarnings("unused")
public class EmailAddressImpl extends CommonIdentifierImpl implements Serializable, EmailAddress {
    private static final long serialVersionUID =1L;

    protected EmailAddressImpl() {
        super();
    }

    public EmailAddressImpl(final String value) {
        super(value.toLowerCase());

    }
}
