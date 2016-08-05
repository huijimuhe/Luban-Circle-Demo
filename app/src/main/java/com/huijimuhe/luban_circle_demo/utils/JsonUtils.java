package com.huijimuhe.luban_circle_demo.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {

	public static String getString(String key, JSONObject jsonObject)
			throws Exception {
		String res = "";
		if (jsonObject.has(key)) {
			if (key == null) {
				return "";
			}
			res = jsonObject.getString(key);
		}
		return res;
	}

	public static int getInt(String key, JSONObject jsonObject)
			throws Exception {
		int res = -1;
		if (jsonObject.has(key)) {
			res = jsonObject.getInt(key);
		}
		return res;
	}

	public static JSONArray getArray(String key, JSONObject jsonObject)
			throws Exception {
		JSONArray res = new JSONArray();
		if (jsonObject.has(key)) {
			try {
				res = jsonObject.getJSONArray(key);
			} finally {
				return res;
			}
		}
		return null;
	}

	public static double getDouble(String key, JSONObject jsonObject)
			throws Exception {
		double res = 0l;
		if (jsonObject.has(key)) {
			res = jsonObject.getDouble(key);
		}
		return res;
	}

	public static long getLong(String key, JSONObject jsonObject)
			throws Exception {
		long res = 0l;
		if (jsonObject.has(key)) {
			res = jsonObject.getLong(key);
		}
		return res;
	}

	public static boolean getBoolean(String key, JSONObject jsonObject)
			throws Exception {
		boolean res = false;
		if (jsonObject.has(key)) {
			res = jsonObject.getBoolean(key);
		}
		return res;
	}

}
