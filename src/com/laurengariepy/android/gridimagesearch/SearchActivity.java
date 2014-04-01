package com.laurengariepy.android.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	private static final int FILTER_REQUEST = 1;
	
	private EditText mEtQuery;
	private Button   mBtnSearch;
	private GridView mGvResults;
	private String   mFilterPreferences;
	
	private ArrayList<ImageResult>  mImageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter mImageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	
		setupViews();
		
		mImageAdapter = new ImageResultArrayAdapter(this, mImageResults);
		mGvResults.setAdapter(mImageAdapter); 
		
		mGvResults.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = mImageResults.get(position); 
//				i.putExtra("url", imageResult.getFullUrl());
				i.putExtra("result", imageResult);
				startActivity(i); 
			}
		});
	}

	private void setupViews() {
//		Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-thin.ttf");
		mEtQuery   = (EditText) findViewById(R.id.etQuery);
//		mEtQuery.setTypeface(font); 
		
		mBtnSearch = (Button) findViewById(R.id.btnSearch);
//		mBtnSearch.setTypeface(font);
		mBtnSearch.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mFilterPreferences = getFilterPreferences();
				String query = mEtQuery.getText().toString();
				Toast.makeText(SearchActivity.this, "Search for " + query, Toast.LENGTH_SHORT).show();
				
				AsyncHttpClient client = new AsyncHttpClient();	
				mImageResults.clear();
				makeAsyncHttpGetRequest(client, 0, query); 
			}
		});
		
		mGvResults = (GridView) findViewById(R.id.gvResults);
		mGvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMoreImages(totalItemsCount); 
			}
		});
	}
	
	private String getFilterPreferences() {
		String filterPreferences,
			   imageSize,
			   colorFilter,
			   imageType,
			   siteFilter;
		
		SharedPreferences filters = getSharedPreferences(FilterActivity.FILTERS, 0);
		
		switch(filters.getInt("image_size", 0)) {
			case 0:  imageSize = "";		      break;
			case 1:  imageSize = "&imgsz=small";  break;
			case 2:  imageSize = "&imgsz=medium"; break;
			case 3:  imageSize = "&imgsz=large";  break;
			case 4:  imageSize = "&imgsz=xlarge"; break;
			default: imageSize = "";
		}
		
		switch(filters.getInt("color_filter", 0)) {
			case 0:  colorFilter = "";                 break;
			case 1:  colorFilter = "&imgcolor=black";  break;
			case 2:  colorFilter = "&imgcolor=blue";   break;
			case 3:  colorFilter = "&imgcolor=brown";  break;
			case 4:  colorFilter = "&imgcolor=gray";   break;
			case 5:  colorFilter = "&imgcolor=green";  break;
			case 6:  colorFilter = "&imgcolor=orange"; break;
			case 7:  colorFilter = "&imgcolor=pink";   break;
			case 8:  colorFilter = "&imgcolor=purple"; break;
			case 9:  colorFilter = "&imgcolor=red";    break;
			case 10: colorFilter = "&imgcolor=teal";   break;
			case 11: colorFilter = "&imgcolor=white";  break;
			case 12: colorFilter = "&imgcolor=yellow"; break;
			default: colorFilter = ""; 
		}
		
		switch(filters.getInt("image_type", 0)) {
		    case 0:  imageType = "";                 break;
			case 1:  imageType = "&imgtype=clipart"; break;
			case 2:  imageType = "&imgtype=face";    break;
			case 3:  imageType = "&imgtype=lineart"; break;
			case 4:  imageType = "&imgtype=photo";   break;
			default: imageType = "";
		}
		
		siteFilter = filters.getString("site_filter", "");
		if (!siteFilter.equals("")) siteFilter = "&as_sitesearch=" + siteFilter;
		
		filterPreferences = imageSize + colorFilter + imageType + siteFilter;
		if (filterPreferences.equals("")) return filterPreferences;
		else return filterPreferences + "&";
	}
	
	public void loadMoreImages(int totalItemsCount) {
		String query = mEtQuery.getText().toString();
		AsyncHttpClient client = new AsyncHttpClient();
		makeAsyncHttpGetRequest(client, totalItemsCount, query);
	}
	
	/*
	 * Submits HTTP GET request via Google Image Search API and populates GridView array with results  
	 */
	private void makeAsyncHttpGetRequest(AsyncHttpClient client, int totalItemsCount, String query) {
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"
					+ mFilterPreferences + "start=" + totalItemsCount + "&v=1.0&q=" 
					+ Uri.encode(query), new JsonHttpResponseHandler() {
						@Override 
						public void onSuccess(JSONObject response) {
							JSONArray imageJsonResults = null;
							try { 
								imageJsonResults = response.getJSONObject("responseData")
										.getJSONArray("results");
								mImageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
								mImageAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = new Intent(this, FilterActivity.class);
		startActivityForResult(i, FILTER_REQUEST); 
		return true;
	}
}
