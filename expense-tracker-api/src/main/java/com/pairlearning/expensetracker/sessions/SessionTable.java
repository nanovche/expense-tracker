package com.pairlearning.expensetracker.sessions;

import com.pairlearning.expensetracker.exceptions.EtAuthException;

import java.util.HashMap;
import java.util.Map;

public class SessionTable {

    private static final String USER_NOT_VERIFIED = "user not verified";

    private static final Map<String, Integer> lookUpTable = new HashMap<>();

    public static void setSessionIdForUser(String sessionId, Integer userId) {
        lookUpTable.put(sessionId, userId);
    }

    public static Integer fetchUserIdBySessionId(String sessionId) throws EtAuthException {

        Integer userId = lookUpTable.get(sessionId);
        if(userId != null) {
            return userId;
        }
        throw new EtAuthException(USER_NOT_VERIFIED);
    }
}
