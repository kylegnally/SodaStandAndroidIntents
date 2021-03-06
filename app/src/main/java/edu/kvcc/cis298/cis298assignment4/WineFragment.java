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

    // these would be used to store the contact name and email extras if they
    // didn't become null in onSaveInstanceState()
    private static final String ARG_CONTACT_NAME = "contact_name";
    private static final String ARG_CONTACT_EMAIL = "contact_email";

    // constant for contact request code
    private static final int REQUEST_CONTACT = 1;

    // Where are the variables to store our widgets??? They can be
    // converted to local variables! See below

    // variable to hold an instance of the WineItem object
    private WineItem mWine;

    // widgets
    private Button mContactButton;
    private Button mSendEmailButton;

    // strings to hold the contact name and email address
    private String mContactEmail;
    private String mContactName;

    // string to hold the prettified version of the isActive property of a WineItem
    // (this is used in the string supplied to the body of an email)
    private String mIsActive;

    // flag to tell us whether we have a contacts app
    private Boolean mHasContactsApp = true;

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

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the string of the wineId from the intent used to start the host activity
        UUID wineId = (UUID) getArguments().getSerializable(ARG_WINE_ID);

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

        // create an implicit intent to select a contact
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        // get the widget
        mContactButton = (Button) v.findViewById(R.id.select_contact_button);

        // set a listener
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start an activity to select a contact
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        // get the packagemanager activity so we can check if an app is present
        PackageManager packageManager = getActivity().getPackageManager();

        // check for a default contacts app to see if it is null (aka missing)
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {

            // if null, disable the select contact button and set the mHasContactsApp flag to false
            mContactButton.setEnabled(false);
            mHasContactsApp = false;
        }

        // get widget
        mSendEmailButton = (Button) v.findViewById(R.id.send_email_button);

        // set listener
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // RESTRUCTURE THIS CODE FOR BREVITY
                // Check for a false flag. If false, create an email intent that has no recipient
                if (mHasContactsApp == false) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
                    Resources emailResources = getResources();

                    // make the "currently active" string pretty
                    if (mWine.isActive() == true) {
                        mIsActive = emailResources.getString(R.string.wine_is_active);
                    } else {
                        mIsActive = emailResources.getString(R.string.wine_is_inactive);
                    }

                    // get the impersonal version of the wine report
                    String wineReport = String.format(emailResources.getString(R.string.wine_report_impersonal), mWine.getId(), mWine.getName(), mWine.getPack(), mWine.getPrice(), mIsActive);

                    // put the extras for the subject and text
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.wine_report_subject));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, wineReport);

                    // start the chooser activity for an email client
                    startActivity(Intent.createChooser(emailIntent, "Send report via: "));
                } else {

                    // if there's a true flag, create a new intent for an email and supply the personalized information for the message
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mContactEmail, null));
                    Resources emailResources = getResources();
                    if (mWine.isActive() == true) {
                        mIsActive = emailResources.getString(R.string.wine_is_active);
                    } else {
                        mIsActive = emailResources.getString(R.string.wine_is_inactive);
                    }

                    // get the personalized version of the string resource for the email subject and body
                    String wineReport = String.format(emailResources.getString(R.string.wine_report), mContactName, mWine.getId(), mWine.getName(), mWine.getPack(), mWine.getPrice(), mIsActive);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.wine_report_subject));
                    emailIntent.putExtra(Intent.EXTRA_TEXT, wineReport);
                    startActivity(Intent.createChooser(emailIntent, "Send report via: "));
                }
            }
        });

        // check to determine whether we should disable the "send email" button. This should only be
        // disabled is there is a default contacts app present
        if (mContactName == null && mHasContactsApp == true) {

            mSendEmailButton.setEnabled(false);

        }

        return v;
    }

    // This gets called twice and both extras become null the second time. Why??
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_CONTACT_NAME, mContactName);
        outState.putString(ARG_CONTACT_EMAIL, mContactEmail);
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

            // create an instance of the ContentResolver
            ContentResolver contentResolver = getActivity().getContentResolver();

            // instantiate a cursor, passing it the contactUri and the query fields
            Cursor c = getActivity().getContentResolver().query(
                    contactUri,
                    queryFields,
                    null,
                    null,
                    null
            );


            Cursor emailCursor = null;

            // if there's nothing in there just return
            try {
                if (c.getCount() == 0) {
                    return;
                }

                // move to the start and get the columns in the DB we will use
                c.moveToFirst();
                String contact = c.getString(0);
                String id = c.getString(1);

                // set them email query field params
                String[] emailQueryFields = new String[]{
                        ContactsContract.CommonDataKinds.Email.DATA
                };

                // create the query, using the content_uri as the URI string
                // and the contact_id for the selection parameter, and match the selection
                // to the id stored in a new string array
                emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        emailQueryFields,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);

                // move the email cursor to the beginning and get the results from the
                // specified coloumn
                emailCursor.moveToFirst();
                Resources stringResources = getResources();
                mContactEmail = emailCursor.getString(0);
                mContactName = contact;

                // set the contact button text to a nicely formatted string containing our
                // invitation to send to the selected contact
                mContactButton.setText(String.format(stringResources.getString(R.string.send_email_to), contact));

                // and show the user the address they'll send to
                mSendEmailButton.setText(mContactEmail);

                // enable the email button if the contact name is not null
                if (mContactName != null) {
                    mSendEmailButton.setEnabled(true);
                }

            // catch an exception and log it just in case
            } catch (Exception e) {
                Log.e("Wine", e.getMessage() + e.getStackTrace());
            } finally {

                // close the cursors
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
