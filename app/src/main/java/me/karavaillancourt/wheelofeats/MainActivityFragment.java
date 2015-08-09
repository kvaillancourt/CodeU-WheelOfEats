package me.karavaillancourt.wheelofeats;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment { //implements ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayAdapter<String> mResturantAdapter;
    // private GoogleApiClient mGoogleApiClient;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //      buildGoogleApiClient();
    }

//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resturantfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchResturantTask resturantTask = new FetchResturantTask();
            resturantTask.execute("lat/long string to change for later");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] fakeResturants = {
                "Chubby's BBQ",
                "La Hacienda Mexican Food",
                "Texas Roadhouse",
                "Gas Lamp Strip Club",
                "The Beautiful South",
                "Golden Banana",
                "Freaky Fridays",
                "Logo Lounge",
                "Pink Tacos",
                "Blue Waffles",
                "Spoons",
                "Cheetaz",
                "Waffle House",
                "IHOP",
                "Cancun"
        };

        List<String> listResturants = new ArrayList<String>(Arrays.asList(fakeResturants));

        mResturantAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_resturants,
                R.id.list_item_resturant_textview,
                listResturants);
        ListView listView = (ListView) rootView.findViewById(
                R.id.list_view_resturant
        );
        listView.setAdapter(mResturantAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                displayMap();
//                        String forecast = mResturantAdapter.getItem(position);
//                        Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }

    public void displayMap() {//(Resturant resturant) {
//        String location = latitutde + longitude + "(" + name + ")";
//        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
//                                .appendQueryParameter("q", location)
//                                .build();
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=latitude,longitude(label)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public class FetchResturantTask extends AsyncTask<String, Void, Resturant[]> {
        private final String LOG_TAG = FetchResturantTask.class.getSimpleName();

        @Override
        protected Resturant[] doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String resturantJsonStr = null;
            String location = "32.873864,-117.217262";
            int radius = 500;
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
                //String startingurl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&key=";

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
                //todo: SUpER JAnKy!!! MUST FiX
                return getResturantDataFromJson(resturantJsonStr, radius);
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


        //Done! future kara, please change this to reflect the data you are recieving from
        // the google places api instead of the weateher api...
        // Lesson 2: Json parsing

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Resturant[] getResturantDataFromJson(String placesJsonStr, int numRestuants)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_RESULTS = "results";
            final String OWM_NAME = "name";
            final String OWM_ID = "id";

            JSONObject placesJson = new JSONObject(placesJsonStr);
            JSONArray placeArray = placesJson.getJSONArray(OWM_RESULTS);


//            Time dayTime = new Time();
//            dayTime.setToNow();

            // we start at the day returned by local time. Otherwise this is a mess.
//            int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

            // now we work exclusively in UTC
//            dayTime = new Time();

            Resturant[] resultStrs = new Resturant[numRestuants];
            for (int i = 0; i < placeArray.length(); i++) {
                String name;
                String id;

                // Get the JSON object representing the restuant
                JSONObject resturantJSON = placeArray.getJSONObject(i);

//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime;
//                // Cheating to convert this to UTC time, which is what we want anyhow
//                dateTime = dayTime.setJulianDay(julianStartDay + i);
//                String day = getReadableDateString(dateTime);

                name = resturantJSON.getString(OWM_NAME);
                id = resturantJSON.getString(OWM_ID);
                Resturant resturant = new Resturant(name, id);
                resultStrs[i] = resturant; //+ "-" + id;
            }

            for (Resturant s : resultStrs) {
                Log.v(LOG_TAG, "Resturant entry: " + s);
            }
            return resultStrs;

        }

        @Override
        protected void onPostExecute(Resturant[] result) {
            if (result != null) {
                mResturantAdapter.clear();
                for (Resturant resturantStr : result) {
                    if (resturantStr != null) {
                        mResturantAdapter.add(resturantStr.name);
                    }
                }
            }
        }
    }
}
