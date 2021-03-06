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

package com.nimbits.client.model.file;

import com.nimbits.client.exception.*;
import com.nimbits.client.model.entity.*;

import java.io.*;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 4/9/12
 * Time: 7:07 PM
 */
public class FileModel extends EntityModel implements Serializable, File {
    private String blobKey;

    protected FileModel() {
    }

    public FileModel(File entity) throws NimbitsException {
        super(entity);
        blobKey = entity.getBlobKey();
    }


    public FileModel(Entity e, String blobKey) throws NimbitsException {
        super(e);
        this.blobKey= blobKey;
    }

    @Override
    public String getBlobKey() {
        return blobKey;
    }
    @Override
    public void setBlobKey(final String blobKey) {
        this.blobKey = blobKey;
    }
}
