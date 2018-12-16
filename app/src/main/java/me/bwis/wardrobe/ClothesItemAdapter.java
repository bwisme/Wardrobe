package me.bwis.wardrobe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.bwis.wardrobe.utils.SquareImageView;

class ClothesItemAdapter extends RecyclerView.Adapter<ClothesItemAdapter.ClothesItemViewHolder>
{

    private List<ClothesItem> items;
    boolean isInMultiSelectMode;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ArrayList<Integer> selectedIndex = new ArrayList<>();

    public boolean isItemSelected(ClothesItem item)
    {
        return getSelectedItems().contains(item);
    }

    public List<ClothesItem> getSelectedItems() {
        Log.d("getSelectedItems", "selected:"+Integer.toString(selectedItems.size()));
        List items = new ArrayList<ClothesItem>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(this.items.get(selectedItems.keyAt(i)));
        }
        return items;
    }

    public void switchSelectedState(int position) {
        if (selectedItems.get(position)) {       // item has been selected, de-select it.
            selectedItems.delete(position);
            selectedIndex.remove((Integer) position);
        } else {
            selectedItems.put(position, true);
            selectedIndex.add(position);
        }
        this.notifyItemChanged(position);
        Log.d("switchSelectedState", "selected:"+Integer.toString(selectedItems.size()));
    }

    public void clearSelectedState() {

        selectedItems.clear();
        for (Integer i : selectedIndex) {
            this.notifyItemChanged(i);
        }
        selectedIndex.clear();
    }




    public ClothesItemAdapter(List<ClothesItem> items)
    {
        this.items = items;
        this.isInMultiSelectMode = false;
    }


    @NonNull
    @Override
    public ClothesItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new ClothesItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_clothes,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ClothesItemViewHolder clothesItemViewHolder, int i)
    {
        final ClothesItem item = items.get(i);
        final int index = i;
        clothesItemViewHolder.name.setText(item.name);
        clothesItemViewHolder.type.setText(item.type);
        clothesItemViewHolder.storeLocation.setText(item.storeLocation);

        Picasso.get().load(new File(item.photoPath)).fit()
                .centerInside().into(clothesItemViewHolder.image);

        clothesItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isInMultiSelectMode)
                {
                    switchSelectedState(index);
                }
                else
                {
                    // open description
                    Intent intent = new Intent(clothesItemViewHolder.itemView.getContext(), ModifyClothesActivity.class);
                    intent.putExtra("currentID", item.id);
                    clothesItemViewHolder.itemView.getContext().startActivity(intent);
                }
            }
        });

        if (isInMultiSelectMode)
        {
            clothesItemViewHolder.checkBox.setVisibility(View.VISIBLE);
            clothesItemViewHolder.checkBox.setChecked(selectedItems.get(i));
        }
        else
        {
            clothesItemViewHolder.checkBox.setChecked(false);
            clothesItemViewHolder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class ClothesItemViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView name;
        TextView type;
        TextView storeLocation;
        CheckBox checkBox;

        public ClothesItemViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.clothes_item_image);
            name = itemView.findViewById(R.id.clothes_item_name);
            type = itemView.findViewById(R.id.clothes_item_type);
            storeLocation = itemView.findViewById(R.id.clothes_item_store_location);
            checkBox = itemView.findViewById(R.id.clothes_item_checkbox);

        }
    }
}
