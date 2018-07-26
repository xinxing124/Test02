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
	private final static int REQUEST_CONNECT_DEVICE = 1;    //�궨���ѯ�豸���
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
	public String filename=""; //��������洢���ļ���
	BluetoothDevice device = null;     //�����豸
	public static BluetoothSocket socket = null;      //����ͨ��socket
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
    private String smsg;    //��ʾ�����ݻ���
    private String fmsg;    //���������ݻ���
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
        // �̶���UUID      
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
	 * ���豸������ �ο�Դ�룺platform/packages/apps/Settings.git 
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

	// ȡ���û�����  
	static public boolean cancelPairingUserInput(Class btClass,  
	        BluetoothDevice device)  

	throws Exception  
	{  
	    Method createBondMethod = btClass.getMethod("cancelPairingUserInput");  
	    // cancelBondProcess()  
	    Boolean returnValue = (Boolean) createBondMethod.invoke(device);  
	    return returnValue.booleanValue();  
	}  

	// ȡ�����  
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
		
	    smsg = "";    //��ʾ�����ݻ���
	    fmsg = "";    //���������ݻ���
	    Flag=0;
	    isconnect=false;//��������״̬
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
			
			textView2.setText("���ֵ��0.000" +"\n" +"��Сֵ��0.000");
			textView3.setText("���ֵ��0.000" +"\n" +"��Сֵ��0.000");
			
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
        //ˢ��
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
        //����
		myBtn4=(Button)findViewById(R.id.button4);
        myBtn4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				/*
			    Dialog dialog = new AlertDialog.Builder(MainActivity.this)  
	            .setTitle("ѡ��ģ��")  
	            .setIcon(android.R.drawable.ic_dialog_info)
	            .setSingleChoiceItems(new String[] { "���ٹ�", "���ٹ�" }, typeDialog,
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
	    			}).setPositiveButton("ȡ��",null).show();
	    		*/
				
				
				//dialogShowCheck();
				if(DebugFlag)
					SaveSingleExcelData(2);//single
				else{
				//	SaveData();

				String status = Environment.getExternalStorageState(); 
				// �ж�SD���Ƿ���� 
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
							.setTitle("��ʾ")
							.setMessage("��ȷ��Ҫ��������ʼ���ݣ�")  
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener(){
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
							                		Toast.makeText(MainActivity.this,"�������ݻ���", Toast.LENGTH_SHORT).show();
							                }})
							.setNegativeButton("ȡ��", null).show();
						}
						else
						{
							new AlertDialog.Builder(MainActivity.this)
							.setTitle("��ʾ")
							.setMessage("��ȷ��Ҫ��������ʼ���ݣ�")  
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener(){
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
													Toast.makeText(MainActivity.this,"�������ݻ���", Toast.LENGTH_SHORT).show();
							                }})
							.setNegativeButton("ȡ��", null).show();

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
.setTitle("������")
.setMessage("��ȷ��Ҫ�˳���������")  
.setIcon(android.R.drawable.ic_dialog_info)
.setView(editText)
.setPositiveButton("ȷ��", null)
.setNegativeButton("ȡ��", null).show();
*/
				
/*
new AlertDialog.Builder(MainActivity.this)
.setTitle("������")
.setMessage("��ȷ��Ҫ�˳���������")  
.setIcon(android.R.drawable.ic_dialog_info)
.setView(new EditText(MainActivity.this))
.setPositiveButton("ȷ��", null)
.setNegativeButton("ȡ��", null).show();
*/

//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL).setTitle("������")
//.setMessage("��ȷ��Ҫ�˳���������")  
//.setIcon(android.R.drawable.ic_dialog_info).setView(
//new EditText(MainActivity.this)).setPositiveButton("ȷ��", null)
//.setNegativeButton("ȡ��", null).show();

//,AlertDialog.THEME_TRADITIONAL
	    		//	.setPositiveButton("ȡ��", new DialogInterface.OnClickListener(){
	            //        public void onClick(DialogInterface dialog, int which) {
	            //        	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
	            //        }}).show();
			    
				//Toast.makeText(getApplicationContext(), Integer.toHexString(typeDialog()), Toast.LENGTH_SHORT).show();
			}
        });
        //�˳�
        myBtn5=(Button)findViewById(R.id.button5);
        myBtn5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "�����˳�", Toast.LENGTH_SHORT).show();
