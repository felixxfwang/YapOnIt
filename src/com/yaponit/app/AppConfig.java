package com.yaponit.app;

import java.io.File;

public class AppConfig {
	private static final String BASE_URL = "http://www.yaponit.com/";
	public static final String POST_GET_URL = BASE_URL + "voiceservice/posts";
	public static final String COMMENT_GET_URL = BASE_URL + "voiceservice/comment";
	public static final String LOGIN_URL=BASE_URL+"voiceservice/authorize";

	public static final String COMMENT_POST_URL = BASE_URL+"voiceservice/comment";
	
	public static final String X_TOKEN_NAME ="X-Api-Key";
	public static final String X_TOKEN_VALUE ="aaaabbbbccccdddd";
	public static final String AUTH_TOKEN_NAME = "X-Auth-Token";
	public static String AccessToken = null;
	public static String FirstName = null;
	
	public static File videoFile = null;
}
