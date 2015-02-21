package com.interparalle.Util;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.methods.HttpGet;

import android.graphics.Path.FillType;
import android.os.Environment;
import android.util.Log;

import com.interparalle.Util.ContextUtil;


/*make sure the application can access the SD card and the network function is accessable.
 * Note: the permission must be set, otherwise the class can't work correctly. 
 * INTERNET&WRITE_EXTERNAL_STORAGE
 */
public class DownLoad {
	
	//final
	final String CLASS_FLAG = "DownLoad";
	
	//the http string url
	private String stringurl;
	
	//the http URL url
	private URL urlurl;
	
	//sd card path
	private String sdcard;
	
	//dir path
	private String dirpath;
	//file name
	private String savefilename;
	
	//http connection
	private HttpURLConnection httpcon;
	
	private URL url; ;
	
	//download thread
	private Thread DLthread;
	
	//download handler
	private DownLoadHandler downloadhandler;
	
	public DownLoad(String url)
	{
		if(url.startsWith("http://"))
		{
			this.stringurl =  url;
		}
		
		//get the sd card enviroment
		this.sdcard = Environment.getExternalStorageDirectory()+"/";
		
		

	}
	
	public DownLoad(URL url)
	{
		if(!stringurl.isEmpty())
		{
			Log.e(CLASS_FLAG, "construction error!");
			return;
		}
		if(url.toString().startsWith("http://"))
		{
			this.urlurl =  url;
		}
		
		//get the sd card enviroment
		this.sdcard = Environment.getExternalStorageDirectory()+"/";
		
		
	}
	
	
	/*create the http connection
	 * NOTE: the connection will fistly connnet to the stringurl
	 * */
	public HttpURLConnection getHttpConnection()
	{
		 
        HttpURLConnection urlcon = null;  
        try {
        	if(!stringurl.isEmpty())
        	{
        		url = new URL(stringurl); 
        	}
        	else if(!urlurl.toString().isEmpty())
        	{
        		url = urlurl;
        	}
        	else
        	{
        		throw new Exception();
        	}
             
            urlcon = (HttpURLConnection) url.openConnection();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return urlcon;
	}
	
	/*get the http text.
	 * like html,xml,js,eg.
	 * NOTE: the function is sync function ,it will blcok the thread,so PLEASE create another thread to call it.
	 * */
	
	public String DownLoadAsString()
	{
		 StringBuilder sb = new StringBuilder();  
	        String temp = null;  
	        try {  
	        	httpcon = getHttpConnection();
	            InputStream is = httpcon.getInputStream();  
	            BufferedReader br = new BufferedReader(new InputStreamReader(is));  
	            while ((temp = br.readLine()) != null) {  
	                sb.append(temp);  
	            }  
	            br.close();  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return sb.toString();  
		
	}
	
	/*dowload to sd card.
	 * 
	 * */
	
	public void DownLoad2Sd(String dir,String filename, DownLoadHandler handler)
	{
		
		dirpath = sdcard+dir;
		savefilename = filename;
		downloadhandler = handler;

		DLthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				
				StringBuilder sb = new StringBuilder(dirpath); 
				File file = new File(sb.toString());  
				if (!file.exists()) 
				{  
					if(!file.mkdirs())
					{
						Log.e(CLASS_FLAG, "create dirs failed");
						return;
					}
					//create dir
					Log.d(CLASS_FLAG, "create "+sb.toString()+" dir successfully!");  
				}  
				
				//get the file handle
				sb.append("/"+savefilename);
				file = new File(sb.toString());  
				
				FileOutputStream fos = null;  
				try 
				{  
					httpcon = getHttpConnection();
					InputStream is = httpcon.getInputStream();  
					
					//create a new file
					if(!file.exists())
					{							
						if(file.createNewFile())
						{
							Log.d(CLASS_FLAG, "create "+sb.toString()+" successfully!");
						}else
						{
							Log.e(CLASS_FLAG, "create "+sb.toString()+" failed!");
						}
					}
					
					fos = new FileOutputStream(file);  
					byte[] buf = new byte[512];
					int len;
					while ((len = is.read(buf)) != -1) 
					{  
					    fos.write(buf);
					    //reset the buffer
					    Arrays.fill(buf, (byte)0);
					    downloadhandler.setSize(len);
					}
					fos.flush();
					fos.close();
					is.close();
					downloadhandler.downloadfinish(file.toString());
				} catch (Exception e) {  
					Log.e(CLASS_FLAG, e.toString());
					return ;  
				}
				return;				
			}
		});
		
		DLthread.start();
	}
	
	/*download to local(not only in sd card)
	 * NOTE: the dirpath must in the application dir, otherwise the system will forbit the operation
	 * */
	public void DownLoad2Local(String dir,String filename, DownLoadHandler handler)
	{
		
		dirpath = ContextUtil.getContext().getApplicationContext().getFilesDir()+dir;
		savefilename = filename;
		downloadhandler = handler;
		
		DLthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringBuilder sb = new StringBuilder(dirpath); 
				File file = new File(sb.toString());  
				if (!file.exists()) 
				{  
					if(!file.mkdirs())
					{
						Log.e(CLASS_FLAG, "create dirs failed");
						return;
					}
					//create dir
					Log.d(CLASS_FLAG, "create "+sb.toString()+" dir successfully!");  
				}
				
				//get the file handle
				sb.append("/"+savefilename);
				file = new File(sb.toString());  
				Log.d(CLASS_FLAG, file.toString());
				
				FileOutputStream fos = null;  
				try 
				{  
					
					
					//create a new file
					if(!file.exists())
					{							
						if(file.createNewFile())
						{
							Log.d(CLASS_FLAG, "create "+file.toString()+" successfully!");
						}else
						{
							Log.e(CLASS_FLAG, "create "+file.toString()+" failed!");
						}
					}
//					httpcon.connect();
					httpcon = getHttpConnection();
					InputStream is = httpcon.getInputStream();  
					fos = new FileOutputStream(file);  
					byte[] buf = new byte[1024];  
					while ((is.read(buf)) != -1) 
					{  
					    fos.write(buf);
					  //reset the buffer
					    Arrays.fill(buf, (byte)0);
					    downloadhandler.setSize(buf.length);
					}  
				    fos.flush();
				    fos.close(); 
					is.close();
					downloadhandler.downloadfinish(file.toString());
				} catch (Exception e) {  
					Log.e(CLASS_FLAG, e.toString());
//					Log.e(CLASS_FLAG, e.getMessage());
					return ;  
				}    
				return;				
			}
		});
		
		DLthread.start();
	}
	
	
	@SuppressWarnings("deprecation")
	public void CancelDownLoad()
	{
		if(DLthread.isAlive())
		{
			DLthread.stop();
		}
		
		DLthread.destroy();		
	}
	
	public int GetHttpContentLenth()
	{
		return httpcon.getContentLength();
	}
	
	public abstract class DownLoadHandler  
    {  
        public abstract void setSize(int size);
        public abstract int downloadfinish(String filename);
    }

}

