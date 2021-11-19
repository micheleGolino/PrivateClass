package jrs.main;

import jrs.test.TestPostCall;
import java.util.HashMap;
import java.util.Map;
import jrs.utility.Utility;

public class Main {
	private Main() {
	}

	public static void main(String[] args) {
//		TestPostCall.startTestCall();
//		TestGetCall.startTestCall();

		Map m = new HashMap<>();
		m.put("test_stringa", "Stringa");
		m.put("test_integer", 1);
		m.put("test_double", 15.9);

//		String json = String.format("json: %s", Utility.convertToJson(m));
//		System.out.println(json);
		
		Utility.printMapStringObject(m);
	}

}
