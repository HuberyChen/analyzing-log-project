package com.quidsi.log.analyzing.web;

import com.quidsi.core.collection.Key;

/**
 * @author Echo
 */
public class SessionConstants {
    public static final Key<Boolean> LOGGED_IN = Key.booleanKey("loggedIn");

    public static final Key<String> API_SECURE_SESSION_ID = Key.stringKey("secureSessionId");

    public static final Key<String> USER_DETAILS = Key.stringKey("user");

}
