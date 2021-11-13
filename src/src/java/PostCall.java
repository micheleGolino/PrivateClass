package src.java;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import src.java.constants.Constants;

/**
 * Classe utilizzata per effettuare una chiamata Post
 * 
 * @author MicheleGolino
 *
 */
public class PostCall {

	private PostCall() {
	}

	private static final Logger LOGGER = getLogger(PostCall.class);

	private static final String EXCEPTION_TESTGEN = "Errore! TestGenVerify [authentication] HttpResponse is null";
	private static final int OK = 200;

	/**
	 * 
	 * @param withAuthorization true if you want call a post with authorization,
	 *                          else false
	 * @param isBearer          true if is an authentication with bearer
	 * @param accessToken       for the authorization, null if withAutorization is
	 *                          flase
	 * @param endpoint          to send request
	 * @param request           to send
	 * @return response json
	 * @throws Exception
	 */
	public static Map<String, String> post(boolean withAuthorization, boolean isBearer, String accessToken,
			String endpoint, Map<String, Object> request) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, isBearer, request) : post(endpoint, request);
	}

	/**
	 * 
	 * @param withAuthorization true if you want call a post with authorization,
	 *                          else false
	 * @param accessToken       for the authorization, null if withAuthorization is
	 *                          false
	 * @param endpoint          to send request
	 * @param request           to send
	 * @return response json
	 * @throws Exception
	 */
	public static Map<String, String> post(boolean withAuthorization, String accessToken, String endpoint,
			Map<String, Object> request) throws Exception {
		return withAuthorization ? post(accessToken, endpoint, false, request) : post(endpoint, request);
	}

	/**
	 * Default call the method post with header Content-type application/json No
	 * authentication
	 * 
	 * @param endpoint to send request
	 * @param request  to send
	 * @return response json
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> post(String endpoint, Map<String, Object> request) throws Exception {
		Gson oGson = new Gson();
		CHttpClient oHttpClient = new CHttpClient();
		String uri = endpoint;
		HttpPost oHttpAuthPost = new HttpPost(uri);
		String jsonRequest = composeJson(request);
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format("HttpPost with endpoint %s and request: %s", endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String
						.format("Errore! Chiamata Post in errore StatusCode: %s ReasonPhrase: %s", statusCode, reason);
				throw new Exception(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format("Status code: %s Response: %s", statusCode, sResponse);
				LOGGER.fatal(logz);
				return oGson.fromJson(sResponse, Map.class);
			} else {
				throw new Exception(EXCEPTION_TESTGEN);
			}
		} else {
			throw new Exception(EXCEPTION_TESTGEN);
		}
	}

	/**
	 * 
	 * @param accessToken
	 * @param endpoint
	 * @param isBearer
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> post(String accessToken, String endpoint, boolean isBearer,
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
		String jsonRequest = composeJson(request);
		StringEntity entity = new StringEntity(jsonRequest);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_CONTENT_TYPE,
				Constants.Header.HEADER_VALUE_APPLICATION_JSON);
		oHttpAuthPost.addHeader(Constants.Header.HEADER_NAME_AUTHORIZATION, authValue);

		oHttpAuthPost.setEntity(entity);
		final String log = String.format("HttpPost with endpoint %s and request: %s", endpoint, jsonRequest);
		LOGGER.fatal(log);
		HttpResponse oHttpAuthResponse = oHttpClient.execute(oHttpAuthPost);
		if (oHttpAuthResponse != null) {
			int statusCode = oHttpAuthResponse.getStatusLine().getStatusCode();
			final String reason = oHttpAuthResponse.getStatusLine().getReasonPhrase();
			if (statusCode != OK) {
				final String exception = String
						.format("Errore! Chiamata Post in errore StatusCode: %s ReasonPhrase: %s", statusCode, reason);
				throw new Exception(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format("Status code: %s Response: %s", statusCode, sResponse);
				LOGGER.fatal(logz);
				return oGson.fromJson(sResponse, Map.class);
			} else {
				throw new Exception(EXCEPTION_TESTGEN);
			}
		} else {
			throw new Exception(EXCEPTION_TESTGEN);
		}
	}

	private static String composeJson(Map<String, Object> request) {
		return new Gson().toJson(request);
	}

}
