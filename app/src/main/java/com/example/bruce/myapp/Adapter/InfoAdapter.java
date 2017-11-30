package com.example.bruce.myapp.Adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bruce.myapp.Data.Information;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by BRUCE on 5/22/2017.
 */

public class InfoAdapter extends ArrayAdapter<Information> {
    @NonNull
    Activity context;
    @LayoutRes
    int resource;
    @NonNull
    List<Information> objects;
    public InfoAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Information> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);

        ImageView imageView = row.findViewById(R.id.infoImg);
        TextView txtInfo= row.findViewById(R.id.infotxt);
        final ProgressBar progressBar = row.findViewById(R.id.download);
        Information menu=this.objects.get(position);

        Picasso.with(getContext()).load(menu.Image).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        txtInfo.setText(menu.Info);
        return row;
    }
}
