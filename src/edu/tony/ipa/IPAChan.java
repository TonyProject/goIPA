package edu.tony.ipa;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem.Type;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class IPAChan extends GDActivity { 
    private String IMAGE_URL = "http://140.112.107.29/images/";
    private String user = null;
    private Integer lohas[] = new Integer[6];
    private static final int ACTION_BAR_INFO = 0;
    static String ipaID = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.ipa_chan);
        addActionBarItem(Type.Info, ACTION_BAR_INFO);
        
        
        SharedPreferences settings = getSharedPreferences("Account", 0);
        user = settings.getString("username", null);	
        Log.i("TESTUSER", "user name: "+ user);
        
    	DB db = new DB();
    	ArrayList<NameValuePair> ipa = new ArrayList<NameValuePair>();
    	ipa.add(new BasicNameValuePair("AccID", user));
    	final ArrayList<JSONObject> result_a = db.DataSearch(ipa,"ipa_search");
    	for(int i=0;i<result_a.size();i++){
    		try {
	            ipaID = result_a.get(i).getString("ipaID");
            } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
    	}
        Log.i("TEST_IPAID", ipaID);
    	
        Button closet = (Button) findViewById(R.id.closet);
        Button ipa_attr = (Button) findViewById(R.id.attr);
        Button honor = (Button) findViewById(R.id.honor);
    	ImageView ipaChan = (ImageView) findViewById(R.id.ipachan);
    	
        Bitmap bitmap = getBitmapFromUrl(IMAGE_URL + "ipachan/" + user + ".png");
        ipaChan.setImageBitmap(bitmap);
        
        closet.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
        		Intent i = new Intent();
      			i.setClass(IPAChan.this, IPACloset.class);
      			startActivity(i);
            }
        });

        honor.setOnClickListener(new Button.OnClickListener(){
			@Override
            public void onClick(View arg0) {
        		Intent i = new Intent();
      			i.setClass(IPAChan.this, IPAHonor.class);
      			startActivity(i);
            }
        });
        
        ipa_attr.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View arg0) {
				attrOnClick(result_a);
            }
        });
        
    }
    
    public void attrOnClick(ArrayList<JSONObject> result_a) {  	
    	//Log.i("TEST", "go go go: "+result_a.toString());
    	for(int i=0;i<result_a.size();i++){
    		try {
	            Log.i("r_act",result_a.get(i).getString("ipaID"));
	            lohas[0] = Integer.parseInt(result_a.get(i).getString("eat"));
	            lohas[1] = Integer.parseInt(result_a.get(i).getString("wear"));
	            lohas[2] = Integer.parseInt(result_a.get(i).getString("live"));
	            lohas[3] = Integer.parseInt(result_a.get(i).getString("move"));
	            lohas[4] = Integer.parseInt(result_a.get(i).getString("edu"));
	            lohas[5] = Integer.parseInt(result_a.get(i).getString("fun"));
            } catch (JSONException e) {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
            }
    	}
    	
    	ImageView radr = new ImageView(this);
        RadarChart rd = new RadarChart();
        String radr_url = rd.getIpaRadr(lohas[0], lohas[1], lohas[2], lohas[3], lohas[4], lohas[5]);
        //String radr_url = rd.example2();
        Bitmap bitmap2 = getBitmapFromUrl(radr_url);
        radr.setImageBitmap(bitmap2);

        final AlertDialog.Builder builder = new AlertDialog.Builder(IPAChan.this);  
        builder.setCancelable(false);
        builder.setTitle("IPA樂活指數");
        builder.setView(radr);
        builder.setPositiveButton("關閉", null); 
        builder.show();
    }

    /**
     * @param imgUrl
     * @return
     */
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
    

}
