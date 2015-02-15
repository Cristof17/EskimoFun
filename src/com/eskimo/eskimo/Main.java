package com.eskimo.eskimo;

import java.util.ArrayList;

import uk.co.chrisjenx.paralloid.Parallaxor;
import uk.co.chrisjenx.paralloid.views.ParallaxScrollView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.eskimo.views.CustomSeekBar;
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
    	
    	LinearLayout global = (LinearLayout)findViewById(R.id.textLayouts);
    	
    	KneeSideSurfaceView surfaceView = new KneeSideSurfaceView(Main.this);
    	LinearLayout surfaceLayout = (LinearLayout)findViewById(R.id.surfaceView);
    	LinearLayout.LayoutParams surfaceParams = new LinearLayout.LayoutParams(
    											LinearLayout.LayoutParams.MATCH_PARENT,
    											LinearLayout.LayoutParams.MATCH_PARENT);
    	surfaceView.setLayoutParams(surfaceParams);
    	surfaceLayout.addView(surfaceView);
    	
 
    	ParallaxScrollView scrollView = (ParallaxScrollView)findViewById(R.id.scroll_view);
    	
    	CustomSeekBar progressBar = new CustomSeekBar(Main.this,null,android.R.attr.progressBarStyleHorizontal);
    	
    	LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    	
    	progressBar.setMax(100);
    	progressBar.setProgress(50);
    	progressBar.setLayoutParams(params2);
    	
    	global.addView(progressBar);
    	
    	
    	if(scrollView instanceof Parallaxor){
    		((Parallaxor)scrollView).parallaxViewBy(surfaceView, 0.5f);
    	}
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
