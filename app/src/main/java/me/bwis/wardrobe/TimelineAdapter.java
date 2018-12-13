package me.bwis.wardrobe;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;


import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimeLineViewHolder>
{

    private List<AVStatus> timelineItems;

    public TimelineAdapter(List<AVStatus> timelineItems)
    {
        this.timelineItems = timelineItems;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new TimeLineViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timeline_item_layout,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder timeLineViewHolder, int i)
    {
        final AVStatus status = timelineItems.get(i);
        Picasso.get().load(status.getImageUrl())
                .fit()
//                .resize(0, timeLineViewHolder.mImageView.getHeight())
                .centerInside()
                .into(timeLineViewHolder.mImageView);
        timeLineViewHolder.mUsername.setText("@"+status.getSource().getUsername());
        timeLineViewHolder.mContent.setText(status.getMessage());
        timeLineViewHolder.mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d("TimelineAdapter", "username:"+status.getSource().getUsername());
            }
        });
    }



    @Override
    public int getItemCount()
    {
        return timelineItems.size();
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder{


        ImageView mImageView;
        TextView mUsername;
        TextView mContent;
        Button mButton;


        public TimeLineViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.timeline_card_image);
            mUsername = itemView.findViewById(R.id.timeline_card_username);
            mContent = itemView.findViewById(R.id.timeline_card_content);



        }

    }
}
