package me.karavaillancourt.wheelofeats;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String LOG_TAG = MainActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public String mLatitudeText = "32.873864";
    public String mLongitudeText = "-117.217262";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, new MainActivityFragment(), MainActivityFragment.MAIN_FRAGMENT_TAG).commit();
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

    public void launchDetailFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main, new DetailsFragment(), DetailsFragment.DETAILS_FRAGMENT_TAG).commit();

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

    public void submitRequest(View view){
        Intent intent = new Intent(this, DetailsActivity.class);
        EditText postalText = (EditText) findViewById(R.id.postal_code);
        EditText radiusText = (EditText) findViewById(R.id.radius_distance);

        String postalCode = postalText.getText().toString();
        String radius = radiusText.getText().toString();

        intent.putExtra("postalKey", postalCode);
        intent.putExtra("radiusKey", radius);

        startActivity(intent);
    }
}
