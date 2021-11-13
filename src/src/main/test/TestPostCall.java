package src.main.test;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import src.java.PostCall;

public class TestPostCall {

	private static final Logger LOGGER = getLogger(TestPostCall.class);
	private static final String ENDPOINT = "https://reqbin.com/echo/post/json";
	
	private TestPostCall() {
	}
	
	public static void startTestCall() {
		final Map<String, Object> map = generateTestMap();
		try {
			PostCall.post(false, null, ENDPOINT, map);
		} catch (Exception e) {
			LOGGER.fatal(String.format("Eccezione in startTestCall %s", e.getMessage()));
		}
	}
	
	private static Map<String, Object> generateTestMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", 78912);
		map.put("Customer", "Jason Sweet");
		map.put("Quantity", 1);
		map.put("Price", 18.00);
		return map;
	}
}
