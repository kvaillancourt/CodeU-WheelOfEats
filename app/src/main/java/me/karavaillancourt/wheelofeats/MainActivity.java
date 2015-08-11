package me.karavaillancourt.wheelofeats;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
//    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildGoogleApiClient();
    }

    //    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(LocationServices.API)
//                .build();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        launchResultsFragment();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = (String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText = (String.valueOf(mLastLocation.getLongitude()));
        }
    }

    public void launchResultsFragment() {
        //TODO need to find a better way to transition between activity main and results fragment...maybe put stuff in activity_main in a fragment?
        FragmentResults resultsFragment = new FragmentResults();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main, resultsFragment, FragmentResults.RESULTS_FRAGMENT_TAG).commit();
    }

    public String getmLatitudeText() {
        return mLatitudeText;
    }

    public String getmLongitudeText() {
        return mLongitudeText;
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
        Intent intent = new Intent(this, FragmentResults.class);
        EditText postalText = (EditText) findViewById(R.id.postal_code);
        EditText radiusText = (EditText) findViewById(R.id.radius_distance);

        String postalCode = postalText.getText().toString();
        String radius = radiusText.getText().toString();

        intent.putExtra("postalKey", postalCode);
        intent.putExtra("radiusKey", radius);

        startActivity(intent);
    }
}
