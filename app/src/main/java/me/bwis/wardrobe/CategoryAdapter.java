package me.bwis.wardrobe;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.bwis.wardrobe.utils.Constant;

public class CategoryAdapter extends RecyclerView.Adapter
{

    int selectedPos = RecyclerView.NO_POSITION;
    List<String> categories;
    ClosetFragment fragment;

    public CategoryAdapter(List<String> categories, ClosetFragment fragment)
    {
        this.categories = categories;
        this.fragment = fragment;
        if (categories.size() > 0)
            selectedPos = 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        Log.d("CategoryAdapter", "onCreateViewHolder");
        if (i == Constant.CATEGORY_FOOTER)
            return new AddCategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_add_category, viewGroup, false));
        else
            return new CategoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_category, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        Log.d("CategoryAdapter", "onBindViewHolder");
        if (viewHolder instanceof CategoryViewHolder)
        {
            String category = categories.get(i);
            ((CategoryViewHolder) viewHolder).name.setText(category);
            ((CategoryViewHolder) viewHolder).itemView.setSelected(selectedPos == i);
        }

    }

    @Override
    public int getItemCount()
    {
        if (WardrobeApplication.ApplicationState.CURRENT_CATEGORY == Constant.CATEGORY_TYPE)
            return categories.size()+1; //add the footer
        return categories.size();

    }


    public String getSelectedCategory()
    {
        if (categories.isEmpty() || selectedPos == RecyclerView.NO_POSITION)
            return "";
        else
            return categories.get(selectedPos);
    }


    @Override
    public int getItemViewType(int position)
    {
        return (position == categories.size()) ? Constant.CATEGORY_FOOTER : Constant.CATEGORY_ITEM;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;

        public CategoryViewHolder(@NonNull final View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.category_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    // set change adapter?
                    notifyItemChanged(selectedPos);
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);
                    fragment.onSwitchData();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (WardrobeApplication.ApplicationState.CURRENT_CATEGORY == Constant.CATEGORY_TYPE)
                        fragment.onLongClickCategory(name.getText().toString());
                    return true;
                }
            });
        }
    }

    public class AddCategoryViewHolder extends RecyclerView.ViewHolder
    {

        public AddCategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    // add category
                    fragment.onAddNewCategory();
                }
            });
        }
    }



}
