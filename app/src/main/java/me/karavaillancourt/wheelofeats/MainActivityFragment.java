package me.karavaillancourt.wheelofeats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment { //implements ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayAdapter<String> mResturantAdapter;
    public final static String MAIN_FRAGMENT_TAG = "MAIN";
    //    private Resturant[] masterList;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchResturantTask resturantTask = new FetchResturantTask(getActivity(), mResturantAdapter);
            resturantTask.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateResturants() {
        FetchResturantTask resturantTask = new FetchResturantTask(getActivity(), mResturantAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        resturantTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateResturants();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).launchDetailFragment();
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

    public void displayMap(Resturant resturant) {
        String location = resturant.getLatitude() + "," + resturant.getLongitude() + "(" + resturant.getName() + ")";
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        //Uri geoLocation = Uri.parse("geo:0,0?q=latitude,longitude(label)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoLocation);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


    public class FetchResturantTask extends AsyncTask<String, Void, Void> {
        private final String LOG_TAG = FetchResturantTask.class.getSimpleName();

        private ArrayAdapter<String> mResturantAdapter;
        private final Context mContext;

        public FetchResturantTask(Context context, ArrayAdapter<String> forecastAdapter) {
            mContext = context;
            mResturantAdapter = forecastAdapter;
        }
        @Override
        protected Void doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resturantJsonStr = null;
            String location = ((MainActivity) getActivity()).mLatitudeText + "," + ((MainActivity) getActivity()).mLongitudeText; //"32.873864,-117.217262";//
            Log.v(LOG_TAG, location);
            int radius = 5000;
            String types = "restaurant";
            String APIKey = "AIzaSyDMY5l8HtWPiV4CtCmMIZK-NkQDXTa23DY";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
                final String LOCATION_PARAM = "location";
                final String RADIUS_PARAM = "radius";
                final String TYPES_PARAM = "types";
                final String API_KEY_PARAM = "key";

                Uri builtUri = Uri.parse(PLACES_BASE_URL).buildUpon()
                        .appendQueryParameter(LOCATION_PARAM, location)
                        .appendQueryParameter(RADIUS_PARAM, Integer.toString(radius))
                        .appendQueryParameter(TYPES_PARAM, types)
                        .appendQueryParameter(API_KEY_PARAM, APIKey)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                //URL url = new URL(startingurl + APIKey);

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                resturantJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Resturant JSON String: " + resturantJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                getResturantDataFromJson(resturantJsonStr);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        /* The date/time conversion code is going to be moved outside the asynctask later,
 * so for convenience we're breaking it out into its own method now.
 */
        private String getReadableDateString(long time) {
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private void getResturantDataFromJson(String placesJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_NAME = "name";
            final String OWM_ID = "id";
            final String OWM_lat = "lat";
            final String OWM_long = "lng";
            final String OWM_open_now = "open_now";
            final String OWM_opening_hours = "opening_hours";
            final String OWM_geometry = "geometry";
            final String OWM_location = "location";

            JSONObject placesJson = new JSONObject(placesJsonStr);
            JSONArray placeArray = placesJson.getJSONArray(OWM_RESULTS);


            Resturant[] resultStrs = new Resturant[placeArray.length()];
            for (int i = 0; i < placeArray.length(); i++) {
                String name;
                String id;
                Double latitude;
                Double longitude;
                boolean open;
                // Get the JSON object representing the restuant
                JSONObject resturantJSON = placeArray.getJSONObject(i);

                name = resturantJSON.getString(OWM_NAME);
                id = resturantJSON.getString(OWM_ID);


                JSONObject geometry = resturantJSON.getJSONObject(OWM_geometry);
                JSONObject location = geometry.getJSONObject(OWM_location);
                latitude = location.getDouble(OWM_lat);
                longitude = location.getDouble(OWM_long);

                //TODO: so the opening_hours sometimes isn't there.. what to do??

                //JSONObject opening_hours = resturantJSON.getJSONObject(OWM_opening_hours);
                open = true; //opening_hours.getBoolean(OWM_open_now);

                Resturant resturant = new Resturant(name, id, latitude, longitude, open);

                resultStrs[i] = resturant; //+ "-" + id;

                manager.setMasterList(resultStrs);
                //masterList = resultStrs;

            }

            for (Resturant resturant : resultStrs) {
                Log.v(LOG_TAG, resturant.getName() + " " + resturant.getId() + " "
                        + resturant.getLatitude() + " " + resturant.getLongitude() + " "
                        + resturant.getOpen());
            }
            manager.setMasterList(resultStrs);

        }

        private void setRestaurantDataInFragment(View view) {

            // Resturant resturant = makeSelection();
            //TODO: plug in real data
            String mRestaurantName = "Trendy Bistro";
            String mRestaurantAddress = "4131 Brooklyn Ave NE";
            String mRestaurantDistance = String.format(getResources().getString(R.string.results_distance_to_restaurant), 5);
            ((TextView) view.findViewById(R.id.restaurant_name)).setText(mRestaurantName);
            ((TextView) view.findViewById(R.id.restaurant_address)).setText(mRestaurantAddress);
            ((TextView) view.findViewById(R.id.restaurant_distance)).setText(mRestaurantDistance);

//        view.findViewById(R.id.open_in_maps_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO plug in restaurant latitude and longitude
//                openRestaurantLocationInMaps(0, 0);
//            }
//        });

            view.findViewById(R.id.spin_again_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: implement reselecting restaurant
                    manager.selectRandom();
                }
            });

        }
        //todo: we dont need this resturantadapter anymore because we don't care about the list view
        //Override
        protected void onPostExecute() {
            //todo: kara figure out how to make this view thing dipslay so i can debug stuff
            //     setRestaurantDataInFragment();
//            if (result != null) {
//                mResturantAdapter.clear();
//                for (Resturant resturantStr : result) {
//                    if (resturantStr != null) {
//                        mResturantAdapter.add(resturantStr.getName());
//                    }
//                }
//            }
        }
    }
}
