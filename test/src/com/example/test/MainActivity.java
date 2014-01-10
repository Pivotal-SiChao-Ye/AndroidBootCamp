package com.example.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {
	TextView title;
	TextView release;
	
	private String searchurl;
	private static String key = "3ju33k3tweekjjkvcfbw6h9j";
	private static String url = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?page_limit=20&page=1&country=us&apikey=" + key;
	private static final String TAG_TITLE = "title";
	private static final String TAG_RELEASE = "theater";
	private static final String TAG_YEAR = "year";
	private ViewFlipper flipper;
	private float init;
	private int mode = 0;
	private int searched = 0;
	
	int movienum = 20;
	
	JSONArray js = null;
	String total;
	CustomList adapter;
	CustomList adapterSearch;
	ListView listView;
	ListView listViewSearch;
	List<RowItem> rowItems;
	List<RowItem> rowItemsSearch;
	
	ArrayList<String> movies = new ArrayList<String>();
	ArrayList<String> dates = new ArrayList<String>();
	ArrayList<String> images = new ArrayList<String>();
	ArrayList<String> searchmovies = new ArrayList<String>();
	ArrayList<String> searchdates = new ArrayList<String>();
	ArrayList<String> searchimages = new ArrayList<String>();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new JSONParse().execute();
           	
        TextView textView = (TextView) findViewById(R.id.searchbutton);
    	textView.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v){
    			search();
    		}
    	});
    	
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setInAnimation(this, android.R.anim.fade_in);
        flipper.setOutAnimation(this, android.R.anim.fade_out);
        
        rowItems = new ArrayList<RowItem>();   
        rowItemsSearch = new ArrayList<RowItem>();
        
        listView = (ListView) findViewById(R.id.listView);
        listViewSearch = (ListView) findViewById(R.id.listViewSearch);
        
        adapter = new CustomList(this, R.layout.listview, rowItems);
        adapterSearch = new CustomList(this, R.layout.listviewsearch, rowItemsSearch);
        
        listView.setAdapter(adapter);
        listViewSearch.setAdapter(adapterSearch);
        
    }
          
    private void search(){
    	System.out.println("clicked");
    	EditText edit = (EditText)findViewById(R.id.searchtxt);
    	String text = edit.getText().toString();
    	System.out.println(text);
    	text = text.replaceAll("\\s+","\\+");
    	System.out.println(text);
    	searchurl = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?q=" + text + "&page_limit=20&page=1&apikey=" + key;
    	searched = 1;
    	new JSONParse().execute();
    }
    
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
    	private ProgressDialog Dialog;
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		Dialog = new ProgressDialog(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
    		Dialog.setMessage("Fetching Data ...");
    		Dialog.setIndeterminate(false);
    		Dialog.setCancelable(false);
    		Dialog.show();
    	}
    	@Override
    	protected JSONObject doInBackground(String... args){
    		JSONParser jParser = new JSONParser();
    		JSONObject json;
    		if (searched == 0){
    			json = jParser.getJSONFromUrl(url);
    		}
    		else json = jParser.getJSONFromUrl(searchurl);
    		return json;
    	}
    	@Override
    	protected void onPostExecute(JSONObject json){
    		Dialog.dismiss();
    		String date;
    		try { 
    			js = json.getJSONArray("movies");
    			for(int i = 0; i < movienum; i++){
    				JSONObject temp = js.getJSONObject(i);
    				String movie = temp.getString(TAG_TITLE);
    				JSONObject temp2 = temp.getJSONObject("release_dates");
    				   				
    				if (temp2.has(TAG_RELEASE)){
    					date = ("Release Date: " + temp2.getString(TAG_RELEASE));
    				}
    				else date = ("Release Year: " + temp.getString(TAG_YEAR));

    				JSONObject temp3 = temp.getJSONObject("posters");
    				String picurl = temp3.getString("profile");
    				if (searched == 0){
    					movies.add(movie);
    					dates.add(date);
    					images.add(picurl);  
    				}
    				else {
    					searchmovies.add(movie);
    					searchdates.add(date);
    					searchimages.add(picurl);  
    				}
    			}
    	} catch (JSONException e){
    		e.printStackTrace();
   			}
    		new DownloadImageTask().execute();
    	}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent touchevent){
    	switch (touchevent.getAction()) {
    	case MotionEvent.ACTION_DOWN:
    		init = touchevent.getX();
    		break;
    	case MotionEvent.ACTION_UP:
    		float fin = touchevent.getX();
    		if (init > fin){
    			if (flipper.getDisplayedChild() == 1){
    				break;
    			}
    			flipper.setInAnimation(this, R.anim.in_right);
                flipper.setOutAnimation(this, R.anim.out_left);
    			flipper.showNext();
    		}
    		else {
    			if (flipper.getDisplayedChild() == 0){
    				break;
    			}
    			flipper.setInAnimation(this, R.anim.in_left);
                flipper.setOutAnimation(this, R.anim.out_right);
    			flipper.showPrevious();
    		}
    		break;
    	}
    	return false;
    }
    
    private class DownloadImageTask extends AsyncTask<String, Void, ArrayList<Bitmap>> {
    	private ImageView bmImage;
    	//private ProgressDialog Dialog;
    	
    	@Override
    	protected void onPreExecute(){
    		//Dialog = new ProgressDialog(MainActivity.this);
    		//Dialog.setMessage("Downloading images ...");
    		//Dialog.setIndeterminate(false);
    		//Dialog.setCancelable(true);
    		//Dialog.show();
    	}
    	
    	@Override
    	protected ArrayList<Bitmap> doInBackground(String... urls){
    		ArrayList<Bitmap> bit = new ArrayList<Bitmap>();
    		if (searched == 0){
    			for (int i = 0 ; i < images.size(); i++){
    				try {   				
    					bit.add(BitmapFactory.decodeStream((InputStream)new URL(images.get(i)).getContent()));   
    				} catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
    				} catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
    				}
    			}
    		}
    		else {
    			for (int i = 0 ; i < searchimages.size(); i++){
    				try {   				
    					bit.add(BitmapFactory.decodeStream((InputStream)new URL(searchimages.get(i)).getContent()));   
    				} catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
    				} catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
    				}
    			}
    		}
    		return bit;
    	}
    	
    	protected void onPostExecute(ArrayList<Bitmap> bit){
    		//Dialog.dismiss();
			System.out.println("flagggg" + bit.size());
			rowItemsSearch.clear();
			if (searched == 0){
				for (int i = 0; i < bit.size(); i++){
					RowItem item = new RowItem(movies.get(i), dates.get(i), bit.get(i), 0);
					rowItems.add(item);
				}
				adapter.notifyDataSetChanged();			
			}
			else {
				System.out.println("bitnumber = " + bit.size());
				for (int i = 0; i < bit.size(); i++){
					RowItem item = new RowItem(searchmovies.get(i), searchdates.get(i), bit.get(i), 1);
					rowItemsSearch.add(item);
				}
				adapterSearch.notifyDataSetChanged();
				searchmovies.clear();
				searchdates.clear();
				searchimages.clear();
				searched = 0;
				
			}			
		}
    }
}