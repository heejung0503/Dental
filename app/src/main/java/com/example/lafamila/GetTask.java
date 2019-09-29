package com.example.lafamila;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GetTask extends AsyncTask <String, Double, String>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected abstract void onPostExecute(String s);

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String port = strings[1];
        String api = strings[2];
        String queryString = "?";
        for(int i=3;i< strings.length;i=i+2){
            queryString += "&"+strings[i]+"="+strings[i+1];
        }
        try {
            HttpGet httpGet = new HttpGet(url+":"+port+"/"+api+queryString);




            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpGet);

            // StatusLine stat = response.getStatusLine();

            //404 : page not found error
            //500 : internal server error
            //200 : 정상
            int res = response.getStatusLine().getStatusCode();

            if (res >= 400) {


            } else {


                InputStreamReader is = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                BufferedReader reader = new BufferedReader(is);


                String line = null;
                String data = "";

                while ((line = reader.readLine()) != null) {
                    data += line;
                }
                reader.close();
                is.close();

                return data;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }
}
