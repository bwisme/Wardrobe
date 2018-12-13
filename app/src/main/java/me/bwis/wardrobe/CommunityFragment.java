package me.bwis.wardrobe;

import android.app.AlertDialog;
import android.content.Context;
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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVPersistenceUtils;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.InboxStatusFindCallback;

import java.util.List;


public class CommunityFragment extends Fragment {

    private TimelineAdapter mTimelineAdapter;
    private RecyclerView mTimelineRecyclerView;

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
        mTimelineRecyclerView = rootView.findViewById(R.id.community_recycler_view);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
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
        switch(item.getItemId())
        {

            case R.id.community_option_following:
                //follow
                return true;
            case R.id.community_option_my_post:
                // show post
                return true;
            case R.id.community_option_search:
                // search
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getTimelineStatus()
    {
        if (!WardrobeApplication.ApplicationState.IS_LOGGED_IN)
        {
            return;
        }
        AVStatusQuery inboxQuery = AVStatus.inboxQuery(AVUser.getCurrentUser() ,AVStatus.INBOX_TYPE.TIMELINE.toString());
        inboxQuery.setLimit(50);  //设置最多返回 50 条状态
        inboxQuery.setSinceId(0);  //查询返回的 status 的 messageId 必须大于 sinceId，默认为 0
        inboxQuery.findInBackground(new InboxStatusFindCallback(){
            @Override
            public void done(final List<AVStatus> avObjects, final AVException avException) {
                mTimelineAdapter = new TimelineAdapter(avObjects);
                mTimelineRecyclerView.setAdapter(mTimelineAdapter);

            }
        });
    }
}
