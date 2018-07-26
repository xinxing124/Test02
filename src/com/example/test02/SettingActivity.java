package com.example.test02;

import java.io.IOException;
import java.io.OutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class SettingActivity extends Activity {
	public static Button myBtnTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//MainActivity.socket.close();
	
        //开始
		myBtnTest=(Button)findViewById(R.id.btnTest);
		myBtnTest.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

			String send; 
	    	int i=0;
	    	int n=0;
	    	
	    	MainActivity.senddata="BEGIN0";
	    	try{
	    		if(MainActivity.socket!=null)
	    		{
		    		OutputStream os = MainActivity.socket.getOutputStream();   //蓝牙连接输出流
		    		//outStream= socket.getOutputStream();   //蓝牙连接输出流
		    		send="BEGIN0"+"\n";
		    		
		    		byte[] bos =send.getBytes();
		    		
		    		//byte[] bos = edit0.getText().toString().getBytes();
		    		for(i=0;i<bos.length;i++){
		    			if(bos[i]==0x0a)n++;
		    		}
		    		byte[] bos_new = new byte[bos.length+n];
		    		n=0;
		    		for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
		    			if(bos[i]==0x0a){
		    				bos_new[n]=0x0d;
		    				n++;
		    				bos_new[n]=0x0a;
		    			}else{
		    				bos_new[n]=bos[i];
		    			}
		    			n++;
		    		}
		    		os.write(bos_new);
	    		}
	    		//os.write(bos);	
	    		
	    	}catch(IOException e){  
	    		Toast.makeText(getApplicationContext(), "蓝牙连接已断开或无连接! ", Toast.LENGTH_LONG).show();
	    	}  
	    	
		}
	    	
			/*
	        String message = "BEGIN"+"\n"; 
	        byte[] msgBuffer = message.getBytes();
	           
	        try {
	            outStream.write(msgBuffer);
	        } catch (IOException e) {
	            //Log.e(TAG, "ON RESUME: Exception during write.", e);
	        	Toast.makeText(getApplicationContext(), "ON RESUME: Exception during write.", Toast.LENGTH_LONG).show();
	        }
	        
	        byte b[]=new byte[1024];
	       // do {
				try {
					//inStream.read(b, 0, 2);
					//while(inStream.read(b)>0)
					//{
					//mResult="";
					//mResult=inStream.toString();
						int n=inStream.read(b);
					   //mResult=new String(b,"utf-8");
						String mResult=new String(b,0,n);
					//}
						textView1.setText(inStream.toString());
					Toast.makeText(getApplicationContext(), "ON RESUME: " +mResult, Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			*/	
			
			
	    });
	}

	BroadcastReceiver mReceiver=new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			//刷新主Activity界面
			//if(intent.getAction().equals("android.intent.action.Setting")){

	            String msg = intent.getStringExtra("msg");

	            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

	         //}
		}
	};

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter("android.intent.action.Setting");
		registerReceiver(mReceiver, intentFilter);	
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(mReceiver);
		super.onStop();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.setting, menu);
//		return true;
//	}

}
