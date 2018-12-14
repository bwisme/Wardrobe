package me.bwis.wardrobe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import me.bwis.wardrobe.utils.SquareImageView;

class ClothesItemAdapter extends RecyclerView.Adapter<ClothesItemAdapter.ClothesItemViewHolder>
{

    private List<ClothesItem> items;

    public ClothesItemAdapter(List<ClothesItem> items)
    {
        this.items = items;
    }

    @NonNull
    @Override
    public ClothesItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new ClothesItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_clothes,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesItemViewHolder clothesItemViewHolder, int i)
    {
        ClothesItem item = items.get(i);
        clothesItemViewHolder.name.setText(item.name);
        clothesItemViewHolder.type.setText(item.type);
        clothesItemViewHolder.storeLocation.setText(item.storeLocation);

        Picasso.get().load(new File(item.photoPath)).fit()
                .centerInside().into(clothesItemViewHolder.image);
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

        public ClothesItemViewHolder(@NonNull View itemView)
        {
            super(itemView);

            image = itemView.findViewById(R.id.clothes_item_image);
            name = itemView.findViewById(R.id.clothes_item_name);
            type = itemView.findViewById(R.id.clothes_item_type);
            storeLocation = itemView.findViewById(R.id.clothes_item_store_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //show detail
                }
            });


        }
    }
}
