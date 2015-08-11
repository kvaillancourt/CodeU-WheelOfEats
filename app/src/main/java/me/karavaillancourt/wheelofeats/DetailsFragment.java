package me.karavaillancourt.wheelofeats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    private View view;
    private Resturant restaurant;
    private GIFView wheelGifContainer;
    private LinearLayout resultsView;
    public static final String DETAILS_FRAGMENT_TAG = "DETAILS";
    private static final long ANIMATION_DELAY = 5000;
    private MainActivity mainActivity;



    public DetailsFragment() {
        //mainActivity = activity;
    }

    public void setMainActivity(MainActivity activity){
        mainActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        resultsView = (LinearLayout) view.findViewById(R.id.results_view);
        wheelGifContainer = (GIFView) view.findViewById(R.id.loading_wheel_gif);
        showAnimation();
        return view;
    }

    public void setRestaurantDataInFragment() {
        wheelGifContainer.setVisibility(View.GONE);
        resultsView.setVisibility(View.VISIBLE);
        String mRestaurantName;
        if (restaurant != null) {
            view.findViewById(R.id.open_in_maps_btn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.open_in_maps_btn_text).setVisibility(View.VISIBLE);
            mRestaurantName = restaurant.getName();
            try {
                URL url = new URL(restaurant.getIcon());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ((ImageView) view.findViewById(R.id.restaurant_img)).setImageBitmap(bmp);
            } catch (Exception ex) {
                view.findViewById(R.id.restaurant_img).setVisibility(View.GONE);
            }
            //String mRestaurantAddress = restaurant.getAddress();
            //String mRestaurantDistance = String.format(getResources().getString(R.string.results_distance_to_restaurant), 5);
            //((TextView) view.findViewById(R.id.restaurant_address)).setText(mRestaurantAddress);
            //((TextView) view.findViewById(R.id.restaurant_distance)).setText(mRestaurantDistance);

            view.findViewById(R.id.open_in_maps_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openRestaurantLocationInMaps();
                }

            });

            view.findViewById(R.id.spin_again_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAnimation();
                    ((MainActivity)getActivity()).addRestaurantDataToFragment();
                }
            });
        } else {
            mRestaurantName = getActivity().getResources().getString(R.string.no_restaurants_found);
            view.findViewById(R.id.open_in_maps_btn).setVisibility(View.GONE);
            view.findViewById(R.id.open_in_maps_btn_text).setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(R.id.restaurant_name)).setText(mRestaurantName);
        // ((TextView) view.findViewById(R.id.restaurant_address)).setText(mRestaurantAddress);
        // ((TextView) view.findViewById(R.id.restaurant_distance)).setText(mRestaurantDistance);

        view.findViewById(R.id.open_in_maps_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO plug in restaurant latitude and longitude
                openRestaurantLocationInMaps();
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

    private void openRestaurantLocationInMaps() {
        Uri geoLocation = Uri.parse(String.format("geo:%f,%f?", restaurant.getLatitude(), restaurant.getLongitude()));
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

    public void setRestaurant(Resturant restaurant) {
        this.restaurant = restaurant;
        if (view.findViewById(R.id.results_view).getVisibility() == View.VISIBLE) {
            setRestaurantDataInFragment();
        }
    }

    public Resturant getRestaurant() {
        return restaurant;
    }

    public void save(View view) throws FileNotFoundException, UnsupportedEncodingException {
        Resturant current = getRestaurant();
        BufferedWriter infoWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(getActivity().getFilesDir().toString() + "/saved_restaurants.txt"), "UTF-8"));
        try {
            infoWriter.write(current.getName() + "\n");
            // infoWriter.write(current ADDRESS

            infoWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
