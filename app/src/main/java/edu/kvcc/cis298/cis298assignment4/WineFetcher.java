package edu.kvcc.cis298.cis298assignment4;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by kyleg on 12/4/2017.
 */

public class WineFetcher {

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        // create a new HTTP connection to the specified URL. If we were to load data
        // from a secure site, we would use HTTPS
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // create a new output stream to hold the data that was read in from
            // the web source
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            // create an input stream from the HTTP connection
            InputStream in = connection.getInputStream();

            // if the response code from the server is not HTTP_OK
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {

                // burp at the user and tell the user why
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            // create some working variables
            int bytesRead = 0;

            // create a buffer that will hold up to 1024 bytes at a time
            byte[] buffer = new byte[1024];

            // while the bytes we are reading are greater than zero
            while ((bytesRead = in.read(buffer)) > 0) {
                // write those bytes into the output stream
                out.write(buffer, 0, bytesRead);
            }

            // close the output stream
            out.close();

            // return the array
            return out.toByteArray();
        } finally {

            // and finally disconnect
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {

        return new String(getUrlBytes(urlSpec));
    }

    public List<WineItem> fetchWines() {

        // make a new local list of crimes to hold what we parse
        // from the returned JSON string
        List<WineItem> wines = new ArrayList<>();
        try {
            String url = Uri.parse("http://barnesbrothers.homeserver.com/beverageapi/")
                    .buildUpon()

                    // add extra parameters here with the method
                    // .appendQueryParameters("param", "vale")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            // The root element we get back is an array, so convert it to JSONArray
            JSONArray jsonBody = new JSONArray(jsonString);
            parseWines(wines, jsonBody);

        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items. ", ioe);
        }

        // return the list of crimes now that they have been populated
        // by the parsing
        return wines;
    }

    private void parseWines(List<WineItem> wines, JSONArray jsonArray) throws IOException, JSONException {
        // loop through all the elements in the array that was sent
        // into this method
        for (int i = 0; i < jsonArray.length(); i++) {

            // fetch a single JSONObject out from the array based on
            // the current index
            JSONObject wineJsonObject = jsonArray.getJSONObject(i);

            // Pull the value from the JSONObject for the key "id"
            String id = wineJsonObject.getString("id");
            String name = wineJsonObject.getString("name");
            String pack = wineJsonObject.getString("pack");
            String price = wineJsonObject.getString("price");
            Boolean isActive = false;

            if (wineJsonObject.getString("isActive").equals("1")) {
                isActive = true;
            }

            // Use that value to create a new UUID for that string
            //UUID uuidForNewCrime = UUID.fromString(uuidString);

            // create a new crime using the UUID we just created
            WineItem wine = new WineItem(id, name, pack, price, isActive);

            // set the title for the crime by retrieving it from the
            // JSON object
            wine.setId(wineJsonObject.getString("id"));
            wine.setName(wineJsonObject.getString("name"));
            wine.setPack(wineJsonObject.getString("pack"));
            wine.setPrice(wineJsonObject.getString("price"));

            // evaluate the is_solved value from the object to see if it is equal
            // to 1. If true, is_solved for the crime is true
            wine.setActive(wineJsonObject.getString("isActive").equals("1"));

            // add the finished crime to the list of crimes that was passed in to this method
            wines.add(wine);
        }
    }
}
