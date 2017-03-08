package com.example.daxing.patriots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomAdapterCommittees extends BaseAdapter {
    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context context;

    static class ViewHolder {
        TextView tv_com_name;
        TextView tv_com_chamber;
        TextView tv_com_id;
    }

    public CustomAdapterCommittees(Context context, ArrayList listData) {
        this.context = context;
        this.listData = listData;
        Collections.sort(listData, new Comparator<CommSchema.Committee>() {
            @Override
            public int compare(CommSchema.Committee lhs, CommSchema.Committee rhs) {
                return (lhs.name).compareTo(rhs.name);
            }
        });
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CustomAdapterCommittees.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_committees, null);
            holder = new CustomAdapterCommittees.ViewHolder();

            holder.tv_com_id = (TextView) convertView.findViewById(R.id.com_id);
            holder.tv_com_name = (TextView) convertView.findViewById(R.id.com_name);
            holder.tv_com_chamber= (TextView) convertView.findViewById(R.id.com_chamber);
            convertView.setTag(holder);
        } else {
            holder = (CustomAdapterCommittees.ViewHolder) convertView.getTag();
        }

        CommSchema.Committee aComm = (CommSchema.Committee) listData.get(position);
        holder.tv_com_id.setText(aComm.committee_id);
        holder.tv_com_name.setText(aComm.name);
        if (aComm.chamber == "house") {
            holder.tv_com_chamber.setText("House");
        } else {
            holder.tv_com_chamber.setText("Senate");
        }

        return convertView;
    }
}
