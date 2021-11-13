package src.java;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

public class Utility {

	private static final Logger LOGGER = getLogger(Utility.class);

	private Utility() {
	}

	public static String convertToJson(Map<String, Object> map) {
		return new Gson().toJson(map);
	}

	public static void printMapStringObject(Map<String, Object> map) {
		if (map != null && !map.isEmpty())
			map.forEach((key, value) -> LOGGER.fatal(String.format("%s : %s", key, value)));
	}

	public static void printMapStringString(Map<String, String> map) {
		if (map != null && !map.isEmpty())
			map.forEach((key, value) -> LOGGER.fatal(String.format("%s : %s", key, value)));
	}

	public static Map<String, Object> convertMapToStringObject(Map<String, String> map) {
		Map<String, Object> newMap = new HashMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof String) {
				newMap.put(entry.getKey(), value);
			}
		}
		return newMap;
	}

	public static Map<String, String> convertMapToStringString(Map<String, Object> map) {
		Map<String, String> newMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String value = (String) entry.getValue();
			if (value instanceof Object) {
				newMap.put(entry.getKey(), value);
			}
		}
		return newMap;
	}

}
