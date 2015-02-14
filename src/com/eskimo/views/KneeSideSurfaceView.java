package com.eskimo.views;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.eskimo.data.Point;

@SuppressLint("WrongCall")
public class KneeSideSurfaceView extends SurfaceView implements SurfaceHolder.Callback ,SensorEventListener {

	//sensors
	private SensorManager sensorManager ;
	private Sensor sensor ;
	private long lastUpdate ; //for sampling the sensor data
	private long current ;
	private float last_x;
	private float last_y;
	private float last_z;
	private final long SHAKE_THRESHOLD = 600;
	private float speed ;
	private int MAX_LENGHT ;
	
	//SurfaceView size
	int width ;
	int height;
	
	//knee variables 
	private int offset = 0 ;
	
	private Context context ;
	private Point knee;  
	private SurfaceHolder holder ;
	private Boolean running ;
	
	public KneeSideSurfaceView(Context context) {
		super(context);
		this.context = context ;
		holder = getHolder();
		holder.addCallback(this);
		
		this.sensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		this.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

	}

	public static final String TAG = "KNEESIDESURFACEVIEW";
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "Knee Side Surface created ");
		
		/*
		 * assign the maximum length for thigh and femur 
		 */
		MAX_LENGHT = getDistance(0, 0, getWidth()/2, getHeight()/2);
		
		UpdateThread thread = new UpdateThread(); 
		thread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "Knee Side Surface changed ");
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Knee Side Surface destroyed ");
		
		sensorManager.unregisterListener(this);
		
	}
	
	
	
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawARGB(255, 255, 0, 0);
		
		Paint knee_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		knee_paint.setColor(Color.YELLOW);
		knee_paint.setStrokeWidth(200);
		
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		//set the joints
		Point thigh = new Point(100, calculateThighY(canvas.getWidth()/2 ));
		Point knee = new Point (calculateKneeX(canvas.getWidth()/2), canvas.getHeight()/2 );
		Point foot = new Point(100 + 30 ,Math.round(canvas.getHeight() - 300 + ((-1)* (0.5f) * (calculateThighY(canvas.getWidth())/2))));
		
		
		canvas.drawCircle(thigh.x ,thigh.y , canvas.getHeight()/canvas.getWidth() * 40, knee_paint);
		canvas.drawCircle(knee.x , knee.y , canvas.getHeight()/canvas.getWidth() * 40, knee_paint);
		
		
		//set the lines between joints
		canvas.drawLine(thigh.x, thigh.y,knee.x,knee.y,knee_paint); //line between thigh and knee
		canvas.drawLine(knee.x , knee.y ,foot.x , foot.y ,knee_paint); //line betweek knee and foot
		
		Paint text_paint = new Paint();
		text_paint.setTextSize(125f);
		
		
				
		canvas.drawText("X = "+last_x , 50, 100,text_paint);
		canvas.drawText("Y = "+last_y, 50, 300,text_paint);
		canvas.drawText("Z = "+last_z, 50, 500,text_paint);
		canvas.drawText("Offset = " + offset, 50, 700, text_paint);
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		sensor = event.sensor;
		if(sensor.getType() == Sensor.TYPE_ORIENTATION){
			
			last_x = event.values[0];
			last_y = event.values[1];
			last_z = event.values[2];
			
			current = System.currentTimeMillis();
			if(current - lastUpdate > 2){
				lastUpdate = current ;
				calculateOffset(event.values[1]);
			}
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	

	public void setRunning(Boolean value){
		this.running = value ;
	}
	
	private int getDistance(int startX , int startY , int stopX , int stopY){
		return (int)Math.abs( Math.round( Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - stopX,2 ))));
	}
	
	private int calculateOffset(float angle){
		if(angle > 90)
			angle = 60; 
		else if(angle < -90)
			angle = -60;
		
		int percent_meu = Math.round((angle * 100)/90) ; // reprezinta cu cat % trebuie sa misc coapsa mai sus sau mai jos 
		offset =(-1) * percent_meu;  //offset-ul cu care trebuie sa mut coapsa mai sus si genunchiul mai la stanga
		return offset ;
	}
	
	private Point moveHorizontal(Point p, int value){
		p.x += value ;
		return p ;
	}
	
	private Point moveVertical(Point p  , int value ){
		p.y += value ; 
		return p ;
	}
	
	private int calculateKneeX(int width){
		return (width) + (offset * width)/100; // offset percent of canvas/2 width 
	}
	
	private int calculateThighY(int height){
		return height + (offset * height)/100; //offset percent of canvas/2 height
	}


	private class UpdateThread extends Thread{
	
		@Override
		public void run() {
			while(running){
//				Log.d(TAG,"Knee side thread Running");
				
				Canvas c = holder.lockCanvas(null);
				onDraw(c);
				holder.unlockCanvasAndPost(c);

				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					Log.e(TAG, "Error at knee side update thread ");
				}
			}
			super.run();
		}
		
		
		
	}
	
}
