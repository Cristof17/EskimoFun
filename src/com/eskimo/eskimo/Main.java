package com.eskimo.eskimo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.eskimo.views.KneeSideSurfaceView;

public class Main extends Activity {
	
	private volatile Boolean running ;
	public static int screenWidth ;
	public static int screenHeight ;
	
	public static final String TAG = "MAINACTIVITY";
	
    protected void onCreate(Bundle savedInstanceState) {
    	running = new Boolean(true);
    	setContentView(R.layout.activity_main);
    	super.onCreate(savedInstanceState);
    	
    	screenWidth = getScreenWidth(this);
    	screenHeight = getScreenHeight(this);
    	
    	LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
    	LinearLayout.LayoutParams surface_view_params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , convertPixelsToDp(1920));
    	KneeSideSurfaceView surfaceknee = new KneeSideSurfaceView(Main.this);
    	surfaceknee.setLayoutParams(surface_view_params);
    	layout.addView(surfaceknee);
    	
    	surfaceknee.setRunning(true);
    	
    	
    	
    	
   	}
    
    
    private static int getScreenHeight(Context context){
    	WindowManager wm  = (WindowManager)context.  getSystemService(Context.WINDOW_SERVICE);
    	return wm.getDefaultDisplay().getHeight();
    	
    }
    
    private static int getScreenWidth(Context context){
    	WindowManager wm  = (WindowManager)context .getSystemService(Context.WINDOW_SERVICE);
    	return wm.getDefaultDisplay().getWidth();
    	
    }
    
    public int convertPixelsToDp(float px){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int)dp;
    }

}
