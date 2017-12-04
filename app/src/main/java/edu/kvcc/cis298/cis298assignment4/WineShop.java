package edu.kvcc.cis298.cis298assignment4;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kyleg on 11/7/2017.
 */

public class WineShop {

    // working variables
    // static variable to hold an instance of this class
    private static WineShop sWineShop;

    // a list to hold all our wines
    private ArrayList<WineItem> mWines;

    // public static method which is used to get the same
    // instance that is in mWineShop (this is the singleton)
    public static WineShop get (Context context) {

        if (sWineShop == null) {
            sWineShop = new WineShop(context);
        }

        return sWineShop;

    }

    // private constructor that is used to make sure the only code
    // that can be used to make an instance of WineShop is this class.
    // That instance is in the get method above.
    private WineShop(Context context) {
         mWines = new ArrayList<>();
     }


    // method to add a single item to the ArrayList above. This will add
    // a WineItem object with parameters id, name, pack, etc.
    public void addWineItem(String id, String name, String pack, String price, Boolean active) {
        mWines.add(new WineItem(id, name, pack, price, active));
    }

    // getter for the list of wines
    public ArrayList<WineItem> getWines() {
         return mWines;
     }

    // getter for a single wine. Uses the ID of any given wine
    // as a unique identifier (its use is similar to a UUID in this case)
    public WineItem getWine(UUID id) {
        for (WineItem wine : mWines) {
            if (wine.getUUID().equals(id)) {
                return wine;
            }
        }
        return null;
    }
}
