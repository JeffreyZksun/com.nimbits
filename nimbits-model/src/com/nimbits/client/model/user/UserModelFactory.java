/*
 * Copyright (c) 2011. Tonic Solutions LLC. All Rights reserved.
 *
 * This source code is distributed under GPL v3 without any warranty.
 */

package com.nimbits.client.model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by bsautner
 * User: benjamin
 * Date: 4/16/11
 * Time: 4:10 PM
 */
public class UserModelFactory {


    public static UserModel createUserModel(final User u) {

        return new UserModel(u);

    }


    public List<UserModel> createUserModels(final Set<User> users) {

        final List<UserModel> retObj = new ArrayList<UserModel>();

        for (final User u : users) {
            retObj.add(createUserModel(u));
        }

        return retObj;


    }

}