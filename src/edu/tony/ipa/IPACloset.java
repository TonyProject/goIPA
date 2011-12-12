package edu.tony.ipa;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.ViewSwitcher.ViewFactory;


public class IPACloset extends GDActivity implements ViewFactory{
	private SimpleAdapter listItemAdapter = null;
	private static ImageView ipachan;
	private ImageView head, upBody, downBody, feet;
	private ListView list;
	private Gallery dressGallery;
	private ArrayList< HashMap<String, Object> > listItem = new ArrayList<HashMap<String, Object>>();
	private String DRESS_URL= 
    	"http://140.112.107.29/images/dress/";
	private String[] type = {"headWear", "upBodyWear", "downBodyWear", "feetWear", "left_hand", "right_hand"};
	private String type_now = type[0];
	private HashMap<String, Object> typeID;
	private String IMAGE_URL = "http://140.112.107.29/images/";
	private static Bitmap ipaNaked = null, ipaBitmap = null, headBitmap = null, upBodyBitmap = null, downBodyBitmap = null, feetBitmap = null;
	private final int ACTION_BAR_SAVE = 0;
	private String user = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActionBarItem(Type.Gallery, ACTION_BAR_SAVE);
        setActionBarContentView(R.layout.ipa_closet);
        
        list = (ListView) findViewById(R.id.item_list);
        ipachan = (ImageView) findViewById(R.id.ipachan);
        head = (ImageView) findViewById(R.id.head);
        upBody = (ImageView) findViewById(R.id.upBody);
        downBody = (ImageView) findViewById(R.id.downBody);
        feet = (ImageView) findViewById(R.id.feet);
        dressGallery = (Gallery)findViewById(R.id.dress_type);
        dressGallery.setAdapter(new ImageAdapter(this));
        dressGallery.setOnItemSelectedListener(myGalleryOnItemSelectedListener);
        
        SharedPreferences settings = getSharedPreferences("Account", 0);
        user = settings.getString("username", null);
        ipaBitmap = IPAChan.getBitmapFromUrl(IMAGE_URL + "ipachan/" + user + ".png");
        ipaNaked = IPAChan.getBitmapFromUrl(IMAGE_URL + "ipachan_naked/" + user + ".png");
        ipachan.setImageBitmap(ipaBitmap);
        
        typeID = new HashMap<String, Object>();
        typeID.put(type[0], "1");
        typeID.put(type[1], "10");
        typeID.put(type[2], "11");
        typeID.put(type[3], "12");
        //Log.i("TypeID", typeID.get(type_now).toString());
        
        findDress(type_now);
		
		
        listItemAdapter = 
        	new SimpleAdapter(
        		this,
        		listItem,
        		R.layout.list_items,  
        		new String[] {"item", "dressID"},   
        		new int[] {R.id.item}  
        	);    
        listItemAdapter.setViewBinder(new MyViewBinder());
        list.setAdapter(listItemAdapter);
		//listItemAdapter.notifyDataSetChanged();
        
        list.setOnItemClickListener(
            	new OnItemClickListener() {
            	@SuppressWarnings("unchecked")
                @Override  
            	public void onItemClick(
            			AdapterView<?> adapter, 
            			View view, 
            			final int position, 
            			long arg){
            			
            			final HashMap<String, Object> select = 
            				(HashMap<String, Object>) list.getItemAtPosition(position);
            			//myImage.setImageBitmap((Bitmap) select.get("item"));
            			String dress_item = select.get("dressID").toString();
            			Log.i("dressID= ", dress_item);
            	        Bitmap bitmap = IPAChan.getBitmapFromUrl(DRESS_URL+typeID.get(type_now).toString()+"/"+dress_item+".png");
            	        if (type_now.equals("headWear")) {
            	        	head.setImageBitmap(bitmap);
            	        	headBitmap = bitmap;
            	        }
            	        else if (type_now.equals("upBodyWear")) {
            	        	upBody.setImageBitmap(bitmap);
            	        	upBodyBitmap = bitmap;
            	        }
            	        else if (type_now.equals("downBodyWear")) {
            	        	downBody.setImageBitmap(bitmap);
            	        	downBodyBitmap = bitmap;
            	        }
            	        else if (type_now.equals("feetWear")) {
            	        	feet.setImageBitmap(bitmap);
            	        	feetBitmap = bitmap;
            	        }
            	}  
            }); 
        
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
    
