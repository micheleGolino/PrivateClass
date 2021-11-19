package jrs.utility;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

/**
 * Classe Utility contenente metodi per:
 * <ul>
 * <li>Conversione di Mappe in Stringhe <b>JSON;</b></li>
 * <li>Conversione di tipi di mappe;</li>
 * <li>Stampa in console di tipi di mappe.</li>
 * </ul>
 * 
 * <b>!!! Questa classe utilizza una forzatura per stampare in console con un
 * livello di log alto, come FATAL !!!</b> <br>
 * 
 * @author MicheleGolino
 *
 */
public class Utility {

	/**
	 * Logger per la stampa in console della classe
	 */
	private static final Logger LOGGER = getLogger(Utility.class);

	/**
	 * Costruttore privato non istanziabile
	 */
	private Utility() {
	}

	/**
	 * Converte il tipo di mappa passata, in una stringa <b>JSON</b>
	 * 
	 * @param <T> qualsiasi tipo di mappa
	 * @param map la mappa da trasformare in json
	 * @return la stringa in json della mappa passata
	 */
	public static <T> String convertToJson(T map) {
		return new Gson().toJson(map);
	}

	/**
	 * Stampa una mappa di tipo Raw
	 * 
	 * @param map Mappa da stampare
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void printMapRaw(Map map) {
		printMapStringObject(map);
	}

	/**
	 * Stampa la mappa String Object passata
	 * 
	 * @param map La mappa da stampare
	 */
	public static void printMapStringObject(Map<String, Object> map) {
		if (map != null && !map.isEmpty())
			map.forEach((key, value) -> LOGGER.fatal(String.format("%s : %s", key, value)));
	}

	/**
	 * Stampa la mappa String String passata
	 * 
	 * @param map La mappa da stampare
	 */
	public static void printMapStringString(Map<String, String> map) {
		if (map != null && !map.isEmpty())
			map.forEach((key, value) -> LOGGER.fatal(String.format("%s : %s", key, value)));
	}

	/**
	 * Converte la mappa di tipo String String in String Object
	 * 
	 * @param map la mappa da convertire
	 * @return la nuova mappa in formato String Object
	 */
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

	/**
	 * Converte la mappa di tipo String Object in String String
	 * 
	 * @param map la mappa da convertire
	 * @return la nuova mappa in formato String String
	 */
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
