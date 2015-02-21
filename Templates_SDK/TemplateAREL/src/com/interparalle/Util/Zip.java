package com.interparalle.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.metaio.sdk.jni.ByteBuffer;

import android.util.Log;


public class Zip {
	
	//final
	final String CLASS_FLAG = "Zip";
	
	public void Unzip(String zipFile, String targetDir) throws Exception{

		ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFile));  
        ZipEntry zipEntry; 
        String szName = "";
       
        String destDir = new File(zipFile).getParent()+File.separator+targetDir+zipFile.substring(zipFile.lastIndexOf("/"),zipFile.lastIndexOf(".zip"));
        File fop = new File(destDir);
        fop.mkdirs();
        
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();    
            if (zipEntry.isDirectory()) {    
                // get the folder name of the widget    
                szName = szName.substring(0, szName.length() - 1); 
                File folder = new File(destDir + File.separator +szName);    
                folder.mkdirs();    
            } else {    
            
                File file = new File(destDir + File.separator + szName); 
                Log.d(CLASS_FLAG, file.toString());
                file.createNewFile();    
                // get the output stream of the file    
                FileOutputStream out = new FileOutputStream(file);    
                int len;    
                byte[] buffer = new byte[512];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer))!= -1) {    
                    // write (len) byte from buffer at the position 0   
                    out.write(buffer, 0, len); 
                    Arrays.fill(buffer, (byte)0);
                }
                out.flush();
//                inZip.closeEntry();
                out.close();    
            }    
        }   
        inZip.close();    
	}
}