//				//finish();
//				Intent intent = new Intent();  //����Ҫ�������ǿ��Կ��ڱ��Activity�����õ���ʼ����ֵ�������޸ĺ��
//		        //intent.setClass(MainActivity.this, SecondActivity.class);
//				intent.setClass(MainActivity.this, ExitActivity.class);
//		        //startActivity(intent);
//		        startActivityForResult(intent,0);
				//dialogShowCheck();
				exitDialog();
			}
        });  
        
        //����
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
			
			Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity.class); //��ת��������
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
    		/*
    		if(UFlag)
    		{
    		//-----------------------------------------------------
    		                // �õ������豸���      
    		                device = mBluetoothAdapter.getRemoteDevice(address);

    						
    		                // �÷���ŵõ�socket
    		                try{
    		                	socket = device.createRfcommSocketToServiceRecord(MY_UUID);
    		                }catch(IOException e){
    		                	Toast.makeText(MainActivity.this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
    		                }
    		                //����socket
    		                try{
    		                	socket.connect();
    		                	Toast.makeText(MainActivity.this, "����"+device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
    		                }catch(IOException e){
    		                	try{
    		                		Toast.makeText(MainActivity.this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
    		                		socket.close();
    		                		socket = null;
    		                	}catch(IOException ee){
    		                		Toast.makeText(MainActivity.this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
    		                	}
    		                	
    		                	return;
    		                }
    		                
    		                
    		                //�򿪽����߳�
    		                try{
    		            		inStream = socket.getInputStream();   //�õ���������������
    		            		}catch(IOException e){
    		            			Toast.makeText(MainActivity.this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
//			if(device.getBondState() != BluetoothDevice.BOND_BONDED){//�жϸ�����ַ�µ�device�Ƿ��Ѿ����  
////		         try{  
////		        	//byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");  
////		        	 //device.setPin(pinBytes);  
////		        	// createBond(device.getClass(), device);
////		        	 setPin(device.getClass(), device, "00000000");//����pinֵ  
////		              createBond(device.getClass(), device);
////		              //cancelPairingUserInput(device.getClass(), device); 
////		          }  
////		          catch (Exception e) {  
////		           // TODO: handle exception  
////		          //System.out.println("��Բ��ɹ�");
////		        	  Toast.makeText(getApplicationContext(), "��Բ��ɹ���", Toast.LENGTH_SHORT).show();
////		          }
//				try {  
//                    // ����  
//                    connect(device);  
//                } catch (IOException e) {  
//                    e.printStackTrace();  
//                }  
//		}  
			 
//			    try {
//					Method createBondMethod =  device.getClass().getMethod("createBond");  
//					Boolean returnValue = (Boolean) createBondMethod.invoke(device);
//				} catch (SecurityException e1) {
//					// TODO �Զ����ɵ� catch ��
//					e1.printStackTrace();
//				} catch (IllegalArgumentException e1) {
//					// TODO �Զ����ɵ� catch ��
//					e1.printStackTrace();
//				} catch (NoSuchMethodException e1) {
//					// TODO �Զ����ɵ� catch ��
//					e1.printStackTrace();
//				} catch (IllegalAccessException e1) {
//					// TODO �Զ����ɵ� catch ��
//					e1.printStackTrace();
//				} catch (InvocationTargetException e1) {
//					// TODO �Զ����ɵ� catch ��
//					e1.printStackTrace();
//				}  
			
			//try {
			//	if (device.getBondState() != BluetoothDevice.BOND_BONDED)
			//	{
			//		Toast.makeText(getApplicationContext(), "δ���豸"+address+"�����ԣ�����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
//				Boolean returnValue;
//				Method createBondMethod,removeBondMethod;
//				// �ֻ��������ɼ������
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
				
					//Toast.makeText(getApplicationContext(), "�޷�(1)�������ӣ�", Toast.LENGTH_SHORT).show();
				//}
				//else
				//Log.e("returnValue", "" + returnValue);
				
				//ClsUtils.setPin(btDevice.getClass(), btDevice, strPsw); // �ֻ��������ɼ������
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
						Toast.makeText(getApplicationContext(), "���Ӷ˿ڴ���ʧ�ܣ�", Toast.LENGTH_SHORT).show();
						
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
			        		//Toast.makeText(getApplicationContext(), "����ʧ��,�����Դ��������ԣ�", Toast.LENGTH_SHORT).show();
			        		socket.close();
			        		socket = null;
	
			        	}catch(IOException ee){
			        		Toast.makeText(getApplicationContext(), "Not connect sucess��", Toast.LENGTH_SHORT).show();
			        	}
			        	return;
			        }
					
					/*
					boolean connectflag=false;
					for(int i=0;i<6;i++)
					{
						try{
							socket.connect();
				        	Toast.makeText(getApplicationContext(), "����"+device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getApplicationContext(), "����ʧ��,�����Դ��������ԣ�", Toast.LENGTH_SHORT).show();
							socket.close();
							socket = null;
						}catch(IOException ee){
			        		Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			        	}
					}
					*/
				//}
				//else
				//	Toast.makeText(getApplicationContext(), "�Ѿ����ӣ�", Toast.LENGTH_SHORT).show();
				//�򿪽����߳�
                try{
                	inStream = socket.getInputStream();   //�õ���������������
            		}catch(IOException e){
            			Toast.makeText(getApplicationContext(), "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
		//	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();

			}
        });
        
        //��ʼ
	    myBtn2=(Button)findViewById(R.id.button2);
	    myBtn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				
			//String send; 
	    	//int i=0;
	    	//int n=0;
