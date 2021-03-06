package com.eskimo.eskimo;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eskimo.data.Record;
import com.eskimo.display.DisplayMeasurements;
import com.eskimo.sensors.SensorsUtils;
import com.eskimo.views.CustomSeekBar;


public class RecordActivity extends Activity implements SensorEventListener , OnClickListener , Runnable{

	private CustomSeekBar bar ;
	private SensorsUtils utils ;
	private TextView text;
	private boolean recording ;
	private Thread addingThread ; 
	private ArrayList<Record> data ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.GRAY);
		setContentView(layout);
		
		this.utils = new SensorsUtils(this, this);
		this.data = new ArrayList<Record>();
		this.addingThread = new Thread(this);
		
		Button recordButton = new Button(RecordActivity.this);
		recordButton.setText("Start Recording");
		recordButton.setBackgroundResource(R.drawable.sign_in_button);
		recordButton.setTextColor(Color.WHITE);
		recordButton.setTextSize(30f);
		recordButton.setOnClickListener(this);
		
		bar = new CustomSeekBar(RecordActivity.this,null ,android.R.attr.progressBarStyleHorizontal);
		bar.setMax(100);
		bar.setProgress(50);
		bar.setStartingPoint(50);
		
		int height = ((WindowManager)(getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getHeight();
		
		
		LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																		 LinearLayout.LayoutParams.WRAP_CONTENT);
		
		button_params.setMargins(100,0,100, 100); //margins for the button 
		
		LinearLayout.LayoutParams bar_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				 LinearLayout.LayoutParams.WRAP_CONTENT);
		
		recordButton.setLayoutParams(button_params);
		
		bar.setLayoutParams(bar_params);
		
		layout.addView(recordButton);
		layout.addView(bar);
		
		text = new TextView(RecordActivity.this);
		text.setLayoutParams(bar_params);
		
		layout.addView(text);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		bar.setProgress(Math.round(((float)(utils.processValue(event) * 0.5) ) + bar.getMax()/2));
		text.setText("Offset is "+ utils.processValue(event));
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		if(v instanceof Button){
			if(!recording){
				v.setBackgroundColor(Color.RED);
				((Button)v).setText("Stop Recording");
				recording = true ;
				Toast.makeText(getApplicationContext(), "Recording descend", Toast.LENGTH_SHORT).show();
				addingThread = new Thread(this);
				addingThread.start();
				data = new ArrayList<Record>();
			}else{
				((Button)v).setText("Start Recording");
				v.setBackgroundResource(R.drawable.sign_in_button);
				recording = false;
				Toast.makeText(getApplicationContext(), "Recording stopped ", Toast.LENGTH_SHORT).show();
				printRecords();
				try {
					addingThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		
		int contor = 0 ;
		
		while(recording){
			
			Record new_record = new Record();
			new_record.setTime(contor);
			new_record.setColor(SensorsUtils.whatColorShouldBe());
			new_record.setOffset(SensorsUtils.offset);
			new_record.setAngle(SensorsUtils.getAngle());
			new_record.setSpeed(0);
			data.add(new_record) ;
			contor ++ ;
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e ){
				Log.e("RECORDINGACTIVITY", "Error at thread sleep");
			}
		}
	}
	
	public void printRecords(){
		for(Record r : data){
			Log.d("RECORDACTIVITY","Results are " + r.getAngle() + " "+ r.getTime() + " "+ r.getAngle());
		}
	}

}
