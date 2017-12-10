package edu.kvcc.cis298.cis298assignment4;

import java.util.UUID;

/**
 * Created by kyleg on 10/29/2017.
 */

public class WineItem {

    // create some private variables for each element of each line of the CSV and a UUID for the wine ID
    private UUID mUUID;
    private String mId;
    private String mName;
    private String mPack;
    private String mPrice;
    private boolean mIsActive;

    // Constructor for this class. Takes four string and a bool
    public WineItem(String id, String name, String pack, String price, Boolean active) {

        // assign the variables to the constructor's requirements
        this.mUUID = UUID.randomUUID();
        this.mId = id;
        this.mName = name;
        this.mPack = pack;
        this.mPrice = price;
        this.mIsActive = active;
    }

    // getters and setters for the variables


    public UUID getUUID() {
        return mUUID;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPack() {
        return mPack;
    }

    public void setPack(String pack) {
        mPack = pack;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }
}

