package edu.tony.ipa;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import greendroid.app.GDMapActivity;

public class Shopmap extends GDMapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private MapView mapview;
	private LinearLayout linear;
	private ZoomControls mZoom;
	private MapController mc;
	private ViewGroup zoom;
	private MyLocationOverlay mylayer;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setActionBarContentView(R.layout.shopmap);
		
		//linear = (LinearLayout)findViewById(R.id.MAPLayout2);
		mapview = (MapView)findViewById(R.id.mapview2);
		//mZoom = (ZoomControls)mapview.getZoomControls();
		
        zoom = (ViewGroup)findViewById (R.id.zoomview2);
		
    	zoom.addView(mapview.getZoomControls());
		
		//linear.addView(mZoom);
		
		mc = mapview.getController();
		
		Bundle bundle = this.getIntent().getExtras();
		
		
		final double a = Double.valueOf(bundle.getString("lat"));
		final double b = Double.valueOf(bundle.getString("lng"));
		
		/*
		GeoPoint station = new GeoPoint(
				(int)(25.047192 * 1000000),
				(int)(121.516981 * 1000000)
				);
		*/
		Log.e("1111", bundle.getString("lat"));
		Log.e("2222", "dedede");
		
		
		final GeoPoint station = new GeoPoint(
				(int)(a  * 1000000),
				(int)(b * 1000000)
				);
		/*
		mc.setZoom(17);
		mc.animateTo(station);
		mapview.setSatellite(true);
		*/
		List<Overlay> overlays = mapview.getOverlays();
		mylayer = new MyLocationOverlay(this, mapview);
		mylayer.runOnFirstFix(new Runnable() {		//每次更新時要執行的動作
			public void run() {
				mc.setZoom(17);
				mc.animateTo(station);
				mapview.setSatellite(true);
			}
		});
		mapview.setBuiltInZoomControls(true);
		
		Drawable point_star = getResources().getDrawable(android.R.drawable.star_on);
		point_star.setBounds(0, 0, point_star.getMinimumWidth(), point_star.getMinimumHeight());
		Landmark myLandmark = new Landmark(point_star, this);
		ArrayList<String> shopID = new ArrayList<String>();
		shopID.add(bundle.getString("ID"));
		myLandmark.getpostions(shopID);
		
		overlays.add(myLandmark);
		overlays.add(mylayer);
	
	}
	

}
