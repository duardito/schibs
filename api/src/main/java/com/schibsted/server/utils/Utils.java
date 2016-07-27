package com.schibsted.server.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edu on 23/07/2016.
 */
public final class Utils {


    public static Map<String, String> queryToMap(final String query) {

        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }


}
