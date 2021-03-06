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

package com.nimbits.shared;


/**
 * Created by bsautner
 * User: benjamin
 * Date: 6/11/11
 * Time: 9:04 AM
 */
public class Utils {

    private static final double DOUBLE = 100.0;

    private Utils() {
    }

    public static boolean isEmptyString(final String string) {
        return string == null || string.trim().isEmpty();

    }


    public static double roundDouble(final double d) {
        int ix = (int) (d * DOUBLE); // scale it
        return (double) ix / DOUBLE;
    }


}
