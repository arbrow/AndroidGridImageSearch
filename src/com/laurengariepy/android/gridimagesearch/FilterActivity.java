package com.laurengariepy.android.gridimagesearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Selection;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterActivity extends ActionBarActivity {
	
	protected static final String FILTERS = "FilterPreferences";
	
	private Spinner  mSpnImageSize,
	 			     mSpnColorFilter,
	 			     mSpnImageType;
	private EditText mEtSiteFilter;
	private Button   mBtnSave;
	
	private ArrayAdapter<CharSequence> aImageSizeAdapter,
							   	   	   aColorFilterAdapter,
							           aImageTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		
		setupViews();
		restoreFilterPreferences();
		setCursor();
	}

	private void setupViews() {
		mSpnImageSize = (Spinner) findViewById(R.id.spnImageSize); 
		aImageSizeAdapter = ArrayAdapter.createFromResource(this, R.array.image_size_options, 
				android.R.layout.simple_spinner_item);
		aImageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnImageSize.setAdapter(aImageSizeAdapter); 
		
		mSpnColorFilter = (Spinner) findViewById(R.id.spnColorFilter); 
		aColorFilterAdapter = ArrayAdapter.createFromResource(this, R.array.color_filter_options,
				android.R.layout.simple_spinner_item); 
		aColorFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		mSpnColorFilter.setAdapter(aColorFilterAdapter); 
		
		mSpnImageType = (Spinner) findViewById(R.id.spnImageType);
		aImageTypeAdapter = ArrayAdapter.createFromResource(this, R.array.image_type_options, 
				android.R.layout.simple_spinner_item);
		aImageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpnImageType.setAdapter(aImageTypeAdapter); 
		
		mEtSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		
		mBtnSave = (Button) findViewById(R.id.btnSave);
		mBtnSave.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
			    setResult(RESULT_OK, i);
			    finish();
			}
		});
	}
	
	private void restoreFilterPreferences() {
		SharedPreferences filters = getSharedPreferences(FILTERS, 0); 
		
		int imageSizeSelection = filters.getInt("image_size", 0);  
		mSpnImageSize.setSelection(imageSizeSelection); 
		
		int colorFilterSelection = filters.getInt("color_filter", 0); 
		mSpnColorFilter.setSelection(colorFilterSelection);
		
		int imageTypeSelection = filters.getInt("image_type", 0); 
		mSpnImageType.setSelection(imageTypeSelection);
		
		String siteFilterSelection = filters.getString("site_filter", mEtSiteFilter.getHint().toString()); 
		mEtSiteFilter.setText(siteFilterSelection);
	}
	
	private void setCursor() {
		int position = mEtSiteFilter.length();
		Selection.setSelection(mEtSiteFilter.getText(), position); 
	}
	
	@Override
	protected void onPause() {
		super.onPause(); 
		saveFilterPreferences(); 
	}
	
	private void saveFilterPreferences() {
		SharedPreferences filters = getSharedPreferences(FILTERS, 0);
		SharedPreferences.Editor editor = filters.edit();
		editor.putInt("image_size", mSpnImageSize.getSelectedItemPosition()); 
		editor.putInt("color_filter", mSpnColorFilter.getSelectedItemPosition());
		editor.putInt("image_type", mSpnImageType.getSelectedItemPosition()); 
		editor.putString("site_filter", mEtSiteFilter.getText().toString()); 
		editor.commit(); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filter, menu);
		return true;
	}

}
