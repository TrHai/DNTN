package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bruce.myapp.R;
public class HobbyAdapter extends BaseAdapter {
    private Context context;
    private String placeName[];
    private int logoImage[];
    private LayoutInflater inflater;
    public HobbyAdapter(Context context, String placeName[], int logoImage[]) {
        this.context = context;
        this.placeName = placeName;
        this.logoImage = logoImage;
    }

    @Override
    public int getCount() {
        return placeName.length;
    }

    @Override
    public Object getItem(int position) {
        return placeName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView=convertView;
        if(convertView==null)
        {
            inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.adater_history,null);
        }
        ImageView imgHobby=gridView.findViewById(R.id.imgHobby);
        TextView txtPlaceName=gridView.findViewById(R.id.txtPlaceName);
        imgHobby.setImageResource(logoImage[position]);
        txtPlaceName.setText(placeName[position]);

        return gridView;
    }
}
