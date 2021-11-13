package java.test;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.http.call.GetCall;
import java.util.Map;
import java.utility.Utility;

import org.apache.logging.log4j.Logger;

public class TestGetCall {

	private static final Logger LOGGER = getLogger(TestGetCall.class);
	private static final String ENDPOINT = "https://api.habboapi.net/badges";

	private TestGetCall() {
	}

	public static void startTestCall() {
		try {
			Map<String, Object> map = Utility.convertMapToStringObject(GetCall.get(ENDPOINT));
			Utility.printMapStringObject(map);
		} catch (Exception e) {
			LOGGER.fatal(String.format("Eccezione in startTestCall %s", e.getMessage()));
		}
	}

}
