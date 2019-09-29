package com.example.lafamila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DentalListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<DentalItem> listViewItemList = new ArrayList<DentalItem>() ;


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_dental, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView title = (TextView) convertView.findViewById(R.id.title) ;
        TextView distance = (TextView) convertView.findViewById(R.id.distance) ;
        TextView address = (TextView) convertView.findViewById(R.id.address) ;
        TextView start = (TextView) convertView.findViewById(R.id.start) ;
        TextView end = (TextView) convertView.findViewById(R.id.end) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final DentalItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        title.setText(listViewItem.getTitle());
        distance.setText(listViewItem.getDistance()+"m");
        address.setText(listViewItem.getAddress());
        start.setText(listViewItem.getStart().substring(0,2) + ":"+listViewItem.getStart().substring(2,4));
        end.setText(listViewItem.getEnd().substring(0,2) + ":"+listViewItem.getEnd().substring(2,4));

        Button tel = (Button) convertView.findViewById(R.id.tel);
        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "tel:"+listViewItem.getTel();
                context.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(url)));
            }
        });
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String distance, String address, String start, String end, String tel) {
        DentalItem item = new DentalItem(title, distance, address, start, end, tel);
        listViewItemList.add(item);
    }
}
