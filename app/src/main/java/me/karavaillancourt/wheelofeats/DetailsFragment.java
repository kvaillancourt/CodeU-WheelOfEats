package me.karavaillancourt.wheelofeats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    private View view;
    private Resturant restaurant;
    private GIFView wheelGifContainer;
    private LinearLayout resultsView;
    private Resturant Restaurant;
    public static final String DETAILS_FRAGMENT_TAG = "DETAILS";
    private static final long ANIMATION_DELAY = 5000;


    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        resultsView = (LinearLayout) view.findViewById(R.id.results_view);
        wheelGifContainer = (GIFView) view.findViewById(R.id.loading_wheel_gif);
        showAnimation();

        setRestaurantDataInFragment();
        return view;
    }

    public void setRestaurantDataInFragment() {
       // Resturant resturant = makeSelection();
        MainActivity activity = ((MainActivity) getActivity());
        ResturantManager manager = activity.getManager();
        restaurant = manager.selectRandom();

        //Resturant resturant = makeSelection();
        //TODO: plug in real data
        wheelGifContainer.setVisibility(View.GONE);
        resultsView.setVisibility(View.VISIBLE);
        //String mRestaurantName = "Trendy Bistro";
        //String mRestaurantAddress = "4131 Brooklyn Ave NE";
        String mRestaurantDistance = String.format(getResources().getString(R.string.results_distance_to_restaurant), 5);
        String mRestaurantName = restaurant.getName();
        //String mRestaurantAddress = restaurant.get
        //String mRestaurantDistance = String.format(getResources().getString(R.string.results_distance_to_restaurant), 5);
        ((TextView) view.findViewById(R.id.restaurant_name)).setText(mRestaurantName);
        // ((TextView) view.findViewById(R.id.restaurant_address)).setText(mRestaurantAddress);
        // ((TextView) view.findViewById(R.id.restaurant_distance)).setText(mRestaurantDistance);

        view.findViewById(R.id.open_in_maps_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO plug in restaurant latitude and longitude
                openRestaurantLocationInMaps(restaurant);
            }

        });

        view.findViewById(R.id.spin_again_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimation();
                setRestaurantDataInFragment();
            }

        });

    }

    private void openRestaurantLocationInMaps(Resturant resturant) {
        String location = resturant.getLatitude() + "," + resturant.getLongitude() + "(" + resturant.getName() + ")";
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
        } else {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder
                    .setMessage(getString(R.string.no_maps_app_error_dialog_text))
                    .setTitle(getString(R.string.no_maps_app_error_dialog_title))
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();

        }
    }

    private void showAnimation() {
        resultsView.setVisibility(View.GONE);
        wheelGifContainer.setVisibility(View.VISIBLE);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRestaurantDataInFragment();
            }
        }, ANIMATION_DELAY);
    }
}
