package com.jiangli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class HelloAndroidActivity extends Activity{
    /** Called when the activity is first created. */
	ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	listView = new ListView(this);
    	 SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.me,
                 new String[]{"title","info","img"},
                 new int[]{R.id.title,R.id.info,R.id.img});
    	 adapter.setViewBinder(new ViewBinder() {   
    		 public boolean setViewValue(View view, Object data,   
    		 String textRepresentation) {   
    		 //判断是否为我们要处理的对象   
    		 if(view instanceof ImageView && data instanceof Bitmap){   
    		 ImageView iv = (ImageView) view;   
    		 iv.setImageBitmap((Bitmap) data);   
    		 return true;   
    		 }else   
    		 return false;   
    		 }   
    		 });
    	 listView.setAdapter(adapter);
    	 setContentView(listView);
     }
  
     
	private List<Map<String, Object>> getData() {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
  
         Map<String, Object> map = new HashMap<String, Object>();
         HttpClient client = new DefaultHttpClient();
         HttpGet get = new HttpGet("http://10.64.48.13:8080/JsonResult/JsonResult");
         StringBuilder builder = new StringBuilder();
         try {
             HttpResponse response = client.execute(get);
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     response.getEntity().getContent()));
             for (String s = reader.readLine(); s != null; s = reader.readLine()) {
            	 builder.append(s);
             }
                 JSONArray jsonObjs = new JSONObject(builder.toString()).getJSONArray("person");   
                 String ss = "";   
                 for(int i = 0; i < jsonObjs.length() ; i++){   
                     JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i)); 
                     String name = jsonObj.getString("name");   
                     int age = jsonObj.getInt("age");  
                     String content = jsonObj.getString("content");
                     String imgurl = jsonObj.getString("imgurl");
                     map.put("title", name);
                     map.put("info", content);
                     map.put("img", getBitmap(imgurl));
                     list.add(map);
             }   
                 
         } catch (Exception e) {
             e.printStackTrace();
         }
         return list;
     }

	public Bitmap getBitmap(String imgurl){  
        Bitmap mBitmap = null;  
        try {  
            URL url = new URL(imgurl);  
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
            InputStream is = conn.getInputStream();  
            mBitmap = BitmapFactory.decodeStream(is);  
              
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        return mBitmap;  
    } 
    
}