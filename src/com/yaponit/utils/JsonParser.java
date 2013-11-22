package com.yaponit.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yaponit.entity.Comment;
import com.yaponit.entity.LoginUser;
import com.yaponit.entity.Post;

public class JsonParser {

	public static ArrayList<Post> parsePosts(String postJson) {

		// for test
//		 postJson = "[" +
//		 "{\"id\":\"1\",\"title\":\"ipsum dolor sit amet\",\"content\":\"Morbi consectetur nunc eu tellus laoreet non consectetur dolor commodo. Nullam bibendum ultricies purus, sed cursus erat bibendum vitae. Suspendisse vitae lacus libero. Donec molestie lacinia tincidunt. Suspendisse malesuada congue urna eget ultrices. Suspendisse ac dictum lectus. Fusce id viverra neque. Suspendisse feugiat, augue at bibendum porta, nunc lectus dictum justo, eu bibendum arcu urna in nisi. Vivamus aliquam pretium sem, sed varius enim luctus sed. Suspendisse eu est tristique lectus mollis blandit sit amet vel urna. Praesent et arcu sapien. In sagittis leo in ipsum vulputate elementum.Morbi consectetur nunc eu tellus laoreet non consectetur dolor commodo. Nullam bibendum ultricies purus, sed cursus erat bibendum vitae. Suspendisse vitae lacus libero. Donec molestie lacinia tincidunt. Suspendisse malesuada congue urna eget ultrices. Suspendisse ac dictum lectus. Fusce id viverra neque. Suspendisse feugiat, augue at bibendum porta, nunc lectus dictum justo, eu bibendum arcu urna in nisi. Vivamus aliquam pretium sem, sed varius enim luctus sed. Suspendisse eu est tristique lectus mollis blandit sit amet vel urna. Praesent et arcu sapien. In sagittis leo in ipsum vulputate elementum.\",\"date\":\"2012-12-25 18:00:00\",\"comments\":\"2\",\"author\":\"xufei\",\"answered\":\"true\"},"
//		 +
//		 "{\"id\":\"2\",\"title\":\"consectetur adipiscing\",\"content\":\"Nam ut nibh enim. Vestibulum tristique hendrerit sodales. Donec lectus urna, euismod vitae lobortis quis, laoreet quis felis. Donec sit amet justo eu sem sagittis ultrices. Donec eget erat libero, eget dignissim dui. Phasellus egestas, odio a laoreet pharetra, ligula dolor vehicula eros, sit amet euismod libero sapien eget ipsum. Pellentesque rutrum pretium urna, et dictum sapien ullamcorper a. Nullam in est ac diam accumsan scelerisque. Donec arcu lacus, tristique in venenatis ac, aliquam id arcu. In id nunc enim, eu lacinia neque. Nulla sed justo id magna condimentum feugiat. Maecenas a mauris lectus, non tincidunt eros. Maecenas accumsan porta interdum. Proin odio orci, pulvinar scelerisque placerat non, tincidunt quis velit. Nullam id quam ac quam dapibus tincidunt ut ut massa. Sed lacinia fermentum nisi, vitae sollicitudin libero tempus sit amet.\",\"date\":\"2012-12-25 18:00:00\",\"comments\":\"2\",\"author\":\"xufei\",\"answered\":\"true\"},"
//		 +
//		 "{\"id\":\"3\",\"title\":\"ut augue\",\"content\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sed tortor congue dolor tempor suscipit. Curabitur at diam vitae urna facilisis blandit. Ut ipsum lectus, vestibulum sed laoreet sed, viverra id orci. Mauris ac blandit justo. Vivamus vel nunc a diam venenatis tincidunt. Vivamus in ullamcorper nibh. Cras vel elit vitae ipsum sodales pharetra in ut ante. Nunc semper pellentesque mauris auctor venenatis. Duis est purus, imperdiet nec lacinia sit amet, cursus sed neque. Mauris et orci est, quis imperdiet dolor. Suspendisse non augue arcu. Donec aliquam neque ut augue porttitor suscipit.\",\"date\":\"2012-12-25 18:00:00\",\"comments\":\"2\",\"author\":\"xufei\",\"answered\":\"true\"},"
//		 +
//		 "{\"id\":\"4\",\"title\":\"Suspendisse\",\"content\":\"Nulla facilisi. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Maecenas accumsan mi at ante lobortis non pellentesque eros pharetra. Fusce blandit accumsan ante, in consequat nunc pharetra nec. Nunc mi magna, adipiscing in malesuada sit amet, feugiat ac massa. Vivamus congue nulla et lectus placerat consequat. Suspendisse nec elit ut eros consequat posuere. Sed a quam quis libero lobortis faucibus. Quisque vitae lorem augue, eget tincidunt tellus. Curabitur sed ligula ligula. Donec in leo eros. Etiam sed nibh quis erat suscipit pulvinar.\",\"date\":\"2012-12-25 18:00:00\",\"comments\":\"2\",\"author\":\"xufei\",\"answered\":\"true\"}]";
//	

		ArrayList<Post> result = new ArrayList<Post>();
		try {
			// JSONObject root = new JSONObject(postJson);
			JSONArray posts = new JSONArray(postJson);
			for (int i = 0; i < posts.length(); i++) {
				JSONObject p = (JSONObject) posts.opt(i);
				Post post = new Post(p.getInt("id"), p.getString("title"),
						p.getString("content"), p.getString("date"),
						p.getInt("comments"),p.getInt("votes"), p.getString("author"),p.getBoolean("answered"));
				result.add(post);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<Comment> parseComments(String commentJson) {

		// for test
		// commentJson = "{\"comments\":[" +
		// "{\"id\":\"1\",\"postId\":\"2\",\"content\":\"Donec vel lacus sit amet enim tincidunt volutpat.\",\"date\":\"2012-12-25 18:00:00\"},"
		// +
		// "{\"id\":\"2\",\"postId\":\"2\",\"content\":\"Quisque ornare vulputate dolor ac iaculis.\",\"date\":\"2012-12-25 18:00:00\"},"
		// +
		// "{\"id\":\"3\",\"postId\":\"2\",\"content\":\"Suspendisse vitae tempus lacus\",\"date\":\"2012-12-25 18:00:00\"},"
		// +
		// "{\"id\":\"4\",\"postId\":\"2\",\"content\":\"Phasellus vel risus id diam ullamcorper congue.\",\"date\":\"2012-12-25 18:00:00\"},]}";
		//
		ArrayList<Comment> result = new ArrayList<Comment>();
		try {
			//JSONObject root = new JSONObject(commentJson);
			JSONArray comments = new JSONArray(commentJson);
			for (int i = 0; i < comments.length(); i++) {
				JSONObject c = (JSONObject) comments.opt(i);
				Comment comment = new Comment(c.getInt("id"),
						 c.getString("content"),c.getString("author"),
						c.getString("date"),c.getBoolean("audio"));
				result.add(comment);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static LoginUser getLoginUser(String tokenJson) {
		if (tokenJson != null) {
			try {
				JSONObject token = new JSONObject(tokenJson);
				return new LoginUser(token.getString("authToken"),token.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
