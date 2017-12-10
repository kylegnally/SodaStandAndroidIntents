package edu.kvcc.cis298.cis298assignment4;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyleg on 11/9/2017.
 */

public class WineListFragment extends Fragment {

    // Create a tag for our fragment
    private static final String TAG = "WineListFragment";

    // private variables for the RecyclerView, Adapter, WineShop, and Context
    private RecyclerView mWineRecyclerView;
    private WineAdapter mAdapter;
    private WineShop mWineList;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

        // starts the new task on a new thread
        new FetchWinesTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view with the inflater, specifying the name of the layout file we will use
        View view = inflater.inflate(R.layout.fragment_wine_list, container, false);

        // Get a reference to the recyclerview
        mWineRecyclerView = (RecyclerView) view.findViewById(R.id.wine_recycler_view);

        // set the mWineRecyclerView's layout manager and pass it a new LinearLayoutManager,
        // calling its getActivity() method
        mWineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // get the singleton and assign it to mWinelist
        mWineList = WineShop.get(mContext);

        // update the UI and then return the view
        updateUI();
        return view;
    }

    // update the UI whenever we call onResume()
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        // call the singleton get method to get the singleton
        WineShop wineShop = WineShop.get(getActivity());

        // and then call the getWines() method of the singleton's instance to
        // get the wines, which we assign to the list
        ArrayList<WineItem> wines = wineShop.getWines();

        // if the adapter is null
        if (mAdapter == null) {

            // create a new adapter, pass it the wines...
            mAdapter = new WineAdapter(wines);

            // ... and then set the adapter
            mWineRecyclerView.setAdapter(mAdapter);

        } else {

            // if it's not null, call the notifyDataSetChaged() method
            // on the adapter. We do this so we see the correct place
            // in the master list when we return to it
            mAdapter.notifyDataSetChanged();
        }

    }

    private void setupAdapter(ArrayList<WineItem> wines) {

        // check to see if the RecyclerView had been added to the fragment
        if (isAdded()) {
            //Make a new adapter for the Recycler View and send over
            //the crime List
            mAdapter = new WineAdapter(wines);

            //Set the adapter on the Recycler View
            mWineRecyclerView.setAdapter(mAdapter);
        }
    }


    // this is our ViewHolder. It expects a TextView and references that view.
    // Since it is an interface, we will need to implement all its methods
    private class WineHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Variable for a single WineItem, which we will use to get...
        private WineItem mWine;

        // ...the three pieces of information we will stow in these three variables
        private TextView mTitleTextView;
        private TextView mIdTextView;
        private TextView mPriceTextView;

        // Create the WineHolder and set an OnClickListener
        public WineHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            // assign our variables to their widgets to construct the single wine item
            // we will see in each entry of the master list
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_wine_title_text_view);
            mIdTextView = (TextView) itemView.findViewById(R.id.list_item_wine_id_text_view);
            mPriceTextView = (TextView) itemView.findViewById(R.id.list_item_wine_price_text_view);

        }

        // Bind the widgets to the getters in the WineItem instance and
        // set the text of each widget to the values stored in the getters
        public void bindWine(WineItem wine) {
            mWine = wine;
            mTitleTextView.setText(mWine.getName());
            mIdTextView.setText(mWine.getId());
            mPriceTextView.setText(mWine.getPrice());
        }

        // onClick listener that will start an activity with an intent, passing
        // the newIntent() method the getActivity() and getId() methods. We are
        // using the wine's own Id number as a unique identifier
        // NOTE: This can cause problems if the Wine's Id is changed to an Id that
        // is already in use.
        @Override
        public void onClick(View v) {
            Intent intent = WinePagerActivity.newIntent(getActivity(), mWine.getUUID());

            // start the new activity with the intent we just created
            startActivity(intent);
        }
    }

    // Create the WineAdapter to map the data between the list and the recyclerview
    private class WineAdapter extends RecyclerView.Adapter<WineHolder> {

        private List<WineItem> mWines;

        public WineAdapter(ArrayList<WineItem> wine) {

            mWines = wine;

        }

        // Create the WineHolder
        @Override
        public WineHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // get a reference to the inflater
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            // inflate the view we will use
            View view = layoutInflater.inflate(R.layout.list_item_wine, parent, false);

            // return the WineHolder with its shiny new View
            return new WineHolder(view);
        }

        // get the position of the wine accoring to its index
        @Override
        public void onBindViewHolder(WineHolder holder, int position) {
            WineItem wine = mWines.get(position);

            // set all the value to the properties of the wine
            holder.bindWine(wine);
        }

        // this gets the size of the list
        @Override
        public int getItemCount() {
            return mWines.size();
        }

    }

    private class FetchWinesTask extends AsyncTask<Void,Void,ArrayList<WineItem>> {

        // fetch the wines in a background task
        @Override
        protected ArrayList<WineItem> doInBackground(Void... voids) {
            return new WineFetcher().fetchWines();
        }

        // after we get the wines into the array, get the activity, singleton, and set
        // up the adapter
        @Override
        protected void onPostExecute(ArrayList<WineItem> wineItems) {
            WineShop shop = WineShop.get(getActivity());
            shop.setWines(wineItems);
            setupAdapter(wineItems);
        }
    }
}
