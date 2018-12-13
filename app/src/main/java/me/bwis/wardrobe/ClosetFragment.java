package me.bwis.wardrobe;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.bwis.wardrobe.dummy.DummyAdapter;
import me.bwis.wardrobe.dummy.DummyItem;



public class ClosetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private  List<DummyItem> items;
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mClothesRecyclerView;



//    private OnFragmentInteractionListener mListener;

    public ClosetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClosetFragment newInstance() {
        ClosetFragment fragment = new ClosetFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        items = new ArrayList<DummyItem>();
        items.add(new DummyItem(1,"a"));
        items.add(new DummyItem(2,"b"));
        items.add(new DummyItem(3,"c"));
        items.add(new DummyItem(4,"d"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));
        items.add(new DummyItem(5,"e"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_closet, container, false);
        mCategoryRecyclerView = rootView.findViewById(R.id.closet_category_view);
        mClothesRecyclerView = rootView.findViewById(R.id.closet_clothes_view);

        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        mClothesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//
        DummyAdapter adapter1 = new DummyAdapter(items);
        DummyAdapter adapter2 = new DummyAdapter(items);
        mCategoryRecyclerView.setAdapter(adapter1);
        //mCategoryRecyclerView.setAdapter();
        mClothesRecyclerView.setAdapter(adapter2);
        mClothesRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
//        mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
////        mClothesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(R.menu.closet_options, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
