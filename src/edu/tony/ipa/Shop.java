package edu.tony.ipa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Shop extends Activity {
	/** Called when the activity is first created. */
	private ArrayList<JSONObject> result_a;
	private ArrayList<JSONObject> result_s;
	private ArrayList<JSONObject> result_s2;
	private DB db;
	private List<String> activityDetailList;
	private List<String> activityList;
	private List<String> shopIDList;
	private LinearLayout linearLayout;
	private LocationManager locationManager;
	private String provider;
	private int x;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);
        x=0;
        linearLayout = (LinearLayout) findViewById(R.id.shoplayout);
        activityList = new ArrayList<String>();
        shopIDList = new ArrayList<String>();
        activityDetailList = new ArrayList<String>();
        
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double b = location.getLongitude();
        double a = location.getLatitude();
        
        Log.e("a",String.valueOf(a));
        Log.e("b",String.valueOf(b));
        db = new DB();
        
        try{
            
            ArrayList<JSONObject> result = db.LocSearch(a,b);
    		Log.e("log_loc","size="+result.size());
    		for(int i=0;i<result.size();i++){
    			Log.e("r_loc",result.get(i).getString("name"));
    			Log.e("r_loc","lat "+
    				result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
    			Log.e("r_loc","lng "+
    					result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
    			double la = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
    			double lo = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
    			getshopfromwhere(la,lo);
    			//getshopfromwhere(64.12334555,122.63445252);
    			
    		}
            }
            catch(Exception e){
        		Log.e("log_tag", "Error get data "+e.toString());				
        		}
        
        //getshopfromwhere(64.12334555,122.63445252);
        //getActivity();
            
            /*
        activityList.add("123123123");
        activityDetailList.add("zzzzz");
        shopIDList.add("100011");
        
        activityList.add("aaasds");
        activityDetailList.add("222");
        shopIDList.add("100012");*/
        addBtn(activityList.size(),activityList);
        
	}
	public void getshopfromwhere(double a,double b)
	{
		try{
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Lng",String.valueOf(b)));
			nameValuePairs.add(new BasicNameValuePair("Lat",String.valueOf(a)));
			
			result_s = db.DataSearch(nameValuePairs, "shop_loc_search");

			//Log.e("m",nameValuePairs.toString());
			Log.e("shopnum","size="+result_s.size());
			for(int i=0;i<result_s.size();i++){
				Log.e("shopid",result_s.get(i).getString("shopID"));
				//shopIDList.add(result_s.get(i).getString("shopID"));
				
				getActivity(result_s.get(i).getString("shopID"));
			}
			
			
        	
        }catch(Exception e){
    		Log.e("log_tag", "Error get data "+e.toString());				
    		}	
	}
	
	
    public void getActivity(String a){
    	try{
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("ShopID",a));
			

			result_a = db.DataSearch(nameValuePairs,"activity_search");
			
			
			
			Log.e("log_act","size="+result_a.size());
			for(int i=0;i<result_a.size();i++){
				Log.e("r_act",result_a.get(i).getString("activityName"));
				activityList.add(result_a.get(i).getString("activityName"));
	
				activityDetailList.add(result_a.get(i).getString("description"));
				
				shopIDList.add(a);
			}
			
			
        	
        }catch(Exception e){
    		Log.e("log_taga", "Error get data "+e.toString());				
    		}	
        
    }
    
    public void addBtn(int count,List<String> activityName){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	if(count > 0){
    		int k;
    		for(int i = 0; i < count; i++){
    			Button tempBtn = new Button(this);
    			ListView list = new ListView(this);
    			tempBtn.setText(activityName.get(i));
    			tempBtn.setId(Integer.valueOf(shopIDList.get(i)));
    			final String des = activityDetailList.get(i);
    			//¡Ÿ≠nºg´ˆButton™∫∞ ß@
    			tempBtn.setOnClickListener(new View.OnClickListener() {
    	             public void onClick(View v) {
    	            	 int id = v.getId();
    	            	// setActivityDetail(id);
    	            	 Intent detailintent = new Intent();
    	            	 detailintent.setClass(Shop.this, ShopDetail.class);
    	            	 Bundle bundle = new Bundle();
    	            	 bundle.putString("ID", String.valueOf(id));
    	            	 
    	            	 detailintent.putExtras(bundle);
    	            	 try{
    	            	 ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    	 				 nameValuePairs.add(new BasicNameValuePair("ShopID", String.valueOf(id)));
    	 				
    	 				 result_s2 = db.DataSearch(nameValuePairs,"shop_search");
    	            	 
    	 				final Bundle bundle1 = new Bundle();
   	            	    bundle1.putString("lat", result_s2.get(0).getString("latitude"));
   	            	    bundle1.putString("lng", result_s2.get(0).getString("longitude"));
   	            	    bundle1.putString("ID", String.valueOf(id));
   	            	    //detailintent.putExtras(bundle1);
    	 				 
    	 				 
    	            	 
    	            	 builder.setMessage("店名:"+result_s2.get(0).getString("shopName")+ "\n" 
    	            			 +"分店:"+result_s2.get(0).getString("branch")+ "\n" 
    	            			 +"地址:"+result_s2.get(0).getString("address")+ "\n"
    	            			 +"電話:"+result_s2.get(0).getString("phone")+ "\n"
    	            			 +"描述:"+result_s2.get(0).getString("description")+ "\n"+ "\n"
    	            			 +"條件:"+des+ "\n"
    	            			 );
    	            	 
    	            	 builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
						 
    	            		 
    	            		 
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
    	            	 
    	            	 builder.setNegativeButton("地圖", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent map = new Intent();
		    	            	map.setClass(Shop.this, Shopmap.class);
		    	            	
		    	            	map.putExtras(bundle1);
		    	            	startActivity(map);
		    	            	Log.e("wdwd", "1111");
							}
						});
    	            	 
    	            	 
    	            	 builder.show();
    	            	 }//startActivity(detailintent);
    	            	 catch(Exception e){
    	     	    		Log.e("log_tag", "Error get data "+e.toString());				
    	     	    		}
    	            	 
    	             }
    	         });
                
    			linearLayout.addView(tempBtn, 300, 100);
    			linearLayout.setGravity(17);
    		}
    	}
    	
    }

}
