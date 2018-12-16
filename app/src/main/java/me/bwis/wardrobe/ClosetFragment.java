package me.bwis.wardrobe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.bwis.wardrobe.dummy.DummyAdapter;
import me.bwis.wardrobe.dummy.DummyItem;
import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;
import me.bwis.wardrobe.utils.SharedPreferenceUtils;


public class ClosetFragment extends Fragment {




    private final List items = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mClothesRecyclerView;
    private ClothesItemAdapter mClothesItemAdapter;
    private CategoryAdapter mCategoryAdapter;
    private ClothesItemDatabase mClothesItemDatabase;
    private Menu mMenu;
    private MenuItem mShowAllType;


    public ClosetFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClosetFragment newInstance() {
        ClosetFragment fragment = new ClosetFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        mClothesItemDatabase = new ClothesItemDatabase(getActivity().getApplication());
        mClothesItemAdapter = new ClothesItemAdapter(items);
        mCategoryAdapter = new CategoryAdapter(categories, this);

        updateCategories();
        onSwitchData();
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mClothesRecyclerView.setAdapter(mClothesItemAdapter);
        mClothesRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mClothesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(R.menu.closet_options, menu);
        mMenu = menu;
        mShowAllType = mMenu.findItem(R.id.closet_option_all_type);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {


            case R.id.closet_option_by_type:
                if (WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        == Constant.CATEGORY_TYPE)
                    return true;
                WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        = Constant.CATEGORY_TYPE;
                break;
            case R.id.closet_option_by_color:
                if (WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        == Constant.CATEGORY_COLOR)
                    return true;
                WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        = Constant.CATEGORY_COLOR;
                break;
            case R.id.closet_option_by_season:
                if (WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        == Constant.CATEGORY_SEASON)
                    return true;
                WardrobeApplication.ApplicationState.CURRENT_CATEGORY
                        = Constant.CATEGORY_SEASON;
                break;
            case R.id.closet_option_all_type:
                WardrobeApplication.ApplicationState.IS_SHOWING_ALL_TYPE = !WardrobeApplication.ApplicationState.IS_SHOWING_ALL_TYPE;
                if (WardrobeApplication.ApplicationState.IS_SHOWING_ALL_TYPE)
                {
                    mShowAllType.setTitle("Show fewer types");
                }
                else
                {
                    mShowAllType.setTitle(R.string.closet_select_all_type);
                }
                updateCategories();
                break;
            case R.id.action_closet_delete:

                mClothesItemAdapter.isInMultiSelectMode = !mClothesItemAdapter.isInMultiSelectMode;
                if (mClothesItemAdapter.isInMultiSelectMode)
                {
                    mMenu.findItem(R.id.action_closet_delete_confirm).setVisible(true);


                }
                else
                {
                    mMenu.findItem(R.id.action_closet_delete_confirm).setVisible(false);

                    mClothesItemAdapter.clearSelectedState();
                }
                mClothesItemAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_closet_delete_confirm:
                onDeleteSelectedClothes();

                return true;
        }
        updateCategories();
        onSwitchData();
        return true;
    }

