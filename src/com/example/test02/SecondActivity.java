package com.example.test02;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends Activity {
	Button btnOk,btnCacel;
	EditText etTrack,etNumber,etOperator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

//		etTrack=(EditText)findViewById(R.id.etTrack);
//		etNumber=(EditText)findViewById(R.id.etNumber);
//		etOperator=(EditText)findViewById(R.id.etOperator);
		
//	     // 读取 SharedPreferences   
//        SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);
//        etTrack.setText(preferences.getString("Track_val", "GT-20130302"));
//        etNumber.setText(String.valueOf(preferences.getInt("Number_val", 0)));
//        etOperator.setText(preferences.getString("Operator_val", "张三"));
		
        
//		 btnOk=(Button)findViewById(R.id.btnOk);
//		 btnOk.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				 SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);   
//				 Editor editor = preferences.edit();   
//				 editor.putString("Track_val", etTrack.getText().toString());   
//				 editor.putInt("Number_val", Integer.valueOf(etNumber.getText().toString()));
//				 editor.putString("Operator_val", etOperator.getText().toString());
//				 editor.commit();
//				 
//				 //Intent intent = new Intent();
//			     //Bundle bundle=new Bundle();
//			     //bundle.putString("name", "Test");
//			     //intent.putExtras(bundle);
//			     //int requestCode=0;
//				 //setResult(RESULT_OK,intent);//返回数据
//				
//				setResult(RESULT_OK);//返回数据
//				finish();
//			}
//	   });
//		 
		 btnCacel=(Button)findViewById(R.id.btnCancel);
		 btnCacel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "取消退出", Toast.LENGTH_SHORT).show();
				finish();
			}
	   });  
	}
}
