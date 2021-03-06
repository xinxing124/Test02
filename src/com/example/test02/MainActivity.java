package com.example.test02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.Format.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
//import org.achartengine.chartdemo.demo.chart.ArrayList;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import org.achartengine.model.XYSeries;  
import org.apache.http.util.EncodingUtils;

public class MainActivity extends Activity {
	public  final static String PAR_KEY = "com.tutor.objecttran.par";  
	private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
	private Timer mTimer = new Timer(true);
	private TimerTask mTimerTask;
	
	EditText etFileName;
	EditText etTrack;
	EditText etNumber;
	EditText etOperator;
	String mode="1";
	ListView lv;
	static int initFlag=0;
	WritableWorkbook book;
	WritableSheet sheet1;
	WritableSheet sheet2;
	int SheetColumn=0;
	public String filename=""; //用来保存存储的文件名
	BluetoothDevice device = null;     //蓝牙设备
	public static BluetoothSocket socket = null;      //蓝牙通信socket
	String address = "FE:85:ED:CD:D7:75";
	String zeroname="";
	double Coefficient=1.0;
	OutputStream outStream = null;      
    InputStream inStream = null; 
    public static String senddata="";
	private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
	XYMultipleSeriesDataset dataset=new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	XYMultipleSeriesDataset dataset1=new XYMultipleSeriesDataset();
	XYMultipleSeriesRenderer renderer1 = new XYMultipleSeriesRenderer();
	double list[];
	int filenum=0;
	int MaxLen=202;//1002;
	static int ShowLen=5;//1;
	//double nz[]=null;
	double m[];
	double n[];
	double p[];
	double q[];
	double r[];
	double s[];
	
	//double m[]=new double[MaxLen];
	//double n[]=new double[MaxLen];
	//double p[]=new double[MaxLen];
	//double q[]=new double[MaxLen];
	//double r[]=new double[MaxLen];
	//double s[]=new double[MaxLen];
	double r0=0.0;
	double s0=0.0;
	double p0=0.0;
	double q0=0.0;
	double max0,max1,min0,min1;
	double Quotiety[][]=new double[9][4];
	int typeDialog=0;
    boolean bRun = true;
    boolean bThread = false;
    boolean bTestFlag = false;
    private String smsg;    //显示用数据缓存
    private String fmsg;    //保存用数据缓存
    Button myBtn1;
    Button myBtn2;
    Button myBtn3;
    Button myBtn4;
    Button myBtn5;
    ImageView ImageView1;
    ImageView ImageView2;
	LinearLayout barchart;
	LinearLayout linechart;
	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	Button btn;
	int Flag;
	boolean isconnect;
	boolean ZeroFlag;
	boolean UFlag;
	boolean DebugFlag;
	
	
    private void connect(BluetoothDevice device) throws IOException {     
        // 固定的UUID      
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";     
        UUID uuid = UUID.fromString(SPP_UUID);     
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);     
        socket.connect();     
    }    
    
	static public boolean createBond(Class btClass, BluetoothDevice btDevice)  
	throws Exception  
	{  
	    Method createBondMethod = btClass.getMethod("createBond");  
	    Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);  
	    return returnValue.booleanValue();  
	}  

	/** 
	 * 与设备解除配对 参考源码：platform/packages/apps/Settings.git 
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java 
	 */  
	static public boolean removeBond(Class btClass, BluetoothDevice btDevice)  
	        throws Exception  
	{  
	    Method removeBondMethod = btClass.getMethod("removeBond");  
	    Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);  
	    return returnValue.booleanValue();  
	}  

	static public boolean setPin(Class btClass, BluetoothDevice btDevice,  
	        String str) throws Exception  
	{  
	    try  
	    {  
	        Method removeBondMethod = btClass.getDeclaredMethod("setPin",  
	                new Class[]  
	                {byte[].class});  
	        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,  
	                new Object[]  
	                {str.getBytes()});  
	        Log.e("returnValue", "" + returnValue);  
	    }  
	    catch (SecurityException e)  
	    {  
	        // throw new RuntimeException(e.getMessage());  
	        e.printStackTrace();  
	    }  
	    catch (IllegalArgumentException e)  
	    {  
	        // throw new RuntimeException(e.getMessage());  
	        e.printStackTrace();  
	    }  
	    catch (Exception e)  
	    {  
	        // TODO Auto-generated catch block  
	        e.printStackTrace();  
	    }  
	    return true;  

	}  

	// 取消用户输入  
	static public boolean cancelPairingUserInput(Class btClass,  
	        BluetoothDevice device)  

	throws Exception  
	{  
	    Method createBondMethod = btClass.getMethod("cancelPairingUserInput");  
	    // cancelBondProcess()  
	    Boolean returnValue = (Boolean) createBondMethod.invoke(device);  
	    return returnValue.booleanValue();  
	}  

	// 取消配对  
	static public boolean cancelBondProcess(Class btClass,  
	        BluetoothDevice device)  

	throws Exception  
	{  
	    Method createBondMethod = btClass.getMethod("cancelBondProcess");  
	    Boolean returnValue = (Boolean) createBondMethod.invoke(device);  
	    return returnValue.booleanValue();  
	}  

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		mTimerTask = new TimerTask()
//		{
//		public void run()
//		{
//		runOnUiThread(new Runnable(){  
//            @Override  
//            public void run() {  
//                setTitle("hear me?");  
//            }});  
//		Log.v("android123","cwj");
//		//myBtn2.setEnabled(true);
//		mTimer.cancel();
//		}
//		};
		
	    smsg = "";    //显示用数据缓存
	    fmsg = "";    //保存用数据缓存
	    Flag=0;
	    isconnect=false;//蓝牙连接状态
		barchart = (LinearLayout) findViewById(R.id.barchart);
		linechart = (LinearLayout) findViewById(R.id.linechart);
		textView1=(TextView)findViewById(R.id.textView1);
		textView2=(TextView)findViewById(R.id.textView2);
		textView3=(TextView)findViewById(R.id.textView3);
		textView4=(TextView)findViewById(R.id.textView4);
		btn = (Button) findViewById(R.id.button2);

			SetXYMultipleSeries(barchart,dataset, renderer,list,false,2);
			SetXYMultipleSeries(linechart,dataset1, renderer1,list,false,3);
			UFlag=true;
			
			textView2.setText("最大值：0.000" +"\n" +"最小值：0.000");
			textView3.setText("最大值：0.000" +"\n" +"最小值：0.000");
			
			UFlag=false;
			String ss="";
			
			DebugFlag=FileExit("Debug.txt");
			
			try {
				address=RWToSDCard("111.txt",address,false);
				UFlag=true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Coefficient=Double.parseDouble(RWToSDCard("Coefficient.txt",Double.toString(Coefficient),false));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FiletoQuotiety(address,Quotiety);
		//Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
		zeroname=address+"Z"+".txt";
		zeroname=zeroname.replaceAll(":", "");
		//ZeroFlag=FiletoDoubles(zeroname,p,q,r,s);
		
		ImageView2=(ImageView)findViewById(R.id.imageView2);
		ImageView2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SetXYMultipleSeries(barchart,dataset, renderer,m,true,2);
        		SetXYMultipleSeries(linechart,dataset1, renderer1,n,true,3);
			}
        });
        //刷新
        myBtn3=(Button)findViewById(R.id.button3);
        myBtn3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
				if(typeDialog==0)
				{
				SetXYMultipleSeries(barchart,dataset, renderer,m,true,2);
        		SetXYMultipleSeries(linechart,dataset1, renderer1,n,true,3);
				}else
				{
					SetXYMultipleSeries(barchart,dataset, renderer,m,true,0);
	        		SetXYMultipleSeries(linechart,dataset1, renderer1,n,true,1);
				}
				
			}
        });
        
		if(mBluetoothAdapter.isEnabled()==false){
			mBluetoothAdapter.enable();
    	}
		
        if(DebugFlag)
        SaveSingleExcelData(1);//single
        //保存
		myBtn4=(Button)findViewById(R.id.button4);
        myBtn4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
			    Dialog dialog = new AlertDialog.Builder(MainActivity.this)  
	            .setTitle("选择模型")  
	            .setIcon(android.R.drawable.ic_dialog_info)
	            .setSingleChoiceItems(new String[] { "普速轨", "高速轨" }, typeDialog,
	        			new DialogInterface.OnClickListener() {  
	    			public void onClick(DialogInterface dialog, final int which) {
	    				typeDialog=which;
	    				if(which==0)
	    					Toast.makeText(getApplicationContext(), Integer.toHexString(0), Toast.LENGTH_SHORT).show();
	    				else
	    					Toast.makeText(getApplicationContext(), Integer.toHexString(1), Toast.LENGTH_SHORT).show();
	    			//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
	    			dialog.dismiss();
	    			 }
	    			}).setPositiveButton("取消",null).show();
	    		*/
				
				
				//dialogShowCheck();
				if(DebugFlag)
					SaveSingleExcelData(2);//single
				else{
				//	SaveData();

				String status = Environment.getExternalStorageState(); 
				// 判段SD卡是否存在 
				if (status.equals(Environment.MEDIA_MOUNTED)) { 
					String dir=Environment.getExternalStorageDirectory()+"/ANC";
				java.io.File a=new java.io.File(dir);
				if (!a.exists())a.mkdir();
				File file=new File(a,zeroname);
				if(!file.exists()) 
				{	
					//if (n[0]!=0.0 )
					//{
						p0+=0.0;
						q0+=0.0;
						r0+=0.0;
						s0+=0.0;
					
						if(n[MaxLen-1]==0.0)
						{
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("提示")
							.setMessage("您确定要保存左起始数据？")  
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确定",  new DialogInterface.OnClickListener(){
							                public void onClick(DialogInterface dialog, int which) {
							                	//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
							                	
							                	for(int i=0;i<MaxLen;i++)
												{
													p[i]=m[i];
													q[i]=n[i];
													if(i<MaxLen-1)
													{
														p0+=p[i];
														q0+=q[i];
														r0+=r[i];
														s0+=s[i];
													}
												}
							                	if(p0!=0.0&&q0!=0.0&&r0!=0.0&&s0!=0.0)
													DoublestoFile(zeroname,p,q,r,s);
							                	else
							                		Toast.makeText(MainActivity.this,"左起数据缓存", Toast.LENGTH_SHORT).show();
							                }})
							.setNegativeButton("取消", null).show();
						}
						else
						{
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("提示")
							.setMessage("您确定要保存右起始数据？")  
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确定",  new DialogInterface.OnClickListener(){
							                public void onClick(DialogInterface dialog, int which) {
							                	//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
												for(int i=0;i<MaxLen;i++)
												{
													r[i]=m[i];
													s[i]=n[i];
													if(i<MaxLen-1)
													{
														p0+=p[i];
														q0+=q[i];
														r0+=r[i];
														s0+=s[i];
													}
												}
												if(p0!=0.0&&q0!=0.0&&r0!=0.0&&s0!=0.0)
													DoublestoFile(zeroname,p,q,r,s);
												else
													Toast.makeText(MainActivity.this,"右起数据缓存", Toast.LENGTH_SHORT).show();
							                }})
							.setNegativeButton("取消", null).show();

						}
					//}
					
				}
				else
					SaveData();
				}
				}
				//SaveExcelData();
				//SaveData();
				//typeDialog();
