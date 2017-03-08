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

public class FavBills extends Fragment {
    private final String TAG = "FavoriteBills";
    private  View view;
    private ListView ls_bills;
    SharedPreferences sharedPrefs;
    Map<String,?> all;
    ArrayList<BillSchema.Bill> listData = new ArrayList<BillSchema.Bill>();
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.active_bills_tab, container, false);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        sharedPrefs = getActivity().getSharedPreferences("favorite_bills", MODE_PRIVATE);
        Map<String,?> all = sharedPrefs.getAll();
        Gson gson = new GsonBuilder().create();
        listData.clear();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            listData.add(gson.fromJson(entry.getValue().toString(),BillSchema.Bill.class));
        }

        Log.i(TAG, "the size of the listData is " + listData.size());

        ls_bills = (ListView) view.findViewById(R.id.lv_active_bills);
        Log.i(TAG, "listview set");
        ls_bills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onBillClicked(adapterView, view, i, l);
            }
        });

        CustomAdapterBills adapter = new CustomAdapterBills(getActivity(), listData);
        ls_bills.setAdapter(adapter);
        ls_bills.setEnabled(true);
    }


    public void onBillClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected bill is the " + i + "bill");
        String bill_id = listData.get(i).bill_id;
        Intent intent = new Intent(getActivity(), BillDetails.class);
        intent.putExtra("bill_id", bill_id);
        intent.putExtra("bill_status", "active");
        startActivity(intent);
    }
}
