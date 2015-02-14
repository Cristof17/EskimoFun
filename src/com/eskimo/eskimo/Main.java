package com.eskimo.eskimo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.eskimo.data.Point;
import com.eskimo.views.KneePreviewSide;


public class Main extends Activity {
	
	public static final String TAG = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * The sideways custom view
         */
        final KneePreviewSide knee_view = new KneePreviewSide(Main.this);
        
        
        final ArrayList<Point> points = new ArrayList<Point>();
        /*
         * Points for test purposes
         */
        for (int i = 0 ; i < 10 ; i++){
        	Point aux = new Point(i,0);
        	aux.y = i + 20 ;
        	points.add(aux);
        
        }
        
        
        Thread update_side = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					
					for (Point p : points){
						
						knee_view.setKneePoint(p);
						knee_view.invalidate();
						knee_view.logPosition();
						Thread.sleep(1000);
						
					}
					
				}catch(InterruptedException e){
					Log.e(TAG,"Sideways knee view thread  interrupted ") ;
				}
				
			}
		});
        
        /*
         * Add the side view to the layout
         */
        LinearLayout main_layout = (LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams side_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT);
        knee_view.setLayoutParams(side_params);
        main_layout.addView(knee_view);
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