/*			
EditText editText = new EditText(MainActivity.this);
editText.setBackgroundColor(Color.WHITE);
new AlertDialog.Builder(MainActivity.this)
.setTitle("请输入")
.setMessage("您确定要退出本程序吗？")  
.setIcon(android.R.drawable.ic_dialog_info)
.setView(editText)
.setPositiveButton("确定", null)
.setNegativeButton("取消", null).show();
*/
				
/*
new AlertDialog.Builder(MainActivity.this)
.setTitle("请输入")
.setMessage("您确定要退出本程序吗？")  
.setIcon(android.R.drawable.ic_dialog_info)
.setView(new EditText(MainActivity.this))
.setPositiveButton("确定", null)
.setNegativeButton("取消", null).show();
*/

//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL).setTitle("请输入")
//.setMessage("您确定要退出本程序吗？")  
//.setIcon(android.R.drawable.ic_dialog_info).setView(
//new EditText(MainActivity.this)).setPositiveButton("确定", null)
//.setNegativeButton("取消", null).show();

//,AlertDialog.THEME_TRADITIONAL
	    		//	.setPositiveButton("取消", new DialogInterface.OnClickListener(){
	            //        public void onClick(DialogInterface dialog, int which) {
	            //        	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
	            //        }}).show();
			    
				//Toast.makeText(getApplicationContext(), Integer.toHexString(typeDialog()), Toast.LENGTH_SHORT).show();
			}
        });
        //退出
        myBtn5=(Button)findViewById(R.id.button5);
        myBtn5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "正常退出", Toast.LENGTH_SHORT).show();
//				//finish();
//				Intent intent = new Intent();  //更重要的是我们可以看在别的Activity中是拿到初始化的值，还是修改后的
//		        //intent.setClass(MainActivity.this, SecondActivity.class);
//				intent.setClass(MainActivity.this, ExitActivity.class);
//		        //startActivity(intent);
//		        startActivityForResult(intent,0);
				//dialogShowCheck();
				exitDialog();
			}
        });  
        
        //连接
        myBtn1=(Button)findViewById(R.id.btnopen);
        myBtn1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
		// TODO Auto-generated method stub
		//imageView1.setEnabled(false);
		//Toast.makeText(getApplicationContext(), "ON RESUME: Exception during write.", Toast.LENGTH_LONG).show();
		//barchart.removeAllViewsInLayout();
		//linechart.removeAllViewsInLayout();
		//SetXYMultipleSeries(barchart,dataset, renderer,list,true,2);
		//SetXYMultipleSeries(linechart,dataset1, renderer1,list,true,3);
				
				
		mBluetoothAdapter.cancelDiscovery();
		
		if(mBluetoothAdapter.isEnabled()==false){
			mBluetoothAdapter.enable();
    	}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//if(device==null)
		//{
		//String address = data.getExtras()
        //        .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		if(DebugFlag)
		{	
			//Intent intent = new Intent();
			//intent.setClass(MainActivity.this, DeviceListActivity.class);
	        //startActivity(intent);
			
			Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class); //跳转程序设置
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
    		/*
    		if(UFlag)
    		{
    		//-----------------------------------------------------
    		                // 得到蓝牙设备句柄      
    		                device = mBluetoothAdapter.getRemoteDevice(address);

    						
    		                // 用服务号得到socket
    		                try{
    		                	socket = device.createRfcommSocketToServiceRecord(MY_UUID);
    		                }catch(IOException e){
    		                	Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
    		                }
    		                //连接socket
    		                try{
    		                	socket.connect();
    		                	Toast.makeText(MainActivity.this, "连接"+device.getName()+"成功！", Toast.LENGTH_SHORT).show();
    		                }catch(IOException e){
    		                	try{
    		                		Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
    		                		socket.close();
    		                		socket = null;
    		                	}catch(IOException ee){
    		                		Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_SHORT).show();
    		                	}
    		                	
    		                	return;
    		                }
    		                
    		                
    		                //打开接收线程
    		                try{
    		            		inStream = socket.getInputStream();   //得到蓝牙数据输入流
    		            		}catch(IOException e){
    		            			Toast.makeText(MainActivity.this, "接收数据失败！", Toast.LENGTH_SHORT).show();
    		            			return;
    		            		}
    		            		if(bThread==false){
    		            			ReadThread.start();
    		            			bThread=true;
    		            		}else{
    		            			bRun = true;
    		            		}
    		            	
    		    
    		}*/
		}
		else
		{
			 
			//if(isconnect) return;
			device = mBluetoothAdapter.getRemoteDevice(address);
//			if(device.getBondState() != BluetoothDevice.BOND_BONDED){//判断给定地址下的device是否已经配对  
////		         try{  
////		        	//byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");  
////		        	 //device.setPin(pinBytes);  
////		        	// createBond(device.getClass(), device);
////		        	 setPin(device.getClass(), device, "00000000");//设置pin值  
////		              createBond(device.getClass(), device);
////		              //cancelPairingUserInput(device.getClass(), device); 
////		          }  
////		          catch (Exception e) {  
////		           // TODO: handle exception  
////		          //System.out.println("配对不成功");
////		        	  Toast.makeText(getApplicationContext(), "配对不成功！", Toast.LENGTH_SHORT).show();
////		          }
//				try {  
//                    // 连接  
//                    connect(device);  
//                } catch (IOException e) {  
//                    e.printStackTrace();  
//                }  
//		}  
			 
//			    try {
//					Method createBondMethod =  device.getClass().getMethod("createBond");  
//					Boolean returnValue = (Boolean) createBondMethod.invoke(device);
//				} catch (SecurityException e1) {
//					// TODO 自动生成的 catch 块
//					e1.printStackTrace();
//				} catch (IllegalArgumentException e1) {
//					// TODO 自动生成的 catch 块
//					e1.printStackTrace();
//				} catch (NoSuchMethodException e1) {
//					// TODO 自动生成的 catch 块
//					e1.printStackTrace();
//				} catch (IllegalAccessException e1) {
//					// TODO 自动生成的 catch 块
//					e1.printStackTrace();
//				} catch (InvocationTargetException e1) {
//					// TODO 自动生成的 catch 块
//					e1.printStackTrace();
//				}  
			
			//try {
			//	if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			//	{
			//		Toast.makeText(getApplicationContext(), "未与设备"+address+"完成配对，请配对后重试！", Toast.LENGTH_SHORT).show();
//				Boolean returnValue;
//				Method createBondMethod,removeBondMethod;
//				// 手机和蓝牙采集器配对
//				//createBondMethod = device.getClass().getMethod("createBond");
//				//returnValue = (Boolean) createBondMethod.invoke(device);
//				
//				String strPsw="00000000";
//				byte[] r=strPsw.getBytes();
//				removeBondMethod = device.getClass().getDeclaredMethod("setPin",new Class[]{byte[].class});
//				returnValue = (Boolean) removeBondMethod.invoke(device.getClass(),new Object[]{strPsw.getBytes()});
//				
				//createBondMethod = device.getClass().getMethod("createBond");
				//returnValue = (Boolean) createBondMethod.invoke(device);
				
				//createBondMethod = device.getClass().getMethod("cancelPairingUserInput");
				// cancelBondProcess()
				//returnValue = (Boolean) createBondMethod.invoke(device);
				
					//Toast.makeText(getApplicationContext(), "无法(1)建立连接！", Toast.LENGTH_SHORT).show();
				//}
				//else
				//Log.e("returnValue", "" + returnValue);
				
				//ClsUtils.setPin(btDevice.getClass(), btDevice, strPsw); // 手机和蓝牙采集器配对
				//ClsUtils.createBond(btDevice.getClass(), btDevice);
				//ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
				
			//} catch (SecurityException e1) {
			//	// TODO Auto-generated catch block
			//	e1.printStackTrace();
			//} catch (IllegalArgumentException e1) {
			//	// TODO Auto-generated catch block
			//	e1.printStackTrace();
				
//			} catch (NoSuchMethodException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (IllegalAccessException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (InvocationTargetException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
				
			//}
			//Log.v("tag", "msg");
			//try {
			//	Thread.sleep(2000);
			//} catch (InterruptedException e1) {
			//	// TODO Auto-generated catch block
			//	e1.printStackTrace();
			//}
			
				//if(socket==null)
				//{
					try{
						if(socket!=null)
						{
						socket.close();
		        		socket = null;
						}
					}catch(IOException e){
					
					}
					try{
					socket = device.createRfcommSocketToServiceRecord(MY_UUID);
					}catch(IOException e){
						Toast.makeText(getApplicationContext(), "连接端口创建失败！", Toast.LENGTH_SHORT).show();
						
					}
					
					try{
						//if(!socket.isConnected())
							socket.connect();
							isconnect=true;
			        	Toast.makeText(getApplicationContext(), "Connect "+device.getName()+" sucess", Toast.LENGTH_SHORT).show();
			        }catch(IOException e){
			        	try{
			        		String msg=e.toString();
			        		msg=msg.replaceFirst("java.io.IOException: ", "");
			        		Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
			        		//Toast.makeText(getApplicationContext(), "连接失败,请检查电源或重新配对！", Toast.LENGTH_SHORT).show();
			        		socket.close();
			        		socket = null;
	
			        	}catch(IOException ee){
			        		Toast.makeText(getApplicationContext(), "Not connect sucess！", Toast.LENGTH_SHORT).show();
			        	}
			        	return;
			        }
					
					/*
					boolean connectflag=false;
					for(int i=0;i<6;i++)
					{
						try{
							socket.connect();
				        	Toast.makeText(getApplicationContext(), "连接"+device.getName()+"成功！", Toast.LENGTH_SHORT).show();
				        	connectflag=true;
				        	break;
				        }catch(IOException e){
				        	connectflag=false;
				        }
						//thress.sleep 1000;
					}
					if(!connectflag)
					{
						try{
							Toast.makeText(getApplicationContext(), "连接失败,请检查电源或重新配对！", Toast.LENGTH_SHORT).show();
							socket.close();
							socket = null;
						}catch(IOException ee){
			        		Toast.makeText(getApplicationContext(), "连接失败！", Toast.LENGTH_SHORT).show();
			        	}
					}
					*/
				//}
				//else
				//	Toast.makeText(getApplicationContext(), "已经连接！", Toast.LENGTH_SHORT).show();
				//打开接收线程
                try{
                	inStream = socket.getInputStream();   //得到蓝牙数据输入流
            		}catch(IOException e){
            			Toast.makeText(getApplicationContext(), "接收数据失败！", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
            			ReadThread.start();
            			bThread=true;
            		}else{
            			bRun = true;
            		}
				
		}	
		//}
		//else
		//	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();

			}
        });
        
        //开始
	    myBtn2=(Button)findViewById(R.id.button2);
	    myBtn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			//String send; 
	    	//int i=0;
	    	//int n=0;
