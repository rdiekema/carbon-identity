/*
 * Copyright (c) 2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.dao;

public class SQLQueries {
    public static final String STORE_AUTHORIZATION_CODE = "INSERT INTO  IDN_OAUTH2_AUTHORIZATION_CODE " +
            "(AUTHORIZATION_CODE, CONSUMER_KEY, CALLBACK_URL, SCOPE, AUTHZ_USER, TIME_CREATED, VALIDITY_PERIOD) " +
            "VALUES (?,?,?,?,?,?,?)";

    public static final String VALIDATE_AUTHZ_CODE = "SELECT AUTHZ_USER, SCOPE, CALLBACK_URL, TIME_CREATED, " +
            "VALIDITY_PERIOD, STATE, TOKEN_ID, AUTHORIZATION_CODE FROM IDN_OAUTH2_AUTHORIZATION_CODE WHERE CONSUMER_KEY = ? " +
                                                     "AND AUTHORIZATION_CODE = ?";

    public static final String UPDATE_TOKEN_AGAINST_AUTHZ_CODE = "UPDATE IDN_OAUTH2_AUTHORIZATION_CODE SET " +
                                                                 "TOKEN_ID=? WHERE AUTHORIZATION_CODE=?";

    public static final String GET_ACCESS_TOKEN_BY_AUTHZ_CODE = "SELECT AUTHORIZATION_CODE FROM " +
                                                                "IDN_OAUTH2_AUTHORIZATION_CODE WHERE " +
                                                                "TOKEN_ID=?";

    public static final String UPDATE_NEW_TOKEN_AGAINST_AUTHZ_CODE = "UPDATE IDN_OAUTH2_AUTHORIZATION_CODE SET " +
                                                                 "TOKEN_ID=? WHERE AUTHORIZATION_CODE= (SELECT " +
                                                                     "AUTHORIZATION_CODE FROM " +
                                                                     "IDN_OAUTH2_AUTHORIZATION_CODE WHERE TOKEN_ID=? )";

    public static final String EXPIRE_AUTHZ_CODE = "UPDATE IDN_OAUTH2_AUTHORIZATION_CODE SET " +
                                                   "STATE='EXPIRED' WHERE AUTHORIZATION_CODE= ?";

    public static final String DEACTIVATE_AUTHZ_CODE_AND_INSERT_CURRENT_TOKEN = "UPDATE IDN_OAUTH2_AUTHORIZATION_CODE SET " +
                                                                           "STATE='INACTIVE', TOKEN_ID=?" +
                                                                            " WHERE AUTHORIZATION_CODE= ?";

    public static final String RETRIEVE_LATEST_ACCESS_TOKEN_BY_CLIENT_ID_USER_SCOPE_ORACLE = "SELECT * FROM (SELECT " +
            "ACCESS_TOKEN, REFRESH_TOKEN, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, " +
            "REFRESH_TOKEN_VALIDITY_PERIOD,TOKEN_STATE, USER_TYPE, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? " +
            "AND AUTHZ_USER=? AND TOKEN_SCOPE=? ORDER BY TIME_CREATED DESC) WHERE ROWNUM < 2 ";

    public static final String RETRIEVE_LATEST_ACCESS_TOKEN_BY_CLIENT_ID_USER_SCOPE_MYSQL = "SELECT ACCESS_TOKEN, " +
            "REFRESH_TOKEN, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, REFRESH_TOKEN_VALIDITY_PERIOD," +
            " TOKEN_STATE, USER_TYPE, TOKEN_ID  FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? AND AUTHZ_USER=? AND " +
            "TOKEN_SCOPE=? ORDER BY TIME_CREATED DESC LIMIT 1";

    public static final String RETRIEVE_LATEST_ACCESS_TOKEN_BY_CLIENT_ID_USER_SCOPE_MSSQL = "SELECT TOP 1 " +
            "ACCESS_TOKEN, REFRESH_TOKEN, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, " +
            "REFRESH_TOKEN_VALIDITY_PERIOD, TOKEN_STATE, USER_TYPE, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? " +
            "AND AUTHZ_USER=? AND TOKEN_SCOPE=? ORDER BY TIME_CREATED DESC";

    public static final String RETRIEVE_LATEST_ACCESS_TOKEN_BY_CLIENT_ID_USER_SCOPE_POSTGRESQL = "SELECT * FROM " +
            "(SELECT ACCESS_TOKEN, REFRESH_TOKEN, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, " +
            "REFRESH_TOKEN_VALIDITY_PERIOD, TOKEN_STATE, USER_TYPE, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? " +
            "AND AUTHZ_USER=? AND TOKEN_SCOPE=? ORDER BY TIME_CREATED DESC) AS TOKEN LIMIT 1 ";

    public static final String RETRIEVE_ACTIVE_ACCESS_TOKEN_BY_CLIENT_ID_USER = "SELECT ACCESS_TOKEN, REFRESH_TOKEN, " +
            "TOKEN_SCOPE, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, REFRESH_TOKEN_VALIDITY_PERIOD, " +
            "USER_TYPE, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? AND AUTHZ_USER=? AND TOKEN_STATE='ACTIVE'";

    public static final String RETRIEVE_ACTIVE_EXPIRED_ACCESS_TOKEN_BY_CLIENT_ID_USER = "SELECT ACCESS_TOKEN, " +
            "REFRESH_TOKEN, TOKEN_SCOPE, TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, " +
            "REFRESH_TOKEN_VALIDITY_PERIOD, USER_TYPE, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE CONSUMER_KEY=? AND " +
            "AUTHZ_USER=? AND (TOKEN_STATE='ACTIVE' OR TOKEN_STATE='EXPIRED')";

    public static final String RETRIEVE_ACTIVE_ACCESS_TOKEN = "SELECT CONSUMER_KEY, AUTHZ_USER, TOKEN_SCOPE, " +
            "TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, REFRESH_TOKEN_VALIDITY_PERIOD, USER_TYPE, " +
            "REFRESH_TOKEN, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE ACCESS_TOKEN=? AND TOKEN_STATE='ACTIVE'";

    public static final String RETRIEVE_ACTIVE_EXPIRED_ACCESS_TOKEN = "SELECT CONSUMER_KEY, AUTHZ_USER, TOKEN_SCOPE, " +
            "TIME_CREATED, REFRESH_TOKEN_TIME_CREATED, VALIDITY_PERIOD, REFRESH_TOKEN_VALIDITY_PERIOD, USER_TYPE, " +
            "REFRESH_TOKEN, TOKEN_ID FROM IDN_OAUTH2_ACCESS_TOKEN WHERE ACCESS_TOKEN=? AND (TOKEN_STATE='ACTIVE' OR " +
            "TOKEN_STATE='EXPIRED')";

    public static final String UPDATE_TOKE_STATE = "UPDATE IDN_OAUTH2_ACCESS_TOKEN SET TOKEN_STATE=?, " +
            "TOKEN_STATE_ID=? WHERE TOKEN_ID=?";

    public static final String REVOKE_ACCESS_TOKEN_BY_TOKEN_ID = "UPDATE IDN_OAUTH2_ACCESS_TOKEN SET TOKEN_STATE=?, " +
            "TOKEN_STATE_ID=? WHERE TOKEN_ID=?";

    public static final String REVOKE_ACCESS_TOKEN = "UPDATE IDN_OAUTH2_ACCESS_TOKEN SET TOKEN_STATE=?, " +
                                                     "TOKEN_STATE_ID=? WHERE ACCESS_TOKEN=?";

    public static final String REVOKE_REFRESH_TOKEN = "UPDATE IDN_OAUTH2_ACCESS_TOKEN SET TOKEN_STATE=?, " +
            "TOKEN_STATE_ID=? WHERE REFRESH_TOKEN=?";

    public static final String GET_DISTINCT_APPS_AUTHORIZED_BY_USER_ALL_TIME = "SELECT DISTINCT CONSUMER_KEY FROM " +
            "IDN_OAUTH2_ACCESS_TOKEN WHERE AUTHZ_USER=? AND (TOKEN_STATE='ACTIVE' OR TOKEN_STATE='EXPIRED')";

    private SQLQueries() {

    }
}
