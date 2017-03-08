package com.example.daxing.patriots;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomAdapterLegislators extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context context;
    static class ViewHolder {
        TextView tv_name;
        TextView tv_info;
        ImageView iv_avater;
    }

    public CustomAdapterLegislators(Context context, ArrayList listData) {
        this.context = context;
        this.listData = listData;
        Collections.sort(listData, new Comparator<LegiSchema.Legislator>() {
            @Override
            public int compare(LegiSchema.Legislator lhs, LegiSchema.Legislator rhs) {
                Log.i("TEST", "rhs" + rhs.last_name);
                Log.i("TEST", "lhs" + lhs.last_name);
                return (lhs.last_name).compareTo(rhs.last_name);
            }
        });
        layoutInflater = LayoutInflater.from(context);
    }

    public CustomAdapterLegislators(Context context, ArrayList listData, String from) {
        this.context = context;
        this.listData = listData;
        if (from.equals("bystate"))
        Collections.sort(listData, new Comparator<LegiSchema.Legislator>() {
            @Override
            public int compare(LegiSchema.Legislator lhs, LegiSchema.Legislator rhs) {
                return lhs.state_name.equals(rhs.state_name )? (lhs.last_name).compareTo(rhs.last_name) : (lhs.state_name).compareTo(rhs.state_name);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_legislators, null);
            holder = new ViewHolder();
            holder.iv_avater = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.tv_name = (TextView) convertView.findViewById(R.id.legi_name);
            holder.tv_info = (TextView) convertView.findViewById(R.id.legi_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LegiSchema.Legislator aLegislator = ( LegiSchema.Legislator) listData.get(position);
        holder.tv_name.setText(aLegislator.last_name + "," + aLegislator.first_name);
        if (aLegislator.district==null) {
            aLegislator.district="0";
        }
        holder.tv_info.setText("(" + aLegislator.party + ")" +  aLegislator.state_name + " - District " + aLegislator.district);

        if (holder.iv_avater != null) {
            String avater_url = "https://theunitedstates.io/images/congress/225x275/" + aLegislator.bioguide_id + ".jpg";
//            new ImageDownloaderTask(holder.iv_avater).execute(avater_url);
            Picasso
                    .with(context)
                    .load(avater_url)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.iv_avater);
        }

        return convertView;
    }

}
