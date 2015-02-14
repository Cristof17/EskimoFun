package com.eskimo.eskimo;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eskimo.data.Point;
import com.eskimo.views.KneeSideSurfaceView;


public class Main extends Activity {
	
	private volatile Boolean running ; 
	
	public static final String TAG = "MAINACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.running = new Boolean(true);
        
        setContentView(R.layout.activity_main);
        
        /*
         * The sideways custom view
         */
        final KneeSideSurfaceView knee_surface = new KneeSideSurfaceView(Main.this);
        knee_surface.setRunning(running);
        
        
        final ArrayList<Point> points = new ArrayList<Point>();
        /*
         * Points for test purposes
         */
        for (int i = 0 ; i < 10 ; i++){
        	Point aux = new Point(i * 100 ,0);
        	aux.y = i * 100 + 20  ;
        	points.add(aux);
        
        }

		for (Point p : points){
			
			knee_surface.setKneePoint(p);
			knee_surface.invalidate();
			knee_surface.logPosition();
			
		}

        
        /*
         * Add the side view to the layout
         */
        LinearLayout main_layout = (LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout.LayoutParams side_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.MATCH_PARENT);
        knee_surface.setLayoutParams(side_params);
        main_layout.addView(knee_surface);
        
        
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
