package edu.kvcc.cis298.cis298assignment4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kyleg on 11/11/2017.
 */

public class WinePagerActivity extends FragmentActivity {

    // String to hold our extra
    private static final String EXTRA_WINE_ID = "edu.kvcc.cis298.assignment3.wine_id";
    // string we will use to store contact name
    private static final String ARG_CONTACT_NAME = "contact_name";

    // string we will use to store contact email address
    private static final String ARG_CONTACT_EMAIL_ADDR = "contact_email";

    // define the variables for the ViewPager and our winelist
    private ViewPager mViewPager;
    private ArrayList<WineItem> mWines;

    // method to create an intent for the WinePager and
    // put the wineId as an extra, then return that intent
    public static Intent newIntent(Context packageContext, UUID wineId) {
        Intent intent = new Intent(packageContext, WinePagerActivity.class);
        intent.putExtra(EXTRA_WINE_ID, wineId);
        return intent;
    }

    // onCreate method for the WinePagerActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the view we will use for the wine pager
        setContentView(R.layout.activity_wine_pager);

        // get the extra from the intent

        UUID wineId = (UUID) getIntent().getSerializableExtra(EXTRA_WINE_ID);

        // get the widget from the view
        mViewPager = (ViewPager) findViewById(R.id.activity_wine_pager_view_pager);

        // get the wines from the singleton
        mWines = WineShop.get(this).getWines();

        // tell getSupportFragmentManager() to get the fragmentmanager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // set up the adapter for the ViewPager, passing the fragmentmanager
        // into it, and overriding the getItem() and getCount() methods
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {

                // get a method from the list by its position
                WineItem wineItem = mWines.get(position);

                // create a fragment using the newInstance() static method,
                // passing it the result stored in wineItem.getId()
                return WineFragment.newInstance(wineItem.getUUID());
            }

            // get the count from mWines.size
            @Override
            public int getCount() {
                return mWines.size();
            }
        });

        // loop until i is greater than the size of mWines
        for (int i = 0; i < mWines.size(); i++) {

            // if the id of mWines equals the wineId
            if (mWines.get(i).getUUID().equals(wineId)) {

                // set the ViewPager to that item
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
