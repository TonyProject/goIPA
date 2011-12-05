package edu.tony.ipa;

import greendroid.app.GDActivity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class IPAHonor extends GDActivity{
	private GridView honor;
	private TextView tv_empty;
	private String HONOR_URL = "http://140.112.107.29/images/honor/";
	private String honor_id, honor_name, honor_des;
	private ArrayList<HashMap<String, Object>> grid_honor
			= new ArrayList<HashMap<String, Object>>(); 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.ipa_honor);
        honor = (GridView) findViewById(R.id.honor); 
        tv_empty = (TextView) findViewById(R.id.tv_empty);
        
        DB db = new DB();
    	ArrayList<NameValuePair> ipa = new ArrayList<NameValuePair>();
    	ipa.add(new BasicNameValuePair("IpaID", IPAChan.ipaID));
    	ArrayList<JSONObject> myHonor = db.DataSearch(ipa,"myhonor_search");
    	//Log.i("HONORTEST", "go go go: "+myHonor.toString());
        
        for(int i=0;i<myHonor.size();i++)  
        {  
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	try {
        		honor_des = myHonor.get(i).getString("description");
        		honor_name = myHonor.get(i).getString("honorName");
	            honor_id = myHonor.get(i).getString("honorID");
	            Bitmap bitmap = IPAChan.getBitmapFromUrl(HONOR_URL+honor_id+".png");
	            map.put("honor", bitmap);
	            map.put("name", honor_name);
	            map.put("description", honor_des);
	            grid_honor.add(map);
            } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
        }
        
        if (grid_honor.size()==0) {
        	tv_empty.setText("您尚未有榮譽勳章和王者勳章");
        }
        
        
        
        else {
        SimpleAdapter saImageItems = new SimpleAdapter(
        	this,  
            grid_honor,
            R.layout.list_items,      
            new String[] {"honor", "name", "description"},
            new int[] {R.id.item});
        saImageItems.setViewBinder(new MyViewBinder());
        honor.setAdapter(saImageItems);
        honor.setOnItemClickListener(
            	new OnItemClickListener() {
            	@SuppressWarnings("unchecked")
                @Override  
            	public void onItemClick(
            			AdapterView<?> adapter, 
            			View view, 
            			final int position, 
            			long arg){
            		final HashMap<String, Object> select = 
            			(HashMap<String, Object>) honor.getItemAtPosition(position);
            		Log.i("OnItemClick", select.get("name").toString()+select.get("description").toString());
            		honorDialog(select.get("name").toString(),select.get("description").toString());
            	}  
            }); 
        }
        
        
    }
    
    public void honorDialog(String name, String description){ 
    	TextView text = new TextView(this);
    	final AlertDialog.Builder builder = new AlertDialog.Builder(IPAHonor.this);
		text.setText(description);
		text.setTextSize(20);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon);  
        builder.setTitle(name);
        builder.setView(text);
        builder.setPositiveButton("關閉", null);
        builder.show();
    }
    
    public class MyViewBinder implements ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data,
                        String textRepresentation) {
        		if( (view instanceof ImageView) & (data instanceof Bitmap) ) {
                        ImageView iv = (ImageView) view;
                        Bitmap bm = (Bitmap) data;     
                        iv.setImageBitmap(bm); 
                        return true;
                }
                return false;
        }
    }
}