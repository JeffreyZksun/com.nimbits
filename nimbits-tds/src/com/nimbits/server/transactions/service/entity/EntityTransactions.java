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

package com.nimbits.server.transactions.service.entity;

import com.google.appengine.api.blobstore.BlobKey;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.exception.NimbitsException;
import com.nimbits.client.model.entity.Entity;
import com.nimbits.client.model.entity.EntityName;
import com.nimbits.server.transactions.memcache.entity.EntityCache;

import java.util.List;
import java.util.Map;

/**
 * Created by Benjamin Sautner
 * User: bsautner
 * Date: 2/28/12
 * Time: 11:46 AM
 */
public interface EntityTransactions extends EntityCache {

    Map<String, Entity> getEntityMap( final EntityType type, final int limit) throws NimbitsException;

    Map<EntityName, Entity> getEntityNameMap( final EntityType type) throws NimbitsException;

    List<Entity> getChildren( final Entity parentEntity,  final EntityType type) throws NimbitsException;

    Entity addUpdateEntity( final Entity entity) throws NimbitsException;

    List<Entity> getEntities() throws NimbitsException;

    List<Entity> deleteEntity( final Entity entity,final Class<?> cls) throws NimbitsException;

    List<Entity> getEntityByKey( final String id,  final Class<?> cls) throws NimbitsException;

    Map<String, Entity> getSystemWideEntityMap(final EntityType type) throws NimbitsException;

    List<Entity> getEntityByName( final EntityName name,  final Class<?> cls) throws NimbitsException;

    List<Entity> getEntitiesBySource(final Entity source, final Class<?> cls) throws NimbitsException;

    List<Entity> getEntityByTrigger(final Entity entity, final Class<?> cls) throws NimbitsException;

    List<Entity> getIdleEntities() throws NimbitsException;

    List<Entity> getSubscriptionsToEntity(final Entity subscribedEntity) throws NimbitsException;

    List<Entity> getEntityByBlobKey(final BlobKey key) throws NimbitsException;
}
