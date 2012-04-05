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

package com.nimbits.server.logging;

import org.apache.commons.lang3.exception.*;

import java.util.logging.*;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 4/5/12
 * Time: 8:26 AM
 */
public class LogHelper {

    public static void logException(final Class<?> processBatchTaskClass, final Throwable ex) {
        final Logger log = Logger.getLogger(processBatchTaskClass.getName());
        log.severe(ex.getMessage());
        log.severe(ExceptionUtils.getStackTrace(ex));


    }
    public static void log(final Class<?> c, final String ex) {
        final Logger log = Logger.getLogger(c.getName());
        log.info(ex);


    }
}