package edu.tony.ipa;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;

public class UploadImage extends Activity{
	 String end = "\r\n";
	 String twoHyphens = "--";
	 String boundary = "*****";
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 URL url;
		 try{
			 url = new URL("http://192.168.11.101/uploadTest/test.php");
    
			 HttpURLConnection con;
			 con = (HttpURLConnection) url.openConnection();

			 con.setDoInput(true);
			 con.setDoOutput(true);
			 con.setUseCaches(false);
	 
			 con.setRequestMethod("POST");
	 
			 // 這邊要加上，不然File就只會當作POST
			 con.setRequestProperty("Connection", "Keep-Alive");
			 con.setRequestProperty("Charset", "UTF-8");
			 con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

			 DataOutputStream ds = new DataOutputStream(con.getOutputStream());
	 
			 String inputName = "uploadFile",
			 fileName = "Image",
			 filePath = "/mnt/sdcard/_ExternalSD/DCIM/Camera/IMG_20100613_193802.jpg";
	 
			 // 寫入檔案
			 ds.writeBytes(twoHyphens + boundary + end);
			 ds.writeBytes("Content-Disposition: form-data;");
			 ds.writeBytes("name=\"" + inputName + "\";filename=\"" + fileName + "\"" + end + end);
			 int bufferSize = 1024;
			 int length = -1;
			 byte[] buffer = new byte[bufferSize];
			 FileInputStream fStream = new FileInputStream(filePath);
			 while ((length = fStream.read(buffer)) != -1) {
				 ds.write(buffer, 0, length);
			 }
			 ds.writeBytes(end);
	 
			 // 結束
			 ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			 // close stream
			 ds.flush();
		 } catch (IOException e) {
		 	// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
	 }
}