//			mTimer.schedule(mTimerTask, 10000,20000); //��1���ÿ5��ִ��һ�ζ�ʱ���еķ��������籾��Ϊ����log.v��ӡ�����
			fmsg="";
	    	if(!DebugFlag)
	    	{
	    		String send; 
		    	int i=0;
		    	int n=0;
	    	try{
		    		if(socket!=null)
		    		{
			    		OutputStream os = socket.getOutputStream();   //�������������
			    		//outStream= socket.getOutputStream();   //�������������
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
			    		for(i=0;i<bos.length;i++){ //�ֻ��л���Ϊ0a,�����Ϊ0d 0a���ٷ���
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
	    		Toast.makeText(getApplicationContext(), "���������ѶϿ���������! ", Toast.LENGTH_LONG).show();
	    	}  	
	    	}
	    	else
	    	{
	    	Dialog dialog = new AlertDialog.Builder(MainActivity.this)  
            .setTitle("ѡ��ģʽ")  
            .setIcon(android.R.drawable.ic_dialog_info)
            .setSingleChoiceItems(new String[] { "�ɼ�", "�궨" }, 0,
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
    		    			//�򿪽����߳�
    		                try{
    		            		inStream = socket.getInputStream();   //�õ���������������
    		            		}catch(IOException e){
    		            			Toast.makeText(MainActivity.this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
    		            			return;
    		            		}
    		            		if(bThread==false){
    		            			ReadThread.start();
    		            			bThread=true;
    		            		}else{
    		            			bRun = true;
    		            		}
    		            		*/
    		            		
    			    		OutputStream os = socket.getOutputStream();   //�������������
    			    		//outStream= socket.getOutputStream();   //�������������
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
    			    		for(i=0;i<bos.length;i++){ //�ֻ��л���Ϊ0a,�����Ϊ0d 0a���ٷ���
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
    	    		Toast.makeText(getApplicationContext(), "���������ѶϿ���������! ", Toast.LENGTH_LONG).show();
    	    	}  
    				
    			//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
    			dialog.dismiss();
    			 }
    			}).setPositiveButton("ȡ��",null).show();
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
		
        // 2,������ʾ
        XYMultipleSeriesDataset dataset_TOP = new XYMultipleSeriesDataset();
        XYMultipleSeriesDataset dataset_Side = new XYMultipleSeriesDataset();
        // 2.1, ������״ͼ����
        Random r = new Random();
        double OverrunX[][]={{0,500,1000},{0,500,1000},{0,500,1000},{0,500,1000}};
    	double OverrunY[][]={{0,0.3,0},{0,0.1,0},{0,0.1,0},{0,-0.2,0}};
    	
        for (int i = 0; i < 4; i++) {
            // ע��,�����������XYSeries ��һ�㲻ͬ!!����ʹ��CategroySeries
            //CategorySeries series = new CategorySeries("test" + (i + 1));
        	XYSeries series = new XYSeries("test" + (i + 1));
        	
        	series.add(OverrunX[i][0],OverrunY[i][0]);
        	series.add(OverrunX[i][1],OverrunY[i][1]);
        	series.add(OverrunX[i][2],OverrunY[i][2]);

            // �������
            //for (int k = 0; k < 100; k++) {
                // ֱ��������Ҫ��ʾ������,��:Y���ֵ
                //series.add(Math.abs(1 + r.nextInt() % 100));
            //	series.add(k,Math.abs(1 + r.nextInt() % 100));
            //}
            // ����Ҫ����ת��
        	dataset_TOP.addSeries(series);
            
        }
        //public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,  List<double[]> yValues, int scale)

        // 1, ������ʾ����Ⱦͼ
        XYMultipleSeriesRenderer renderer_TOP = new XYMultipleSeriesRenderer();
        renderer_TOP.setApplyBackgroundColor(true);
        renderer_TOP.setBackgroundColor(Color.argb(0, 0, 0, 0));
        // 3, �Ե�Ļ��ƽ�������
        XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        // 3.1������ɫ
        //xyRenderer.setColor(Color.BLUE);
        xyRenderer.setColor(Color.RED);
        // 3.2���õ����ʽ
        // xyRenderer.setPointStyle(PointStyle.SQUARE);
        // 3.3, ��Ҫ���Ƶĵ����ӵ����������
        renderer_TOP.addSeriesRenderer(xyRenderer);
        
        
        
        // 3.4,�ظ� 3.1~3.3�Ĳ�����Ƶڶ���ϵ�е�
        xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.RED);
        //xyRenderer.setPointStyle(PointStyle.CIRCLE);
        renderer_TOP.addSeriesRenderer(xyRenderer);
        
        
        
        // ע������x,y min ��Ҫ��ͬ
        // ������һ�����õ�����x,y��Χ�ķ���
        
        //˳����:minX, maxX, minY, maxY
        double[] range = { 0, 1000, -1, 3 };
        renderer_TOP.setRange(range);
        
        // �ȼ���:
        // -------------------
        // renderer.setXAxisMin(0);
        // renderer.setXAxisMax(10);
        // renderer.setYAxisMin(1);
        // renderer.setYAxisMax(200);
        // -------------------
 
        // ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
        renderer_TOP.setXLabels(21);
        renderer_TOP.setYLabels(10);
        //renderer.setXLabels(0); //����X�᲻��ʾ���֣������Զ����ֵ��  
        //renderer.addTextLabel(1, "����"); //����X������1��ʾ��ֵ
        
        // ����x,y����ʾ������,Ĭ���� Align.CENTER
        renderer_TOP.setXLabelsAlign(Align.CENTER);
        renderer_TOP.setYLabelsAlign(Align.RIGHT);
        
        //renderer.setBarSpacing(0.01);//���ü��
        //renderer.setXLabels(0);//���� X �᲻��ʾ���֣����������ֶ����ӵ����ֱ�ǩ����;//����X����ʾ�Ŀ̶ȱ�ǩ�ĸ���
        //renderer.setYLabels(15);// ���ú��ʵĿ̶ȣ���������ʾ�������� MAX / labels
        //renderer.setMargins(new int[] { 40, 40, 40, 40 });//ͼ�� 4 �߾� ����4������  ����ͼ������߿�
        //renderer.setYLabelsAlign(Align.RIGHT);//����y����ʾ�ķ��У�Ĭ���� Align.CENTER
        //renderer.setPanEnabled(true, false);//����x������Ի�����y���򲻿��Ի���
        //renderer.setZoomEnabled(false,false);//����x��y���򶼲����ԷŴ����С
        
        //renderer.setDisplayValues(true);
        //renderer.setGridColor(Color.RED);
        //renderer.setXLabelsColor(Color.BLACK);
        //renderer.setYLabelsColor(0, Color.BLACK);
        // ����������,�����ɫ
        //renderer.setAxesColor(Color.RED);
        //renderer.setBackgroundColor(Color.BLACK);
        
        // ��ʾ����
        renderer_TOP.setShowGrid(true);
        // ����x,y���ϵĿ̶ȵ���ɫ
        renderer_TOP.setLabelsColor(Color.RED);
 
        // ����ҳ�߿հ׵���ɫ
        renderer_TOP.setMarginsColor(Color.BLACK);
        // �����Ƿ���ʾ,���������,Ĭ��Ϊ true
        renderer_TOP.setShowAxes(true);
        //renderer.setShowLabels(false);
        renderer_TOP.setShowLegend(false);
        
        // ��������ͼ֮��ľ���
        //renderer.setBarSpacing(0.1);
        //int length = renderer.getSeriesRendererCount();
        
        
        //for (int i = 0; i < length; i++) {
        //    SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
        //    // ��֪�����ߵľ�������ô�����,Ĭ����Align.CENTER,���Ƕ����������ϵ�������ʾ
        //    // �ͻ��������ұ�
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
        	
        	// ��ȡ SharedPreferences   
            SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);
            Log.v("MSG",preferences.getString("Track_val", "GT-20130302"));
            Log.v("MSG",String.valueOf(preferences.getInt("Number_val", 0)));
            Log.v("MSG",preferences.getString("Operator_val", "����"));
            
            
        }
        super.onActivityResult(requestCode, resultCode, data);
        */
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //���ӽ������DeviceListActivity���÷���
            if (resultCode == Activity.RESULT_OK) {   //���ӳɹ�����DeviceListActivity���÷���
                // MAC��ַ����DeviceListActivity���÷���
            	address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            	try {
    				address=RWToSDCard("111.txt",address,true);
    				UFlag=true;
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                //Log.v("MSG", data.getExtras().getString("�豸��ַ"));
            	
            	//-----------------------------------------------------
                // �õ������豸���      
                device = mBluetoothAdapter.getRemoteDevice(address);

				
                // �÷���ŵõ�socket
                try{
                	socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                }catch(IOException e){
                	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                }
                //����socket
                try{
                	socket.connect();
                	Toast.makeText(this, "����"+device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                		socket.close();
                		socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                
                //�򿪽����߳�
                try{
            		inStream = socket.getInputStream();   //�õ���������������
            		
            		}catch(IOException e){
            			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
//    	if(msDelay==undefined) msDelay=3000; //Ĭ��ֵ��Ϊ5
//        for(int i; i < msDelay && !shell.isDisposed(); i++)
//            display.readAndDispatch(); 
//    }
    
	public String RWToSDCard(String filename,String content,boolean flag) throws Exception{
		

		String status = Environment.getExternalStorageState(); 
		// �ж�SD���Ƿ���� 
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
					//	��ȡ����
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
					//д������
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
//					��ȡ����
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
					//д������
					FileOutputStream outStream=new FileOutputStream(file);
					outStream.write(content.getBytes());
					outStream.close();
					return content;
				}
			}
		}

	return "";
}
	//valΪ����double ���֣�precsionΪ����С��λ����

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
	            // ע��,�����������XYSeries ��һ�㲻ͬ!!����ʹ��CategroySeries
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
	                // �������
	        		
	                //for (int k = 0,l=0; k < 1005; k+=5,l++)
	        		if(MaxLen<1000) jump=5;
	        		for (int k = 0,l=0; k < 1005; k+=jump,l++)
	        		//for (int k = 0,l=0; k < MaxLen-1; k++,l++)
	                {
	                    // ֱ��������Ҫ��ʾ������,��:Y���ֵ
	                	//series.add(k,Math.abs(1 + r.nextInt() % 100));
	                	//series.add(k, r.nextDouble()/8-0.05);
	                	//series.add(k, roundDouble(r.nextDouble()/8-0.05,3));
	                	
	                	series.add(k, XYValue[l]);

	                }
	        		}
	        	}
	        	
	            // ����Ҫ����ת��
	        	dataset.addSeries(series);
	        	
	        	//series.clear();
	        }
	        //Log.v("random", "TT:"+Integer.toString(dataset.getSeriesCount()));
	        //Log.v("random", "add"); 
        // 1, ������ʾ����Ⱦͼ
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
	        	// 3, �Ե�Ļ��ƽ�������
	        	xyRenderer = new XYSeriesRenderer();
	        	if(i<2)
	        	{
	        	// 3.1������ɫ
	        	xyRenderer.setColor(Color.RED);
	        	// 3.2���õ����ʽ
	        	//xyRenderer.setPointStyle(PointStyle.SQUARE);
	        	}
	        	else
	        	{
	        		if(ValueFlag)
	        		{
	        		xyRenderer.setColor(Color.BLUE);
	        		
	        		}
	        	}
	        	// 3.3, ��Ҫ���Ƶĵ����ӵ����������
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
	        	//˳����:minX, maxX, minY, maxY
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
	        	//˳����:minX, maxX, minY, maxY
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
	        	//˳����:minX, maxX, minY, maxY
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
	        	//˳����:minX, maxX, minY, maxY
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
            // ��֪�����ߵľ�������ô�����,Ĭ����Align.CENTER,���Ƕ����������ϵ�������ʾ
            // �ͻ��������ұ�
            ssr.setChartValuesTextAlign(Align.RIGHT);
            ssr.setChartValuesTextSize(12);
            ssr.setDisplayChartValues(true);
            */
        
        // ע������x,y min ��Ҫ��ͬ
        // ������һ�����õ�����x,y��Χ�ķ���
        
        //˳����:minX, maxX, minY, maxY
        //double[] range = { 0, 1000, -0.5, 0.5 };
        //renderer.setRange(range);
        
        // �ȼ���:
        // -------------------
        // renderer.setXAxisMin(0);
        // renderer.setXAxisMax(10);
        // renderer.setYAxisMin(1);
        // renderer.setYAxisMax(200);
        // -------------------
 
        // ���ú��ʵĿ̶�,��������ʾ�������� MAX / labels
        renderer.setXLabels(21);
        renderer.setYLabels(10);
        //renderer.setXLabels(0); //����X�᲻��ʾ���֣������Զ����ֵ��  
        //renderer.addTextLabel(1, "����"); //����X������1��ʾ��ֵ
        
        // ����x,y����ʾ������,Ĭ���� Align.CENTER
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setYLabelsAlign(Align.RIGHT);
        
        //renderer.setBarSpacing(1);//���ü��
        //renderer.setXLabels(0);//���� X �᲻��ʾ���֣����������ֶ����ӵ����ֱ�ǩ����;//����X����ʾ�Ŀ̶ȱ�ǩ�ĸ���
        //renderer.setYLabels(15);// ���ú��ʵĿ̶ȣ���������ʾ�������� MAX / labels
        renderer.setMargins(new int[] { 40, 40, 40, 40 });//ͼ�� 4 �߾� ����4������  ����ͼ������߿�
        //renderer.setYLabelsAlign(Align.RIGHT);//����y����ʾ�ķ��У�Ĭ���� Align.CENTER
        //renderer.setPanEnabled(false,true);//����x������Ի�����y���򲻿��Ի���
        //renderer.setZoomEnabled(false,false);//����x��y���򶼲����ԷŴ����С
        
        //renderer.setDisplayValues(true);
        //renderer.setDisplayChartValuesDistance(30);
        //renderer.setBarSpacing(1);//���ü��
        renderer.setLabelsTextSize(18.0f);
        renderer.setBarSpacing(20);//���ü��
        //renderer.setGridColor(Color.RED);
        //renderer.setXLabelsColor(Color.BLACK);
        //renderer.setYLabelsColor(0, Color.BLACK);
        // ����������,�����ɫ
        //renderer.setAxesColor(Color.RED);
        //renderer.setBackgroundColor(Color.BLACK);
        
        // ��ʾ����
        renderer.setShowGrid(true);
        // ����x,y���ϵĿ̶ȵ���ɫ
        renderer.setLabelsColor(Color.RED);
 
        // ����ҳ�߿հ׵���ɫ
        renderer.setMarginsColor(Color.BLACK);
        // �����Ƿ���ʾ,���������,Ĭ��Ϊ true
        renderer.setShowAxes(true);
        //renderer.setShowLabels(false);
        renderer.setShowLegend(false);
      
		// ������Ⱦ����ʾ���Ű�ť
		//renderer.setZoomButtonsVisible(true);
		// ������Ⱦ�������Ŵ���С
		//renderer.setZoomEnabled(true);
		//renderer.setClickEnabled(true);//����ͼ���Ƿ��������
		
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
    //�رճ�����ô�������
    @Override
	public void onDestroy(){
    	super.onDestroy();
    	if(socket!=null)  //�ر�����socket
    	try{
    		socket.close();
    	}catch(IOException e){}
    	mBluetoothAdapter.disable();
    	//SaveSingleExcelData(3);//single
    	mBluetoothAdapter.disable();  //�ر���������
    }

	/*
	  //���������߳�
    Thread ReadThread=new Thread(){
    	
    	@Override
		public void run(){
    		int num = 0;
    		byte[] buffer = new byte[1024];
    		byte[] buffer_new = new byte[1024];
    		int i = 0;
    		int n = 0;
    		bRun = true;
    		//�����߳�
    		while(true){
    			try{
    				while(inStream.available()==0){
    					while(bRun == false){}
    				}
    				n=0;
    				while(true){
    					num = inStream.read(buffer);         //��������
    					
    					String s0 = new String(buffer,0,num);
    					
    					fmsg+=s0;    //�����յ�����
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
    					//smsg+=s;   //д����ջ���
    					
    					//fmsg+=s0;
    					//for(i=0;i<fmsg.length();i++){
    						//if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
    						//	fmsg=fmsg.substring(i+2);
    						//	smsg.substring(0,i);
    						//}
    					//}
    					if(inStream.available()==0)break;  //��ʱ��û�����ݲ�����������ʾ
    				}
    				//if(!smsg.equalsIgnoreCase("\nUart Commected!"))
    					//������ʾ��Ϣ��������ʾˢ��
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    };
  */  
  //���������߳�
  Thread ReadThread=new Thread(){
  	
  	@Override
		public void run(){
  		int num = 0;
  		byte[] buffer = new byte[1024];
  		byte[] buffer_new = new byte[1024];
  		int i = 0;
  		int n = 0;
  		bRun = true;
  		//�����߳�
  		while(true){
  			try{
  				while(inStream.available()==0){
  					while(bRun == false){}
  				}
  				while(true){
  					num = inStream.read(buffer);         //��������
  					n=0;
  					
  					String s0 = new String(buffer,0,num);
  					fmsg+=s0;    //�����յ�����
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
  					smsg+=s;   //д����ջ���
  					*/
  					//if(inStream.available()==0)break;  //��ʱ��û�����ݲ�����������ʾ
  				}
  				//������ʾ��Ϣ��������ʾˢ��
  					//handler.sendMessage(handler.obtainMessage());       	    		
  	    		}catch(IOException e){
  	    		}
  		}
  	}
  };
  
    //��Ϣ��������
    Handler handler= new Handler(){
    	@Override
		public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		
//    		textView1.setText(smsg+"\n\r������"+smsg.length());   //��ʾ����
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
    		
    		//textView1.setText(smsg+"\n\r������"+c.length);   //��ʾ����
    		//textView4.setText(smsg);
   		
    		try {
				if(smsg.equalsIgnoreCase("\nUart Connected!\r\n"+senddata+"\r\n")
						||smsg.equalsIgnoreCase(senddata+"\r\n")){
					myBtn2.setEnabled(false);
					Flag=1;
					//����Activity����һ���㲥
					//Intent intent = new Intent("android.intent.action.Setting");
					//intent.putExtra("msg", "����ע��㲥�ɹ���"+smsg);
					//sendBroadcast(intent);
				}
				else if(Flag==1)
				{
					//textView1.setText(smsg+"\n\r������"+smsg.length());   //��ʾ����
					//Toast.makeText(getApplicationContext(), subMsg, Toast.LENGTH_LONG).show();
					setTitle("�������ƽ��[������:"+c.length +"]");
					//textView1.setText(subMsg);   //��ʾ����
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
					//Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					//�����յ����ݷֳɶ��������
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
					textView2.setText("���ֵ��" + decimalFormat.format(max0) +"\n" +"��Сֵ��" + decimalFormat.format(min0));
					max1 = getMax(n);
					min1 = getMin(n);
					textView3.setText("���ֵ��" + decimalFormat.format(max1) +"\n" +"��Сֵ��" + decimalFormat.format(min1));

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
				// TODO �Զ����ɵ� catch ��
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
	    * Convert byte[] to hex string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����  
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
	    		OutputStream os = socket.getOutputStream();   //�������������
	    		//outStream= socket.getOutputStream();   //�������������
	    		send="BEGIN"+"\n";
	    		
	    		byte[] bos =send.getBytes();
	    		
	    		//byte[] bos = edit0.getText().toString().getBytes();
	    		for(i=0;i<bos.length;i++){
	    			if(bos[i]==0x0a)n++;
	    		}
	    		byte[] bos_new = new byte[bos.length+n];
	    		n=0;
	    		for(i=0;i<bos.length;i++){ //�ֻ��л���Ϊ0a,�����Ϊ0d 0a���ٷ���
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
	    		Toast.makeText(getApplicationContext(), "���������ѶϿ���������! ", Toast.LENGTH_LONG).show();
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
					Toast.makeText(getApplicationContext(), "���Ӷ˿ڴ���ʧ�ܣ�", Toast.LENGTH_SHORT).show();
					
				}
				try{
					if(!socket.isConnected())
						socket.connect();

		        	Toast.makeText(getApplicationContext(), "����"+device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
		        }catch(IOException e){
		        	try{
		        		Toast.makeText(getApplicationContext(), "����ʧ��,�����Դ��������ԣ�", Toast.LENGTH_SHORT).show();
		        		socket.close();
		        		socket = null;

		        	}catch(IOException ee){
		        		Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
		        	}
		        	return;
		        }
			}
			else
				Toast.makeText(getApplicationContext(), "�Ѿ����ӣ�", Toast.LENGTH_SHORT).show();
			//�򿪽����߳�
           try{
           	inStream = socket.getInputStream();   //�õ���������������
       		}catch(IOException e){
       			Toast.makeText(getApplicationContext(), "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
	//	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();

		}
	*/	
   public boolean FiletoQuotiety(String address,double Quotiety[][])
	{
	    int i,j; 
	   	String filename="Quotiety.txt";
		String status = Environment.getExternalStorageState(); 
		// �ж�SD���Ƿ���� 
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
			//д������
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
                 //���ж�ȡ
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
            .setTitle("ѡ��ģ��")  
            .setInverseBackgroundForced(true)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setSingleChoiceItems(new String[] { "���ٹ�", "���ٹ�" }, typeDialog,
        			new DialogInterface.OnClickListener() {  
            	
    			public void onClick(DialogInterface dialog, final int which) {
    			typeDialog= which;
    			//Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
    			setResult(RESULT_OK);
    			dialog.dismiss();
    			 }
    			}).setPositiveButton("ȡ��", null).show();
    return typeDialog;
    	/*		
    			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
                    }}).show();
    			//.setPositiveButton("ȷ��", null).show();
    			//setNegativeButton("ȡ��", null).show();
    */
    
    /*
    	Dialog dialog = new AlertDialog.Builder(this)  
        .setTitle("ѡ��ģ��")  
        .setIcon(android.R.drawable.ic_dialog_info)
        .setSingleChoiceItems(new String[] { "Item1", "Item2" }, 0,null)
    	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                	Toast.makeText(MainActivity.this,Integer.toString(which), Toast.LENGTH_SHORT).show();
                }}).show();
			//.setPositiveButton("ȷ��", null).show();
			//setNegativeButton("ȡ��", null).show();
    	*/
   }  
    
    private void exitDialog(){  
    //Dialog dialog = new AlertDialog.Builder(this,AlertDialog.THEME_TRADITIONAL)//.THEME_DEVICE_DEFAULT_DARK)  
    	Dialog dialog = new AlertDialog.Builder(this)//.THEME_DEVICE_DEFAULT_DARK)
            .setTitle("�˳�����")  
            .setMessage("��ȷ��Ҫ�˳���������")  
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        //finish();
                       	if(socket!=null)  //�ر�����socket
                    	try{
                    		socket.close();
                    	}catch(IOException e){}
                    	mBluetoothAdapter.disable();  //�ر���������
                    	if(DebugFlag)
                    		SaveSingleExcelData(3);//single	
                    	System.exit(0);
                    	return;
                    }  
                }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {                    
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
            	.setTitle("��ʾ")
            	
            	.setMessage("�������ļ����ƣ�")  
            	.setIcon(android.R.drawable.ic_dialog_info)
            	.setCancelable(false)
            	.setView(editText)
            	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	
                    	filename=editText.getText().toString();
                    	Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
                    	filenum++;
                        try {
                        	File file = new File("mnt/sdcard/data/");
                        	if (!file.exists()) file.mkdirs();
							book = Workbook.createWorkbook(new File(  
							        "mnt/sdcard/data/"+filename+".xls"));  
							
							// ������Ϊ����һҳ���Ĺ�����,����0��ʾ���ǵ�һҳ  
                  sheet1 = book.createSheet("Sheet1", 0);  
                  sheet2 = book.createSheet("Shee2", 1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	//return;
                    }  
                })
            	.setNegativeButton("ȡ��", null).show();
            	//Toast.makeText(MainActivity.this,filename+"YYYYYYY", Toast.LENGTH_SHORT).show();
            // �������Excel�ļ�  
           // WritableWorkbook book = Workbook.createWorkbook(new File(  
           //         "mnt/sdcard/data/"+filename+".xls"));  
  
            // ������Ϊ����һҳ���Ĺ�����,����0��ʾ���ǵ�һҳ  
            //WritableSheet sheet1 = book.createSheet("Sheet1", 0);  
            //WritableSheet sheet2 = book.createSheet("Shee2", 1);
            break;
        	case 2:
            // ��Label����Ĺ��캯����,Ԫ��λ���ǵ�һ�е�һ��(0,0)�Լ���Ԫ������Ϊtest  
            //Label label = new Label(0, 0, "test");  
            
            // ������õĵ�Ԫ�����ӵ���������  
            //sheet1.addCell(label);
            for(int i=0;i<MaxLen-1;i++){
    			jxl.write.Number number = new jxl.write.Number(SheetColumn*2+0, i, m[i]);
    			sheet1.addCell(number);
    			jxl.write.Number number1 = new jxl.write.Number(SheetColumn*2+1, i, n[i]);
    			sheet1.addCell(number1);  
    		}
            Toast.makeText(MainActivity.this,"�����������У�"+Integer.toString(SheetColumn), Toast.LENGTH_SHORT).show();
            SheetColumn++;
            //String s;
            //s.split("BEGIN\r\n")
            //����һ���������ֵĵ�Ԫ��.����ʹ��Number��������·��,�������﷨����  
            //jxl.write.Number number = new jxl.write.Number(0, 0, 555.12541);
            //sheet1.addCell(number);  
            break;
        	case 3:
            // д�����ݲ��ر��ļ�  
            book.write();  
            book.close();
            Toast.makeText(MainActivity.this,"���ݱ��浽:"+filename, Toast.LENGTH_SHORT).show();
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
    	.setTitle("��ʾ")
    	.setMessage("�������ļ����ƣ�")  
    	.setIcon(android.R.drawable.ic_dialog_info)
    	.setView(editText)
    	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int whichButton) {  
            	
            	filename=editText.getText().toString();
            	Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
            	filenum++;
            	
                try {  
                	File file = new File("mnt/sdcard/data/");
                	if (!file.exists()) file.mkdirs();
                    // �������Excel�ļ�  
                    WritableWorkbook book = Workbook.createWorkbook(new File(  
                            "mnt/sdcard/data/"+filename+".xls"));  
          
                    // ������Ϊ����һҳ���Ĺ�����,����0��ʾ���ǵ�һҳ  
                    WritableSheet sheet1 = book.createSheet("Sheet1", 0);  
                    WritableSheet sheet2 = book.createSheet("Shee2", 1);  
          
                    // ��Label����Ĺ��캯����,Ԫ��λ���ǵ�һ�е�һ��(0,0)�Լ���Ԫ������Ϊtest  
                    //Label label = new Label(0, 0, "test");  
                    
                    // ������õĵ�Ԫ�����ӵ���������  
                    //sheet1.addCell(label);
                    for(int i=0;i<MaxLen-1;i++){
	        			jxl.write.Number number = new jxl.write.Number(0, i, m[i]);
	        			sheet1.addCell(number);
	        			jxl.write.Number number1 = new jxl.write.Number(1, i, n[i]);
	        			sheet1.addCell(number1);  
	        		}
                    
                    //String s;
                    //s.split("BEGIN\r\n")
                    //����һ���������ֵĵ�Ԫ��.����ʹ��Number��������·��,�������﷨����  
                    //jxl.write.Number number = new jxl.write.Number(1, 0, 555.12541);
                    //sheet2.addCell(number);  
          
                    // д�����ݲ��ر��ļ�  
                    book.write();  
                    book.close();  
                } catch (Exception e) {  
                    System.out.println(e);  
                }  
                
            	return;
            }  
        })
    	.setNegativeButton("ȡ��", null).show();
    }
    private void SaveData() {
		//��ʾ�Ի��������ļ���
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		etFileName = (EditText)DialogView.findViewById(R.id.etFileName);
		etTrack = (EditText)DialogView.findViewById(R.id.etTrack);
		etNumber = (EditText)DialogView.findViewById(R.id.etNumber);
		etOperator= (EditText)DialogView.findViewById(R.id.etOperator);
	     // ��ȡ SharedPreferences   
		SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);
		etFileName.setText(preferences.getString("File_val", "Test_2013"));
		etTrack.setText(preferences.getString("Track_val", "GT-20130302"));
		etNumber.setText(String.valueOf(preferences.getInt("Number_val", 0)));
		etOperator.setText(preferences.getString("Operator_val", "����"));
		
		//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("�ļ���")
		.setCancelable(false)
		.setView(DialogView)   //������ͼģ��
		.setPositiveButton("ȷ��",
		new DialogInterface.OnClickListener() //ȷ��������Ӧ����
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton){
				int pos;
				String wmsg="";
				//EditText text1 = (EditText)DialogView.findViewById(R.id.etNumber);  //�õ��ļ����������
				//filename = text1.getText().toString();  //�õ��ļ���
				filename=etFileName.getText().toString();
				
				SharedPreferences preferences = getSharedPreferences("yhdsave", MODE_PRIVATE);   
				Editor editor = preferences.edit();
				editor.putString("File_val", etFileName.getText().toString()); 
				editor.putString("Track_val", etTrack.getText().toString());   
				editor.putInt("Number_val", Integer.valueOf(etNumber.getText().toString()));
				editor.putString("Operator_val", etOperator.getText().toString());
				editor.commit();
				
				try{
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
						
						filename =filename+".txt";   //���ļ���ĩβ����.txt		
						Toast.makeText(MainActivity.this,filename, Toast.LENGTH_SHORT).show();
						
						File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
						File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
						if(BuildDir.exists()==false)BuildDir.mkdirs();
						File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
						FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
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
						Toast.makeText(MainActivity.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "û�д洢����", Toast.LENGTH_LONG).show();
					}
				
				}catch(IOException e){
					return;
				}
				
			}
		})
		.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
			}})
    .show();  //��ʾ�Ի���
    }
    
    private void dialogShowCheck() {  
    	
        String[] s = new String[] {"ѡ��1", "ѡ��2", "ѡ��3", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4", "ѡ��4"};  
        AlertDialog dialog=new AlertDialog.Builder(this)  
        .setTitle("��ѡ��") 
        .setSingleChoiceItems(s, 0, null)  
        //.setMultiChoiceItems(s, null, null)  
        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
            	/*
               String s = "��ѡ���ˣ�";
               // ɨ�����е��б�������ǰ�б��ѡ�У����б�����ı�׷�ӵ�s�����С�
               for (int i = 0; i < s.length(); i++)
               {
                   if (lv.getCheckedItemPositions().get(i))
                   {
                      s += i + ":" + lv.getAdapter().getItem(i) + " ";
                   }
               }
               // �û�����ѡ����һ���б���
               if (lv.getCheckedItemPositions().size() > 0)
               {
                   new AlertDialog.Builder(MainActivity.this)
                           .setMessage(s).show();
                   System.out.println(lv.getCheckedItemPositions().size());
               }

               // �û�δѡ���κ��б���
               else if(lv.getCheckedItemPositions().size() <= 0 )
               {
                   new AlertDialog.Builder(MainActivity.this)
                           .setMessage("��δѡ���κ�ʡ��").show();
               }
               */
            }

        }).setNegativeButton("ȡ��", null).create();
        lv = dialog.getListView();
        dialog.show(); 
        
        dialog.getWindow().setLayout(700, 600);   
    }  
    /*
    private void SaveData_1() {
    	final DecimalFormat df = new DecimalFormat("0.000");
		//��ʾ�Ի��������ļ���
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		//new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_TRADITIONAL)
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("�ļ���")
		.setCancelable(false)
		.setView(DialogView)   //������ͼģ��
		.setPositiveButton("ȷ��",
		new DialogInterface.OnClickListener() //ȷ��������Ӧ����
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
				EditText text1 = (EditText)DialogView.findViewById(R.id.etNumber);  //�õ��ļ����������
				filename = text1.getText().toString();  //�õ��ļ���
				
				try{
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
						Toast.makeText(MainActivity.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(MainActivity.this, "û�д洢����", Toast.LENGTH_LONG).show();
					}
				
				}catch(IOException e){
					return;
				}
			}
		})
		.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) { 
			}})
    .show();  //��ʾ�Ի���
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
//				Toast.makeText(MainActivity.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
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
				.setTitle("����")
				.setMessage("������汾��1.0.0")  
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("ȷ��", null).show();
				return true;
		}
		return false;
	}
	public boolean DoublestoFile(String filename,double p[],double q[],double r[],double s[]){
		//File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
		//File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
		//if(BuildDir.exists()==false)BuildDir.mkdirs();
		//File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
		String status = Environment.getExternalStorageState(); 
		// �ж�SD���Ƿ���� 
		if (status.equals(Environment.MEDIA_MOUNTED)) { 
			String dir=Environment.getExternalStorageDirectory()+"/ANC";
		java.io.File a=new java.io.File(dir);
		if (!a.exists())a.mkdir();
		File file=new File(a,filename);
		
		//File f = new File(filename);
		try {
			FileOutputStream stream = new FileOutputStream(file);  //���ļ�������
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
		// �ж�SD���Ƿ���� 
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
		// �ж�SD���Ƿ���� 
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