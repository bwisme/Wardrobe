package me.bwis.wardrobe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.HomeItemViewHolder>
{


    private List items;

    public HomeItemAdapter(List items)
    {
        this.items = items;
    }

    @NonNull
    @Override
    public HomeItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new HomeItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_clothes_home, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeItemViewHolder homeItemViewHolder, int i)
    {
        final ClothesItem item = (ClothesItem) items.get(i);
        homeItemViewHolder.text.setText(item.name);
        Picasso.get().load(new File(item.photoPath)).resize(200,200).centerInside().into(homeItemViewHolder.image);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class HomeItemViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        TextView text;

        public HomeItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.home_item_image);
            text = itemView.findViewById(R.id.home_item_name);
        }
    }
}
