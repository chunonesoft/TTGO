package com.chunsoft.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetJsonData {
	private HttpClient httpClient;
	private HttpResponse response;
	private HttpPost httpPost;
	private HttpEntity httpEntity;
	private String URL;
	private JSONObject server_data;
	private JSONArray array_serverdata;
	private StringBuffer sb;
	private BufferedReader br;
	private String str;

	public JSONObject getJSONObjectDataH(String url, JSONObject sendData) {
		URL = Constant.IP + url;
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(URL);
		try {
			httpPost.setEntity(new StringEntity(sendData.toString()));

			response = httpClient.execute(httpPost);
			httpEntity = response.getEntity();
			br = new BufferedReader(new InputStreamReader(
					httpEntity.getContent()));
			str = null;
			sb = new StringBuffer();

			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			server_data = new JSONObject(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server_data;
	}

	public JSONObject getJSONObjectDataN(String url) {
		URL = Constant.IP + url;
		System.out.println(URL);

		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(URL);

		try {
			httpEntity = response.getEntity();
			br = new BufferedReader(new InputStreamReader(
					httpEntity.getContent()));
			str = null;
			sb = new StringBuffer();

			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			server_data = new JSONObject(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server_data;
	}

	public JSONArray getJSONArrayDataN(String url) {
		URL = Constant.IP + url;

		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(URL);

		try {
			response = httpClient.execute(httpPost);
			httpEntity = response.getEntity();
			br = new BufferedReader(new InputStreamReader(
					httpEntity.getContent()));
			str = null;
			sb = new StringBuffer();
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			array_serverdata = new JSONArray(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array_serverdata;
	}

	public JSONArray getJSONArrayDataH(String url, JSONObject sendData) {
		URL = Constant.IP + url;

		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(URL);

		try {

			httpPost.setEntity(new StringEntity(sendData.toString()));
			response = httpClient.execute(httpPost);
			httpEntity = response.getEntity();
			br = new BufferedReader(new InputStreamReader(
					httpEntity.getContent()));
			str = null;
			sb = new StringBuffer();
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			array_serverdata = new JSONArray(sb.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array_serverdata;
	}
}
