package com.example.baseadapter.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baseadapter.R;
import com.example.baseadapter.bean.Planet;

import java.util.List;

public class PlanetBaseAdapter extends BaseAdapter {
    private Context mContext;
    private List<Planet> mPlanetList;

    public PlanetBaseAdapter(Context mContext, List<Planet> mPlanetList) {
        this.mContext = mContext;
        this.mPlanetList = mPlanetList;
    }

    @Override
    public int getCount() {
        return mPlanetList.size();
    }

    @Override
    public Object getItem(int i) {
        return mPlanetList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_list, null);
        ImageView iv_planet=view.findViewById(R.id.iv_planet);
        TextView tv_name=view.findViewById(R.id.tv_name);
        TextView tv_desc=view.findViewById(R.id.tv_desc);

        Planet planet=mPlanetList.get(i);
        iv_planet.setImageResource(planet.image);
        tv_name.setText(planet.name);
        tv_desc.setText(planet.desc);
        return view;
    }
}
