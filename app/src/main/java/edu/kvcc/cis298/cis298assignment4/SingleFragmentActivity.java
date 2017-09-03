package edu.kvcc.cis298.cis298assignment4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by David Barnes on 11/3/2015.
 * Class that contains some reusable code for activities that use a single fragment
 */
public abstract class SingleFragmentActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the content to the activity_fragment layout
        setContentView(R.layout.activity_fragment);

        //create a new fragment manager, which is required to attach a new fragment
        FragmentManager fm = getSupportFragmentManager();

        //find the fragment that is already in teh container. Might be null.
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //if the fragment is null, we can create a new one and add it to the container
        if (fragment == null) {
            //create the fragment
            fragment = createFragment();
            //start a transaction to put it in the container
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit(); //must commit the transaction to make it complete
        }
    }

    //Define a abstract method that must be overridden in child classes to return a fragment to use.
    protected abstract Fragment createFragment();

}
