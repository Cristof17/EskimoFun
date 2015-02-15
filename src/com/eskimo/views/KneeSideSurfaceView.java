package com.eskimo.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.eskimo.data.Point;
import com.eskimo.eskimo.Main;

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
	private int oldOffset ;
	private boolean change_done ; //tells us if there was a change in direction
	
	
	private Point leftPoint;  //the leftMost point
	private Point rightPoint;  //the rightMost point 
	private Point currentPoint ; //the point from which the next line is going to appear
	private Point nextPoint ; //the point to which the next line will appear 
	
	//canvas which to save as bitmap
	Canvas canvas ;
	Bitmap bitmap ; //the bitmap on which I have control 
	
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
		
		/*
		 * instantiate the left and right points
		 */
		leftPoint = new Point(0 + 300 , 100);
		rightPoint = new Point(Main.screenWidth - 300 , 100 );
		currentPoint =  new Point(Main.screenWidth/2 , 100); //set the starting point of the drawing in the middle
		nextPoint = new Point(Main.screenWidth/2 , 100); //set the ending point at the same position as the starting point 
		
		/*
		 * set the background color for the canvas 
		 */
		bitmap = Bitmap.createBitmap(getWidth(),getHeight(),Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		canvas.drawARGB(255, 255, 0, 0);
		
		/*
		 * create and start the update thread 
		 */
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
	protected void onDraw(Canvas c) {
		super.onDraw(canvas);
		
//		canvas.drawARGB(255, 255, 0, 0);
		
		Paint text_paint = new Paint();
		text_paint.setTextSize(50f);
		
		Paint knee_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		knee_paint.setColor(Color.YELLOW);
		knee_paint.setStrokeWidth(20);
		knee_paint.setTextSize(50);
		width = canvas.getWidth();
		height = canvas.getHeight();
		
		checkChangeInDirection();
		
		if(!change_done){
			changeDirection(nextPoint);
			drawLine(canvas, currentPoint, nextPoint);
		}
		
		canvas.drawText("Old offset  = " + oldOffset, 50, 500, text_paint);
		
		oldOffset = offset ;
		
		
		
		
				
		canvas.drawText("X = "+last_x , 50, 100,text_paint);
		canvas.drawText("Y = "+last_y, 50, 200,text_paint);
		canvas.drawText("Z = "+last_z, 50, 300,text_paint);
		canvas.drawText("Offset = " + offset, 50, 400, text_paint);
		
		c.drawBitmap(bitmap, 0, 0, text_paint); //pass the bitmap that we have control over and 
												// put it on the screen 
		//this right here is the most important line 
		
		
		
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
				calculateOffset(event.values[2]);
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
	
	private void checkChangeInDirection(){
		
		if((oldOffset < 0 && offset > 0) || ( oldOffset > 0 && offset < 0 )) {
			change_done = false; //there was change in direction
		}else {
			change_done = true; //there was no change in direction
		}
	}
	
	private void changeDirection(Point nextPoint){
		
		if(offset < 0){
			nextPoint.x = leftPoint.x;
			nextPoint.y = currentPoint.y + 75 ;
		}else if (offset > 0){
			nextPoint.x = rightPoint.x;
			nextPoint.y = currentPoint.y + 75 ;
		}
		
		if(nextPoint.y > getHeight())
			resetPositions();
		
//		currentPoint = nextPoint ;
	}
	
	private void resetPositions(){
		currentPoint.x = nextPoint.x;
		currentPoint.y = 0 ;
		
		changeDirection(nextPoint);
		canvas.drawARGB(255, 255, 0, 0);
		
	}
	
	private void drawLine(Canvas c , Point currentPoint , Point nextPoint){
		Paint line_paint  = new Paint ();
		line_paint.setColor(Color.CYAN);
		line_paint.setStrokeWidth(50f);
		c.drawLine(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, line_paint);
		currentPoint.x = nextPoint.x ; //current = next 
		currentPoint.y = nextPoint.y ; //current = next
	}
	
	private int getDistance(int startX , int startY , int stopX , int stopY){
		return (int)Math.abs( Math.round( Math.sqrt(Math.pow(stopX - startX, 2) + Math.pow(stopY - stopX,2 ))));
	}
	
	private int calculateOffset(float angle){
		if(angle > 90)
			angle = 90; 
		else if(angle < -90)
			angle = -90;
		
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
					Thread.sleep(200);
				}catch(InterruptedException e){
					Log.e(TAG, "Error at knee side update thread ");
				}
			}
			super.run();
		}
		
		
		
	}
	
}