//			mTimer.schedule(mTimerTask, 10000,20000); //在1秒后每5秒执行一次定时器中的方法，比如本文为调用log.v打印输出。
			fmsg="";
	    	if(!DebugFlag)
	    	{
	    		String send; 
		    	int i=0;
		    	int n=0;
	    	try{
		    		if(socket!=null)
		    		{
			    		OutputStream os = socket.getOutputStream();   //蓝牙连接输出流
			    		//outStream= socket.getOutputStream();   //蓝牙连接输出流
			    		senddata="BEGIN1";
			    		
			    		Toast.makeText(getApplicationContext(), senddata, Toast.LENGTH_SHORT).show();
			    		
			    		send=senddata+"\n";
			    		
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
/*			    		byte[] bos_new = new byte[6];
			    		bos_new[0]=0x02;
			    		bos_new[1]=0x30;
			    		bos_new[2]=0x02;
			    		bos_new[3]=0x02;
			    		bos_new[4]=(byte) (bos_new[1]^bos_new[2]^bos_new[3]);
			    		bos_new[5]=0x93;*/
			    		
			    		os.write(bos_new);
		    		}
	    		//os.write(bos);	
	    		
	    	}catch(IOException e){  
	    		Toast.makeText(getApplicationContext(), "蓝牙连接已断开或无连接! ", Toast.LENGTH_LONG).show();
	    	}  	
	    	}
	    	else
	    	{
	    	Dialog dialog = new AlertDialog.Builder(MainActivity.this)  
            .setTitle("选择模式")  
            .setIcon(android.R.drawable.ic_dialog_info)
            .setSingleChoiceItems(new String[] { "采集", "标定" }, 0,
        			new DialogInterface.OnClickListener() {  
    			public void onClick(DialogInterface dialog, final int which) {

    				if(which==0)
    					mode="1";
    				else
    					
    					//Toast.makeText(getApplicationContext(), Integer.toHexString(1), Toast.LENGTH_SHORT).show();
    					mode="0";
    					
    				try{
        				String send; 
        		    	int i=0;
        		    	int n=0;
    		    		if(socket!=null)
    		    		{
    		    			/*
    		    			//打开接收线程
    		                try{
    		            		inStream = socket.getInputStream();   //得到蓝牙数据输入流
    		            		}catch(IOException e){
    		            			Toast.makeText(MainActivity.this, "接收数据失败！", Toast.LENGTH_SHORT).show();
    		            			return;
    		            		}
    		            		if(bThread==false){
    		            			ReadThread.start();
    		            			bThread=true;
    		            		}else{
    		            			bRun = true;
    		            		}
    		            		*/
    		            		
    			    		OutputStream os = socket.getOutputStream();   //蓝牙连接输出流
    			    		//outStream= socket.getOutputStream();   //蓝牙连接输出流
    			    		senddata="BEGIN" +mode;
    			    		
    			    		Toast.makeText(getApplicationContext(), senddata, Toast.LENGTH_SHORT).show();
    			    		
    			    		send=senddata+"\n";
    			    		
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
    				
    			//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
    			dialog.dismiss();
    			 }
    			}).setPositiveButton("取消",null).show();
	    	}
	    	//for(int k=0;k<10;k++)
	    	//{
	    		//bTestFlag=false;
	    	
	    	//while(!bTestFlag);
	    	//}
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

	    
		/*
		PointF x;
		
        // 2,进行显示
        XYMultipleSeriesDataset dataset_TOP = new XYMultipleSeriesDataset();
        XYMultipleSeriesDataset dataset_Side = new XYMultipleSeriesDataset();
        // 2.1, 创建柱状图数据
        Random r = new Random();
        double OverrunX[][]={{0,500,1000},{0,500,1000},{0,500,1000},{0,500,1000}};
    	double OverrunY[][]={{0,0.3,0},{0,0.1,0},{0,0.1,0},{0,-0.2,0}};
    	
        for (int i = 0; i < 4; i++) {
            // 注意,这里与昨天的XYSeries 有一点不同!!这里使用CategroySeries
            //CategorySeries series = new CategorySeries("test" + (i + 1));
        	XYSeries series = new XYSeries("test" + (i + 1));
        	
        	series.add(OverrunX[i][0],OverrunY[i][0]);
        	series.add(OverrunX[i][1],OverrunY[i][1]);
        	series.add(OverrunX[i][2],OverrunY[i][2]);

            // 填充数据
            //for (int k = 0; k < 100; k++) {
                // 直接填入需要显示的数据,即:Y轴的值
                //series.add(Math.abs(1 + r.nextInt() % 100));
            //	series.add(k,Math.abs(1 + r.nextInt() % 100));
            //}
            // 这里要进行转换
        	dataset_TOP.addSeries(series);
            
        }
        //public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,  List<double[]> yValues, int scale)

        // 1, 构造显示用渲染图
        XYMultipleSeriesRenderer renderer_TOP = new XYMultipleSeriesRenderer();
        renderer_TOP.setApplyBackgroundColor(true);
        renderer_TOP.setBackgroundColor(Color.argb(0, 0, 0, 0));
        // 3, 对点的绘制进行设置
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        // 3.1设置颜色
        //xyRenderer.setColor(Color.BLUE);
        xyRenderer.setColor(Color.RED);
        // 3.2设置点的样式
        // xyRenderer.setPointStyle(PointStyle.SQUARE);
        // 3.3, 将要绘制的点添加到坐标绘制中
        renderer_TOP.addSeriesRenderer(xyRenderer);
        
        
        
        // 3.4,重复 3.1~3.3的步骤绘制第二组系列点
        xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.RED);
        //xyRenderer.setPointStyle(PointStyle.CIRCLE);
        renderer_TOP.addSeriesRenderer(xyRenderer);
        
        
        
        // 注意这里x,y min 不要相同
        // 这里用一种内置的设置x,y范围的方法
        
        //顺序是:minX, maxX, minY, maxY
        double[] range = { 0, 1000, -1, 3 };
        renderer_TOP.setRange(range);
        
        // 等价于:
        // -------------------
        // renderer.setXAxisMin(0);
        // renderer.setXAxisMax(10);
        // renderer.setYAxisMin(1);
        // renderer.setYAxisMax(200);
        // -------------------
 
        // 设置合适的刻度,在轴上显示的数量是 MAX / labels
        renderer_TOP.setXLabels(21);
        renderer_TOP.setYLabels(10);
        //renderer.setXLabels(0); //设置X轴不显示数字（改用自定义的值）  
        //renderer.addTextLabel(1, "昆明"); //设置X轴坐标1显示的值
        
        // 设置x,y轴显示的排列,默认是 Align.CENTER
        renderer_TOP.setXLabelsAlign(Align.CENTER);
        renderer_TOP.setYLabelsAlign(Align.RIGHT);
        
        //renderer.setBarSpacing(0.01);//设置间距
        //renderer.setXLabels(0);//设置 X 轴不显示数字（改用我们手动添加的文字标签））;//设置X轴显示的刻度标签的个数
        //renderer.setYLabels(15);// 设置合适的刻度，在轴上显示的数量是 MAX / labels
        //renderer.setMargins(new int[] { 40, 40, 40, 40 });//图形 4 边距 设置4边留白  设置图表的外边框
        //renderer.setYLabelsAlign(Align.RIGHT);//设置y轴显示的分列，默认是 Align.CENTER
        //renderer.setPanEnabled(true, false);//设置x方向可以滑动，y方向不可以滑动
        //renderer.setZoomEnabled(false,false);//设置x，y方向都不可以放大或缩小
        
        //renderer.setDisplayValues(true);
        //renderer.setGridColor(Color.RED);
        //renderer.setXLabelsColor(Color.BLACK);
        //renderer.setYLabelsColor(0, Color.BLACK);
        // 设置坐标轴,轴的颜色
        //renderer.setAxesColor(Color.RED);
        //renderer.setBackgroundColor(Color.BLACK);
        
        // 显示网格
        renderer_TOP.setShowGrid(true);
        // 设置x,y轴上的刻度的颜色
        renderer_TOP.setLabelsColor(Color.RED);
 
        // 设置页边空白的颜色
        renderer_TOP.setMarginsColor(Color.BLACK);
        // 设置是否显示,坐标轴的轴,默认为 true
        renderer_TOP.setShowAxes(true);
        //renderer.setShowLabels(false);
        renderer_TOP.setShowLegend(false);
        
        // 设置条形图之间的距离
        //renderer.setBarSpacing(0.1);
        //int length = renderer.getSeriesRendererCount();
        
        
        //for (int i = 0; i < length; i++) {
        //    SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
        //    // 不知道作者的居中是怎么计算的,默认是Align.CENTER,但是对于两个以上的条形显示
        //    // 就画在了最右边
        //    ssr.setChartValuesTextAlign(Align.RIGHT);
        //    ssr.setChartValuesTextSize(12);
        //    //ssr.setDisplayChartValues(true);
        //}
        
        // Intent intent = new LinChart().execute(this);
        // Intent intent = ChartFactory
        // .getBarChartIntent(this, dataset, renderer, Type.DEFAULT);
        // startActivity(intent);
        
        LinearLayout barchart = (LinearLayout) findViewById(R.id.barchart);
        //GraphicalView mChartView = ChartFactory.getBarChartView(this, dataset,
        //        renderer, Type.DEFAULT);
        GraphicalView mChartView = ChartFactory.getLineChartView(this, dataset_TOP,
        		renderer_TOP);
        //GraphicalView chartView=ChartFactory.getBarChartView(this,dataset,
        //        renderer, Type.DEFAULT);
        
        barchart.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        //barchart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,
        //               LayoutParams.FILL_PARENT));
 
        LinearLayout linechart = (LinearLayout) findViewById(R.id.linechart);
        GraphicalView mLineView = ChartFactory.getLineChartView(this, dataset_TOP,
        		renderer_TOP);
        linechart.addView(mLineView, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        */
        
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	/*
        if(resultCode==RESULT_OK){
            //Bundle bundle=data.getExtras();
            //Toast.makeText(this, bundle.getString("name")+"/"+bundle.getString("pwd"), Toast.LENGTH_LONG);
            //Log.v("MSG", bundle.getString("name"));
        	Toast.makeText(MainActivity.this, "ddd", Toast.LENGTH_LONG);
        	
        	// 读取 SharedPreferences   
            SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);
            Log.v("MSG",preferences.getString("Track_val", "GT-20130302"));
            Log.v("MSG",String.valueOf(preferences.getInt("Number_val", 0)));
            Log.v("MSG",preferences.getString("Operator_val", "张三"));
            
            
        }
        super.onActivityResult(requestCode, resultCode, data);
        */
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
            if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                // MAC地址，由DeviceListActivity设置返回
            	address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            	try {
    				address=RWToSDCard("111.txt",address,true);
    				UFlag=true;
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                //Log.v("MSG", data.getExtras().getString("设备地址"));
            	
            	//-----------------------------------------------------
                // 得到蓝牙设备句柄      
                device = mBluetoothAdapter.getRemoteDevice(address);

				
                // 用服务号得到socket
                try{
                	socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                }catch(IOException e){
                	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                }
                //连接socket
                try{
                	socket.connect();
                	Toast.makeText(this, "连接"+device.getName()+"成功！", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                		socket.close();
                		socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                
                //打开接收线程
                try{
            		inStream = socket.getInputStream();   //得到蓝牙数据输入流
            		
            		}catch(IOException e){
            			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
            			ReadThread.start();
            			bThread=true;
            		}else{
            			bRun = true;
            		}
            	
            }
    		break;
    	default:break;
    	}
    }
    
//    void doEvents(int msDelay)   {
//    	if(msDelay==undefined) msDelay=3000; //默认值设为5
//        for(int i; i < msDelay && !shell.isDisposed(); i++)
//            display.readAndDispatch(); 
//    }
    
	public String RWToSDCard(String filename,String content,boolean flag) throws Exception{
		

		String status = Environment.getExternalStorageState(); 
		// 判段SD卡是否存在 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		
			java.io.File a=new java.io.File(dir);
			if (!a.exists())a.mkdir();
			File file=new File(a,filename);
			//
			if(flag)
			{
				if(content.equals(""))
				{
					//	读取数据
					FileInputStream inStream=new FileInputStream(file);
					int length = inStream.available(); 
					byte [] buffer = new byte[length];   
					inStream.read(buffer);       
					//InputStream in = new BufferedInputStream(inStream);  
					inStream.close();
					//return EncodingUtils.getString(buffer, "UTF-8");
					return EncodingUtils.getString(buffer, "ANSI");
					//FileOutputStream
				}
				else
				{
					//写入数据
					FileOutputStream outStream=new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.close();
					return content;
				}
			}
			else
			{
				if (file.exists())
				{
//					读取数据
					FileInputStream inStream=new FileInputStream(file);
					int length = inStream.available(); 
					byte [] buffer = new byte[length];   
					inStream.read(buffer);       
					//InputStream in = new BufferedInputStream(inStream);  
					inStream.close();
					//return EncodingUtils.getString(buffer, "UTF-8");
					return EncodingUtils.getString(buffer, "ANSI");
					//FileOutputStream
				}
				else
				{
					//写入数据
					FileOutputStream outStream=new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.close();
					return content;
				}
			}
		}

	return "";
}
	//val为处理double 数字，precsion为保留小数位数。

	public static Double roundDouble(double val, int precision) {  
	  Double ret = null;  
	        try {  
	           double factor = Math.pow(10, precision);  
	           ret = Math.floor(val * factor + 0.5) / factor;  
	        } catch (Exception e) {  
	           e.printStackTrace();  
	        }  
	       return ret;  
	    } 
	private void SetXYMultipleSeries(LinearLayout chart,XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer renderer,double XYValue[],boolean ValueFlag,int Type){
		XYSeries series;
		XYSeriesRenderer xyRenderer;
		int lines,num,jump=1;
		//dataset= new XYMultipleSeriesDataset();
		  
        Random r = new Random();
        double OverrunX[][]={{500,500},{500,500},{500,500},{500,500}};
    	double OverrunY[][]={{0.3,0.1},{0.1,-0.2},{0.3,0},{0.3,-0.3}};
    	
    	  while(dataset.getSeries().length > 0){  
              series= dataset.getSeries()[0];  
              dataset.removeSeries(series);  
              series.clear();  
          }  
	/*
        for (int i = 0; i < xValues.size(); i++) {  
            XYSeries series= new XYSeries(titles[i]);  
            double[] xV = xValues.get(i);  
            double[] yV = yValues.get(i);  
            int seriesLength = xV.length;  
            for (int k = 0; k < seriesLength; k++) {  
              series.add(xV[k], yV[k]);  
            }  
            dataset.addSeries(series);  
            view.repaint();  
       }    
    */   
    
    	lines=3;
    	
    	if((num=dataset.getSeriesCount())>0)
    	{
    		//for(int i=0;i<num;i++)
    			dataset.clear();
    			//Log.v("random", "TT:"+Integer.toString(dataset.getSeriesCount())); 
    		
    	}
    	//Log.v("random", Integer.toString(num)); 
    		//dataset.clear();
    		if(MaxLen<1000) jump=5;
	        for (int i = 0; i < lines; i++) {
	            // 注意,这里与昨天的XYSeries 有一点不同!!这里使用CategroySeries
	            //CategorySeries series = new CategorySeries("test" + (i + 1));
	        	//series = new XYSeries("test" +i+Type*3);
	        	series = new XYSeries("test"+i);
	        	if(i<2)
	        	{
	        		series.clear();
	        		series.add(0,0);
	        		series.add(OverrunX[Type][i],OverrunY[Type][i]);
	        		series.add(1000,0);
	        		
	        	}
	        	else
	        	{
	        		if(ValueFlag)
	        		{
	        			series.clear();
	                // 填充数据
	        		
	                //for (int k = 0,l=0; k < 1005; k+=5,l++)
	        		if(MaxLen<1000) jump=5;
	        		for (int k = 0,l=0; k < 1005; k+=jump,l++)
	        		//for (int k = 0,l=0; k < MaxLen-1; k++,l++)
	                {
	                    // 直接填入需要显示的数据,即:Y轴的值
	                	//series.add(k,Math.abs(1 + r.nextInt() % 100));
	                	//series.add(k, r.nextDouble()/8-0.05);
	                	//series.add(k, roundDouble(r.nextDouble()/8-0.05,3));
	                	
	                	series.add(k, XYValue[l]);

	                }
	        		}
	        	}
	        	
	            // 这里要进行转换
	        	dataset.addSeries(series);
	        	
	        	//series.clear();
	        }
	        //Log.v("random", "TT:"+Integer.toString(dataset.getSeriesCount()));
	        //Log.v("random", "add"); 
        // 1, 构造显示用渲染图
        //renderer = new XYMultipleSeriesRenderer();
	    if((num=renderer.getSeriesRendererCount())>0)
	    {
	    	//for(int i=0;i<num;i++)
	    		renderer.removeAllRenderers();
	    		 //Log.v("random", "TT1:"+Integer.toString(renderer.getSeriesRendererCount())); 
	    }
	    //Log.v("random", "TT2:"+Integer.toString(renderer.getSeriesRendererCount())); 
	    
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.argb(0, 0, 0, 0));
        
	        for(int i=0;i<lines;i++)
	        {
	        	// 3, 对点的绘制进行设置
	        	xyRenderer = new XYSeriesRenderer();
	        	if(i<2)
	        	{
	        	// 3.1设置颜色
	        	xyRenderer.setColor(Color.RED);
	        	// 3.2设置点的样式
	        	//xyRenderer.setPointStyle(PointStyle.SQUARE);
	        	}
	        	else
	        	{
	        		if(ValueFlag)
	        		{
	        		xyRenderer.setColor(Color.BLUE);
	        		
	        		}
	        	}
	        	// 3.3, 将要绘制的点添加到坐标绘制中
	        	renderer.addSeriesRenderer(xyRenderer);
	        	 
	        }
	        
	        double minmun=0.0;
        	double maxmun=0.0;
	        if(ValueFlag){
	        	minmun=XYValue[0];
	        	maxmun=XYValue[0];
				for(int i=0;i<MaxLen-1;i++)
				{
					if( XYValue[i]<minmun) minmun= XYValue[i];
					if( XYValue[i]>maxmun) maxmun= XYValue[i];
				}
			}
			double maxX=1000;
			//if(MaxLen<1000) maxX=200;
			double minValue=0.0;
			double maxValue=0.0;
	        switch(Type)
	        {
	        case 0:
	        	//顺序是:minX, maxX, minY, maxY
	        	minValue=0.0;
	        	maxValue=0.301; 
	        	
	        	if(ValueFlag){
	        		if(minValue>minmun) minValue=minmun+0.01;
		        	if(maxValue<maxmun) maxValue=maxmun+0.01;
	        		//minValue=minmun-0.01;
	        		//maxValue=maxmun+0.01;
	        	}
	        	double[] range_0 = { 0, maxX, minValue, maxValue };
	            //double[] range_0 = { 0, 1000, 0.0, 0.301 };
	            renderer.setRange(range_0);
	        	break;
	        case 1:
	        	//顺序是:minX, maxX, minY, maxY
	        	minValue= -0.201;
	        	maxValue=0.101; 
	        	
	        	if(ValueFlag){
	        		if(minValue>minmun) minValue=minmun;
		        	if(maxValue<maxmun) maxValue=maxmun;
	        		//minValue=minmun-0.01;
	        		//maxValue=maxmun+0.01;
	        	}
	        	double[] range_1 = { 0, maxX, minValue, maxValue };
	            //double[] range_1 = { 0, 1000, -0.201, 0.101 };
	            renderer.setRange(range_1);
	        	break;
	        case 2:
	        	//顺序是:minX, maxX, minY, maxY
	        	minValue=  0.0;
	        	maxValue=0.301; 
	        	
	        	if(ValueFlag){
	        		if(minValue>minmun) minValue=minmun;
		        	if(maxValue<maxmun) maxValue=maxmun;
	        		//minValue=minmun-0.01;
	        		//maxValue=maxmun+0.01;
	        	}
	        	double[] range_2 = { 0, maxX, minValue, maxValue };
	            //double[] range_2 = { 0, 1000, 0.0, 0.301 };
	            renderer.setRange(range_2);
	        	break;
	        case 3:
	        	//顺序是:minX, maxX, minY, maxY
	        	minValue=-0.301;
	        	maxValue=0.301; 

	        	if(ValueFlag){
		        	if(minValue>minmun) minValue=minmun;
		        	if(maxValue<maxmun) maxValue=maxmun;
	        		//minValue=minmun-0.01;
	        		//maxValue=maxmun+0.01;
	        	}
	        	double[] range_3 = { 0, maxX, minValue, maxValue };
	            //double[] range_3 = { 0, 1000, -0.301, 0.301 };
	            renderer.setRange(range_3);
	        	break;
	        }
	        
	        //Log.v("random", "TT3:"+Integer.toString(renderer.getSeriesRendererCount())); 
        
	        /*
            SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(2);
            // 不知道作者的居中是怎么计算的,默认是Align.CENTER,但是对于两个以上的条形显示
            // 就画在了最右边
            ssr.setChartValuesTextAlign(Align.RIGHT);
            ssr.setChartValuesTextSize(12);
            ssr.setDisplayChartValues(true);
            */
        
        // 注意这里x,y min 不要相同
        // 这里用一种内置的设置x,y范围的方法
        
        //顺序是:minX, maxX, minY, maxY
        //double[] range = { 0, 1000, -0.5, 0.5 };
        //renderer.setRange(range);
        
        // 等价于:
        // -------------------
        // renderer.setXAxisMin(0);
        // renderer.setXAxisMax(10);
        // renderer.setYAxisMin(1);
        // renderer.setYAxisMax(200);
        // -------------------
 
        // 设置合适的刻度,在轴上显示的数量是 MAX / labels
        renderer.setXLabels(21);
        renderer.setYLabels(10);
        //renderer.setXLabels(0); //设置X轴不显示数字（改用自定义的值）  
        //renderer.addTextLabel(1, "昆明"); //设置X轴坐标1显示的值
        
        // 设置x,y轴显示的排列,默认是 Align.CENTER
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        
        //renderer.setBarSpacing(1);//设置间距
        //renderer.setXLabels(0);//设置 X 轴不显示数字（改用我们手动添加的文字标签））;//设置X轴显示的刻度标签的个数
        //renderer.setYLabels(15);// 设置合适的刻度，在轴上显示的数量是 MAX / labels
        renderer.setMargins(new int[] { 40, 40, 40, 40 });//图形 4 边距 设置4边留白  设置图表的外边框
        //renderer.setYLabelsAlign(Align.RIGHT);//设置y轴显示的分列，默认是 Align.CENTER
        //renderer.setPanEnabled(false,true);//设置x方向可以滑动，y方向不可以滑动
        //renderer.setZoomEnabled(false,false);//设置x，y方向都不可以放大或缩小
        
        //renderer.setDisplayValues(true);
        //renderer.setDisplayChartValuesDistance(30);
        //renderer.setBarSpacing(1);//设置间距
        renderer.setLabelsTextSize(18.0f);
        renderer.setBarSpacing(20);//设置间距
        //renderer.setGridColor(Color.RED);
        //renderer.setXLabelsColor(Color.BLACK);
        //renderer.setYLabelsColor(0, Color.BLACK);
        // 设置坐标轴,轴的颜色
        //renderer.setAxesColor(Color.RED);
        //renderer.setBackgroundColor(Color.BLACK);
        
        // 显示网格
        renderer.setShowGrid(true);
        // 设置x,y轴上的刻度的颜色
        renderer.setLabelsColor(Color.RED);
 
        // 设置页边空白的颜色
        renderer.setMarginsColor(Color.BLACK);
        // 设置是否显示,坐标轴的轴,默认为 true
        renderer.setShowAxes(true);
        //renderer.setShowLabels(false);
        renderer.setShowLegend(false);
      
		// 设置渲染器显示缩放按钮
		//renderer.setZoomButtonsVisible(true);
		// 设置渲染器允许放大缩小
		//renderer.setZoomEnabled(true);
		//renderer.setClickEnabled(true);//设置图表是否允许点击
		
        //GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);

        //chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        //chartView.invalidate();

        GraphicalView chartView= ChartFactory.getLineChartView(this, dataset,renderer);
        chart.removeAllViewsInLayout();  
        chart.addView(chartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        chartView.repaint();
        //chartView.invalidate();
        //chart.invalidate();
        
	}
    //关闭程序掉用处理部分
    @Override
	public void onDestroy(){
    	super.onDestroy();
    	if(socket!=null)  //关闭连接socket
    	try{
    		socket.close();
    	}catch(IOException e){}
    	mBluetoothAdapter.disable();
    	//SaveSingleExcelData(3);//single
    	mBluetoothAdapter.disable();  //关闭蓝牙服务
    }

	/*
	  //接收数据线程
    Thread ReadThread=new Thread(){
    	
    	@Override
		public void run(){
    		int num = 0;
    		byte[] buffer = new byte[1024];
    		byte[] buffer_new = new byte[1024];
    		int i = 0;
    		int n = 0;
    		bRun = true;
    		//接收线程
    		while(true){
    			try{
    				while(inStream.available()==0){
    					while(bRun == false){}
    				}
    				n=0;
    				while(true){
    					num = inStream.read(buffer);         //读入数据
    					
    					String s0 = new String(buffer,0,num);
    					
    					fmsg+=s0;    //保存收到数据
    					byte b[]=fmsg.getBytes();
    					for(i=0;i<b.length;i++){
    						if((b[i] == 0x0d)&&(b[i+1]==0x0a)){
    							buffer_new[n] = 0x0;
    							//smsg = new String(buffer_new,0,n);
    							//i++;
    							smsg = new String(buffer_new,0,n);
    							n=0;
    						}else{
    							buffer_new[n] = b[i];
    						}
    						n++;
    					}
    					
    					
    					//String s = new String(buffer_new,0,n);
    					//smsg+=s;   //写入接收缓存
    					
    					//fmsg+=s0;
    					//for(i=0;i<fmsg.length();i++){
    						//if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
    						//	fmsg=fmsg.substring(i+2);
    						//	smsg.substring(0,i);
    						//}
    					//}
    					if(inStream.available()==0)break;  //短时间没有数据才跳出进行显示
    				}
    				//if(!smsg.equalsIgnoreCase("\nUart Commected!"))
    					//发送显示消息，进行显示刷新
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    };
  */  
  //接收数据线程
  Thread ReadThread=new Thread(){
  	
  	@Override
		public void run(){
  		int num = 0;
  		byte[] buffer = new byte[1024];
  		byte[] buffer_new = new byte[1024];
  		int i = 0;
  		int n = 0;
  		bRun = true;
  		//接收线程
  		while(true){
  			try{
  				while(inStream.available()==0){
  					while(bRun == false){}
  				}
  				while(true){
  					num = inStream.read(buffer);         //读入数据
  					n=0;
  					
  					String s0 = new String(buffer,0,num);
  					fmsg+=s0;    //保存收到数据
  					if(fmsg.length()>0){
	  					int pos=fmsg.indexOf("\r\n");
	  					if(pos>-1){
	  						smsg=fmsg.substring(0, pos+2);
	  						if(smsg.equalsIgnoreCase("\nUart Connected!\r\n")==false)
									handler.sendMessage(handler.obtainMessage());
								else
									smsg="";
							fmsg=fmsg.substring(pos+2);
	  					}
  					}
  					/*
  					for(i=0;i<num;i++){
  						if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
  							smsg=fmsg.substring(0, i-1);
  							//smsg=fmsg;
  							if(smsg.equalsIgnoreCase("\nUart Connected!\r\n")==false)
  								handler.sendMessage(handler.obtainMessage());
  							else
  								smsg="";
  							fmsg=fmsg.substring(i+2);
  							//fmsg="";
  						}
  						
  					}*/
  					/*
  					for(i=0;i<num;i++){
  						if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
  							buffer_new[n] = 0x0a;
  							//String s = new String(buffer_new,0,n);
  							//smsg=s;
  							//handler.sendMessage(handler.obtainMessage());
  							i++;
  						}else{
  							buffer_new[n] = buffer[i];
  						}
  						n++;
  					}
  					String s = new String(buffer_new,0,n);
  					smsg+=s;   //写入接收缓存
  					*/
  					//if(inStream.available()==0)break;  //短时间没有数据才跳出进行显示
  				}
  				//发送显示消息，进行显示刷新
  					//handler.sendMessage(handler.obtainMessage());       	    		
  	    		}catch(IOException e){
  	    		}
  		}
  	}
  };
  
    //消息处理队列
    Handler handler= new Handler(){
    	@Override
		public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		
//    		textView1.setText(smsg+"\n\r结束："+smsg.length());   //显示数据
    		String subMsg=""; 
    		double max0=0.0,max1=0.0;
    		double min0=0.0,min1=0.0;
    		double sign=-1.0;
    		double m0[];
    		double n0[];
    		//double m0[]=new double[MaxLen-1];
    		//double n0[]=new double[MaxLen-1];
    		
    		byte b[]=smsg.getBytes();
    		int c[]=hexStringToInts(smsg);
    		
    		//textView1.setText(smsg+"\n\r结束："+c.length);   //显示数据
    		//textView4.setText(smsg);
   		
    		try {
				if(smsg.equalsIgnoreCase("\nUart Connected!\r\n"+senddata+"\r\n")
						||smsg.equalsIgnoreCase(senddata+"\r\n")){
					myBtn2.setEnabled(false);
					Flag=1;
					//给主Activity发送一个广播
					//Intent intent = new Intent("android.intent.action.Setting");
					//intent.putExtra("msg", "接收注册广播成功！"+smsg);
					//sendBroadcast(intent);
				}
				else if(Flag==1)
				{
					//textView1.setText(smsg+"\n\r结束："+smsg.length());   //显示数据
					//Toast.makeText(getApplicationContext(), subMsg, Toast.LENGTH_LONG).show();
					setTitle("激光电子平尺[数据量:"+c.length +"]");
					//textView1.setText(subMsg);   //显示数据
					MaxLen=c.length/2;
					 if(m == null || m.length == 0||n == null || n.length == 0||p == null || p.length == 0||q == null || q.length == 0||r == null || r.length == 0||s == null || s.length == 0)
					 {
						m=new double[MaxLen];
						n=new double[MaxLen];
						p=new double[MaxLen];
						q=new double[MaxLen];
						r=new double[MaxLen];
						s=new double[MaxLen];
					 }
					m0=new double[MaxLen-1];
		    		n0=new double[MaxLen-1];
		    		
					ZeroFlag=FiletoDoubles(zeroname,p,q,r,s);
					if(!ZeroFlag) 
						sign=1.0;
					else
						sign=-1.0;
					
					//subMsg.format("P:%d A:%d",m.length*2,c.length);
					//Toast.makeText(getApplicationContext(), "ON RESUME: ", Toast.LENGTH_LONG).show();
					//Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
					//将接收的数据分成顶面与侧面
					for(int i=0,j=0;i<MaxLen*2;i++){
						if(i<MaxLen-1)
							m[i]=c[i]/1000.0;
						else
							if(i>=MaxLen){
								n[j]=c[i]/1000.0;
								j++;	
							}
					}
					
					if(!DebugFlag){
						for(int i=0;i<MaxLen-1;i++){
							m[i]=Svalue(Quotiety,true,c[MaxLen-1],m[i]);
							n[i]=Svalue(Quotiety,false,c[MaxLen-1],n[i]);
							m0[i]=m[i];
							n0[i]=n[i];
						}

						//if(mode.equalsIgnoreCase("1"))
						//{
						
							for(int i=0;i<MaxLen-1;i++)
							{
				    			m[i]=(m0[i]-((m0[MaxLen-2]-m0[0])*i/(MaxLen-2)+m0[0]))*Math.cos(Math.atan((m0[MaxLen-2]-m0[0])/(MaxLen-2)));
				    			n[i]=(n0[i]-((n0[MaxLen-2]-n0[0])*i/(MaxLen-2)+n0[0]))*Math.cos(Math.atan((n0[MaxLen-2]-n0[0])/(MaxLen-2)));
				    			//m[i]=(m0[i]-((m0[MaxLen-2]-m0[0])*i+m0[0]))*Math.cos(Math.atan((m0[MaxLen-2]-m0[0])/1000.0));
				    			//n[i]=(n0[i]-((n0[MaxLen-2]-n0[0])*i+n0[0]))*Math.cos(Math.atan((n0[MaxLen-2]-n0[0])/1000.0));
							}
							
							if(c[MaxLen*2-1]==0.0)
							{
				        		for(int i=0;i<MaxLen-1;i++){
				        				m[i]=m[i]+sign*p[i];
				        				n[i]=n[i]+sign*q[i];
				        		}
							}
							else
							{
								for(int i=0;i<MaxLen-1;i++){
				    				m[i]=m[i]+sign*r[i];
				    				n[i]=n[i]+sign*s[i];
								}
							}
						//}
					}
					max0 = getMax(m);
					min0 = getMin(m);
					DecimalFormat decimalFormat = new DecimalFormat("0.000");
					textView2.setText("最大值：" + decimalFormat.format(max0) +"\n" +"最小值：" + decimalFormat.format(min0));
					max1 = getMax(n);
					min1 = getMin(n);
					textView3.setText("最大值：" + decimalFormat.format(max1) +"\n" +"最小值：" + decimalFormat.format(min1));

					SetXYMultipleSeries(barchart,dataset, renderer,m,true,2);
					SetXYMultipleSeries(linechart,dataset1, renderer1,n,true,3);
					
					myBtn2.setEnabled(true);
					Flag=0;
					bTestFlag=true;
				}
				smsg="";

				
				//Yvalue(b);
				/*
				//byte b[]=smsg.getBytes();
				//if(smsg.equalsIgnoreCase("\nUart Commected!\r\n"))
				String str=new String("\nUart Connected!\r\n");
				//byte c[]=str.getBytes();
				//String s2 = "\nUart Co" +"nnected!\r\n"; 
				if(smsg.equalsIgnoreCase("\nUart Connected!\r\n"))
				//if(smsg.(object)("\nUart Commected!\r\n"))
					Toast.makeText(getApplicationContext(), "ON RESUME: Output stream creation failed.", Toast.LENGTH_LONG).show();
				else
					*/
				//Toast.makeText(getApplicationContext(), "ON RESUME: ", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
    	}
    };
    
	public static int[] bytes2ints(byte[] b) { 
		int[] ret=new int[b.length/2]; 
		for (int i = 0,j=0; i < b.length; i+=2,j++) { 
			ret[j]=b[i]<<8|b[i+1];
		} 
		return ret; 
	}
	
	 public static double getMax(double[] arr)
	 {
	  double max = arr[0];

	  for(int x=0; x<arr.length-1; x++)
	  {
	   if(arr[x]>max)
	    max = arr[x];
	  }
	  return max;
	 }
	 
	 public static double getMin(double[] arr)
	 {
	  double min = arr[0];
	  for(int x=0; x<arr.length-1; x++)
	  {
	   if(arr[x]<min)
	    min = arr[x];
	  }
	  return min;
	 }
	 
		/*
	    * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。  
	    * @param src byte[] data  
	    * @return hex string  
	    */     
	   public static String bytesToHexString(byte[] src){  
	       StringBuilder stringBuilder = new StringBuilder("");  
	       if (src == null || src.length <= 0) {  
	           return null;  
	       }  
	       for (int i = 0; i < src.length; i++) {  
	           int v = src[i] & 0xFF;  
	           String hv = Integer.toHexString(v);  
	           if (hv.length() < 2) {  
	               stringBuilder.append(0);  
	           }  
	           stringBuilder.append(hv);  
	       }  
	       return stringBuilder.toString();  
	   }  
	   /** 
	    * Convert hex string to byte[] 
	    * @param hexString the hex string 
	    * @return byte[] 
	    */  
	   public static byte[] hexStringToBytes(String hexString) {  
	       if (hexString == null || hexString.equals("")) {  
	           return null;  
	       }  
	       hexString = hexString.toUpperCase();  
	       int length = hexString.length() / 2;  
	       char[] hexChars = hexString.toCharArray();  
	       byte[] d = new byte[length];  
	       for (int i = 0; i < length; i++) {  
	           int pos = i * 2;  
	           d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	       }  
	       return d;  
	   }  
	   /** 
	    * Convert char to byte 
	    * @param c char 
	    * @return byte 
	    */  
	    private static byte charToByte(char c) {  
	       return (byte) "0123456789ABCDEF".indexOf(c);  
	   }  
	   public static int[] hexStringToInts(String hexString) {
		   if (hexString == null || hexString.equals("")) {  
	           return null;  
	       }  
	       hexString = hexString.toUpperCase();  
	       int length = hexString.length() / 4;  
	       char[] hexChars = hexString.toCharArray();  
	       int[] d = new int[length];
	       int []c=new int[2];
	       for (int i = 0; i < length; i++) {  
	           int pos = i * 4;  
	           c[0] =charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]);
	           c[1]=charToByte(hexChars[pos+2]) << 4 | charToByte(hexChars[pos + 3]);
	           d[i]=c[0]<<8|c[1];
	       }  
	       return d;  
	    }
	/*
	   public void On_Click(View v) {

			String send; 
	    	int i=0;
	    	int n=0;
	    	try{
	    		OutputStream os = socket.getOutputStream();   //蓝牙连接输出流
	    		//outStream= socket.getOutputStream();   //蓝牙连接输出流
	    		send="BEGIN"+"\n";
	    		
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
	    		
	    		//os.write(bos);	
	    		
	    	}catch(IOException e){  
	    		Toast.makeText(getApplicationContext(), "蓝牙连接已断开或无连接! ", Toast.LENGTH_LONG).show();
	    	}  
	    	
		}
		public void BT_Click(View v) {
	// TODO Auto-generated method stub
	//imageView1.setEnabled(false);
	//Toast.makeText(getApplicationContext(), "ON RESUME: Exception during write.", Toast.LENGTH_LONG).show();
	//barchart.removeAllViewsInLayout();
	//linechart.removeAllViewsInLayout();
	//SetXYMultipleSeries(barchart,dataset, renderer,list,true,2);
	//SetXYMultipleSeries(linechart,dataset1, renderer1,list,true,3);
	
	if(mBluetoothAdapter.isEnabled()==false){
		mBluetoothAdapter.enable();
	}
	//if(device==null)
	//{
		device = mBluetoothAdapter.getRemoteDevice(address);
			if(socket==null)
			{

				try{
				socket = device.createRfcommSocketToServiceRecord(MY_UUID);
				}catch(IOException e){
					Toast.makeText(getApplicationContext(), "连接端口创建失败！", Toast.LENGTH_SHORT).show();
					
				}
				try{
					if(!socket.isConnected())
						socket.connect();

		        	Toast.makeText(getApplicationContext(), "连接"+device.getName()+"成功！", Toast.LENGTH_SHORT).show();
		        }catch(IOException e){
		        	try{
		        		Toast.makeText(getApplicationContext(), "连接失败,请检查电源或重新配对！", Toast.LENGTH_SHORT).show();
		        		socket.close();
		        		socket = null;

		        	}catch(IOException ee){
		        		Toast.makeText(getApplicationContext(), "连接失败！", Toast.LENGTH_SHORT).show();
		        	}
		        	return;
		        }
			}
			else
				Toast.makeText(getApplicationContext(), "已经连接！", Toast.LENGTH_SHORT).show();
			//打开接收线程
           try{
           	inStream = socket.getInputStream();   //得到蓝牙数据输入流
       		}catch(IOException e){
       			Toast.makeText(getApplicationContext(), "接收数据失败！", Toast.LENGTH_SHORT).show();
       			return;
       		}
       		if(bThread==false){
       			ReadThread.start();
       			bThread=true;
       		}else{
       			bRun = true;
       		}
			
			
	//}
	//else
	//	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();

		}
	*/	
   public boolean FiletoQuotiety(String address,double Quotiety[][])
	{
	    int i,j; 
	   	String filename="Quotiety.txt";
		String status = Environment.getExternalStorageState(); 
		// 判段SD卡是否存在 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		java.io.File a=new java.io.File(dir);
		if (!a.exists())a.mkdir();
		filename=filename.replaceFirst("Quotiety", address);
		filename=filename.replaceAll(":", "");
		
		File file=new File(a,filename);
		String content="";
		if(!file.exists()) 
		{
			for(i=0;i<9;i++)
			{
				for(j=0;j<4;j++)
				{
					Quotiety[0][1]=1.0;
					Quotiety[1][0]=0.0;//12.602;
					Quotiety[2][0]=1.0;//-3.277;
					Quotiety[3][0]=0.0;//0.1025;
					Quotiety[4][0]=0.0;//-0.0116;
					Quotiety[5][0]=0.0;//12.774;
					Quotiety[6][0]=1.0;//-3.0685;
					Quotiety[7][0]=0.0;//0.0343;
					Quotiety[8][0]=0.0;//-0.0037;
					
					if(j<3)
						content+=String.valueOf(Quotiety[i][j]) +";";
					else
						content+=String.valueOf(Quotiety[i][j]) +"\r\n";
						//content+=String.valueOf(Quotiety[i][j]) +";a+bx+cx^2+dx^3\r\n";//"\r\n";	
				}
				//content+="\n";
			}
			//写入数据
			try {
				FileOutputStream outStream=new FileOutputStream(file);
				outStream.write(content.getBytes());
				outStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		try {
			 InputStream instream = new FileInputStream(file); 
			 if (instream != null)
             {
                 InputStreamReader inputreader = new InputStreamReader(instream);
                 BufferedReader buffreader = new BufferedReader(inputreader);
                 String line;
                 //分行读取
                 i=0;
                 while (( line = buffreader.readLine()) != null) {
                     //content += line + "\n";
                	 String dest = "";
                     if (line!=null) {
                         Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                         Matcher m = p.matcher(line);
                         dest = m.replaceAll("");
                     }
                     
                	 //String dd[] =line.split(";");
                     String dd[] =dest.split(";");
                	 for (j = 0; j < dd.length; j++) {
 						Quotiety[i][j] = Double.parseDouble(dd[j]);
 					}
                	 i++;
                 }               
                 instream.close();
             }
//			 FileInputStream fis = new FileInputStream(file);
//			byte[] b = new byte[(int)file.length()];
//			  fis.read(b);
//			  fis.close();
//			  String bb = new String(b);
//			  String cc[]=bb.split("\r\n");
			  
//				for (i = 0; i < cc.length; i++) {
//					// System.out.print(b[i]);
//					String dd[] = cc[i].split(";");
//					for (j = 0; j < dd.length; j++) {
//						Quotiety[i][j] = Double.parseDouble(dd[j]);
//					}
//				}
			  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		}  
		return true;
		  
		 
	}

	   
	private double Svalue(double Quotiety[][],boolean dflag,double t,double x)
	{	double st,sa,sb,sc,sd,y;
		st=Quotiety[0][0]+Quotiety[0][1]*Math.pow(t,1.0)+Quotiety[0][2]*Math.pow(t,2.0)+Quotiety[0][3]*Math.pow(t,3.0);
		if(dflag)
		{
			sa=Quotiety[1][0]+Quotiety[1][1]*Math.pow(st,1.0)+Quotiety[1][2]*Math.pow(st,2.0)+Quotiety[1][3]*Math.pow(st,3.0);
			sb=Quotiety[2][0]+Quotiety[2][1]*Math.pow(st,1.0)+Quotiety[2][2]*Math.pow(st,2.0)+Quotiety[2][3]*Math.pow(st,3.0);
			sc=Quotiety[3][0]+Quotiety[3][1]*Math.pow(st,1.0)+Quotiety[3][2]*Math.pow(st,2.0)+Quotiety[3][3]*Math.pow(st,3.0);
			sd=Quotiety[4][0]+Quotiety[4][1]*Math.pow(st,1.0)+Quotiety[4][2]*Math.pow(st,2.0)+Quotiety[4][3]*Math.pow(st,3.0);
			
		}
		else
		{
			sa=Quotiety[5][0]+Quotiety[5][1]*Math.pow(st,1.0)+Quotiety[5][2]*Math.pow(st,2.0)+Quotiety[5][3]*Math.pow(st,3.0);
			sb=Quotiety[6][0]+Quotiety[6][1]*Math.pow(st,1.0)+Quotiety[6][2]*Math.pow(st,2.0)+Quotiety[6][3]*Math.pow(st,3.0);
			sc=Quotiety[7][0]+Quotiety[7][1]*Math.pow(st,1.0)+Quotiety[7][2]*Math.pow(st,2.0)+Quotiety[7][3]*Math.pow(st,3.0);
			sd=Quotiety[8][0]+Quotiety[8][1]*Math.pow(st,1.0)+Quotiety[8][2]*Math.pow(st,2.0)+Quotiety[8][3]*Math.pow(st,3.0);
			
		}
		y=sa+sb*Math.pow(x,1.0)+sc*Math.pow(x,2.0)+sd*Math.pow(x,3.0);
		return y;
	}
	
	private double [] Yvalue(double a[],double b[],double max,double min)
	{
		int i;
		double minmun=0.0;
		double maxmun=0.0;
		double d[]=new double[b.length];
		for(i=0;i<MaxLen-1;i++)
		{
			//(y-[(y200-y0)*x*5/1000+y0])cos(tg-1((y200-y0)/1000))
			d[i]=(a[i]-((b[MaxLen-2]-b[0])*i*5/1000+b[0]))*Math.cos(Math.atan((b[MaxLen-2]-b[0])/1000));
			if(d[i]<minmun) minmun=d[i];
			if(d[i]>maxmun) maxmun=d[i];
		}
		min=minmun;
		max=maxmun;
		return d;
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event){  
    if(keyCode == KeyEvent.KEYCODE_BACK){
        this.exitDialog();
    	//finish();
    	//return false;
    }  
    return false;  
   }  

    private int typeDialog(){
    //Dialog dialog = new AlertDialog.Builder(this,AlertDialog.THEME_TRADITIONAL)  
    	Dialog dialog = new AlertDialog.Builder(this)
            .setTitle("选择模型")  
            .setInverseBackgroundForced(true)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setSingleChoiceItems(new String[] { "高速轨", "普速轨" }, typeDialog,
        			new DialogInterface.OnClickListener() {  
            	
    			public void onClick(DialogInterface dialog, final int which) {
    			typeDialog= which;
    			//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
    			setResult(RESULT_OK);
    			dialog.dismiss();
    			 }
    			}).setPositiveButton("取消", null).show();
    return typeDialog;
    	/*		
    			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
                    }}).show();
    			//.setPositiveButton("确定", null).show();
    			//setNegativeButton("取消", null).show();
    */
    
    /*
    	Dialog dialog = new AlertDialog.Builder(this)  
        .setTitle("选择模型")  
        .setIcon(android.R.drawable.ic_dialog_info)
        .setSingleChoiceItems(new String[] { "Item1", "Item2" }, 0,null)
    	.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
                }}).show();
			//.setPositiveButton("确定", null).show();
			//setNegativeButton("取消", null).show();
    	*/
   }  
    
    private void exitDialog(){  
    //Dialog dialog = new AlertDialog.Builder(this,AlertDialog.THEME_TRADITIONAL)//.THEME_DEVICE_DEFAULT_DARK)  
    	Dialog dialog = new AlertDialog.Builder(this)//.THEME_DEVICE_DEFAULT_DARK)
            .setTitle("退出程序")  
            .setMessage("您确定要退出本程序吗？")  
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        //finish();
                       	if(socket!=null)  //关闭连接socket
                    	try{
                    		socket.close();
                    	}catch(IOException e){}
                    	mBluetoothAdapter.disable();  //关闭蓝牙服务
                    	if(DebugFlag)
                    		SaveSingleExcelData(3);//single	
                    	System.exit(0);
                    	return;
                    }  
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {                    
                    public void onClick(DialogInterface dialog, int whichButton) { }  
                }).create();  
    dialog.show();  
   }  
    
    private void SaveSingleExcelData(final int flag) {

        try {  
        	switch(flag)
        	{
        	case 1:
            	final EditText editText = new EditText(MainActivity.this);
            	editText.setBackgroundColor(Color.WHITE);
            	editText.setText("Test_"+Integer.toString(filenum));
            	//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
            	new AlertDialog.Builder(MainActivity.this)
            	.setTitle("提示")
            	
            	.setMessage("请输入文件名称：")  
            	.setIcon(android.R.drawable.ic_dialog_info)
            	.setCancelable(false)
            	.setView(editText)
            	.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	
                    	filename=editText.getText().toString();
                    	Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
                    	filenum++;
                        try {
                        	File file = new File("mnt/sdcard/data/");
                        	if (!file.exists()) file.mkdirs();
							book = Workbook.createWorkbook(new File(  
							        "mnt/sdcard/data/"+filename+".xls"));  
							
							// 生成名为“第一页”的工作表,参数0表示这是第一页  
                  sheet1 = book.createSheet("Sheet1", 0);  
                  sheet2 = book.createSheet("Shee2", 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	//return;
                    }  
                })
            	.setNegativeButton("取消", null).show();
            	//Toast.makeText(MainActivity.this,filename+"YYYYYYY", Toast.LENGTH_SHORT).show();
            // 创建或打开Excel文件  
           // WritableWorkbook book = Workbook.createWorkbook(new File(  
           //         "mnt/sdcard/data/"+filename+".xls"));  
  
            // 生成名为“第一页”的工作表,参数0表示这是第一页  
            //WritableSheet sheet1 = book.createSheet("Sheet1", 0);  
            //WritableSheet sheet2 = book.createSheet("Shee2", 1);
            break;
        	case 2:
            // 在Label对象的构造函数中,元格位置是第一列第一行(0,0)以及单元格内容为test  
            //Label label = new Label(0, 0, "test");  
            
            // 将定义好的单元格添加到工作表中  
            //sheet1.addCell(label);
            for(int i=0;i<MaxLen-1;i++){
    			jxl.write.Number number = new jxl.write.Number(SheetColumn*2+0, i, m[i]);
    			sheet1.addCell(number);
    			jxl.write.Number number1 = new jxl.write.Number(SheetColumn*2+1, i, n[i]);
    			sheet1.addCell(number1);  
    		}
            Toast.makeText(MainActivity.this,"保存数据序列："+Integer.toString(SheetColumn), Toast.LENGTH_SHORT).show();
            SheetColumn++;
            //String s;
            //s.split("BEGIN\r\n")
            //生成一个保存数字的单元格.必须使用Number的完整包路径,否则有语法歧义  
            //jxl.write.Number number = new jxl.write.Number(0, 0, 555.12541);
            //sheet1.addCell(number);  
            break;
        	case 3:
            // 写入数据并关闭文件  
            book.write();  
            book.close();
            Toast.makeText(MainActivity.this,"数据保存到:"+filename, Toast.LENGTH_SHORT).show();
        	}
        } catch (Exception e) {  
            System.out.println(e);  
        }  
        
    }
    boolean isFolderExists(String strFolder) {
    	File file = new File(strFolder);
    	if (!file.exists()) 
    	{            
    		if (file.mkdirs()) {
    			return true;
    			} 
    		else {
    				return false;
    			}        
    		}
    	return true;
    	}
    
    private void SaveExcelData() {
    	final EditText editText = new EditText(MainActivity.this);
    	editText.setBackgroundColor(Color.WHITE);
    	editText.setText("Test_"+Integer.toString(filenum));
    	//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
    	new AlertDialog.Builder(MainActivity.this)
    	.setTitle("提示")
    	.setMessage("请输入文件名称：")  
    	.setIcon(android.R.drawable.ic_dialog_info)
    	.setView(editText)
    	.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
            	
            	filename=editText.getText().toString();
            	Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
            	filenum++;
            	
                try {  
                	File file = new File("mnt/sdcard/data/");
                	if (!file.exists()) file.mkdirs();
                    // 创建或打开Excel文件  
                    WritableWorkbook book = Workbook.createWorkbook(new File(  
                            "mnt/sdcard/data/"+filename+".xls"));  
          
                    // 生成名为“第一页”的工作表,参数0表示这是第一页  
                    WritableSheet sheet1 = book.createSheet("Sheet1", 0);  
                    WritableSheet sheet2 = book.createSheet("Shee2", 1);  
          
                    // 在Label对象的构造函数中,元格位置是第一列第一行(0,0)以及单元格内容为test  
                    //Label label = new Label(0, 0, "test");  
                    
                    // 将定义好的单元格添加到工作表中  
                    //sheet1.addCell(label);
                    for(int i=0;i<MaxLen-1;i++){
	        			jxl.write.Number number = new jxl.write.Number(0, i, m[i]);
	        			sheet1.addCell(number);
	        			jxl.write.Number number1 = new jxl.write.Number(1, i, n[i]);
	        			sheet1.addCell(number1);  
	        		}
                    
                    //String s;
                    //s.split("BEGIN\r\n")
                    //生成一个保存数字的单元格.必须使用Number的完整包路径,否则有语法歧义  
                    //jxl.write.Number number = new jxl.write.Number(1, 0, 555.12541);
                    //sheet2.addCell(number);  
          
                    // 写入数据并关闭文件  
                    book.write();  
                    book.close();  
                } catch (Exception e) {  
                    System.out.println(e);  
                }  
                
            	return;
            }  
        })
    	.setNegativeButton("取消", null).show();
    }
    private void SaveData() {
		//显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		etFileName = (EditText)DialogView.findViewById(R.id.etFileName);
		etTrack = (EditText)DialogView.findViewById(R.id.etTrack);
		etNumber = (EditText)DialogView.findViewById(R.id.etNumber);
		etOperator= (EditText)DialogView.findViewById(R.id.etOperator);
	     // 读取 SharedPreferences   
		SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);
		etFileName.setText(preferences.getString("File_val", "Test_2013"));
		etTrack.setText(preferences.getString("Track_val", "GT-20130302"));
		etNumber.setText(String.valueOf(preferences.getInt("Number_val", 0)));
		etOperator.setText(preferences.getString("Operator_val", "张三"));
		
		//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("文件名")
		.setCancelable(false)
		.setView(DialogView)   //设置视图模板
		.setPositiveButton("确定",
		new DialogInterface.OnClickListener() //确定按键响应函数
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
				int pos;
				String wmsg="";
				//EditText text1 = (EditText)DialogView.findViewById(R.id.etNumber);  //得到文件名输入框句柄
				//filename = text1.getText().toString();  //得到文件名
				filename=etFileName.getText().toString();
				
				SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);   
				Editor editor = preferences.edit();
				editor.putString("File_val", etFileName.getText().toString()); 
				editor.putString("Track_val", etTrack.getText().toString());   
				editor.putInt("Number_val", Integer.valueOf(etNumber.getText().toString()));
				editor.putString("Operator_val", etOperator.getText().toString());
				editor.commit();
				
				try{
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
						
						filename =filename+".txt";   //在文件名末尾加上.txt		
						Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
						
						File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
						File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
						if(BuildDir.exists()==false)BuildDir.mkdirs();
						File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
						FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
						wmsg=etOperator.getText().toString()+"\r\n";
						//stream.write(wmsg.getBytes());
						wmsg+=etTrack.getText().toString() +etNumber.getText().toString()+"\r\n";
						//stream.write(wmsg.getBytes());
						//wmsg="";
						DecimalFormat df = new DecimalFormat("0.000");
						DecimalFormat df1 = new DecimalFormat("0.000");
						for(int i=0;i<MaxLen;i++){
		        			wmsg+=df.format(m[i]) +","+df1.format(n[i])+"\r\n";
							//wmsg+=m[i];
		        		}
						
						/*
						for(int i=0;i<MaxLen;i++){
		        			wmsg+=df.format(m[i]);
							//wmsg+=m[i];
		        			if(i==MaxLen-1)
		        			{
		        				wmsg+="\r\n";
		        			}
		        			else
		        			{
		        				wmsg+=";";
		        			}
		        		}

		        		for(int i=MaxLen,j=0;i<MaxLen*2;i++,j++){
		        			wmsg+=df.format(n[j]);
		        			//wmsg+=n[j];
		        			if(i==MaxLen*2-1)
		        			{
		        				wmsg+="\r\n";
		        			}
		        			else
		        			{
		        				wmsg+=";";
		        			}
		        		}
*/
						//pos=fmsg.indexOf("BEGIN\r\n");
						//wmsg=fmsg.substring(pos+7);
						stream.write(wmsg.getBytes());
						stream.close();
						Toast.makeText(MainActivity.this, "存储成功！", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "没有存储卡！", Toast.LENGTH_LONG).show();
					}
				
				}catch(IOException e){
					return;
				}
				
			}
		})
		.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
			}})
    .show();  //显示对话框
    }
    
    private void dialogShowCheck() {  
    	
        String[] s = new String[] {"选项1", "选项2", "选项3", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4", "选项4"};  
        AlertDialog dialog=new AlertDialog.Builder(this)  
        .setTitle("多选框") 
        .setSingleChoiceItems(s, 0, null)  
        //.setMultiChoiceItems(s, null, null)  
        .setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	/*
               String s = "您选择了：";
               // 扫描所有的列表项，如果当前列表项被选中，将列表项的文本追加到s变量中。
               for (int i = 0; i < s.length(); i++)
               {
                   if (lv.getCheckedItemPositions().get(i))
                   {
                      s += i + ":" + lv.getAdapter().getItem(i) + " ";
                   }
               }
               // 用户至少选择了一个列表项
               if (lv.getCheckedItemPositions().size() > 0)
               {
                   new AlertDialog.Builder(MainActivity.this)
                           .setMessage(s).show();
                   System.out.println(lv.getCheckedItemPositions().size());
               }

               // 用户未选择任何列表项
               else if(lv.getCheckedItemPositions().size() <= 0 )
               {
                   new AlertDialog.Builder(MainActivity.this)
                           .setMessage("您未选择任何省份").show();
               }
               */
            }

        }).setNegativeButton("取消", null).create();
        lv = dialog.getListView();
        dialog.show(); 
        
        dialog.getWindow().setLayout(700, 600);   
    }  
    /*
    private void SaveData_1() {
    	final DecimalFormat df = new DecimalFormat("0.000");
		//显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("文件名")
		.setCancelable(false)
		.setView(DialogView)   //设置视图模板
		.setPositiveButton("确定",
		new DialogInterface.OnClickListener() //确定按键响应函数
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
//				String[] names = new String[] { "Linux" , "Windows7" , "Eclipse" , "Suse" , "Ubuntu" , "Solaris" , "Android" , "iPhone" , "Linux" , "Windows7" , "Eclipse" , "Suse" , "Ubuntu" , "Solaris" , "Android" , "iPhone" }; 
//				//setListAdapter( new ArrayAdapter<String>( this , android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, names)); 
//				setListAdapter( new ArrayAdapter<String>( this , android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, names));
//				ListView listView = getListView(); 
//				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); 
//				
				int pos;
				String wmsg="";
				EditText text1 = (EditText)DialogView.findViewById(R.id.etNumber);  //得到文件名输入框句柄
				filename = text1.getText().toString();  //得到文件名
				
				try{
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
						Toast.makeText(MainActivity.this, "存储成功！", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "没有存储卡！", Toast.LENGTH_LONG).show();
					}
				
				}catch(IOException e){
					return;
				}
			}
		})
		.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
			}})
    .show();  //显示对话框
    }
   */ 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_opens:
				startActivity(new Intent(this, OpenActivity.class));
				return true;
			case R.id.action_settings:
