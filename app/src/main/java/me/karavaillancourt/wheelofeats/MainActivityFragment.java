package me.karavaillancourt.wheelofeats;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment { //implements ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayAdapter<String> mResturantAdapter;
    private ResturantManager manager;

    public MainActivityFragment() {
        manager = new ResturantManager();
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
    @TargetApi(4)
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchResturantTask resturantTask = new FetchResturantTask(getActivity(), manager, (MainActivity) getActivity());
            resturantTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(4)
    private void updateResturants() {
        FetchResturantTask resturantTask = new FetchResturantTask(getActivity(), manager, (MainActivity) getActivity());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        resturantTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateResturants();
    }

    //todo kara: this mResturantAdapter can go because we don't use it anymore :P will fix soon
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mResturantAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_resturants,
                R.id.list_item_resturant_textview,
                new ArrayList<String>());
        ListView listView = (ListView) rootView.findViewById(
                R.id.list_view_resturant
        );
        listView.setAdapter(mResturantAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                displayMap(manager.select(position));
                String forecast = mResturantAdapter.getItem(position);
                Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }

    @TargetApi(4)
    public void displayMap(Resturant resturant) {
        String location = resturant.getLatitude() + "," + resturant.getLongitude() + "(" + resturant.getName() + ")";
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}