    private void onDeleteSelectedClothes()
    {
        List<ClothesItem> items = mClothesItemAdapter.getSelectedItems();
        if (items.isEmpty())
        {
            mMenu.findItem(R.id.action_closet_delete_confirm).setVisible(false);
            mClothesItemAdapter.isInMultiSelectMode = false;
            mClothesItemAdapter.clearSelectedState();
            mClothesItemAdapter.notifyDataSetChanged();

            return;
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this.getActivity());
        builder.setTitle("Confirm?");
        builder.setMessage("These data will be deleted forever!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                List<ClothesItem> items = mClothesItemAdapter.getSelectedItems();
                Log.d("onDeleteSelectedClothes", "items to be deleted size:"+Integer.toString(items.size()));
                for (ClothesItem item : items)
                {
                    mClothesItemDatabase.deleteClothes(item.id);
                }

                mMenu.findItem(R.id.action_closet_delete_confirm).setVisible(false);
                mClothesItemAdapter.isInMultiSelectMode = false;
                updateCategories();
                mClothesItemAdapter.notifyDataSetChanged();
                onSwitchData();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();



    }

    public void onSwitchData()
    {
        //fetch data
        mClothesItemAdapter.clearSelectedState();
        String category = mCategoryAdapter.getSelectedCategory();
        if (category == "")
        {
            items.clear();
            mClothesItemAdapter.notifyDataSetChanged();
            return;
        }

        List data = new ArrayList<>();
        Log.d("onSwitchData", "CURRENT_CATEGORY:"+
                Integer.toString(WardrobeApplication.ApplicationState.CURRENT_CATEGORY));
        switch (WardrobeApplication.ApplicationState.CURRENT_CATEGORY)
        {
            case Constant.CATEGORY_TYPE:
                data = mClothesItemDatabase.getClothesBy(
                        ClothesItemContract.ClothesItemEntry.COLUMN_NAME_TYPE,category);
                break;
            case Constant.CATEGORY_SEASON:
                data = mClothesItemDatabase.getClothesBy(
                        ClothesItemContract.ClothesSeasonEntry.COLUMN_NAME_SEASON,category);
                break;
            case Constant.CATEGORY_COLOR:
                data = mClothesItemDatabase.getClothesBy(
                        ClothesItemContract.ClothesItemEntry.COLUMN_NAME_COLOR_TYPE,category);
                break;

        }
        Log.d("onSwitchData", "dataset size:"+Integer.toString(data.size()));
        items.clear();
        items.addAll(data);
//        mClothesItemAdapter = new ClothesItemAdapter(items);
//        mClothesRecyclerView.swapAdapter(mClothesItemAdapter, true);

        mClothesItemAdapter.notifyDataSetChanged();

    }

    private void updateCategories()
    {
        int currentPos = mCategoryAdapter.selectedPos;
        Log.d("updateCategories", "currentPos"+Integer.toString(currentPos));
        categories.clear();
        switch (WardrobeApplication.ApplicationState.CURRENT_CATEGORY)
        {
            case Constant.CATEGORY_TYPE:
                if (WardrobeApplication.ApplicationState.IS_SHOWING_ALL_TYPE)
                    categories.addAll(Res.TYPE_SET);
                else
                    categories.addAll(mClothesItemDatabase.getTypes());
                break;
            case Constant.CATEGORY_COLOR:
                categories.addAll(Constant.COLOR_LIST);
                break;
            case Constant.CATEGORY_SEASON:
                categories.addAll(Constant.SEASON_LIST);
                break;
        }
        if (!categories.isEmpty())
        {
            if (currentPos >= categories.size())
            {
                mCategoryAdapter.selectedPos = (categories.size() - 1 > 0) ? categories.size()-1:0;
            }
            else if (currentPos != RecyclerView.NO_POSITION)
            {
                mCategoryAdapter.selectedPos = currentPos;
            }
            else
                mCategoryAdapter.selectedPos = 0;
        }
        else
        {
            mCategoryAdapter.selectedPos = RecyclerView.NO_POSITION;
        }
        mCategoryAdapter.notifyDataSetChanged();
        Log.d("updateCategories", "selectedPos"+Integer.toString(mCategoryAdapter.selectedPos));

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("ClosetFragment", "onResume");
        updateCategories();
        onSwitchData();
    }

    public void onAddNewCategory()
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_type, null);
        final EditText mNewType = mView.findViewById(R.id.dialog_new_type_text);
        MaterialButton mButton = mView.findViewById(R.id.dialog_new_type_confirm);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String type = mNewType.getText().toString();
                if (!Res.TYPE_SET.contains(type))
                {
                    SharedPreferenceUtils.putStringToStringSet(Constant.PREF_TYPE_SET, type);
                    Res.TYPE_SET.add(type);
                }
                else
                {
                    Toast.makeText(getActivity(), "Type already exist!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

    public void onLongClickCategory(final String category)
    {
        android.support.v7.app.AlertDialog.Builder getImageFrom = new android.support.v7.app.AlertDialog.Builder(getActivity());

        getImageFrom.setTitle("Select:");
        final CharSequence[] opsChars = {"Rename",
                "Delete"};
        getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    renameCategory(category);
                }else
                if(which == 1){
                    deleteCategory(category);
                }
                dialog.dismiss();
            }
        });
        getImageFrom.show();
    }

    private void renameCategory(final String oldName)
    {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialog_rename_type, null);
        final EditText mNewType = mView.findViewById(R.id.dialog_rename_type_text);
        mNewType.setText(oldName);
        MaterialButton mButton = mView.findViewById(R.id.dialog_rename_type_confirm);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String type = mNewType.getText().toString();
                if (!Res.TYPE_SET.contains(type) && !type.equals(""))
                {
                    //Rename in database
                    mClothesItemDatabase.renameType(oldName, type);
                    SharedPreferenceUtils.putStringToStringSet(Constant.PREF_TYPE_SET, type);
                    SharedPreferenceUtils.removeStringFromStringSet(Constant.PREF_TYPE_SET, oldName);
                    Res.TYPE_SET.remove(oldName);
                    Res.TYPE_SET.add(type);
                    updateCategories();
                    onSwitchData();
                }
                else
                {
                    Toast.makeText(getContext(), "Category already exist!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    private void deleteCategory(final String category)
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Warning!");
        builder.setMessage("You will also delete ALL ITEMS of this category!");
        builder.setPositiveButton("Delete Anyway", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mClothesItemDatabase.deleteClothesByType(category);
                SharedPreferenceUtils.removeStringFromStringSet(Constant.PREF_TYPE_SET, category);
                Res.TYPE_SET.remove(category);
                updateCategories();
                onSwitchData();

            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
