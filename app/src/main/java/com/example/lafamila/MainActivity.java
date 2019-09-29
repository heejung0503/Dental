package com.example.lafamila;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    ListView lv;
    DentalListAdapter adapter;
    LocationManager locationManager;
    String longitude;
    String latitude;
    ArrayList<HashMap<String, String>> maps;
    ArrayList<ArrayList<String>> results;
    boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.lv);


//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//
//        }
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude() + "";
        longitude = -location.getLongitude() + "";
        Log.d("lafamilia", latitude);
        if(!isLoaded && latitude != null && longitude != null){
            (new APITask()).execute();
            isLoaded = !isLoaded;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    class APITask extends AsyncTask<String, Double, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput( new StringReader( s ) ); // pass input whatever xml you have
                int eventType = xpp.getEventType();

                results = new ArrayList<ArrayList<String>>();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        // XML 데이터 시작
                    } else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                        eventType = xpp.next();
                        ArrayList<String> item = new ArrayList<String>();
                        boolean read = false;
                        while(eventType != XmlPullParser.END_TAG || (eventType == XmlPullParser.END_TAG && !xpp.getName().equals("item"))){
                            if (eventType == XmlPullParser.TEXT) {
                                if(read){
                                    item.add(xpp.getText());
                                }
                            }
                            else if (eventType == XmlPullParser.START_TAG){
                                String tagName = xpp.getName();
                                if(tagName.equals("distance") || tagName.equals("dutyDiv") || tagName.equals("dutyName") || tagName.equals("dutyTell") || tagName.equals("dutyAddr") || tagName.equals("endTime") || tagName.equals("startTime") || tagName.equals("latitude") || tagName.equals("longitude") ){
                                    read = true;
                                }
                                else{
                                    read = false;
                                }
                            }
                            else if(eventType == XmlPullParser.END_TAG){
                                read = false;
                            }

                            eventType = xpp.next();
                        }
                        if(item.get(2).equals("B") || item.get(2).equals("N")){
                            results.add(item);
                        }

                    }
                    eventType = xpp.next();
                }
                adapter = new DentalListAdapter();

                String data = "";
                maps = new ArrayList<HashMap<String, String>>();
                for(int i=0;i<results.size();i++){
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", results.get(i).get(3));
                    map.put("distance", results.get(i).get(0));
                    maps.add(map);

                    LatLng SEOUL = new LatLng(Double.parseDouble(results.get(i).get(5)), Double.parseDouble(results.get(i).get(6)));

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEOUL);
                    markerOptions.title(results.get(i).get(3));
                    markerOptions.snippet(results.get(i).get(1));
                    mMap.addMarker(markerOptions);
                    adapter.addItem(results.get(i).get(3), results.get(i).get(0), results.get(i).get(1), results.get(i).get(7), results.get(i).get(4), "0222937582");

                }


                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        maps.get(i)
                        Intent intent = new Intent(getBaseContext(), ContentActivity.class);
                        intent.putExtra("item", results.get(i));
                        startActivity(intent);
                    }
                });



                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



                //                result.setText(data);
                Log.d("lafamila", data);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String data = "";
            try{
                HttpGet httppost = new HttpGet("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?WGS84_LON="+longitude+"&WGS84_LAT="+latitude+"&pageNo=1&numOfRows=300&ServiceKey=l2iEjiVoEwvVKOOnr9iujpLFh5mKJdS13PyvFK7Az%2F49RlYCvc3mPMrszpyr%2BeEE6nUMkK%2FSl65hSW49VrTkLQ%3D%3D");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int res = response.getStatusLine().getStatusCode();





                if(res >= 400){


                }
                else{

                    InputStreamReader is = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
                    BufferedReader reader = new BufferedReader(is);
                    String line = null;
                    data = "";
                    while((line = reader.readLine())!=null){
                        Log.d("lafamila", line);
                        data += line;
                    }
                    reader.close();
                    is.close();


                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return data;
        }
    }
}
