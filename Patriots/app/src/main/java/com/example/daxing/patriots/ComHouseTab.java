package com.example.daxing.patriots;


import android.content.Intent;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ComHouseTab extends Fragment {

    private final String TAG = "ComHouseTab";
    private View view;
    private ListView lv_com_house;
    CommSchema commSchema = new CommSchema();
    AsyncHttpClient async_com_house_list = new AsyncHttpClient();

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.com_house_tab, container, false);
        lv_com_house = (ListView) view.findViewById(R.id.lv_com_hosue);
        lv_com_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onCommitteeClicked(adapterView, view, i, l);
            }
        });

        String url = "http://104.198.0.197:8080/committees?chamber=house&per_page=all";
        async_com_house_list.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                commSchema = gson.fromJson(responseString, CommSchema.class);
                Log.i(TAG, "Json got");

                CustomAdapterCommittees adapter = new CustomAdapterCommittees(getActivity(), commSchema.results);

                lv_com_house.setAdapter(adapter);
                lv_com_house.setEnabled(true);
                Log.i(TAG, "Adapter setEnabled");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "cannot obtain legislators information");
            }
        });
        return view;
    }

    public void onCommitteeClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected committee is the " + i + " committee");
        String comm_id = commSchema.results.get(i).committee_id;
        Intent intent = new Intent(getActivity(), CommitteeDetails.class);
        intent.putExtra("committee_id", comm_id);
        startActivity(intent);
    }

}
