/*
 * Copyright (c) 2011. Nimbits Inc. All Rights reserved.
 *
 * This source code is distributed under GPL v3 without any warranty.
 */

package com.nimbits.client.model.point;

import com.nimbits.client.enums.*;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.entity.Entity;


/**
 * Created by bsautner
 * User: benjamin
 * Date: 4/16/11
 * Time: 12:06 PM
 */
public class PointModelFactory {


    private PointModelFactory() {
    }


    public static Point createPointModel(final Point point) throws NimbitsException {

        return new PointModel(point);

    }
    public static Point createPointModel(Entity entity,
                                         double highAlarm,
                                         int expire,
                                         String unit,
                                         double lowAlarm,
                                         boolean highAlarmOn,
                                         boolean lowAlarmOn,
                                         boolean idleAlarmOn,
                                         Integer idleSeconds,
                                         boolean idleAlarmSent,
                                         FilterType filterType,
                                         double filterValue,
                                         boolean inferLocation
                                        ) throws NimbitsException {
        return new PointModel(
                entity,  highAlarm,
                expire,  unit,  lowAlarm,
                highAlarmOn,  lowAlarmOn,
                idleAlarmOn,  idleSeconds,  idleAlarmSent,
                filterType,  filterValue, inferLocation);
    }




    public static Point createPointModel(Entity entity) throws NimbitsException {
        return  PointModelFactory.createPointModel(entity,0.0, 90, "", 0.0, false, false, false, 0, false, FilterType.fixedHysteresis, 0.1, false );
    }
}
