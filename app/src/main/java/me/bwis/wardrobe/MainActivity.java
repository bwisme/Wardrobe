package me.bwis.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;


    private Fragment closetFragment;
    private Fragment settingFragment;
    private Fragment communityFragment;
    private Fragment activeFragment;
    private Fragment homeFragment;

    private FloatingActionButton addClothesButton;
    private View.OnClickListener addClothesButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (activeFragment == communityFragment)
            {
                //new post
                if (WardrobeApplication.ApplicationState.IS_LOGGED_IN)
                {
                    Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Intent intent = new Intent(MainActivity.this, AddClothesActivity.class);
                startActivity(intent);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.closetFragment = ClosetFragment.newInstance();
        this.homeFragment = HomeFragment.newInstance();
        this.settingFragment = SettingFragment.newInstance();
        this.communityFragment = CommunityFragment.newInstance();

        activeFragment = homeFragment;
        addClothesButton = findViewById(R.id.fab_add_clothes);
        addClothesButton.setOnClickListener(addClothesButtonOnClickListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, communityFragment).detach(communityFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, settingFragment).detach(settingFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, homeFragment).attach(homeFragment).commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_closet:
                    if (activeFragment != closetFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, closetFragment).commit();
//                        getSupportFragmentManager().beginTransaction().detach(activeFragment).attach(closetFragment).commit();
                        activeFragment = closetFragment;
                        addClothesButton.show();
                    }
                    break;
                case R.id.navigation_home:
                    if (activeFragment != homeFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit();
//                        getSupportFragmentManager().beginTransaction().detach(activeFragment).attach(homeFragment).commit();
                        activeFragment = homeFragment;
                        addClothesButton.show();
                    }
                    break;
                case R.id.navigation_community:
                    if (activeFragment != communityFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, communityFragment).commit();
//                        getSupportFragmentManager().beginTransaction().detach(activeFragment).attach(communityFragment).commit();
                        activeFragment = communityFragment;
                        addClothesButton.show();
                    }
                    break;
                case R.id.navigation_settings:
                    if (activeFragment != settingFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, settingFragment).commit();
//                        getSupportFragmentManager().beginTransaction().detach(activeFragment).attach(settingFragment).commit();
                        activeFragment = settingFragment;
                        addClothesButton.hide();
                    }
                    break;
            }
            return true;
        }
    };



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_closet_options:
                //show options
                return true;
        }
        return super.onOptionsItemSelected(item);
    }






}
