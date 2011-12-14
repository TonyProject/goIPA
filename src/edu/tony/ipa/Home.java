package edu.tony.ipa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends GDActivity{


	private LocationManager locationManager;
	private String provider;
	private ArrayList<JSONObject> result;
	private DB db;
	private Double lat,lng;
	private String ipaID;
	private String accountID;
	private String[] eee = {};


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	final int ACTION_BAR_INFO = 0;
        super.onCreate(savedInstanceState);
        
        addActionBarItem(Type.LocateMyself, ACTION_BAR_INFO);
        setActionBarContentView(R.layout.home);

        
        ImageButton ipaChan = (ImageButton) findViewById(R.id.imageButton1);
        ImageButton lookBtn = (ImageButton) findViewById(R.id.imageButton2);
        ImageButton hotnewsBtn = (ImageButton) findViewById(R.id.imageButton3);
        ImageButton myCoupon = (ImageButton) findViewById(R.id.imageButton4);
        ImageView ipaChar = (ImageView) findViewById(R.id.image1);
        SharedPreferences settings = getSharedPreferences("Account", 0);
        String user = settings.getString("username", "nouser");
        
       
        
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString("lat", "");
    	editor.putString("lng","");
    	editor.commit();
        
        Bitmap bitmap = IPAChan.getBitmapFromUrl("http://140.112.107.29/images/ipachan/"+user+".png");
        ipaChar.setImageBitmap(bitmap);
        
        
        
        
        ipaChan.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{ 
        		Intent i = new Intent();
      			i.setClass(Home.this, IPAChan.class);
      			startActivity(i);
        	}
        }); 
        
        lookBtn.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{ 
        		Intent j = new Intent();
      			j.setClass(Home.this, LookAround.class);
      			startActivity(j);
        	}
        }); 
        
        hotnewsBtn.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{ 
        		Intent j = new Intent();
      			j.setClass(Home.this, TabTestActivity.class);
      			startActivity(j);
        	}
        });
        
        myCoupon.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{ 
        		Intent j = new Intent();
      			j.setClass(Home.this, myCoupon.class);
      			startActivity(j);
        	}
        });
   
    }
 
    /*---------------•¥•d∂}©l---------------*/
    
    @Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) { 
		//actionbar's onclick
		switch(item.getItemId()){
		case 0:
			//****checkin here
			
			//¶€§v™∫∏gΩn´◊
			locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			provider = locationManager.getBestProvider(criteria, false);
			Location location = locationManager.getLastKnownLocation(provider);
			double b = location.getLongitude();
			double a = location.getLatitude();
			Log.e("longitude", String.valueOf(b));
			Log.e("latitude", String.valueOf(a));
			result = new ArrayList<JSONObject>();
			db = new DB();
			//end¶€§v™∫∏gΩn´◊
			
			
			
			
			//Google's shop
			final String[] items = {};
			final List<String> Gshops = new ArrayList<String>();
			
			final String[] items2 = {};
			final List<String> GetsName = new ArrayList<String>();
			final List<String> GetsID = new ArrayList<String>();
			final List<String> AddWhat = new ArrayList<String>();
			
			try{
				result = db.LocSearch(a,b);
				
				for(int i = 0; i < result.size(); i++){
					Gshops.add(i, result.get(i).getString("name"));
				}
			}
			catch(Exception e){
				Log.e("log_tag", "Error get data "+e.toString());				
			}
			//end Google's shop
			
			
			//´ÿ•ﬂ§U©‘¶°øÔ≥Ê by Google's shop
			
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
	
			
			
			builder.setTitle("請選擇店家").setItems(Gshops.toArray(items), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
 				    //øÔæ‹©±Æa´·°A≠n•hDB∏Ãß‰¶≥®S¶≥≥o≠”©±Æa
					String shopGet = Gshops.get(which);
					ArrayList<NameValuePair> location = new ArrayList<NameValuePair>();
					ArrayList<JSONObject> shop_loc = new ArrayList<JSONObject>();
					Log.e("check", shopGet);
					for(int i = 0; i < result.size(); i++){
						try {
							if(result.get(i).getString("name").equals(shopGet)){
								lat = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
								lng = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
								
								location.add(new BasicNameValuePair("Lng",lng.toString()));
								location.add(new BasicNameValuePair("Lat",lat.toString()));
								shop_loc = db.DataSearch(location,"shop_loc_search");
								
								for(int j=0;j<shop_loc.size();j++){
									Log.e("shop_id",shop_loc.get(j).getString("shopType"));
								}
							}
						}catch(Exception e){
							Log.e("log_tag", "Error get data "+e.toString());				
						}	
					}
					
					
					//•ŒaccountIDß‰ipaID
					SharedPreferences settings = getSharedPreferences("Account", 0);
			        ipaID = settings.getString("ipaID", "nouser");
			        accountID = settings.getString("username", "nouser");
			        Log.e("ipaID", ipaID);
			        
		        	SharedPreferences.Editor editor = settings.edit();
		        	editor.putString("lat", String.valueOf(lat));
		        	editor.putString("lng",String.valueOf(lng));
		        	editor.commit();
			        
			        //----∑sºW¶‹checkin table----
					try{
						ArrayList<NameValuePair> check_nameValuePairs = new ArrayList<NameValuePair>();
						check_nameValuePairs.add(new BasicNameValuePair("IpaID",ipaID));
						check_nameValuePairs.add(new BasicNameValuePair("Lat",String.valueOf(lat)));
						check_nameValuePairs.add(new BasicNameValuePair("Lng",String.valueOf(lng)));
						if(shop_loc.size() == 0){
							check_nameValuePairs.add(new BasicNameValuePair("ShopID",""));
						}
						else{
							check_nameValuePairs.add(new BasicNameValuePair("ShopID",shop_loc.get(0).getString("shopID")));
						}
						
						
						ArrayList<JSONObject> result_c = db.DataSearch(check_nameValuePairs,"checkin");
						Log.e("log_act111","size=" + result_c.size());
						

						if(shop_loc.size() != 0)
						{
							ArrayList<NameValuePair> get_nameValuePairs = new ArrayList<NameValuePair>();
							get_nameValuePairs.add(new BasicNameValuePair("ShopID",shop_loc.get(0).getString("shopID")));
							get_nameValuePairs.add(new BasicNameValuePair("Type",shop_loc.get(0).getString("shopType")));
							ArrayList<JSONObject> result_d = db.DataSearch(get_nameValuePairs,"get_condition");
							Log.e("log_get","size=" + result_d.size());
							if(result_d.size() > 0)
							{
								ArrayList<NameValuePair> ipa_nameValuePairs = new ArrayList<NameValuePair>();
								ipa_nameValuePairs.add(new BasicNameValuePair("AccID",accountID));
								ArrayList<JSONObject> result_e = db.DataSearch(ipa_nameValuePairs,"ipa_search");
								Log.e("log_ipa","size=" + result_e.size());
								String typeMoney;
								if(shop_loc.get(0).getString("shopType").equals("0"))
									typeMoney = "eatMoney";
								else if(shop_loc.get(0).getString("shopType").equals("1"))
									typeMoney = "wearMoney";
								else if(shop_loc.get(0).getString("shopType").equals("2"))
									typeMoney = "liveMoney";
								else if(shop_loc.get(0).getString("shopType").equals("3"))
									typeMoney = "moveMoney";
								else if(shop_loc.get(0).getString("shopType").equals("4"))
									typeMoney = "eduMoney";
								else
									typeMoney = "funMoney";
								int count = 0;
								for(int i = 0; i < result_d.size(); i++)
								{
									if(Integer.valueOf(result_d.get(i).getString("money")) <= Integer.valueOf(result_e.get(0).getString(typeMoney)))
									{
										
										if(!result_d.get(i).getString("couponID").equals("0"))
										{
											GetsID.add(count,result_d.get(i).getString("couponID"));
											
											ArrayList<NameValuePair> c_detail_nameValuePairs = new ArrayList<NameValuePair>();
											c_detail_nameValuePairs.add(new BasicNameValuePair("CouponID",result_d.get(i).getString("couponID")));
											ArrayList<JSONObject> result_c_detail = db.DataSearch(c_detail_nameValuePairs,"coupon_search");
											
											GetsName.add(count,"coupon: "+result_c_detail.get(0).getString("couponName"));
											AddWhat.add(count,"coupon");
											count++;
										}
										else if(!result_d.get(i).getString("honorID").equals("0"))
										{
											GetsID.add(count,result_d.get(i).getString("honorID"));
											GetsName.add(count,"honor:"+result_d.get(i).getString("honorID"));
											AddWhat.add(count,"honor");
											count++;
										}
										else if(!result_d.get(i).getString("clothesID").equals("0"))
										{
											GetsID.add(count,result_d.get(i).getString("clothesID"));
											GetsName.add(count,"clothes:"+result_d.get(i).getString("clothesID"));
											AddWhat.add(count,"clothes");
											count++;
										}
									}
								}
								Log.e("coupon", GetsID.get(0));
								
//								
								builder2.setTitle("目前打卡總共點數可兌換此店家所提供的優惠，請選擇").setItems(GetsName.toArray(eee), new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										
										if(AddWhat.get(which).equals("coupon")){
											ArrayList<NameValuePair> couponAdd_nameValuePairs = new ArrayList<NameValuePair>();
											couponAdd_nameValuePairs.add(new BasicNameValuePair("AccID",accountID));
											couponAdd_nameValuePairs.add(new BasicNameValuePair("CouponID",GetsID.get(which)));
											ArrayList<JSONObject> result_e = db.DataSearch(couponAdd_nameValuePairs,"coupon_add");
											//¶©money¡Ÿ®Sºg
										}
										else if(AddWhat.get(which).equals("clothes")){
											ArrayList<NameValuePair> couponAdd_nameValuePairs = new ArrayList<NameValuePair>();
											couponAdd_nameValuePairs.add(new BasicNameValuePair("IpaID",ipaID));
											couponAdd_nameValuePairs.add(new BasicNameValuePair("ClothID",GetsID.get(which)));
											ArrayList<JSONObject> result_e = db.DataSearch(couponAdd_nameValuePairs,"cloth_add");
										}
										else if(AddWhat.get(which).equals("honor")){
											ArrayList<NameValuePair> couponAdd_nameValuePairs = new ArrayList<NameValuePair>();
											couponAdd_nameValuePairs.add(new BasicNameValuePair("IpaID",ipaID));
											couponAdd_nameValuePairs.add(new BasicNameValuePair("HonorID",GetsID.get(which)));
											ArrayList<JSONObject> result_e = db.DataSearch(couponAdd_nameValuePairs,"honor_add");
										}
										
								}});
								builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						 
    	            		 
    	            		 
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						});
								builder2.show();
							    
							}
							
							
						}
						
					}
					catch(Exception e){
			        	Log.e("log_tag", "Error get data "+e.toString());				
			        }
					
					
				}
			});
			
			
			
			
			//AlertDialog ad = builder.create();
			//ad.show();
			builder.show();
			
			
			break;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}
    
    /*---------------•¥•dµ≤ßÙ---------------*/
}
    


