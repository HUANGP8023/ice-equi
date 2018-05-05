package com.ice.utils;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月18日
 * @Desc HTTPCLIENT 请求工具包
 */
public class HttpClientUtils {

	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static SSLConnectionSocketFactory sslsf = null;
	private static PoolingHttpClientConnectionManager cm = null;
	private static SSLContextBuilder builder = null;

	static {
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			});
			sslsf = new SSLConnectionSocketFactory(builder.build(),
					new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register(HTTP, new PlainConnectionSocketFactory()).register(HTTPS, sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param header
	 * @param params
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, String> header, Map<String, String> params,
			HttpEntity entity) throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			// 设置头信息
			if (null != header && header.size() != 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (null != params && params.size() != 0) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 设置实体 优先级高
			if (entity != null) {
				httpPost.setEntity(entity);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, String> params) throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			// 设置请求参数
			if (null != params && params.size() != 0) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}
	
	
	public static String httpUploadFile(String httpUrl,MultipartFile file) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(httpUrl);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", file.getInputStream(),ContentType.MULTIPART_FORM_DATA, fileName);
            builder.addTextBody("filename", fileName);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
	   

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpUrl
	 * @param header
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String httpUrl, Map<String, String> header, Map<String, String> params)
			throws Exception {
		String result = "";
		CloseableHttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
			HttpPost httpPost = new HttpPost(httpUrl);
			// 设置头信息
			if (null != header && header.size() != 0) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (null != params && params.size() != 0) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				readHttpResponse(httpResponse);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		return result;
	}

	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月20日
	 * @Desc
	 *
	 * @param httpUrl 请求地址
	 * @param params 请求头参数
	 * @return
	 * @throws Exception
	 */
	 public static String httpGet(String httpUrl,Map<String, Object> params) throws Exception {
	        StringBuffer param = new StringBuffer();
	        int i = 0;
	        for (String key : params.keySet()) {
	            if (i == 0)
	                param.append("?");
	            else
	                param.append("&");
	            param.append(key).append("=").append(params.get(key));
	            i++;
	        }
	        httpUrl += param;
	        String result = null;
	        CloseableHttpClient httpClient = getHttpClient();
	        try {
	            HttpGet httpGet = new HttpGet(httpUrl);
	            HttpResponse response = httpClient.execute(httpGet);
	            int statusCode = response.getStatusLine().getStatusCode();
	            
	            if (statusCode == HttpStatus.SC_OK) {
					HttpEntity resEntity = response.getEntity();
					result = EntityUtils.toString(resEntity);
				} else {
					readHttpResponse(response);
				}
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
				if (httpClient != null) {
					httpClient.close();
				}
			}
	        return result;
	    }
	
	

	public static CloseableHttpClient getHttpClient() throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(cm)
				.setConnectionManagerShared(true).build();
		return httpClient;
	}

	/**
	 * @Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String readHttpResponse(HttpResponse httpResponse) throws ParseException, IOException {
		StringBuilder builder = new StringBuilder();
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		builder.append("status:" + httpResponse.getStatusLine());
		builder.append("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			builder.append("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			builder.append("response length:" + responseString.length());
			builder.append("response content:" + responseString.replace("\r\n", ""));
		}
		return builder.toString();
	}

	public static void main(String[] args) throws Exception {
		String httpUrl = "http://127.0.0.1:8081/test/initSession";
		String str = httpPost(httpUrl, null, null, null);
		System.out.println("======:" + str);
		Map<String, String> header = new HashMap<String, String>();
		header.put("x-auth-token", str);
		httpUrl = "http://127.0.0.1:8081/test/aa";
		System.out.println("==================================================");
		System.out.println(httpPost(httpUrl, header, null, null));
		System.out.println("==================================================");
		System.out.println(httpPost(httpUrl, header, null, null));
		System.out.println("==================================================");

	}

}