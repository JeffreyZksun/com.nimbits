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

package com.nimbits.server.transactions.orm.legacy;

import com.nimbits.client.common.Utils;
import com.nimbits.client.constants.Const;
import com.nimbits.client.enums.AlertType;
import com.nimbits.client.model.value.Value;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "false")
@Deprecated
public class RecordedValue implements Value {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private Double lat;
    @Persistent
    private Double lng;
    @Persistent
    private Double d;
    @Persistent
    private Date timestamp;
    @Persistent
    private Long pointFK;
    @Persistent
    private String note;
    @Persistent
    private String data;
    @NotPersistent
    private AlertType alertState;


    public RecordedValue(final double lat, final double lng, final double d, final Date timestamp, final long pointFK, final String note, final String data) {
        this.lat = lat;
        this.lng = lng;
        this.d = d;
        this.timestamp = new Date(timestamp.getTime());
        this.pointFK = pointFK;
        this.note = note;
        this.data = data;
    }

//    public RecordedValue(final Point point, final Value v) {
//
//        this.lat = v.getLatitude();
//        this.lng = v.getLongitude();
//        this.d = v.getDoubleValue();
//        this.timestamp = v.getTimestamp();
//        this.pointFK = point.getId();
//        this.note = v.getNote();
//        this.data = v.getData();
//        this.pointFK = point.getId();
//    }


    public RecordedValue() {
    }

    @Override
    public long getId() {
        return id == null ? 0 : id;
    }

    @Override
    public String getNote() {
        return note == null ? "" : note;
    }

    @Override
    public double getLatitude() {
        return lat == null ? 0 : lat;
    }

    @Override
    public double getLongitude() {
        return lng == null ? 0 : lng;
    }


    public String getEntity() {
        return null;  //auto generated
    }

//    @Override
//    public long getPoint() {
//        return pointFK;
//    }

    @Deprecated
    public double getValue() {
        return d == null ? 0.0 : d;
    }

    public double getDoubleValue() {
        return d == null ? 0.0 : d;
    }

    @Override
    public String getValueWithNote() {
        StringBuilder sb = new StringBuilder();
        if ( this.d != Const.CONST_IGNORED_NUMBER_VALUE) {
            sb.append(this.d);
        }
        if ( ! Utils.isEmptyString(this.note)) {
            sb.append(" ");
            sb.append(this.note);
        }
        return sb.toString().trim();
    }


    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public AlertType getAlertState() {
        return alertState;
    }


    public void setAlertType(final AlertType alertState) {
        this.alertState = alertState;
    }

    @Override
    public String getData() {
        return data == null ? "" : data;
    }


    @Override
    public int compareTo(Value value) {
        return 0;
    }
}