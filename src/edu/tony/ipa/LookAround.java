package edu.tony.ipa;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import edu.tony.ipa.R;
import greendroid.app.GDMapActivity;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.app.AlertDialog;


public class LookAround extends GDMapActivity {
    /** Called when the activity is first created. */
	private LocationManager locationManager;
	private String provider;
	
    LinearLayout linearLayout;
    LinearLayout imageLayout;
	MapView mapView;
	MapController mapController;
	ZoomControls mZoom;
	int ipa_id;
	String ipa_img;
	int like_amount;
	//find near
	ArrayList<people_near> people_near_list = new ArrayList<people_near>();
	HashMap<Integer, people_near> people_near_list_compare = new HashMap<Integer, people_near>();
	
	@SuppressWarnings("deprecation")	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.look_around);
        
        linearLayout = (LinearLayout) findViewById(R.id.zoomview);
        imageLayout= (LinearLayout) findViewById(R.id.linearLayout1);
        mapView = (MapView) findViewById(R.id.mapview);
        mZoom = (ZoomControls) mapView.getZoomControls();
        linearLayout.addView(mZoom);
        
        mapController =mapView.getController();	//³]©w±±¨îªºmapª«¥ó
		//get location
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
	
		setupMap();
        
		addBtn(people_near_list);
    }
	
	
	//for image button!!
    public static Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
                url = new URL(imgUrl);
                InputStream is = url.openConnection().getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bitmap = BitmapFactory.decodeStream(bis);
                bis.close();
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return bitmap;
    }

	
	MyLocationOverlay mylayer;
	
	private void setupMap() {
		//¥[¤J©w¦ì¼h
		List<Overlay> overlays = mapView.getOverlays();	//¦b¦a¹Ï¤W«Ø¥ß¤@­Ó®y¼Ð¼h
		mylayer = new MyLocationOverlay(this, mapView);	//«Ø¥ß©w¦ì¼h¡A¨Ã¨ú±o¥Ø«e®y¼Ð¦ì¸m
		mylayer.runOnFirstFix(new Runnable() {		//¨C¦¸§ó·s®É­n°õ¦æªº°Ê§@
			public void run() {
				mapView.setSatellite(false) ;//³]©w¦a¹ÏÀË¥Ü¼Ò¦¡
				//.setTraffic(true)¡G¤@¯ë¦a¹Ï
				//.setSatellite(true)¡G½Ã¬P¦a¹Ï
				//.setStreetView¡Gµó´º¹Ï
 
				mapController.setZoom(17);	//³]©w©ñ¤j­¿²v1(¦a²y)-21(µó´º)
				mapController.animateTo(mylayer.getMyLocation());	//«ü©w¦a¹Ï¤¤¥¡ÂI¬°¥Ø«e¦ì¸m
			}
		});
		mapView.setBuiltInZoomControls(true);	//¥[¤JÁY©ñ±±¨î
		
		//catch own position
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider); 
		Double own_lat = location.getLatitude();
		Double own_lng = location.getLongitude();
		
		//§äªþªñ©±®a¸ê°T(all) + transform
		ArrayList<Position_transform> pos_array = new ArrayList<Position_transform>();
		ArrayList<String> stores_in_db = new ArrayList<String>();
		
		
		DB db = new DB();
		try{
			Double a =own_lat, b=own_lng;
			//Double a =25.019943, b=121.542353;
			ArrayList<JSONObject> result = db.LocSearch(a,b);
			
			for(int i=0;i<result.size();i++){
				Position_transform pos = 
					new Position_transform(	result.get(i).getString("name"), 
											result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"), 
											result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng")) ;
				pos_array.add(pos);
			}
			//Check for correctness
			/*
			for(int i=0;i<pos_array.size();i++){
				Log.e("pos_name = ",pos_array.get(i).location_name);
				Log.e("pos_lat = ",pos_array.get(i).location_lat.toString());
				Log.e("pos_lon = ",pos_array.get(i).location_lon.toString());
			}*/
			
			//Check ¬O§_¬°§Ú­Ìªº¦X§@¼t°Ó
						
			ArrayList<NameValuePair> location_1 = new ArrayList<NameValuePair>();
			ArrayList<JSONObject> shop_loc = new ArrayList<JSONObject>();
			ArrayList<JSONObject> friend = new ArrayList<JSONObject>();
			
			for(int i=0; i<pos_array.size();i++){
				location_1.add(new BasicNameValuePair("Lng",pos_array.get(i).location_lon.toString()));
				location_1.add(new BasicNameValuePair("Lat",pos_array.get(i).location_lat.toString()));
				
				shop_loc = db.DataSearch(location_1,"shop_loc_search");
				
				friend = db.DataSearch(location_1,"friend_search");
				
				for(int j=0;j<shop_loc.size();j++){
					//Log.e("shop_id",shop_loc.get(j).getString("shopID"));
					stores_in_db.add(shop_loc.get(j).getString("shopID"));
				}
				//§äªþªñIPAªº¹Ï(People_near¬Oglobal variable)
				for(int j=0;j<friend.size();j++){
					//Log.e("ipa_id",friend.get(j).getString("accountID"));
					//people_near_id.add(friend.get(j).getInt("ipaID") );
					//people_near_img.add(friend.get(j).getString("img"));
					people_near neighbor = new people_near(friend.get(j).getInt("ipaID"),friend.get(j).getString("img"), friend.get(j).getInt("likenum"));
					if(!people_near_list_compare.containsKey(friend.get(j).getInt("ipaID"))){
						people_near_list_compare.put(friend.get(j).getInt("ipaID"), neighbor);
						people_near_list.add(neighbor);
					}
					
					
				}
			}
			
			
			//check for Stores_in_db¬O§_¼g¤J
			/*
			for(int i=0; i<stores_in_db.size(); i++){
				Log.e("shop_id = ",stores_in_db.get(i));
			}*/
			
		}
		catch(Exception e){
			Log.e("log_tag", "Error get data "+e.toString());				
		}	
				
		//«Å§i¦a¼Ð¹Ï¥Üfrom¤º«Ø¹Ï®w
		Drawable point_star = getResources().getDrawable(android.R.drawable.star_on);
		//³]©w¬P¬P¹Ï¥Ü¤j¤p
		point_star.setBounds(0, 0, point_star.getMinimumWidth(), point_star.getMinimumHeight());
		Landmark myLandmark = new Landmark(point_star, this);
		myLandmark.getpostions(stores_in_db);
		
		
		overlays.add(myLandmark);	//±N¦a¼Ð¼h¥[¤J¦a¹Ï®y¼Ð¼h¤¤
		overlays.add(mylayer);	//±N©w¦ì¼h¥[¤J¦a¹Ï®y¼Ð¼h¤¤
	}
 
	@Override
	protected void onResume() {
		super.onResume();
		mylayer.enableMyLocation();	//¶i¤J­¶­±®É¶}©l§ó·s©w¦ì¸ê°T
	}
 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mylayer.disableMyLocation();	//Â÷¶}­¶­±®É°±¤î§ó·s
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
		
	public void addBtn(final ArrayList<people_near> people_near_list){
    	if(people_near_list.size() > 0){
    		
    		for(int i = 0; i < people_near_list.size(); i++){
    			
    			ImageButton tempBtn = new ImageButton(this);
    			
    			if(!people_near_list.get(i).img.equals("")){
    				Bitmap bitmap = getBitmapFromUrl(people_near_list.get(i).img);
    				tempBtn.setImageBitmap(bitmap);
    				tempBtn.setAdjustViewBounds(true);
    				tempBtn.setId(Integer.valueOf(people_near_list.get(i).ipaID));
    				    				
    				tempBtn.setId(i);
    				tempBtn.setOnClickListener(new View.OnClickListener() {
    					public void onClick(View v) {
    						int id = v.getId();
       	            	 	// setActivityDetail(id);
       	            	 	Intent intent = new Intent();
       	            	 	intent.setClass(LookAround.this, people_info_image.class);
       	            	 	intent.putExtra("ipaID", people_near_list.get(id).ipaID);
       	            	 	intent.putExtra("img", people_near_list.get(id).img);
       	            	 	intent.putExtra("likenum", people_near_list.get(id).likenum);
       	            	 	
       	            	 	startActivity(intent);
       	            	 
       	             }
    				});
    				
    				
    			}

    			imageLayout.addView(tempBtn);
    		}
    	}
    	
    }

	
}

