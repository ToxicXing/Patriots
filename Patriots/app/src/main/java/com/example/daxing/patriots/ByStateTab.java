package com.example.daxing.patriots;

import android.content.Intent;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ByStateTab extends Fragment {
    private final String TAG = "bystatetab";

    private View view;
    private ListView ls_bystate;
    private LegiSchema legiSchema;
    AsyncHttpClient async_bystate_list = new AsyncHttpClient();
    Map<String, Integer> mapIndex;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        String url = "http://sample-env.gm2pyryj2q.us-west-1.elasticbeanstalk.com/backend.php?scope=state";
        String url = "http://104.198.0.197:8080/legislators?per_page=all";
        view = inflater.inflate(R.layout.by_state_tab, container, false);
        async_bystate_list.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                legiSchema = gson.fromJson(responseString, LegiSchema.class);
                Log.i(TAG, "Json got");

                CustomAdapterLegislators adapter = new CustomAdapterLegislators(getActivity(), legiSchema.results, "bystate");

                ls_bystate.setAdapter(adapter);
                ls_bystate.setEnabled(true);
                Log.i(TAG, "Adapter setEnabled");


                mapIndex = new LinkedHashMap<String, Integer>();
                for (int i = 0; i < legiSchema.results.size(); i++) {
                    LegiSchema.Legislator aLegislator = (LegiSchema.Legislator) legiSchema.results.get(i);
                    String state_name = aLegislator.state_name;
                    String index = state_name.substring(0, 1);

                    if (mapIndex.get(index) == null)
                        mapIndex.put(index, i);
                }

                LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "cannot obtain legislators information");
            }
        });

        ls_bystate = (ListView) view.findViewById(R.id.ls_legi_bystate);
        Log.i(TAG, "listview set");
        ls_bystate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onLegislatorClicked(adapterView, view, i, l);
            }
        });

        return view;
    }


    public void onLegislatorClicked(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "selected legislator is the " + i + "legislator");
        String bioguide_id = legiSchema.results.get(i).bioguide_id;
        Intent intent = new Intent(getActivity(), LegislatorDetails.class);
        intent.putExtra("bioguide_id", bioguide_id);
        startActivity(intent);
    }


}