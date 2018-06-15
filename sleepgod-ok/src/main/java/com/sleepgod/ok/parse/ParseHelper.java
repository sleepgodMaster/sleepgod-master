package com.sleepgod.ok.parse;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析辅助类，封装了Json解析的基本方法
 */
public abstract class ParseHelper {


    public static boolean has(JSONObject jsonObject, String name) {
        if (jsonObject == null) {
            return false;
        }
        return jsonObject.has(name);
    }

    public static int getLength(JSONArray jsonArray) {
        if (jsonArray == null) {
            return 0;
        }

        return jsonArray.length();
    }

    public static int getInt(JSONObject jsonObject, String name) throws JSONException {

        int value = -1;

        if (has(jsonObject, name)) {
            String valueString = getString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                value = Integer.parseInt(valueString);
            }
        }

        return value;
    }

    public static int getInt(JSONArray jsonArray, int index) throws JSONException {

        int value = -1;
        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            value = Integer.parseInt(valueString);
        }

        return value;
    }

    public static long getLong(JSONObject jsonObject, String name) throws JSONException {

        long value = -1;

        if (has(jsonObject, name)) {
            String valueString = getString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                value = Long.parseLong(valueString);
            }
        }

        return value;
    }

    public static long getLong(JSONArray jsonArray, int index) throws JSONException {

        long value = -1;

        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            value = Long.parseLong(valueString);
        }

        return value;
    }

    public static boolean getBoolean(JSONObject jsonObject, String name) throws JSONException {

        boolean value = false;

        if (has(jsonObject, name)) {
            value = jsonObject.getBoolean(name);
        }

        return value;
    }

    public static boolean getBoolean(JSONArray jsonArray, int index) throws JSONException {

        boolean value = false;

        value = jsonArray.getBoolean(index);

        return value;
    }

    public static float getFloat(JSONObject jsonObject, String name) throws JSONException {

        float value = -1;

        if (has(jsonObject, name)) {
            String valueString = getString(jsonObject, name);
            if (!TextUtils.isEmpty(valueString)) {
                try {
                    value = Float.parseFloat(jsonObject.get(name).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    public static float getFloat(JSONArray jsonArray, int index) throws JSONException {

        float value = -1;
        String valueString = getString(jsonArray, index);
        if (!TextUtils.isEmpty(valueString)) {
            try {
                value = Float.parseFloat(valueString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public static String getString(JSONObject jsonObject, String name) throws JSONException {

        String value = "";

        if (!jsonObject.has(name)) {
            return value;
        }

        value = jsonObject.getString(name);

        if ("null".equalsIgnoreCase(value)) {
            value = "";
        }

        return value;
    }

    public static String getString(JSONArray jsonArray, int index) throws JSONException {

        String value = "";
        value = jsonArray.getString(index);

        if ("null".equalsIgnoreCase(value)) {
            value = "";
        }

        return value;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONArray array = null;
        if (has(jsonObject, name)) {
            array = jsonObject.getJSONArray(name);
        }

        return array;
    }

    public static JSONArray getJSONArray(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONArray array = jsonArray.getJSONArray(index);

        return array;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) throws JSONException {

        if (jsonObject == null) {
            throw new JSONException("JSONObject is null");
        }
        JSONObject object = null;
        if (has(jsonObject, name)) {
            object = jsonObject.getJSONObject(name);
        }

        return object;
    }

    public static JSONObject getJSONObject(JSONArray jsonArray, int index) throws JSONException {

        if (jsonArray == null) {
            throw new JSONException("JSONArray is null");
        }

        JSONObject object = jsonArray.getJSONObject(index);

        return object;
    }
}
