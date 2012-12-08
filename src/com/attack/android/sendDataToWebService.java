package com.attack.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class sendDataToWebService{
public static final String LoginServiceUri = "http://192.168.43.119:8080/ping";
private static HttpClient mHttpClient;
private String location;
private String firstName;
private String secondName;
private String address;
private static Context context;
private int videoNum;

   public sendDataToWebService(Context contxt, String loc, String[] personalInfo, Cursor contacts, int num) {
	   location = loc;
	   context = contxt;
	   firstName = personalInfo[0];
	   secondName = personalInfo[1];
	   address = personalInfo[2];
	   videoNum = num - 1;
	   send();
   }

   public void send() {
	   String path = Environment.getExternalStorageDirectory() +
		        "" + File.separatorChar + "videocapture_example"+videoNum+".mp4";
	   
	   File f = new File(path);
	   
	   byte[] filebyte = null;
	try {
		filebyte = FileUtils.readFileToByteArray(f);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   String decode = Base64.encodeToString(filebyte, Context.MODE_APPEND);
	   
	   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 
       nameValuePairs.add(new BasicNameValuePair("Videofile", decode));
       nameValuePairs.add(new BasicNameValuePair("location", location)); 
       nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
       nameValuePairs.add(new BasicNameValuePair("secondName", secondName));
       nameValuePairs.add(new BasicNameValuePair("address", address));
       try {
		executeHttpPost(LoginServiceUri, nameValuePairs);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

   }

   
   
   public static void executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
	   
	           BufferedReader in = null;
	   
	           try {
	   
	               HttpClient client = getHttpClient();
	   
	               HttpPost request = new HttpPost(url);
	   
	               UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
	   
	               request.setEntity(formEntity);
	   
	               HttpResponse response = client.execute(request);
	               HttpEntity value = response.getEntity();
	               InputStream is = value.getContent();

	               BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	               String line = reader.readLine();
	               if(line.equals("got it")){
	            	   Toast.makeText(context, R.string.video_received, 1000).show();
	               }
	   
	           } finally {
	   
	               if (in != null) {
	   
	                   try {
	   
	                       in.close();
	   
	                   } catch (IOException e) {
	   
	                       e.printStackTrace();
	   
	                   }
	   
	               }
	   
	           }
	   
	       }

   private static HttpClient getHttpClient() {
	   
	           if (mHttpClient == null) {
	   
	               mHttpClient = new DefaultHttpClient();
	   
	               final HttpParams params = mHttpClient.getParams();
	   
	               HttpConnectionParams.setConnectionTimeout(params, 0);
	   
	               HttpConnectionParams.setSoTimeout(params, 0);
	   
	               ConnManagerParams.setTimeout(params, 0);
	   
	           }
	   
	           return mHttpClient;
	   
	       }


}

