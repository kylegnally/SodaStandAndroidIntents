package edu.kvcc.cis298.cis298assignment4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by kyleg on 11/9/2017.
 */

    // A generic activity for hosting a single fragment. It is abstract, so it
    // cannot be instantiated
public abstract class SingleFragmentActivity extends FragmentActivity {

    // abstract method for creating a fragment that can be hosted
    // in its activity. All child activities must implement this method.
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the View we will use for the fragment
        setContentView(R.layout.activity_fragment);

        // create a FragmentManager. We use the one in Support
        FragmentManager fm = getSupportFragmentManager();

        // create a Fragment and send it the Id of the framelayout.
        // The first time it is run, there will be nothing there...
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        // ... so we check for null. The first time it is run, it will be null, and
        // will create a new fragment. We only need to do this once...
        if (fragment == null) {

            // ...because we only create the fragment once (on the first run).
            fragment = createFragment();

            // Use the BeginTransaction() method in the fragmentmanager, adding
            // the fragment into the framelayout, and then call commit()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
