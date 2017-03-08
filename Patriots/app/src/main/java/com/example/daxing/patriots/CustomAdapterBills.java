package com.example.daxing.patriots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class CustomAdapterBills extends BaseAdapter {
    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context context;
    static class ViewHolder {
        TextView tv_id;
        TextView tv_title;
        TextView tv_introduced_on;
    }

    public CustomAdapterBills(Context context, ArrayList listData) {
        this.context = context;
        this.listData = listData;
        Collections.sort(listData, new Comparator<BillSchema.Bill>() {
            @Override
            public int compare(BillSchema.Bill lhs, BillSchema.Bill rhs) {
                return (rhs.introduced_on).compareTo(lhs.introduced_on);
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
        CustomAdapterBills.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_bills, null);
            holder = new CustomAdapterBills.ViewHolder();

            holder.tv_id = (TextView) convertView.findViewById(R.id.bill_id);
            holder.tv_title = (TextView) convertView.findViewById(R.id.bill_title);
            holder.tv_introduced_on = (TextView) convertView.findViewById(R.id.bill_introduced_on);
            convertView.setTag(holder);
        } else {
            holder = (CustomAdapterBills.ViewHolder) convertView.getTag();
        }

        BillSchema.Bill aBill = (BillSchema.Bill) listData.get(position);
        holder.tv_id.setText(aBill.bill_id.toUpperCase());
        if (aBill.short_title == null) {
            holder.tv_title.setText(aBill.official_title);
        } else {
            holder.tv_title.setText(aBill.short_title);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date introduced_on_date = formatter.parse(aBill.introduced_on);
            holder.tv_introduced_on.setText(new SimpleDateFormat("MMM dd, yyyy").format(introduced_on_date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
