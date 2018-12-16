package me.bwis.wardrobe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>
{

    private List<AVUser> items;
    private Context context;
    private boolean showFollow;


    public UserListAdapter(List<AVUser> items, Context context, boolean showFollow)
    {
        this.items = items;
        this.context = context;
        this.showFollow = showFollow;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new UserListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder userListViewHolder, int i)
    {
        final AVUser user = items.get(i);
        userListViewHolder.name.setText(user.getUsername());
        if (!showFollow)
            userListViewHolder.button.setText("UnFollow");
        userListViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //check follow status
                final boolean[] isFollowing = new boolean[1];
                AVQuery<AVUser> followeeNameQuery = AVUser.getCurrentUser().followeeQuery(AVUser.getCurrentUser().getObjectId(), AVUser.class);
                followeeNameQuery.whereEqualTo("followee", user);

                    followeeNameQuery.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e)
                        {
                            if (e == null)
                            {
                                if (!showFollow)
                                {

                                    //取消关注
                                    AVUser.getCurrentUser().unfollowInBackground(user.getObjectId(), new FollowCallback() {
                                        @Override
                                        public void done(AVObject object, AVException e) {
                                            if (e == null) {
                                                Log.i("USERLIST", "unfollow succeeded.");
                                                Toast.makeText(context, "unfollow succeeded.", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Log.w("USERLIST", "unfollow failed.");
                                                Toast.makeText(context, "unfollow failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else
                                {
                                    //关注
                                    AVUser.getCurrentUser().followInBackground(user.getObjectId(), new FollowCallback() {
                                        @Override
                                        public void done(AVObject object, AVException e) {
                                            if (e == null) {
                                                Log.i("USERLIST", "follow succeeded.");
                                                Toast.makeText(context, "follow succeeded.", Toast.LENGTH_SHORT).show();

                                            } else if (e.getCode() == AVException.DUPLICATE_VALUE) {
                                                Log.w("USERLIST", "Already followed.");
                                                Toast.makeText(context, "Already followed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });


            }
        });
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder
    {

        TextView name;
        MaterialButton button;

        public UserListViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.item_user_name);
            button = itemView.findViewById(R.id.item_user_follow);

        }
    }


}
