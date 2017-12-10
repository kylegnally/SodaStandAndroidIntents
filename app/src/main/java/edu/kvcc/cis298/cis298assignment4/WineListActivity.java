/*
CIS298
Kyle Nally
CIS298 ASSIGNMENT 3: WINELIST APPLICATION

This application displays a list of wines, their product IDs, and their prices to a user.
The list is loaded from a CSV which contains the each wine's id, name, pack, price, and
status as active or inactive. The main list display on the first application screen displays
only the name,Id, and price.

Touching any wine on the main screen loads a detail screen, on which the user may edit any of the pieces
of information shown. This information persists on screen rotation, and any changes to the three pieces of
information shown on the main screen are displayed there when the user touches the "back" button on their
device.

Additionally, on the detail page, the user is able to swipe left or right to move to the next item in the
list. If the user presses the back button on their device at any time, the list will be shown at the
location in the list the user was viewing when they first entered the details page.

This application uses a RecyclerView to display the main wine list, fragments to store each list item,
and a ViewPager to handle swipe events on the details pages.

 */




package edu.kvcc.cis298.cis298assignment4;
import android.support.v4.app.Fragment;

/**
 * Created by kyleg on 11/9/2017.
 */

public class WineListActivity extends SingleFragmentActivity {

    // create a new WineListFragment and return it
    @Override
    protected Fragment createFragment() {

        return new WineListFragment();

    }
}
