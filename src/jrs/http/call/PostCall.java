package jrs.http.call;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import jrs.constants.Constants;
import jrs.http.CHttpClient;
import jrs.utility.Utility;

/**
 * Classe utilizzata per effettuare una chiamata <b>POST</b>
 * 
 * @author MicheleGolino
 *
 */
public class PostCall {

	private PostCall() {
	}

	private static final Logger LOGGER = getLogger(PostCall.class);

	private static final String LOG_EXCEPTION_STATUSCODE_NOT_OK = "Errore! Chiamata Post in errore StatusCode: %s ReasonPhrase: %s";
	private static final String LOG_POST_ENDPOINT_REQUEST = "HttpPost with endpoint %s and request: %s";
	private static final String LOG_STATUSCODE_RESPONSE = "Status code: %s Response: %s";
	private static final String EXCEPTION_TESTGEN = "Errore! TestGenVerify [authentication] HttpResponse is null";
	private static final int OK = 200;

	/**
	 * 
	 * @param withAuthorization vero se si sta facendo una chiamata con
	 *                          autorizzazione, falso altrimenti
	 * @param isBearer          vero se è un'autenticazione con bearer, falso
	 *                          altrimenti
	 * @param accessToken       per l'autorizzazione, null se l'autorizzazione è
	 *                          falsa
	 * @param endpoint          a cui fare la chiamata
	 * @param request           da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	public static Map<String, Object> post(boolean withAuthorization, boolean isBearer, String accessToken,
			String endpoint, Map<String, Object> request) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, isBearer, request) : post(endpoint, request);
	}

	/**
	 * Chiamata <b>POST</b> con header <b>Content-type application/json</b> e
	 * autenticazione
	 * 
	 * @param withAuthorization vero se si sta facendo una chiamata con
	 *                          autorizzazione, falso altrimenti
	 * @param accessToken       per l'autorizzazione, null se l'autorizzazione è
	 *                          falsa
	 * @param endpoint          a cui fare la chiamata
	 * @param jsonRequest       da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	public static Map<String, Object> post(boolean withAuthorization, String accessToken, String endpoint,
			String jsonRequest) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, false, jsonRequest) : post(endpoint, jsonRequest);
	}

	/**
	 * 
	 * @param withAuthorization vero se si sta facendo una chiamata con
	 *                          autorizzazione, falso altrimenti
	 * @param isBearer          vero se è un'autenticazione con bearer, falso
	 *                          altrimenti
	 * @param accessToken       per l'autorizzazione, null se l'autorizzazione è
	 *                          falsa
	 * @param endpoint          a cui fare la chiamata
	 * @param jsonRequest       da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	public static Map<String, Object> post(boolean withAuthorization, boolean isBearer, String accessToken,
			String endpoint, String jsonRequest) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, isBearer, jsonRequest) : post(endpoint, jsonRequest);
	}

	/**
	 * Chiamata <b>POST</b> con header <b>Content-type application/json</b> e
	 * autenticazione
	 * 
	 * @param withAuthorization vero se si sta facendo una chiamata con
	 *                          autorizzazione, falso altrimenti
	 * @param accessToken       per l'autorizzazione, null se l'autorizzazione è
	 *                          falsa
	 * @param endpoint          a cui fare la chiamata
	 * @param request           da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	public static Map<String, Object> post(boolean withAuthorization, String accessToken, String endpoint,
			Map<String, Object> request) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, false, request) : post(endpoint, request);
	}

	/**
	 * Chiamata <b>POST</b> con header <b>Content-type application/json</b> senza
	 * autenticazione
	 * 
	 * @param endpoint a cui fare la chiamata
	 * @param request  da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> post(String endpoint, Map<String, Object> request) throws Exception {
		CHttpClient oHttpClient = new CHttpClient();
		String uri = endpoint;
		HttpPost oHttpAuthPost = new HttpPost(uri);
		String jsonRequest = Utility.convertToJson(request);
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format(LOG_POST_ENDPOINT_REQUEST, endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format(LOG_EXCEPTION_STATUSCODE_NOT_OK, statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format(LOG_STATUSCODE_RESPONSE, statusCode, sResponse);
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
	 * Chiamata <b>POST</b> con header <b>Content-type application/json</b> senza
	 * autenticazione
	 * 
	 * @param endpoint    a cui fare la chiamata
	 * @param requestJson da inviare alla chiamata
	 * @return response risposta <b>JSON</b> dalla chiamata
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> post(String endpoint, String requestJson) throws Exception {
		CHttpClient oHttpClient = new CHttpClient();
		String uri = endpoint;
		HttpPost oHttpAuthPost = new HttpPost(uri);
		String jsonRequest = requestJson;
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format(LOG_POST_ENDPOINT_REQUEST, endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format(LOG_EXCEPTION_STATUSCODE_NOT_OK, statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format(LOG_STATUSCODE_RESPONSE, statusCode, sResponse);
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
	 * 
	 * Chiamata <b>POST</b> con un'autorizzazione d'accesso
	 * 
	 * @param accessToken per l'autorizzazione, null se <b>withAuthorization</b> è
	 *                    false
	 * @param endpoint    a cui fare richiesta
	 * @param isBearer    impostare a true se è un'autenticazione con bearer
	 * @param request     da inviare
	 * @return response risposta della chiamata in formato <b>JSON</b>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> post(String accessToken, String endpoint, boolean isBearer,
			Map<String, Object> request) throws Exception {
		Gson oGson = new Gson();
		CHttpClient oHttpClient = new CHttpClient();
		String accToken = accessToken;
		String uri = endpoint;
		String authValue = "";
		if (isBearer)
			authValue = "Bearer ";
		authValue += accToken;
		HttpPost oHttpAuthPost = new HttpPost(uri);
		String jsonRequest = Utility.convertToJson(request);
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_AUTHORIZATION, authValue);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format(LOG_POST_ENDPOINT_REQUEST, endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format(LOG_EXCEPTION_STATUSCODE_NOT_OK, statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format(LOG_STATUSCODE_RESPONSE, statusCode, sResponse);
				LOGGER.fatal(logz);
				return oGson.fromJson(sResponse, Map.class);
			} else {
				throw new IllegalArgumentException(EXCEPTION_TESTGEN);
			}
		} else {
			throw new IllegalArgumentException(EXCEPTION_TESTGEN);
		}
	}

	/**
	 * 
	 * Chiamata <b>POST</b> con un'autorizzazione d'accesso
	 * 
	 * @param accessToken per l'autorizzazione, null se <b>withAuthorization</b> è
	 *                    false
	 * @param endpoint    a cui fare richiesta
	 * @param isBearer    impostare a true se è un'autenticazione con bearer
	 * @param request     json da inviare
	 * @return response risposta della chiamata in formato <b>JSON</b>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> post(String accessToken, String endpoint, boolean isBearer, String requestJson)
			throws Exception {
		Gson oGson = new Gson();
		CHttpClient oHttpClient = new CHttpClient();
		String accToken = accessToken;
		String uri = endpoint;
		String authValue = "";
		if (isBearer)
			authValue = "Bearer ";
		authValue += accToken;
		HttpPost oHttpAuthPost = new HttpPost(uri);
		String jsonRequest = requestJson;
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_AUTHORIZATION, authValue);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format(LOG_POST_ENDPOINT_REQUEST, endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String.format(LOG_EXCEPTION_STATUSCODE_NOT_OK, statusCode, reason);
				throw new IllegalArgumentException(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format(LOG_STATUSCODE_RESPONSE, statusCode, sResponse);
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
