package java.http.call;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.constants.Constants;
import java.http.CHttpClient;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

/**
 * Classe utilizzata per effettuare una chiamata Get
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
	public static Map<String, String> get(boolean withAuthorization, boolean isBearer, String accessToken,
			String endpoint) throws Exception {
		return withAuthorization ? get(accessToken, endpoint, isBearer) : get(endpoint);
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
	public static Map<String, String> get(boolean withAuthorization, String accessToken, String endpoint)
			throws Exception {
		return withAuthorization ? get(accessToken, endpoint, false) : get(endpoint);
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
				throw new Exception(exception);
			}
			HttpEntity oHttpAuthEntity = oHttpAuthResponse.getEntity();
			if (oHttpAuthEntity != null) {
				String sResponse = EntityUtils.toString(oHttpAuthEntity);
				EntityUtils.consume(oHttpAuthEntity);
				final String logz = String.format("Status code: %s Response: %s", statusCode, sResponse);
				LOGGER.fatal(logz);
				return new Gson().fromJson(sResponse, Map.class);
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

}
