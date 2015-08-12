package me.karavaillancourt.wheelofeats;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kvaillancourt on 8/11/15.
 */
public class DetailTask extends AsyncTask<Resturant, Void, Void> {

    private static final String LOG_TAG = DetailTask.class.getSimpleName();
    private DetailsFragment fragment;

    public DetailTask(DetailsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Resturant... params) {
        return null;
    }

    private void getSingleResturantDataFromJson(String resturantJsonStr, Resturant resturant) throws JSONException {

// These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "result";
        final String OWM_ADDRESS = "formatted_address";
        final String OWM_OPENING_HOURS = "opening_hours";
        final String OWM_OPEN_NOW = "open_now";

        JSONObject placesJson = new JSONObject(resturantJsonStr);
        JSONObject parsedGoogle = placesJson.getJSONObject(OWM_RESULTS);

        JSONObject open_hours = parsedGoogle.getJSONObject(OWM_OPENING_HOURS);
        boolean open = open_hours.getBoolean(OWM_OPEN_NOW);

        String address = parsedGoogle.getString(OWM_ADDRESS);

        resturant.setOpen(open);
        resturant.setAddress(address);

    }

    private Void getOneResturantJSON(Resturant resturant) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String place_id = resturant.getId();
        // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=API_KEY

        // Will contain the raw JSON response as a string.
        String resturantJsonStr = null;

        String APIKey = MainActivity.APIKEY;

        try {

            final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
            final String PLACE_ID_PARAM = "placeid";

            final String API_KEY_PARAM = "key";

            Uri builtUri = Uri.parse(PLACES_BASE_URL).buildUpon()
                    .appendQueryParameter(PLACE_ID_PARAM, place_id)
                    .appendQueryParameter(API_KEY_PARAM, APIKey)
                    .build();

            URL url = new URL(builtUri.toString());
            //URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyDMY5l8HtWPiV4CtCmMIZK-NkQDXTa23DY");
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

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
            getSingleResturantDataFromJson(resturantJsonStr, resturant);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void V) {

        fragment.setRestaurantDataInFragment();

    }

}
