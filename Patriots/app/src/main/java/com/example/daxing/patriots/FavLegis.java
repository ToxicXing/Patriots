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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavLegis extends Fragment {
    private final String TAG = "FavoriteLegis";
    private  View view;
    private ListView ls_bystate;
    SharedPreferences sharedPrefs;
    Map<String,?> all;
    ArrayList<LegiSchema.Legislator> listData = new ArrayList<LegiSchema.Legislator>();
    Map<String, Integer> mapIndex;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.by_state_tab, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPrefs = getActivity().getSharedPreferences("favorite_legis", MODE_PRIVATE);
        Map<String,?> all = sharedPrefs.getAll();
        Gson gson = new GsonBuilder().create();
        listData.clear();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            listData.add(gson.fromJson(entry.getValue().toString(),LegiSchema.Legislator.class));
        }

        Log.i(TAG, "the size of the listData is " + listData.size());

        ls_bystate = (ListView) view.findViewById(R.id.ls_legi_bystate);
        Log.i(TAG, "listview set");
        ls_bystate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onLegislatorClicked(adapterView, view, i, l);
            }
        });

        CustomAdapterLegislators adapter = new CustomAdapterLegislators(getActivity(), listData);
        ls_bystate.setAdapter(adapter);
        ls_bystate.setEnabled(true);

        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < listData.size(); i++) {
            LegiSchema.Legislator aLegislator = (LegiSchema.Legislator) listData.get(i);
            String state_name = aLegislator.last_name;
            String index = state_name.substring(0, 1);
            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }

        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index);
        indexLayout.removeAllViews();

        TextView tv_index;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            tv_index = (TextView) getActivity().getLayoutInflater().inflate(R.layout.side_index_item, null);
            tv_index.setText(index);
            tv_index.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                    TextView selectedIndex = (TextView) vv;
                    ls_bystate.setSelection(mapIndex.get(selectedIndex.getText()));
                }
            });
            indexLayout.addView(tv_index);
        }
    }


    public void onLegislatorClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected legislator is the " + i + "legislator");
        String bioguide_id = listData.get(i).bioguide_id;
        Intent intent = new Intent(getActivity(), LegislatorDetails.class);
        intent.putExtra("bioguide_id", bioguide_id);
        startActivity(intent);
    }
}
