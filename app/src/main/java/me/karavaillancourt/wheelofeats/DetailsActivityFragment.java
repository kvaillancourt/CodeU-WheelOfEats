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
import android.widget.TextView;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private View view;
    private Resturant Restaurant;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        showAnimation();
        setRestaurantDataInFragment();
        return view;
    }

    private void setRestaurantDataInFragment() {

       // Resturant resturant = makeSelection();
        //TODO: plug in real data
        String mRestaurantName = "Trendy Bistro";
        String mRestaurantAddress = "4131 Brooklyn Ave NE";
        String mRestaurantDistance = String.format(getResources().getString(R.string.results_distance_to_restaurant), 5);
        ((TextView) view.findViewById(R.id.restaurant_name)).setText(mRestaurantName);
        ((TextView) view.findViewById(R.id.restaurant_address)).setText(mRestaurantAddress);
        ((TextView) view.findViewById(R.id.restaurant_distance)).setText(mRestaurantDistance);

        view.findViewById(R.id.open_in_maps_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO plug in restaurant latitude and longitude
                openRestaurantLocationInMaps(0, 0);
            }

        });

        view.findViewById(R.id.spin_again_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimation();
                setRestaurantDataInFragment();
            }

        });

        view.findViewById(R.id.start_over_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openRestaurantLocationInMaps(float latitude, float longitude) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
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

    }
}
