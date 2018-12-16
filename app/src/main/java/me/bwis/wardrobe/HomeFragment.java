package me.bwis.wardrobe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment {


    private HomeItemAdapter mHomeItemAdapter;
    private RecyclerView mHomeItemRecyclerView;
    private List items = new ArrayList<>();
    private ClothesItemDatabase mClothesItemDatabase;
    private View rootView;



    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        initWeatherStatus();


        mClothesItemDatabase = new ClothesItemDatabase(getActivity().getApplication());
        mHomeItemRecyclerView = rootView.findViewById(R.id.home_recommend_recycler_view);
        mHomeItemRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        items = mClothesItemDatabase.getRandomClothes(6);
        mHomeItemAdapter = new HomeItemAdapter(items);
        mHomeItemRecyclerView.setAdapter(mHomeItemAdapter);

        return rootView;
    }

    private void initWeatherStatus()
    {

        HashMap<String, String> weatherStatus = WardrobeApplication.ApplicationState.weatherStatus;
        try
        {
            TextView location = rootView.findViewById(R.id.weather_location);
            location.setText(weatherStatus.get("city"));
            TextView type = rootView.findViewById(R.id.weather_status);
            type.setText(weatherStatus.get("type"));
            TextView todayMax = rootView.findViewById(R.id.today_max_temp);
            todayMax.setText(weatherStatus.get("todayMax"));
            TextView todayMin = rootView.findViewById(R.id.today_min_temp);
            todayMin.setText(weatherStatus.get("todayMin"));
            TextView tomorrowMax = rootView.findViewById(R.id.tomorrow_max_temp);
            tomorrowMax.setText(weatherStatus.get("tomorrowMax"));
            TextView tomorrowMin = rootView.findViewById(R.id.tomorrow_min_temp);
            tomorrowMin.setText(weatherStatus.get("tomorrowMin"));
            TextView notice = rootView.findViewById(R.id.weather_text_info);
            notice.setText(weatherStatus.get("notice"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
