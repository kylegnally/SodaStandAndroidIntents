package edu.kvcc.cis298.cis298assignment4;

import android.content.Context;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by kyleg on 10/23/2017.
 */

// Class to read the CSV
public class CSVReader {

    // variables to hold the context and an istance of WineShop
    private Context mContext;
    private WineShop mWineShop;

    // Constructor. Takes the context and passes it into the ReadCSV() method
    public CSVReader(Context context) {

        mContext = context;
        ReadCSV(mContext);

    }

    private void ReadCSV(Context context) {

        // create an instance of InputStream, get the resource, and open it
        InputStream csvFile = context.getResources().openRawResource(R.raw.beverage_list);

        // create an instance of Scanner and hand it the instance of InputStream
        Scanner listReader = new Scanner(csvFile);

        // define a local variable to hold a single line from the file
        String wineLine;

        // try to read a line
        try {

            // while there's a next line
            while (listReader.hasNextLine()) {

                // assign that line to the local string variable
                wineLine = listReader.nextLine();

                // if that line is not blank
                if (!(wineLine.contentEquals(""))) {

                    // call ProcessOneLine, passing it the line as a string
                    this.ProcessOneLine(wineLine);
                }
            }
        }

        // if the try block fails, close the instance of the Scanner
        finally {
            listReader.close();
        }
    }

    private void ProcessOneLine(String line) {

        // assign the singleton to mWineShop using its get() method
        mWineShop = WineShop.get(mContext);

        // parse each entry in the line from the file into an array
        String parts[] = line.split(",");

        String id = parts[0];
        String name = parts[1];
        String pack =  parts[2];
        String price = mContext.getString(R.string.dollar_sign) + parts[3];
        boolean active = false;

        // use a regular expression to pattern match the boolean,
        // and set the result to an actual boolean if it matches
        if (parts[4].matches("True"))
        {
            active = true;
        }

        // use the addWineItem() method in the instance of the WineShop
        // to add a single WineItem to the ArrayList in the WineShop class
        mWineShop.addWineItem(id, name, pack, price, active);

    }
}
