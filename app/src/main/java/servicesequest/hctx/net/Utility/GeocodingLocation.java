package servicesequest.hctx.net.Utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import servicesequest.hctx.net.Model.Point;
import servicesequest.hctx.net.Model.RequestTypeSet;
import servicesequest.hctx.net.Model.contact;

public class GeocodingLocation {


    public static Point getAddressFromLocation(final String locationAddress,
                                               final Context context) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Point result = null;
        try {
            List addressList = geocoder.getFromLocationName(locationAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = (Address) addressList.get(0);
                result = new Point();
                result.x = address.getLatitude();
                result.y = address.getLongitude();
            }
        } catch (IOException e) {
//            result = new Point();
//            result.x = 29.7;
//            result.y = -95.3;
        }

        return result;
    }


    private static final String URL = "https://apps.harriscountytx.gov/ServicesRequestMobile/srm.svc/FindCommPrecinct";

    public static String geoCode(String address, Context context) {
        String results = null;

        try {

            Point geoPoint = getAddressFromLocation(address, context);
            if (geoPoint != null) {
                java.net.URL json = new URL(URL + "?x=" + geoPoint.x + "&y=" + geoPoint.y);

                URLConnection jc = json.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));

                String line = reader.readLine();
                JSONObject jsonResponse = new JSONObject(line);
                return jsonResponse.getString("FindCommPrecinctResult");
            }
        } catch (Exception e) {
            results = null;
        }

        return results;
    }


    private static final String URL1 = "https://apps.harriscountytx.gov/ServicesRequestMobile/srm.svc/GetRequestTypePrecinct";

    public static boolean loadDB(Double x, Double y) {
        boolean results = true;

        try {
            URL json = new URL(URL1 + "?x=" + x.toString() + "&y=" + y.toString());
            URLConnection jc = json.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));

            String line = reader.readLine();

            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("GetRequestTypePrecinctResult");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = (JSONObject) jsonArray.get(i);

                RequestTypeSet.requestTypeSet.add(new RequestTypeSet(jObject.getString("Name"),
                        jObject.getString("Category"),
                        jObject.getString("Code")
                ));
            }

        } catch (Exception e) {
            Log.i("LOG", "Error: " + e.toString());
            results = false;
        }

        return results;
    }

    private static final String contactUrl = "https://apps.harriscountytx.gov/ServicesRequestMobile/GetAllPctContactInfo.json";

    public static List<contact> getContacts() {
        ArrayList<contact> results = new ArrayList<contact>();

        try {
            URL json = new URL(contactUrl);
            URLConnection jc = json.openConnection();

            String alllines = "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                alllines += line;
            }


            JSONObject jsonResponse = new JSONObject(alllines);
            JSONArray jsonArray = jsonResponse.getJSONArray("GetAllPctContactInfoResult");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = (JSONObject) jsonArray.get(i);

                contact c = new contact();
                c.precinct = jObject.getString("Precinct");

                if (c.precinct.equals("-1")) {
                    c.precinct = "Judge";
                } else {
                    c.precinct = "Precinct " + c.precinct;
                }

                c.title = jObject.getString("Title");
                c.firstname = jObject.getString("Name");
                c.lastname = "";
                c.streetaddress = jObject.getString("Address1");
                c.city = jObject.getString("Address2");
                c.phone = jObject.getString("Phone");
                c.email = jObject.getString("Email");
                c.website = jObject.getString("Url");
                c.state = "";
                c.zipcode = "";
                results.add(c);
            }

        } catch (Exception e) {
            Log.i("LOG", "Error: " + e.toString());
            results = null;
        }

        return results;
    }
}
