package com.laurengariepy.android.gridimagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {

	private static final long serialVersionUID = 4147933576402654047L;
	
	private String fullUrl;			// Full URL (designated url in Google Image Search API)
	private String thumbUrl;		// Thumbnail URL (designated tbUrl in Google Image Search API)
	
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl  = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl  = null;
			this.thumbUrl = null;
		}
	}
	
	public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < array.length(); i++) {
			try {
				results.add(new ImageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	
	@Override
	public String toString() {
		return this.thumbUrl;
	}
	
}
