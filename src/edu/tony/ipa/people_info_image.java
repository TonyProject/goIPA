package edu.tony.ipa;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import greendroid.app.GDActivity;

public class people_info_image extends GDActivity  {
	
	private ImageView info_ipachan;
	private ImageView image_like;
	private TextView like_amount;
	private ImageButton like_imagebutton;
	int likenumplus;
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.people_info_image);
        
        info_ipachan = (ImageView)findViewById(R.id.info_ipachan);
        image_like = (ImageView)findViewById(R.id.image_like);
        like_amount = (TextView)findViewById(R.id.like_amount);
        //like_imagebutton = (ImageButton)findViewById(R.id.like_imageButton);
        
        Intent intent = getIntent();
        final Integer ipaid = intent.getIntExtra("ipaID", -1);
        final String img = intent.getStringExtra("img") ;
        final Integer likenum = intent.getIntExtra("likenum", 0) ;
        
        Bitmap bitmap = getBitmapFromUrl(img);
        info_ipachan.setImageBitmap(bitmap);
        like_amount.setText(likenum.toString());
        
        
        
        like_imagebutton = (ImageButton)findViewById(R.id.like_imageButton); 
	     like_imagebutton.setOnClickListener(new OnClickListener(){
	        	public void onClick(View v) {
	        		DB db = new DB();
	        		try{
	        			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	        			nameValuePairs.add(new BasicNameValuePair("IpaID",ipaid.toString()));
	        			ArrayList<JSONObject> result_a = db.DataSearch(nameValuePairs,"like");
	        			ArrayList<JSONObject> result_b = db.DataSearch(nameValuePairs,"ipa_searchipa");
	        			
	        			for(int i=0;i<result_b.size();i++){
	        				likenumplus = result_b.get(i).getInt("likenum");
	        			}
	        			
	        			
		        		Intent intent=new Intent();
		        		intent.putExtra("ipaID", ipaid);
		        		intent.putExtra("img", img);
   	            	 	intent.putExtra("likenum", likenumplus);
   	            	 	
		        		intent.setClass(people_info_image.this, people_info_image.class);
						startActivity(intent);
	        		}
	        		catch(Exception e){
	        			Log.e("log_tag", "Error get data "+e.toString());				
	        		}
	        	}
	    });
		
	}
	
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
