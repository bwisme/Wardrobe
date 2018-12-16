package me.bwis.wardrobe;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVPersistenceUtils;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.InboxStatusFindCallback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.bwis.wardrobe.utils.Constant;
import me.bwis.wardrobe.utils.Res;
import me.bwis.wardrobe.utils.SharedPreferenceUtils;


public class CommunityFragment extends Fragment {

    private TimelineAdapter mTimelineAdapter;
    private RecyclerView mTimelineRecyclerView;
    private TextView emptyText;

    public CommunityFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();

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

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        emptyText = rootView.findViewById(R.id.empty_text);
        mTimelineRecyclerView = rootView.findViewById(R.id.community_recycler_view);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        if (!WardrobeApplication.ApplicationState.IS_LOGGED_IN)
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.INVISIBLE);
        getTimelineStatus();
        return rootView;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(R.menu.community, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (!WardrobeApplication.ApplicationState.IS_LOGGED_IN)
        {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch(item.getItemId())
        {

            case R.id.community_option_refresh:
                getTimelineStatus();
                return true;
            case R.id.community_option_following:
                //follow
                showFollowList();
                break;
            case R.id.community_option_my_post:
                // show post
                showMyPost();
                break;
            case R.id.community_option_search:
                // search
                searchForUsers();
                break;
        }
        getTimelineStatus();
        return super.onOptionsItemSelected(item);
    }

    public void getTimelineStatus()
    {
        if (!WardrobeApplication.ApplicationState.IS_LOGGED_IN)
        {
            return;
        }
        AVStatusQuery inboxQuery = AVStatus.inboxQuery(AVUser.getCurrentUser() ,AVStatus.INBOX_TYPE.TIMELINE.toString());
        inboxQuery.setLimit(50);
        inboxQuery.setSinceId(0);  //查询返回的 status 的 messageId 必须大于 sinceId，默认为 0
        inboxQuery.findInBackground(new InboxStatusFindCallback(){
            @Override
            public void done(final List<AVStatus> avObjects, final AVException avException) {
                mTimelineAdapter = new TimelineAdapter(avObjects, CommunityFragment.this,false);
                mTimelineRecyclerView.setAdapter(mTimelineAdapter);

            }
        });
    }

    private void showFollowList()
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getLayoutInflater().inflate(R.layout.dialog_follow_list, null);
        final RecyclerView recyclerView = mView.findViewById(R.id.follow_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //fetch all followee
        //查询关注者
        AVQuery<AVUser> followeeQuery = AVUser.followeeQuery(AVUser.getCurrentUser().getObjectId(), AVUser.class);
        followeeQuery.include("followee");
        followeeQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avObjects, AVException avException) {
                //avObjects 就是用户的关注用户列表
                UserListAdapter userListAdapter = new UserListAdapter(avObjects, getContext(), false);
                recyclerView.setAdapter(userListAdapter);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

    }

    private void showMyPost()
    {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getLayoutInflater().inflate(R.layout.dialog_my_post, null);
        final RecyclerView recyclerView = mView.findViewById(R.id.my_post_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<AVStatus> statuses = new ArrayList<>();
        final TimelineAdapter timelineAdapter = new TimelineAdapter(statuses, this, true);

        recyclerView.setAdapter(timelineAdapter);

        AVStatusQuery query = null;
        try
        {
            query = AVStatus.statusQuery(AVUser.getCurrentUser());
            query.setLimit(50);    //设置最多返回 50 条状态
            query.setSinceId(0);   //查询返回的 status 的 messageId 必须大于 sinceId，默认为 0
            query.addDescendingOrder("createdAt");
            //query.setInboxType(AVStatus.INBOX_TYPE.TIMELINE.toString()); 此处可以通过这个方法来添加查询的状态条件，当然这里你也可以用你自己定义的状态类型，因为这里接受的其实是一个字符串类型。
            query.findInBackground(new FindCallback<AVStatus>(){
                @Override
                public void done(final List<AVStatus> avObjects,final AVException avException) {
                    statuses.clear();
                    statuses.addAll(avObjects);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            });
        } catch (AVException e)
        {
            e.printStackTrace();
            return;
        }

    }

    private void searchForUsers()
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getLayoutInflater().inflate(R.layout.dialog_search_users, null);
        final EditText editText = mView.findViewById(R.id.search_user_input);
        final RecyclerView recyclerView = mView.findViewById(R.id.search_list_recycler_view);
        final MaterialButton button = mView.findViewById(R.id.search_user_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<AVUser> mList = new ArrayList<>();
        final UserListAdapter userListAdapter = new UserListAdapter(mList, getContext(), true);
        recyclerView.setAdapter(userListAdapter);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String search = editText.getText().toString();
                AVQuery<AVUser> query = new AVQuery<>("_User");
                query.whereContains("username", search);
                query.setLimit(100);
                query.findInBackground(new FindCallback<AVUser>()
                {

                    @Override
                    public void done(List<AVUser> list, AVException e)
                    {
                        mList.clear();
                        if (e != null || list.isEmpty())
                        {
                            Toast.makeText(getContext(), "user not found", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mList.clear();
                            mList.addAll(list);
                            userListAdapter.notifyDataSetChanged();
                        }

                    }
                });

            }
        });


    }


    public void onLongClickMyPost(final AVStatus post)
    {
        android.support.v7.app.AlertDialog.Builder deleteConfirm = new android.support.v7.app.AlertDialog.Builder(getActivity());

        deleteConfirm.setTitle("Select:");
        final CharSequence[] opsChars = {"Delete"};
        deleteConfirm.setItems(opsChars, new android.content.DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirm");
                    builder.setMessage("delete this post?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInner, int id) {
                            AVStatus.deleteStatusWithIDInBackgroud(post.getObjectId(), new DeleteCallback() {
                                @Override
                                public void done(AVException e)
                                {
                                    if (e == null)
                                        Toast.makeText(getContext(), "delete succeeded.", Toast.LENGTH_SHORT).show();
                                    dialogInner.dismiss();;
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                dialog.dismiss();
            }
        });
        deleteConfirm.show();
    }
}
