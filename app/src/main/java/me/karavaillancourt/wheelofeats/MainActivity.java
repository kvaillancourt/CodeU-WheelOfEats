package me.karavaillancourt.wheelofeats;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String LOG_TAG = MainActivity.class.getSimpleName();
    private ResturantManager mManager;
    public static final String APIKEY = "AIzaSyB7Eo2EnKGLq7qd71ytgj64GZgMeE1NHeM";

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public String mLatitudeText = "32.873864";
    public String mLongitudeText = "-117.217262";
    private ResturantManager manager;
    private int radius;
    private Handler mHandler;
    private DetailsFragment mDetailsFragment;
    private int ANIMATION_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, new MainActivityFragment(), MainActivityFragment.MAIN_FRAGMENT_TAG).commit();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        manager = new ResturantManager();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                launchDetailFragment();
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(LOG_TAG, "called onConnect");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            mLatitudeText = (String.valueOf(mLastLocation.getLatitude()));
            Log.v(LOG_TAG, mLatitudeText);

            mLongitudeText = (String.valueOf(mLastLocation.getLongitude()));
            Log.v(LOG_TAG, mLongitudeText);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DetailsFragment.DETAILS_FRAGMENT_TAG);
        outState.putInt("RADIUS", radius);
        if (detailsFragment != null && detailsFragment.getRestaurant() != null) {
            Resturant selectedRestaurant = detailsFragment.getRestaurant();
            outState.putString("NAME", selectedRestaurant.getName());
            outState.putString("ID", selectedRestaurant.getId());
            outState.putDouble("LATITUDE", selectedRestaurant.getLatitude());
            outState.putDouble("LONGITUDE", selectedRestaurant.getLongitude());
            outState.putString("ICON", selectedRestaurant.getIcon());
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        radius = savedInstanceState.getInt("RADIUS");
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag(DetailsFragment.DETAILS_FRAGMENT_TAG);
        if (detailsFragment != null) {
            detailsFragment.setRestaurant(new Resturant(savedInstanceState.getString("NAME"), savedInstanceState.getString("ID"),
                    savedInstanceState.getDouble("LATITUDE"), savedInstanceState.getDouble("LONGITUDE"), savedInstanceState.getString("ICON")));
            detailsFragment.setRestaurantDataInFragment();
        }
    }

    public void launchFetchResturantTask() {
        //todo make sure this doens't break anything
        Context context = getApplicationContext();
        EditText radiusText = (EditText) findViewById(R.id.radius_distance);
        String radius = radiusText.getText().toString();

        if (radius.length() > 0 && isNumeric(radius)) {
            setRadius();
            FetchResturantTask fetchResturantTask = new FetchResturantTask(this);
            fetchResturantTask.execute();
        } else {
            CharSequence text = "Radius input is invalid. Please try again.";
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }


    }

    private void setRadius() {
        EditText radiusText = (EditText) findViewById(R.id.radius_distance);
        String radiusMiles = radiusText.getText().toString();
        int radiusMilesInt = Integer.parseInt(radiusMiles);
        radius = radiusMilesInt * 1609;
    }

    public void launchAnimationFragment() {
        launchFetchResturantTask();
        mDetailsFragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
                .replace(R.id.activity_main, new WheelAnimationFragment(), WheelAnimationFragment.ANIMATION_FRAGMENT_TAG)
                .addToBackStack(MainActivityFragment.MAIN_FRAGMENT_TAG).commit();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                launchDetailFragment();
            }
        }, ANIMATION_DELAY);

        // getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, new DetailsFragment(), DetailsFragment.DETAILS_FRAGMENT_TAG).commit();
    }

    public void launchDetailFragment() {
        if (mDetailsFragment == null) {
            mDetailsFragment = new DetailsFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
                .replace(R.id.activity_main, mDetailsFragment, DetailsFragment.DETAILS_FRAGMENT_TAG)
                .addToBackStack(WheelAnimationFragment.ANIMATION_FRAGMENT_TAG).commit();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void addRestaurantDataToFragment() {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentByTag(DetailsFragment.DETAILS_FRAGMENT_TAG);
        if (detailsFragment != null) {
            detailsFragment.setRestaurant(manager.selectRandom());
            detailsFragment.setRestaurantDataInFragment();
        }
    }

    public ResturantManager getManager() {
        return manager;
    }
}
