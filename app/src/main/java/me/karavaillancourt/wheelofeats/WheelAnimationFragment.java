package me.karavaillancourt.wheelofeats;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WheelAnimationFragment extends Fragment {

    public static final String ANIMATION_FRAGMENT_TAG = "ANIMATION";

    public static WheelAnimationFragment newInstance(String param1, String param2) {
        WheelAnimationFragment fragment = new WheelAnimationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public WheelAnimationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wheel_animation, container, false);
    }

}
