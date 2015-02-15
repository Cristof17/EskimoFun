package com.eskimo.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsUtils {

	private  SensorManager sensorManager ;
	private  Sensor sensor;
	private  SensorEventListener listener;
	private  long lastUpdate; 
	public static  int offset ;
	
	public SensorsUtils(Context context ,SensorEventListener listener){
		
		this.listener = listener ;
		this.sensorManager =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		this.sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
		
	}
	
	public  int processValue(SensorEvent event){
		
		sensor = event.sensor;
		if(sensor.getType() == Sensor.TYPE_ORIENTATION){
			
			
			long current = System.currentTimeMillis();
			if(current - lastUpdate > 2){
				lastUpdate = current ;
				calculateOffset(event.values[2]);
			}
		}
		return offset; 
	}
	
	public  void unRegisterListener(){
		sensorManager.unregisterListener(listener);
	}
	
	private  int calculateOffset(float angle){
		if(angle > 90)
			angle = 90; 
		else if(angle < -90)
			angle = -90;
		
		int percent_meu = Math.round((angle * 100)/90) ; // reprezinta cu cat % trebuie sa misc coapsa mai sus sau mai jos 
		offset =(-1) * percent_meu;  //offset-ul cu care trebuie sa mut coapsa mai sus si genunchiul mai la stanga
		return offset ;
	}
	
	

}
