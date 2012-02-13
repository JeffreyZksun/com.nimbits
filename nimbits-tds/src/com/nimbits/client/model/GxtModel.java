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

package com.nimbits.client.model;

import com.extjs.gxt.ui.client.data.*;
import com.nimbits.client.enums.AlertType;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.model.common.CommonFactoryLocator;
import com.nimbits.client.model.entity.*;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.user.User;
import com.nimbits.client.model.value.Value;

import java.io.Serializable;
import java.util.*;


/**
 * Created by bsautner
 * User: benjamin
 * Date: 7/8/11
 * Time: 5:42 PM
 */
public class GxtModel extends BaseTreeModel implements Serializable {
    private String uuid;
    private EntityName name;
    private AlertType alertType;
    private EntityType entityType;
    private boolean isReadOnly;
    private boolean isDirty;
    private Value value;
    private Entity baseEntity;


    public GxtModel(Entity entity) {
        this.uuid = entity.getEntity();
        this.name = entity.getName();
        this.alertType = entity.getAlertType();
        this.entityType = entity.getEntityType();
        this.isReadOnly = entity.isReadOnly();
        this.baseEntity = entity;
        set(Const.PARAM_ID, this.uuid);
        set(Const.PARAM_NAME, this.name.getValue());
        set(Const.PARAM_ENTITY_TYPE, entity.getEntityType().getCode());
    }
    public GxtModel(User user) {
        this.uuid = user.getUuid();
        this.name = CommonFactoryLocator.getInstance().createName(user.getEmail().getValue());
        this.alertType = AlertType.OK;
        this.entityType = EntityType.user;
        this.isReadOnly = true;
        this.baseEntity = EntityModelFactory.createEntity(user);
        set(Const.PARAM_ID, this.uuid);
        set(Const.PARAM_NAME, this.name.getValue());
        set(Const.PARAM_ENTITY_TYPE,  this.entityType.getCode());
    }



    @Deprecated
    public GxtModel(User u, Point point) {
        this.uuid = point.getUUID();
        this.name = point.getName();
        this.alertType = point.getAlertState();
        this.entityType = point.getEntityType();
        this.isReadOnly = point.getReadOnly();
        this.baseEntity = EntityModelFactory.createEntity(u, point);
        set(Const.PARAM_ID, this.uuid);
        set(Const.PARAM_NAME, this.name.getValue());
        // set(Const.PARAM_ICON, Const.PARAM_DIAGRAM);
        set(Const.PARAM_ENTITY_TYPE, point.getEntityType().getCode());
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getId() {
        return uuid;
    }

    public void setId(String id) {
        this.uuid = id;
    }

    public EntityName getName() {
        return this.name;
    }

    public void setName(EntityName name) {
        this.name = name;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public String getUUID() {
        return uuid;
    }

    public void setValue(Value value) {
        set(Const.PARAM_VALUE, value.getNumberValue());
        this.value = value;
    }

    public Entity getBaseEntity() {
        return baseEntity;
    }

}