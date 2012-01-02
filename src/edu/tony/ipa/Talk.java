package edu.tony.ipa;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Talk extends Activity {
	/** Called when the activity is first created. */
	private TextView showtext;
	private Button say,renew;
	private EditText text;
	private ArrayList<JSONObject> result_msg,result_mess;
	private DB db;
	private LinearLayout linearlayout,showlayout;
	private List<String> msgList;
	private List<String> idList;
	private List<String> timeList;
	private String provider;
	private LocationManager locationManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);
        
        showtext = (TextView)findViewById(R.id.show);
        say = (Button)findViewById(R.id.say);
        renew = (Button)findViewById(R.id.renew);
        text = (EditText)findViewById(R.id.text);
        linearlayout = (LinearLayout)findViewById(R.id.tal);
        showlayout = (LinearLayout)findViewById(R.id.showtalk);
        idList = new ArrayList<String>();
        msgList = new ArrayList<String>();
        timeList = new ArrayList<String>();
        
        SharedPreferences settings = getSharedPreferences("Account", 0);
        String islng = settings.getString("lng", "");
		String islat = settings.getString("lat", "");
		//say.setClickable(false);
		//say.setEnabled(false);
		renew();
        if(islng=="")
        {
        	say.setEnabled(false);
        	
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);	
        	builder.setMessage("請先check in,才能使用大聲公");
        	builder.show();
        }
        else
        {
        	say.setEnabled(true);
        }
        //say.setOnClickListener(sendmsg);
        say.setOnClickListener(new Button.OnClickListener()
        {
        public void onClick(View v)
        {
        	String msg = text.getText().toString();
    		SharedPreferences settings = getSharedPreferences("Account", 0);
    		String user = settings.getString("username", "nouser");
    		
    		String lng = settings.getString("lng", "");
    		String lat = settings.getString("lat", "");
    		
    		//double lat = 25.015654;
    		//double lng = 121.537778;

    		
    		db = new DB();
    		try{
            	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            	
            	//accID,lng,lat ≠nΩ’æ„
            	nameValuePairs.add(new BasicNameValuePair("AccID",user));
            	nameValuePairs.add(new BasicNameValuePair("Lng",String.valueOf(lng)));
    			nameValuePairs.add(new BasicNameValuePair("Lat",String.valueOf(lat)));
    			nameValuePairs.add(new BasicNameValuePair("Message",msg));
    			
    			Log.e("account",user);
    			Log.e("lng",String.valueOf(lng));
    			Log.e("lat",String.valueOf(lat));
    			Log.e("msg",msg);
    			
    			result_msg = db.DataSearch(nameValuePairs, "speak_add");
    			text.setText("");
                renew();
    			
            	
            }catch(Exception e){
        		Log.e("log_tag", "Error get data "+e.toString());				
        		}
            
        }
        });   
        
        
        //renew = (Button)findViewById(R.id.renew);
        renew.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{
        		renew();
        		/*
        	    showlayout.removeAllViews();
        	    double lat = 25.0134875;
        		double lng = 121.5424255;
        		
        		db = new DB();
        		try{
                    
                    ArrayList<JSONObject> result = db.LocSearch(lat,lng);
            		Log.e("log_loc","size="+result.size());
            		for(int i=0;i<result.size();i++){
            			Log.e("r_loc",result.get(i).getString("name"));
            			Log.e("r_loc","lat "+
            				result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            			Log.e("r_loc","lng "+
            					result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
            			double la = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            			double lo = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            			getmsgfromwhere(la,lo);
            			
            			
            		}
                    }
                    catch(Exception e){
                		Log.e("log_tag", "Error get data "+e.toString());				
                		}
        	    
                    sort(timeList, 0, timeList.size()-1);
                    
                    
                    
                    addview(msgList,idList);
        	    */
        	}
        });
	}
	
	public void renew()
	{
		showlayout.removeAllViews();
	    //double lat = 25.015654;
		//double lng = 121.537778;
		/*SharedPreferences settings = getSharedPreferences("Account", 0);
		String ng = settings.getString("lng", "");
		String at = settings.getString("lat", "");
		double lat = Double.valueOf(at);
		double lng = Double.valueOf(ng);
		*/
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lng = location.getLongitude();
        double lat = location.getLatitude();
		
		
		db = new DB();
		try{
            
            ArrayList<JSONObject> result = db.LocSearch(lat,lng);
    		Log.e("log_loc","size="+result.size());
    		for(int i=0;i<result.size();i++){
    			Log.e("r_loc",result.get(i).getString("name"));
    			Log.e("r_loc","lat "+
    				result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
    			Log.e("r_loc","lng "+
    					result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
    			double la = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
    			double lo = result.get(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
    			getmsgfromwhere(la,lo);
    			
    			
    		}
            }
            catch(Exception e){
        		Log.e("log_tag", "Error get data "+e.toString());				
        		}
	    if(timeList.size()>0)
            sort(timeList, 0, timeList.size()-1);
            /*Log.e("mmm",timeList.get(0));
            Log.e("mmm",timeList.get(1));
            Log.e("mmm",timeList.get(2));
            Log.e("mmm",timeList.get(3));
            
            Log.e("mmm",msgList.get(0));
            Log.e("mmm",msgList.get(1));
            Log.e("mmm",msgList.get(2));
            Log.e("mmm",msgList.get(3));
            
            Log.e("mmm",idList.get(0));
            Log.e("mmm",idList.get(1));
            Log.e("mmm",idList.get(2));
            Log.e("mmm",idList.get(3));
            */
          if(msgList.size()>0)  
            addview(msgList,idList);
	}
	
	
	
	public void getmsgfromwhere(double a,double b)
	{
		try{
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Lng",String.valueOf(b)));
			nameValuePairs.add(new BasicNameValuePair("Lat",String.valueOf(a)));
			
			result_mess = db.DataSearch(nameValuePairs, "speak_search");

			//Log.e("m",nameValuePairs.toString());
			Log.e("msgnum","size="+result_mess.size());
			for(int i=0;i<result_mess.size();i++){
				Log.e("mmssgg",result_mess.get(i).getString("message"));
				msgList.add(result_mess.get(i).getString("message"));
				Log.e("man",result_mess.get(i).getString("accountID"));
				idList.add(result_mess.get(i).getString("accountID"));
				Log.e("time",result_mess.get(i).getString("createdTime").replace("-","").replace(":","").replace(" ",""));
				timeList.add(result_mess.get(i).getString("createdTime").replace("-","").replace(":","").replace(" ",""));
			}
			
        	
        }catch(Exception e){
    		Log.e("log_tag", "Error get data "+e.toString());				
    		}	
	}
	
	public void addview(List<String> msg,List<String> id)
	{
		if(msg.size()>0)
		{
			for(int i = 0;i<msg.size();i++)
			{
				TextView t = new TextView(this);
				t.setText(id.get(i)+ " : " +msg.get(i));
				t.setTextColor(R.color.black);
				t.setTextSize(20);
				showlayout.addView(t);
			}
		}
	}
	
	
	public void sort(List<String> time,int left,int right)
	{
		if(left < right) { 
            int i = left; 
            int j = right + 1; 
            while(true) { 
                // ¶V•kß‰ 
            	//Date date = new Date();
            	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	
            	while(i + 1 < time.size() && Long.valueOf(time.get(++i)) < Long.valueOf(time.get(left)) );
            	
            		//while(i + 1 < time.size() && (Time.valueOf(df.format(time.get(++i)))).before(Time.valueOf(df.format(time.get(left))))) ;   
                // ¶V•™ß‰ 
            	while(j -1 > -1 &&  Long.valueOf(time.get(--j)) > Long.valueOf(time.get(left))) ;
            	
                if(i >= j) 
                    break; 
                swap(time, i, j); 
            } 
            swap(time, left, j); 
            sort(time, left, j-1);   // πÔ•™√‰∂i¶Êªº∞j 
            sort(time, j+1, right);  // πÔ•k√‰∂i¶Êªº∞j 
        }
	}
    
	public void swap(List<String> time, int i, int j)
	{
		 
		 
		 if(j>0)
		 {
			 String t = time.get(i); 
			 time.remove(i);
			 time.add(i, time.get(j-1));
		     time.remove(j);
			 time.add(j,t);
			 
			 String m = msgList.get(i);
			 msgList.remove(i);
			 msgList.add(i, msgList.get(j-1));
			 msgList.remove(j);
			 msgList.add(j,m);
			 
			 
			 String ii = idList.get(i);
			 idList.remove(i);
			 idList.add(i, idList.get(j-1));
			 idList.remove(j);
			 idList.add(j,ii);
		 }
		 
	}
	
	
	
}
