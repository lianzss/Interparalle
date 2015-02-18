package com.interparalle.Util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpConnection;

import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.util.Log;


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
	
	//http connection
	private HttpURLConnection httpcon;
	
	//download thread
	private Thread DLthread;
	
	public DownLoad(String url)
	{
		this.stringurl =  url;
		
		//get the sd card enviroment
		if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
		{
			this.sdcard = Environment.getExternalStorageDirectory()+"/";
		}
		else
		{
			Log.e(CLASS_FLAG,"SD card not ready!");
			this.stringurl = null;
			this.sdcard = null;
			return;
		}
		
		if(url.startsWith("http://"))
		{
			httpcon = getHttpConnection();
		}
		
	}
	
	public DownLoad(URL url)
	{
		if(!stringurl.isEmpty())
		{
			Log.e(CLASS_FLAG, "construction error!");
			return;
		}
		
		this.urlurl =  url;
		
		//get the sd card enviroment
		if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
		{
			this.sdcard = Environment.getExternalStorageDirectory()+"/";
		}
		else
		{
			Log.e(CLASS_FLAG,"SD card not ready!");
			this.stringurl = null;
			this.sdcard = null;
			return;
		}
		
		if(url.toString().startsWith("http://"))
		{
			httpcon = getHttpConnection();
		}
		
	}
	
	
	/*create the http connection
	 * NOTE: the connection will fistly connnet to the stringurl
	 * */
	public HttpURLConnection getHttpConnection()
	{
		URL url;  
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
	
	public void DowLoad2Local(String path,String filename, DownLoadHandler handler)
	{
		DLthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		});
		
		DLthread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void Cancel2Local()
	{
		if(DLthread.isAlive())
		{
			DLthread.stop();
		}
		
		DLthread.destroy();		
	}
	
	public abstract class DownLoadHandler  
    {  
        public abstract void setSize(int size);
        public abstract int downloadfinish();
    }

}

