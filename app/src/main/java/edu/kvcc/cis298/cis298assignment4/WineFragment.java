package edu.kvcc.cis298.cis298assignment4;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by kyleg on 11/6/2017.
 */

public class WineFragment extends Fragment {

    // string we will use to store the wine Id extra
    private static final String ARG_WINE_ID = "wine_id";

    // string we will use to store contact name
    private static final String ARG_CONTACT_NAME = "contact_name";

    // string we will use to store contact email address
    private static final String ARG_CONTACT_EMAIL_ADDR = "contact_email";

    // constant for contact request code
    private static final int REQUEST_CONTACT = 1;

    // Where are the variables to store our widgets??? They can be
    // converted to local variables! See below

    // variable to hold an instance of the WineItem object
    private WineItem mWine;

    private Button mContactButton;
    private Button mSendEmailButton;
    private String mContactEmail;
    private String mContactName;
    private String mIsActive;
    private int mFlag = 0;

    // WineFragment newInstance() method that will be
    // called when we need a new fragment
    public static WineFragment newInstance(UUID wineId) {

        // a bundle to store the args
        Bundle args = new Bundle();

        // put the string holding the wineId into the bundle
        args.putSerializable(ARG_WINE_ID, wineId);

        // create a new fragment
        WineFragment fragment = new WineFragment();

        // set the arguments, passing it args (which holds the wineId)
        fragment.setArguments(args);

        // return the fragment
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the string of the wineId from the intent used to start the host activity
        UUID wineId = (UUID) getArguments().getSerializable(ARG_WINE_ID);
        mContactName = getArguments().getString(ARG_CONTACT_NAME);
        mContactEmail = getArguments().getString(ARG_CONTACT_EMAIL_ADDR);

        // use the singleton to get the wine item with that Id
        mWine = WineShop.get(getActivity()).getWine(wineId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the view and store it in a vriable, v, so we can set up listeners
        View v = inflater.inflate(R.layout.fragment_wine, container, false);

        // The next several listener blocks are all basically identical, so I will
        // only comment this one. We've converted the widget assignments to local variables,
        // which is why we see an EditText preceding the actual variable here (instead of
        // defining it at the top). We assign each one to its view, then call setText() on
        // each of them (except the checkbox), using the getter in the WineItem instance
        // to retrieve the value.
        // Then we add an addTextChangedListener() to each. This appears to be an interface,
        // which is why it adds beforeTextChanged(), onTextChanged(), and afterTextChanged()
        // to each block when it is added. We aren't using two of those three methods, but
        // they are still required (hence the assumption of being an interface).
        EditText productName = (EditText) v.findViewById(R.id.wine_name);
        productName.setText(mWine.getName());
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {

                // We aren't going to use this

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // use the setter to assign CharSequence s to this EditText fiels
                mWine.setName(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

                // we aren't using this either

            }
        });

        EditText productId = (EditText) v.findViewById(R.id.wine_id);
        productId.setText(mWine.getId());
        productId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mWine.setId(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText productPack = (EditText) v.findViewById(R.id.wine_pack);
        productPack.setText(mWine.getPack());
        productPack.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mWine.setPack(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText productPrice = (EditText) v.findViewById(R.id.wine_price);
        productPrice.setText(mWine.getPrice());
        productPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mWine.setPrice(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set up a listener for the checkbox. This, apparently, is NOT
        // an interface, probably because there are only two possible states
        // and so doesn't require the handling of anything but a bool
        CheckBox productActive = (CheckBox) v.findViewById(R.id.wine_active);

        // this line toggles the state of the checkbox based on
        // the value stored in the WineItem isActive() getter
        productActive.setChecked(mWine.isActive());
        productActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // use the setter to set its status
                mWine.setActive(b);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mContactButton = (Button) v.findViewById(R.id.select_contact_button);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mContactButton.setEnabled(false);
        }

        mSendEmailButton = (Button) v.findViewById(R.id.send_email_button);
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mContactEmail, null));
                Resources emailResources = getResources();
                if (mWine.isActive() == true) {
                    mIsActive = emailResources.getString(R.string.wine_is_active);
                } else {
                    mIsActive = emailResources.getString(R.string.wine_is_inactive);
                }
                String wineReport = String.format(emailResources.getString(R.string.wine_report), mWine.getContactName(), mWine.getId(), mWine.getName(), mWine.getPack(), mWine.getPrice(), mIsActive);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.wine_report_subject));
                emailIntent.putExtra(Intent.EXTRA_TEXT, wineReport);
                startActivity(Intent.createChooser(emailIntent, "Send report via: "));
            }
        });

        if (mWine.getContactName() == null) {
            mSendEmailButton.setEnabled(false);
        }

        // return the view
        return v;
    }

    // This gets called twice consecutively and the values always go null the second time. Why???
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putString(ARG_CONTACT_NAME, mContactName);
            outState.putString(ARG_CONTACT_EMAIL_ADDR, mContactEmail);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();

            // Specify which fields we want to return data for
            String[] queryFields = new String[] {

                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID

            };

            ContentResolver contentResolver = getActivity().getContentResolver();

            Cursor c = getActivity().getContentResolver().query(
                    contactUri,
                    queryFields,
                    null,
                    null,
                    null
            );

            Cursor emailCursor = null;


            try {
                if (c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String contact = c.getString(0);
                String id = c.getString(1);

                String[] emailQueryFields = new String[]{
                        ContactsContract.CommonDataKinds.Email.DATA
                };

                emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        emailQueryFields,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);

                emailCursor.moveToFirst();
                Resources stringResources = getResources();
                mContactEmail = emailCursor.getString(0);
                mContactName = contact;
                mWine.setContactName(contact);
                String contactButtonText = String.format(stringResources.getString(R.string.send_email_to), contact);
                mContactButton.setText(contactButtonText);
                mSendEmailButton.setText(mContactEmail);
                if (mWine.getContactName() != null) {
                    mSendEmailButton.setEnabled(true);
                }

            } catch (Exception e) {
                Log.e("Crime", e.getMessage() + e.getStackTrace());
            } finally {
                if (c != null) {
                    c.close();
                }
                if (emailCursor != null) {
                    emailCursor.close();
                }
            }
        }
    }
}
