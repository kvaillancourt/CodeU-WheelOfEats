package me.karavaillancourt.wheelofeats;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
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
            }
        });

    }
}