//				Toast.makeText(MainActivity.this, "存储成功！", Toast.LENGTH_SHORT).show();
//		        Person mPerson = new Person();  
//		        mPerson.setName("frankie");  
//		        mPerson.setAge(25);  
//		        Intent mIntent = new Intent(this,SettingActivity.class);  
//		        Bundle mBundle = new Bundle();  
//		        mBundle.putSerializable(PAR_KEY,socket);  
//		        mIntent.putExtras(mBundle);  
//		        startActivity(mIntent);  
		        
				startActivity(new Intent(this, SettingActivity.class));
				return true;
			case R.id.action_about:
				new AlertDialog.Builder(MainActivity.this)
				.setTitle("关于")
				.setMessage("本程序版本：1.0.0")  
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", null).show();
				return true;
		}
		return false;
	}
	public boolean DoublestoFile(String filename,double p[],double q[],double r[],double s[]){
		//File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
		//File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
		//if(BuildDir.exists()==false)BuildDir.mkdirs();
		//File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
		String status = Environment.getExternalStorageState(); 
		// 判段SD卡是否存在 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		java.io.File a=new java.io.File(dir);
		if (!a.exists())a.mkdir();
		File file=new File(a,filename);
		
		//File f = new File(filename);
		try {
			FileOutputStream stream = new FileOutputStream(file);  //打开文件输入流
			String wmsg="";
			for(int i=0;i<MaxLen;i++){
				wmsg+=p[i];
				if(i==MaxLen-1)
					wmsg+="\r\n";
				else
					wmsg+=";";
			}
			for(int i=0;i<MaxLen;i++){
				wmsg+=q[i];
				if(i==MaxLen-1)
					wmsg+="\r\n";
				else
					wmsg+=";";
			}
			for(int i=0;i<MaxLen;i++){
				wmsg+=r[i];
				if(i==MaxLen-1)
					wmsg+="\r\n";
				else
					wmsg+=";";
			}
			for(int i=0;i<MaxLen;i++){
				wmsg+=s[i];
				if(i==MaxLen-1)
					wmsg+="\r\n";
				else
					wmsg+=";";
			}
			stream.write(wmsg.getBytes());
			stream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return true;
	}
	public boolean FileExit(String filename)
	{
		String status = Environment.getExternalStorageState(); 
		// 判段SD卡是否存在 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		java.io.File a=new java.io.File(dir);
		if (!a.exists())a.mkdir();
		File file=new File(a,filename);
		
		if(!file.exists()) return false;
		}
		return true;
	}
	public boolean FiletoDoubles(String filename,double p[],double q[],double r[],double s[])
	{
		String status = Environment.getExternalStorageState(); 
		// 判段SD卡是否存在 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		java.io.File a=new java.io.File(dir);
		if (!a.exists())a.mkdir();
		File file=new File(a,filename);

		if(!file.exists()) return false;
		
		  //File f = new File(filename);
		  //if(!f.exists()) return false;
		  
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[(int)file.length()];
			  fis.read(b);
			  fis.close();
			  String bb = new String(b);
			  String cc[]=bb.split("\r\n");
			  
			  for(int i=0;i<cc.length;i++)
			  {
			   //System.out.print(b[i]);
				  String dd[]=cc[i].split(";");
				  /*
				  MaxLen=dd.length;
				  switch(i)
				  {
				  case 0:
					  p=new double[dd.length];
					  break;
				  case 1:
					  q=new double[dd.length];
					  break;
				  case 2:
					  r=new double[dd.length];
					  break;
				  case 3:
					  s=new double[dd.length];
					  break;
				  }
				  */
				  for(int j=0;j<dd.length;j++)
				  {
					  switch(i)
					  {
					  case 0:
						  p[j]=Double.parseDouble(dd[j]);
						  break;
					  case 1:
						  q[j]=Double.parseDouble(dd[j]);
						  break;
					  case 2:
						  r[j]=Double.parseDouble(dd[j]);
						  break;
					  case 3:
						  s[j]=Double.parseDouble(dd[j]);
						  break;
					  }
				  }
			  }
			  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		}  
//		  String bb = new String(b);
//		  System.out.println(bb);
//		  boolean a = true;
//		  //File ff = new File("d://temp//newread2.txt");
//		  //FileOutputStream fos = new FileOutputStream(ff,a);
//		  //fos.write(b);
//		  String abc = "abc";
//		  byte[] ab = abc.getBytes();
//		  //fos.write(ab);
//		  
//		  //fos.close();
		return true;
		  
		 
	}
	
}
