package me.karavaillancourt.wheelofeats;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

@TargetApi(4)
public class FetchResturantTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchResturantTask.class.getSimpleName();
    private MainActivity mActivity;
    int radius;
    private Context mContext;

    private ResturantManager mManager;

    public FetchResturantTask(Context context, ResturantManager manager, MainActivity activity) {

        this.mActivity = activity;
        this.mContext = context;
        mManager = manager;
        EditText radiusText = (EditText) mActivity.findViewById(R.id.radius_distance);
        String radiusMiles = radiusText.getText().toString();
        Integer radiusMilesInt = Integer.parseInt(radiusMiles);
        radius = radiusMilesInt * 1609;

    }

    @Override
    protected Void doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String resturantJsonStr = null;
        String location = mActivity.mLatitudeText + "," + mActivity.mLongitudeText; //"32.873864,-117.217262";//
        Log.v(LOG_TAG, location);

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

            mManager.setMasterList(resultStrs);
            //masterList = resultStrs;

        }

        for (Resturant resturant : resultStrs) {
            Log.v(LOG_TAG, resturant.getName() + " " + resturant.getId() + " "
                    + resturant.getLatitude() + " " + resturant.getLongitude() + " "
                    + resturant.getOpen());
        }
        mManager.setMasterList(resultStrs);

    }


    //todo: we dont need this resturantadapter anymore because we don't care about the list view
    @Override
    protected void onPostExecute(Void V) {
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