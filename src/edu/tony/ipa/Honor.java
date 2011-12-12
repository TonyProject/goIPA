package edu.tony.ipa;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Honor extends Activity {
	/** Called when the activity is first created. */
	private LinearLayout linearLayout;
	private DB db;
	private List<String> honorList, nameList,desList;
	private ArrayList<JSONObject> result_h, result_id, result_ipa;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.honor);
        linearLayout = (LinearLayout) findViewById(R.id.honorlayout);
        honorList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        desList = new ArrayList<String>();
        db = new DB();
       /* 
        //輸入要公布的ID
        String[] honor = {"100000001","100000000"};
        //
        
        for(int i=0;i<honor.length;i++)
        {
            search(honor[i]);
        }
        */
        
        ////search king
        try{
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("HonorID",honor));
			
			result_h = db.DataSearch(nameValuePairs,"king_search");

			Log.e("log_act","size="+result_h.size());
			
			
			
			
			for(int i=0;i<result_h.size();i++){
				Log.e("r_act",result_h.get(i).getString("honorName"));
				honorList.add(result_h.get(i).getString("honorName"));
				desList.add(result_h.get(i).getString("description"));
				ArrayList<NameValuePair> nameValuePairs_id = new ArrayList<NameValuePair>();
				Log.e("r_act",result_h.get(i).getString("honorID"));
				nameValuePairs_id.add(new BasicNameValuePair("HonorID",result_h.get(i).getString("honorID")));
				result_id = db.DataSearch(nameValuePairs_id,"honor_search");
			    nameList.add(result_id.get(0).getString("ipaID"));
			}
			
			
			
			//honorList.add(result_h.get(0).getString("honorName"));
			
			//search myhonor
			/*
			ArrayList<NameValuePair> nameValuePairs_id = new ArrayList<NameValuePair>();
			nameValuePairs_id.add(new BasicNameValuePair("HonorID",honor));
			result_id = db.DataSearch(nameValuePairs,"honor_search");
			
			*/
			
			/*
			Log.e("log_act","size="+result_id.size());
			Log.e("r_act",result_id.get(0).getString("ipaID"));
			String name = (result_id.get(0).getString("ipaID"));
			nameList.add(result_id.get(0).getString("ipaID"));
			*/
			
			//search ipaid
			/*
			ArrayList<NameValuePair> nameValuePairs_ipaid = new ArrayList<NameValuePair>();
			nameValuePairs_ipaid.add(new BasicNameValuePair("ipaID",name));
			result_id = db.DataSearch(nameValuePairs,"myhonor_search");
			
			nameList.add(result_ipa.get(0).getString("name"));
			*/
			
			//search ipa
			/*
			for(int i=0;i<result_h.size();i++){
				Log.e("r_act",result_h.get(i).getString("name"));
				honorList.add(result_h.get(i).getString("name"));
				activityDetailList.add(result_a.get(i).getString("description"));
			}*/
			
			
        	
        }catch(Exception e){
    		Log.e("log_tag", "Error get data "+e.toString());				
    		}
        //
        
        addBtn(honorList.size(),honorList,nameList);

	}
	
	
    public void search(String honor){
    	try{
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//nameValuePairs.add(new BasicNameValuePair("HonorID",honor));
			
            
			result_h = db.DataSearch(nameValuePairs,"king_search");

			Log.e("log_act","size="+result_h.size());
			Log.e("r_act",result_h.get(0).getString("honorName"));
			honorList.add(result_h.get(0).getString("honorName"));
			
			//search myhonor
			ArrayList<NameValuePair> nameValuePairs_id = new ArrayList<NameValuePair>();
			nameValuePairs_id.add(new BasicNameValuePair("HonorID",honor));
			result_id = db.DataSearch(nameValuePairs,"honor_search");
			
			
			Log.e("log_act","size="+result_id.size());
			Log.e("r_act",result_id.get(0).getString("ipaID"));
			String name = (result_id.get(0).getString("ipaID"));
			nameList.add(result_id.get(0).getString("ipaID"));
			//search ipaid
			/*
			ArrayList<NameValuePair> nameValuePairs_ipaid = new ArrayList<NameValuePair>();
			nameValuePairs_ipaid.add(new BasicNameValuePair("ipaID",name));
			result_id = db.DataSearch(nameValuePairs,"myhonor_search");
			
			nameList.add(result_ipa.get(0).getString("name"));
			*/
			
			//search ipa
			/*
			for(int i=0;i<result_h.size();i++){
				Log.e("r_act",result_h.get(i).getString("name"));
				honorList.add(result_h.get(i).getString("name"));
				activityDetailList.add(result_a.get(i).getString("description"));
			}*/
			
			
        	
        }catch(Exception e){
    		Log.e("log_tag", "Error get data "+e.toString());				
    		}	
        
    }
	
    public void addBtn(int count,final List<String> honorName, final List<String> Name){
    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	if(count > 0){
    		int k;
    		for(int i = 0; i < count; i++){
    			Button tempBtn = new Button(this);
    			tempBtn.setText("恭喜使用者ID為"+Name.get(i)+"玩家榮獲"+honorName.get(i));
    			
    			//+ " " +Name.get(i)
    			final String des = desList.get(i);
    			tempBtn.setId(i);
    			//還要寫按Button的動作
    			tempBtn.setOnClickListener(new View.OnClickListener() {
    	             public void onClick(View v) {
    	            	 int id = v.getId();
    	            	 
    	            	 try{
        	            	 
        	            	 
        	 				final Bundle bundle1 = new Bundle();
       	            	   
       	            	    bundle1.putString("ID", Name.get(id));
       	            	    
        	 				 
        	 				 
        	            	 
        	            	 builder.setMessage("Honor名稱:"+ honorName.get(id)+ "\n" 
        	            			 +"Honor說明:"+des+ "\n" + "\n"
        	            			 
        	            			 );
        	            	 
        	            	 builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
    						 
        	            		 
        	            		 
    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								// TODO Auto-generated method stub
    								
    							}
    						});
        	            	 
        	            	 builder.setNegativeButton("觀看得主", new DialogInterface.OnClickListener() {
    							
    							@Override
    							public void onClick(DialogInterface dialog, int which) {
    								// TODO Auto-generated method stub
    								//Intent man = new Intent();
    		    	            	//man.setClass(Honor.this, ??.class);
    		    	            	
    		    	            	//man.putExtras(bundle1);
    		    	            	//startActivity(man);
    		    	            	
    							}
    						});
        	            	 
        	            	 
        	            	 builder.show();
        	            	 }//startActivity(detailintent);
        	            	 catch(Exception e){
        	     	    		Log.e("log_tag", "Error get data "+e.toString());				
        	     	    		}
    	            	// setActivityDetail(id);
    	             }
    	         });
    			
    			linearLayout.addView(tempBtn, 350, 100);
    			linearLayout.setGravity(17);
    		}
    	}
    	
    }

}
