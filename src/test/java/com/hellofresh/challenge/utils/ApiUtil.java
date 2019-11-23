package com.hellofresh.challenge.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * Created by sekarayukarindra.
 */
public class ApiUtil {

    public static JsonPath getJsonPath (String root, Response res) {
        String json = res.asString();
        return new JsonPath(json).setRoot(root);
    }

    public static JsonPath getJsonPath (Response res) {
        String json = res.asString();
        return new JsonPath(json);
    }
}
