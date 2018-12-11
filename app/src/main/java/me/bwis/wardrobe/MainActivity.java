package me.bwis.wardrobe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
            Intent intent = new Intent(MainActivity.this, AddClothesActivity.class);
            startActivity(intent);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_closet);
        addClothesButton = findViewById(R.id.fab_add_clothes);
        addClothesButton.setOnClickListener(addClothesButtonOnClickListener);

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
                        activeFragment = closetFragment;
                    }
                    return true;
                case R.id.navigation_home:
                    if (activeFragment != homeFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, homeFragment).commit();
                        activeFragment = homeFragment;
                    }
                    return true;
                case R.id.navigation_community:
                    if (activeFragment != communityFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, communityFragment).commit();
                        activeFragment = communityFragment;
                    }
                    return true;
                case R.id.navigation_settings:
                    if (activeFragment != settingFragment)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, settingFragment).commit();
                        activeFragment = settingFragment;
                    }
                    return true;
            }
            return false;
        }
    };






}
