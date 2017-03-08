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

public class NewBillsTab extends Fragment {

    private final String TAG = "NewBillsTab";
    private View view;
    private ListView lv_new_bills;
    BillSchema billSchema = new BillSchema();
    AsyncHttpClient async_new_bills_list = new AsyncHttpClient();

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.new_bills_tab, container, false);
        lv_new_bills = (ListView) view.findViewById(R.id.lv_new_bills);
        lv_new_bills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onBillClicked(adapterView, view, i, l);
            }
        });
        String url ="http://104.198.0.197:8080/bills?history.active=false&per_page=50";
        async_new_bills_list.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                billSchema = gson.fromJson(responseString, BillSchema.class);
                Log.i(TAG, "Json got");

                CustomAdapterBills adapter = new CustomAdapterBills(getActivity(), billSchema.results);
                lv_new_bills.setAdapter(adapter);
                lv_new_bills.setEnabled(true);
                Log.i(TAG, "Adapter setEnabled");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "cannot obtain bills information");
            }
        });

        return view;
    }

    public void onBillClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected bill is the " + i + "bill");
        String bill_id = billSchema.results.get(i).bill_id;
        Intent intent = new Intent(getActivity(), BillDetails.class);
        intent.putExtra("bill_id", bill_id);
        intent.putExtra("bill_status", "new");
        startActivity(intent);
    }
}
