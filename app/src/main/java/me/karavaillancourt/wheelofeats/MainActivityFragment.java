package me.karavaillancourt.wheelofeats;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment { //implements ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayAdapter<String> mResturantAdapter;
    public final static String MAIN_FRAGMENT_TAG = "MAIN";
    //    private Resturant[] masterList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resturantfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchResturantTask resturantTask = new FetchResturantTask((MainActivity) getActivity());
            resturantTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateResturants() {
        FetchResturantTask resturantTask = new FetchResturantTask((MainActivity) getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        resturantTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        //updateResturants();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).launchFetchResturantTask();
            }
        });

//        mResturantAdapter = new ArrayAdapter<String>(
//                getActivity(),
//                R.layout.list_item_resturants,
//                R.id.list_item_resturant_textview,
//                new ArrayList<String>());
//        ListView listView = (ListView) rootView.findViewById(
//                R.id.list_view_resturant
//        );
//        listView.setAdapter(mResturantAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                displayMap(manager.select(position));
//                String forecast = mResturantAdapter.getItem(position);
//                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;

    }


}


