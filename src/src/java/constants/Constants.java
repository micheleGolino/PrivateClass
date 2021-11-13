package src.java.constants;

/**
 * Classi di costanti
 * @author MicheleGolino
 *
 */
public class Constants {

	private Constants() {
	}

	/**
	 * Costanti Header per le chiamate Http
	 * @author MicheleGolino
	 *
	 */
	public class Header {

		private Header() {
		}

		public static final String HEADER_NAME_CONTENT_TYPE = "Content-type";
		public static final String HEADER_NAME_AUTHORIZATION = "Authorization";
		public static final String HEADER_VALUE_APPLICATION_JSON = "application/json";

	}
}