    private OnItemSelectedListener myGalleryOnItemSelectedListener
     	= new OnItemSelectedListener(){
    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    		// TODO Auto-generated method stub
    		listItem.clear();
    		type_now = type[arg2];
    		findDress(type_now);
    		listItemAdapter.notifyDataSetChanged();	
    	}
    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub
    	}
    };
    
    public void findDress(String type_name){
        DB db = new DB();
    	ArrayList<NameValuePair> ipa = new ArrayList<NameValuePair>();
    	ipa.add(new BasicNameValuePair("IpaID", IPAChan.ipaID));
    	ipa.add(new BasicNameValuePair("type", type_name));
    	ArrayList<JSONObject> myCloset = db.DataSearch(ipa,"closet_search");
    	Log.i("CLOSETTEST", "wear: "+myCloset.toString());
        
    	for (int i=0; i<myCloset.size(); i++) {
    		//The list of items
    		HashMap<String, Object> item = new HashMap<String, Object>();
    		try {
	            String dress_item = myCloset.get(i).getString("clothesID");
	            Bitmap bitmap = IPAChan.getBitmapFromUrl(DRESS_URL+typeID.get(type_now).toString()+"/thumbs/"+dress_item+".png");
	            item.put("item", bitmap);
	            item.put("dressID", dress_item);
	            //Log.i("Item_test", item.get("item").toString());
	            listItem.add(item);
            } catch (JSONException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
            }
    	}
    }
    
    
    private Integer[] mThumbIds = {
    		R.drawable.hair,
            R.drawable.body_up,
            R.drawable.body_down,
            R.drawable.shoes,
            R.drawable.left_hand,
            R.drawable.right_hand
          };
    
    public class ImageAdapter extends BaseAdapter{
    	private Context context;
    	public ImageAdapter(Context c){
    		context = c;
    	}
     
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return mThumbIds.length;
    	}

    	public Object getItem(int position) {
    		// TODO Auto-generated method stub
    		return mThumbIds[position];
    	}

    	public long getItemId(int position) {
    		// TODO Auto-generated method stub
    		return position;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		ImageView imageView;
    		if (convertView == null) {
    			// if it's not recycled, initialize some attributes
    			imageView = new ImageView(context);
    			imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
    			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    			imageView.setPadding(5, 5, 5, 5);
    		} else {
    			imageView = (ImageView) convertView;
    		}

    		imageView.setImageResource(mThumbIds[position]);
    		return imageView;
    	}
    }

    public View makeView() {
    	// TODO Auto-generated method stub
    	ImageView i = new ImageView(this);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        return i;
    }
    
    public static Bitmap mergeBitmap(Bitmap currentBitmap) {
        Bitmap mBmOverlay = Bitmap.createBitmap(ipaBitmap.getWidth(), ipaBitmap.getHeight(), ipaBitmap.getConfig());
        Canvas canvas = new Canvas(mBmOverlay);
        canvas.drawBitmap(ipaBitmap, new Matrix(), null);
        canvas.drawBitmap(currentBitmap, 0, 0, null);
        return mBmOverlay;
    }
    
    @Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) { 
		//actionbar's onclick
		switch(item.getItemId()){
			case 0:
				Log.i("ItemClick", "click!!");
				if (upBodyBitmap!=null)
					ipaBitmap = mergeBitmap(upBodyBitmap);
				if (headBitmap!=null)
					ipaBitmap = mergeBitmap(headBitmap);
				if (downBodyBitmap!=null)
					ipaBitmap = mergeBitmap(downBodyBitmap);
				if (feetBitmap!=null)
					ipaBitmap = mergeBitmap(feetBitmap);
				//head.setImageBitmap(ipaNaked);
				uploadFile();
        		Intent i = new Intent();
      			i.setClass(IPACloset.this, IPAChan.class);
      			startActivity(i);
				break;
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}
    
    InputStream is;
    
	private void uploadFile() {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		ipaBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
        byte [] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("image",ba1));
        nameValuePairs.add(new BasicNameValuePair("img_name",user));
           
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new
            HttpPost("http://140.112.107.29/upload_image.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }       
	}
}

