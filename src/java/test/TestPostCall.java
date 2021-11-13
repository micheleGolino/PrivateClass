package java.test;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.http.call.PostCall;
import java.util.HashMap;
import java.util.Map;
import java.utility.Utility;

import org.apache.logging.log4j.Logger;

public class TestPostCall {

	private static final Logger LOGGER = getLogger(TestPostCall.class);
	private static final String ENDPOINT = "https://reqbin.com/echo/post/json";

	private TestPostCall() {
	}

	public static void startTestCall() {
		try {
			Utility.printMapStringObject(PostCall.post(false, null, ENDPOINT, generateTestMap()));
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
