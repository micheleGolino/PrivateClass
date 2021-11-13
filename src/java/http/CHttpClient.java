package java.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Client Http che wrappa {@link HttpClient} ed accetta tutti i certificati
 * @author MicheleGolino
 */
public class CHttpClient {

	private String proxyIpAddress;
	private String hostname;
	private int proxyPort;
	private int port;
	private DefaultHttpClient client;
	private StatusLine lastStatus;

	public CHttpClient() {
		client = new DefaultHttpClient();
		acceptCertificates();
	}

	public CHttpClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		client = new DefaultHttpClient();
		acceptCertificates();
	}

	public CHttpClient(String hostname, int port, String proxyIpAddress, int proxyPort) {
		this.hostname = hostname;
		this.port = port;
		this.proxyIpAddress = proxyIpAddress;
		this.proxyPort = proxyPort;

		client = new DefaultHttpClient();

		if (proxyIpAddress != null) {
			HttpHost proxy = new HttpHost(proxyIpAddress, proxyPort, HttpHost.DEFAULT_SCHEME_NAME);

			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		acceptCertificates();
	}

	public CHttpClient(String hostname, int port, String proxyIpAddress, int proxyPort, String proxyUsername,
			String proxyPassword, String workstation, String domain) {
		init(hostname, port, proxyIpAddress, proxyPort, proxyUsername, proxyPassword, workstation, domain);
	}

	public CHttpClient(String hostname, int port, String proxyIpAddress, int proxyPort, String proxyUsername,
			String proxyPassword) {
		init(hostname, port, proxyIpAddress, proxyPort, proxyUsername, proxyPassword, null, null);
	}

	private void init(String hostname, int port, String proxyIpAddress, int proxyPort, String proxyUsername,
			String proxyPassword, String workstation, String domain) {
		this.hostname = hostname;
		this.port = port;
		this.proxyIpAddress = proxyIpAddress;
		this.proxyPort = proxyPort;

		client = new DefaultHttpClient();

		if (proxyIpAddress != null) {
			client.getCredentialsProvider().setCredentials(new AuthScope(proxyIpAddress, proxyPort),
					(workstation != null && domain != null)
							? new NTCredentials(proxyUsername, proxyPassword, workstation, domain)
							: new UsernamePasswordCredentials(proxyUsername, proxyPassword));

			HttpHost proxy = new HttpHost(proxyIpAddress, proxyPort, HttpHost.DEFAULT_SCHEME_NAME);
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		acceptCertificates();
	}

	private void acceptCertificates() {
		try {
			SSLContext sc;
			sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, getTrustingManager(), new SecureRandom());
			SSLSocketFactory socketFactory = new SSLSocketFactory(sc);
			Scheme scheme = new Scheme("https", socketFactory, 443);
			client.getConnectionManager().getSchemeRegistry().register(scheme);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	private TrustManager[] getTrustingManager() {
		return new TrustManager[] { new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}

			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

			}

			public X509Certificate[] getAcceptedIssuers() {

				return null;
			}
		} };
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
	}

	public HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return client.execute(request);
	}

	public String doRequest(HttpUriRequest request) throws ParseException, IOException {
		HttpEntity entity = null;
		try {
			HttpResponse response = client.execute(request);
			entity = response.getEntity();
			lastStatus = response.getStatusLine();

			return EntityUtils.toString(entity);
		} finally {
			try {
				if (entity != null)
					entity.consumeContent();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProxyIpAddress() {
		return proxyIpAddress;
	}

	public void setProxyIpAddress(String proxyIpAddress) {
		this.proxyIpAddress = proxyIpAddress;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public StatusLine getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(StatusLine lastStatus) {
		this.lastStatus = lastStatus;
	}
}
