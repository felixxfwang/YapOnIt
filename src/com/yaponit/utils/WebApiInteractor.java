package com.yaponit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.yaponit.app.AppConfig;
import com.yaponit.entity.Comment;
import com.yaponit.entity.HttpResult;
import com.yaponit.entity.LoginUser;
import com.yaponit.entity.Post;

public class WebApiInteractor {
	private final int HTTP_200 = 200;
	Context context;

	public WebApiInteractor(Context context){
		this.context = context;
	}
	
	public boolean preRequest() {
		boolean netAvailable = NetworkProber.isNetworkAvailable(context);
		if (!netAvailable) {
			Toast.makeText(context, "Network is not available",
					Toast.LENGTH_LONG).show();
		}
		return netAvailable;
	}

	public LoginUser login(String userName, String password) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", userName));
		params.add(new BasicNameValuePair("pwd", password));
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair(AppConfig.X_TOKEN_NAME,
				AppConfig.X_TOKEN_VALUE));

		HttpResult result = post(AppConfig.LOGIN_URL, params, headers);
		if (result.getSuccess()) {
			LoginUser user = JsonParser.getLoginUser(result.getResult());
			return user;
		}
		return null;
	}

	public ArrayList<Post> queryPosts(int page,String order,String orderby, String type) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("page", page + ""));
		params.add(new BasicNameValuePair("order", order));
		params.add(new BasicNameValuePair("orderby", orderby));
		params.add(new BasicNameValuePair("type",type));
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		String postJson = post(AppConfig.POST_GET_URL, params, headers)
				.getResult();
		return JsonParser.parsePosts(postJson);
	}

	public ArrayList<Comment> queryComments(String id, int page) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("page", page + ""));
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		String commentJson = get(AppConfig.COMMENT_GET_URL, params, headers)
				.getResult();
		return JsonParser.parseComments(commentJson);
	}

	public boolean sendTextResponse(int postId, int parentId, String comment) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("postId", postId + ""));
		params.add(new BasicNameValuePair("parentId", parentId + ""));
		params.add(new BasicNameValuePair("type", "text"));
		params.add(new BasicNameValuePair("content", comment));
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		return post(AppConfig.COMMENT_POST_URL, params, headers).getSuccess();

	}

	public boolean sendRecordResponse(int postId, int parentId, String type,File recordFile) {
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("postId", postId + ""));
		params.add(new BasicNameValuePair("parentId", parentId + ""));
		params.add(new BasicNameValuePair("type", type));

		try {
			FileInputStream in = new FileInputStream(recordFile);
			byte[] buffer = new byte[(int) recordFile.length() + 100];
			int length = in.read(buffer);
			String comment = Base64.encodeToString(buffer, 0, length,
					Base64.DEFAULT);
			params.add(new BasicNameValuePair("content", comment));
			return post(AppConfig.COMMENT_POST_URL, params, headers)
					.getSuccess();
		} catch (IOException e) {
		}
		return false;

	}	

	private HttpResult get(String url, ArrayList<NameValuePair> params,
			ArrayList<NameValuePair> headers) {
		url += "?";
		for (NameValuePair param : params) {
			url += param.getName() + "=" + param.getValue() + "&";
		}
		url = url.substring(0, url.length() - 1);
		// 锟铰斤拷HttpGet锟斤拷锟斤拷
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader(AppConfig.AUTH_TOKEN_NAME, AppConfig.AccessToken);

		for (NameValuePair header : headers) {
			if (header != null) {
				httpGet.addHeader(header.getName(), header.getValue());
			}
		}
		HttpClient httpClient = new DefaultHttpClient();// 锟斤拷取HttpClient锟斤拷锟斤拷
		try {
			HttpResponse httpResp = httpClient.execute(httpGet);// 锟斤拷取HttpResponse实锟斤拷
			int statusCode = httpResp.getStatusLine().getStatusCode();// 锟叫讹拷锟角癸拷锟斤拷锟斤拷晒锟�
			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");// 锟斤拷取锟斤拷锟截碉拷锟斤拷锟�
			Log.v("code", statusCode + "");
			Log.v("message", result);
			return new HttpResult(statusCode == HTTP_200, result);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HttpResult(false, null);
	}

	public HttpResult post(String url, ArrayList<NameValuePair> params,
			ArrayList<NameValuePair> headers) {
		HttpPost httpPost = new HttpPost(url);// 锟铰斤拷HttpPost锟斤拷锟斤拷
		for (NameValuePair header : headers) {
			if (header != null) {
				httpPost.addHeader(header.getName(), header.getValue());
			}
		}
		httpPost.addHeader(AppConfig.AUTH_TOKEN_NAME, AppConfig.AccessToken);
		try {
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);// 锟斤拷锟斤拷锟街凤拷
			httpPost.setEntity(entity);// 锟斤拷锟矫诧拷锟斤拷实锟斤拷

			HttpClient httpClient = new DefaultHttpClient();// 锟斤拷取HttpClient锟斤拷锟斤拷
			HttpResponse httpResp = httpClient.execute(httpPost);// 锟斤拷取HttpResponse实锟斤拷

			int statusCode = httpResp.getStatusLine().getStatusCode();// 锟叫讹拷锟角癸拷锟斤拷锟斤拷晒锟�
			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");// 锟斤拷取锟斤拷锟截碉拷锟斤拷锟�
			Log.v("code", statusCode + "");
			Log.v("message", result);
			return new HttpResult(statusCode == HTTP_200, result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HttpResult(false, null);
	}
}