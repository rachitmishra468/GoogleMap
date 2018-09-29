package com.media_mosaic.httpwww.doubloons.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.media_mosaic.httpwww.doubloons.Data_Model.Invite_Model;
import com.media_mosaic.httpwww.doubloons.R;

import java.util.ArrayList;

/**
 * Created by Rachit on 2/15/2018.
 */

public class Invite_CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Invite_Model> itemModelList;
    public Invite_CustomAdapter(Context context, ArrayList<Invite_Model> modelList) {
        this.context = context;
        this.itemModelList = modelList;
    }
    @Override
    public int getCount() {
        return itemModelList.size();
    }
    @Override
    public Object getItem(int position) {
        return itemModelList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.invite_item, null);
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            ImageView imgRemove = (ImageView) convertView.findViewById(R.id.imgRemove);
            Invite_Model m = itemModelList.get(position);
            tvName.setText(m.getName());
            imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemModelList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}