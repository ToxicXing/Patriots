package com.example.daxing.patriots;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FavComms extends Fragment {
    private final String TAG = "FavoriteCommittees";
    private  View view;
    private ListView ls_comms;
    SharedPreferences sharedPrefs;
    Map<String,?> all;
    ArrayList<CommSchema.Committee> listData = new ArrayList<CommSchema.Committee>();
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.com_house_tab, container, false);
        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG,"onResume");
        super.onResume();
        sharedPrefs = getActivity().getSharedPreferences("favorite_committees", MODE_PRIVATE);
        Map<String,?> all = sharedPrefs.getAll();
        Gson gson = new GsonBuilder().create();
        listData.clear();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            listData.add(gson.fromJson(entry.getValue().toString(),CommSchema.Committee.class));
        }

        Log.i(TAG, "the size of the listData is " + listData.size());

        ls_comms = (ListView) view.findViewById(R.id.lv_com_hosue);
        Log.i(TAG, "listview set");
        ls_comms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCommitteeClicked(adapterView, view, i, l);
            }
        });

        CustomAdapterCommittees adapter = new CustomAdapterCommittees(getActivity(), listData);
        ls_comms.setAdapter(adapter);
        ls_comms.setEnabled(true);
    }

    public void onCommitteeClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected committee is the " + i + " committee");
        String comm_id = listData.get(i).committee_id;
        Intent intent = new Intent(getActivity(), CommitteeDetails.class);
        intent.putExtra("committee_id", comm_id);
        startActivity(intent);
    }
}
