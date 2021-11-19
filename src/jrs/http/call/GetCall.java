package jrs.http.call;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import jrs.constants.Constants;
import jrs.http.CHttpClient;

/**
 * Classe utilizzata per effettuare una chiamata <b>GET</b>
 * 
 * @author MicheleGolino
 *
 */
public class GetCall {

	private GetCall() {
	}

	private static final Logger LOGGER = getLogger(GetCall.class);

	private static final String EXCEPTION_TESTGEN = "Errore! TestGenVerify [authentication] HttpResponse is null";
	private static final int OK = 200;

	/**
	 * Chiamata <b>GET</b> con un'autorizzazione d'accesso
	 * 
	 * @param withAuthorization impostare a true se si sta effettaundo una chiamata
	 *                          con un'autorizzazione, false altrimenti
	 * @param isBearer          impostare a true se è un'autenticazione con bearer
	 * @param accessToken       per l'autorizzazione, null se
	 *                          <b>withAuthorization</b> è false
	 * @param endpoint          a cui fare richiesta
	 * @param request           da inviare
	 * @return response risposta della chiamata in formato <b>JSON</b>
	 * @throws Exception
	 */
	public static Map<String, String> get(boolean withAuthorization, boolean isBearer, String accessToken,
			String endpoint) throws Exception {
		return withAuthorization ? get(accessToken, endpoint, isBearer) : get(endpoint);
	}

	/**
	 * Chiamata <b>GET</b> con un'autorizzazione d'accesso
	 * 
	 * @param withAuthorization impostare a true se si sta effettaundo una chiamata
	 *                          con un'autorizzazione, false altrimenti
	 * @param accessToken       per l'autorizzazione, null se <b>
	 *                          withAuthorization</b> è false
	 * @param endpoint          a cui fare richiesta
	 * @param request           da inviare
	 * @return response risposta della chiamata in formato <b>JSON</b>
	 * @throws Exception
	 */
	public static Map<String, String> get(boolean withAuthorization, String accessToken, String endpoint)
			throws Exception {
		return withAuthorization ? get(accessToken, endpoint, false) : get(endpoint);
	}

	/**
	 * Chiamata POST di default con Header <b>Content-type application/json</b> No
	 * autenticazione
	 * 
	 * @param endpoint a cui inviare la request
	 * @param request  da inviare
	 * @return response risposta della chiamata in formato <b>JSON</b>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> get(String endpoint) throws Exception {
		CHttpClient oHttpClient = new CHttpClient();
		String uri = endpoint;
		HttpGet oHttpAuthGet = new HttpGet(uri);

		final String log = String.format("HttpGet with endpoint %s", endpoint);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthGet);

		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format("Errore! Chiamata Get in errore StatusCode: %s ReasonPhrase: %s",
						statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format("Status code: %s Response: %s", statusCode, sResponse);
				LOGGER.fatal(logz);
				return new Gson().fromJson(sResponse, Map.class);
			} else {
				throw new IllegalArgumentException(EXCEPTION_TESTGEN);
			}
		} else {
			throw new IllegalArgumentException(EXCEPTION_TESTGEN);
		}
	}

	/**
	 * Chiamata <b>GET</b> un'autorizzazione d'accesso
	 * 
	 * @param accessToken per l'autorizzazione, null se <b> withAuthorization</b> è
	 *                    false
	 * @param endpoint    a cui fare richiesta
	 * @param request     da inviare
	 * @return response risposta in formato <b>JSON</b>
	 * 
	 * @param isBearer impostare a true se è un'autenticazione con bearer
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> get(String accessToken, String endpoint, boolean isBearer) throws Exception {
		Gson oGson = new Gson();
		CHttpClient oHttpClient = new CHttpClient();
		String accToken = accessToken;
		String uri = endpoint;
		String authValue = "";
		if (isBearer)
			authValue = "Bearer ";
		authValue += accToken;
		HttpGet oHttpAuthGet = new HttpGet(uri);
		oHttpAuthGet.addHeader(Constants.Header.HEADER_NAME_AUTHORIZATION, authValue);

		final String log = String.format("HttpGet with endpoint %s", endpoint);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthGet);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format("Errore! Chiamata Get in errore StatusCode: %s ReasonPhrase: %s",
						statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format("Status code: %s Response: %s", statusCode, sResponse);
				LOGGER.fatal(logz);
				return oGson.fromJson(sResponse, Map.class);
			} else {
				throw new IllegalArgumentException(EXCEPTION_TESTGEN);
			}
		} else {
			throw new IllegalArgumentException(EXCEPTION_TESTGEN);
		}
	}

}
