package edu.tony.ipa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class myCoupon extends GDActivity {

	private LinearLayout linearLayout;
	private DB db;
	private List<String> couponName;
	private String user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	   	
	    final int ACTION_BAR_INFO = 0;//•[§Wcheckin button in action bar
	    addActionBarItem(Type.LocateMyself, ACTION_BAR_INFO);
	       
	    setActionBarContentView(R.layout.mycoupon);
	    linearLayout = (LinearLayout) findViewById(R.id.couponLayout);
	    db = new DB();
	    couponName = new ArrayList<String>();
	    
	    SharedPreferences settings = getSharedPreferences("Account", 0);
	    user = settings.getString("username", "nouser");
	    Log.e("user", user);
	    
	    showCoupon();
	    
	}
	
	public void addBtn(int count,final List<String> couponName,final String AccID){
    	
    	if(count > 0){
    		int k;
    		for(int i = 0; i < count; i++){
    			Button tempBtn = new Button(this);
    			//ßÏCouponßπæ„∏Í∞T
    			try{
    				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    				nameValuePairs.add(new BasicNameValuePair("CouponID",couponName.get(i)));
    				
    				//ß‰coupon∏Í∞T
    				ArrayList<JSONObject> result_a = db.DataSearch(nameValuePairs,"coupon_search");
    				Log.e("log_act","size="+result_a.size());
//    				for(int j=0;j<result_a.size();j++){
//    					Log.e("r_act",result_a.get(j).getString("shopID"));
//    				}
    
    				//ß‰shop∏Í∞T
    				ArrayList<NameValuePair> shop_nameValuePairs = new ArrayList<NameValuePair>();
    				shop_nameValuePairs.add(new BasicNameValuePair("ShopID",result_a.get(0).getString("shopID")));

    				ArrayList<JSONObject> result_b = db.DataSearch(shop_nameValuePairs,"shop_search");
    				Log.e("log_act2","size="+result_b.size());
    				
    				tempBtn.setHeight(500);
    				tempBtn.setBackgroundResource(R.drawable.mycoupon);
    				tempBtn.setPadding(0, 20, 0, 0);
    				
    				tempBtn.setText(result_b.get(0).getString("shopName")+" 分店："+result_b.get(0).getString("branch")+"\n"+ result_a.get(0).getString("couponName") + "\n" + result_a.get(0).getString("startDate").substring(0, 10)+"~"+ result_a.get(0).getString("endDate").substring(0, 10)+"\n說明:"+result_a.get(0).getString("description"));
    				
    				
    			}
    			catch(Exception e){
    				Log.e("log_tag", "Error get data "+e.toString());				
    			}	
     			
    			
    			//+ " " +Name.get(i)
    			tempBtn.setId(Integer.valueOf(couponName.get(i)));
    			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			//¡Ÿ≠nºg´ˆButton™∫∞ ß@
    			tempBtn.setOnClickListener(new View.OnClickListener() {
    	             public void onClick(View v) {
    	            	 final int id = v.getId();
    	            	// setActivityDetail(id);
    	            	 builder.setMessage("請選擇Coupon\n" + "序號 :" + String.valueOf(id)).setPositiveButton("使用", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								try{
									ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
									nameValuePairs.add(new BasicNameValuePair("AccID",AccID));
									nameValuePairs.add(new BasicNameValuePair("CouponID",String.valueOf(id)));

									ArrayList<JSONObject> result_a = db.DataSearch(nameValuePairs,"coupon_use");
									Log.e("log_act","size="+result_a.size());
									
									couponName.clear();
									linearLayout.removeAllViews();
									showCoupon();
									
								}
								catch(Exception e){
									Log.e("log_tag", "Error get data "+e.toString());				
								}	
								
							}
						}).
						setNegativeButton("不使用", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								
								
							}
						});
    	            	 
    	            	 AlertDialog ad = builder.create();
    	            	 ad.show();
    	            	 
    	             }
    	         });
    			
    			linearLayout.addView(tempBtn, 300, 180);
    			
    		}
    	}
	}

	private void showCoupon(){
		try{
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	nameValuePairs.add(new BasicNameValuePair("AccID",user));

	    	ArrayList<JSONObject> result_a = db.DataSearch(nameValuePairs,"mycoupon_search");
	    	Log.e("log_act","size="+result_a.size());
	    	for(int i=0;i<result_a.size();i++){
	    		Log.e("r_act",result_a.get(i).getString("couponID"));
	    		couponName.add(result_a.get(i).getString("couponID"));
	    	}
	    	addBtn(result_a.size(),couponName,user);
		}
	    catch(Exception e){
	    	Log.e("log_tag", "Error get data "+e.toString());				
	    }	
	    
	}

}
