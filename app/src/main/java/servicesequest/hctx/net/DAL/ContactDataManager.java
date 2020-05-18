package servicesequest.hctx.net.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.Utility.AppPreferences;

import static servicesequest.hctx.net.Utility.GeocodingLocation.getContacts;

public class ContactDataManager {

    public ArrayList<contact> getAllContacts(ServiceRequestDbHelper dbHelper) {
        ArrayList<contact> contacts = new ArrayList<contact>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] contact_columns = new String[]{ServiceRequestDbContract.ContactEntry.COLUMN_ID, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT};

        Cursor cu = db.query(ServiceRequestDbContract.ContactEntry.TABLE_NAME, contact_columns, null, null, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_ID);
        int firstNamePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME);
        int lastNamePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS);
        int cityPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY);
        int statePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE);
        int zipCodePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE);
        int emailPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL);
        int primPhonePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE);
        int titlePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE);
        int webPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT);

        while (cu.moveToNext()) {
            String id = cu.getString(idPos);
            String firstName = cu.getString(firstNamePos);
            String lastName = cu.getString(lastNamePos);
            String add = cu.getString(addPos);
            String city = cu.getString(cityPos);
            String state = cu.getString(statePos);
            String zipCode = cu.getString(zipCodePos);
            String email = cu.getString(emailPos);
            String primPhone = cu.getString(primPhonePos);
            String title = cu.getString(titlePos);
            String website = cu.getString(webPos);
            String precinct = cu.getString(prePos);


            contact p1 = new contact();
            p1._id = id;
            p1.firstname = firstName;
            p1.lastname = lastName;
            p1.streetaddress = add;
            p1.city = city;
            p1.state = state;
            p1.zipcode = zipCode;
            p1.email = email;
            p1.phone = primPhone;
            p1.title = title;
            p1.website = website;
            p1.zipcode = zipCode;
            contacts.add(p1);
        }

        cu.close();
        return contacts;
    }

    public contact getContactByPrecinct(ServiceRequestDbHelper dbHelper, String precinct) {
        contact c1 = new contact();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] contact_columns = new String[]{ServiceRequestDbContract.ContactEntry.COLUMN_ID, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL,
                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE, ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT};

        String selection = ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT + " LIKE ? ";
        String[] selectionArgs = new String[]{precinct};


        Cursor cu = db.query(ServiceRequestDbContract.ContactEntry.TABLE_NAME, contact_columns, selection, selectionArgs, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_ID);
        int firstNamePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME);
        int lastNamePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS);
        int cityPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY);
        int statePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE);
        int zipCodePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE);
        int emailPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL);
        int primPhonePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE);
        int titlePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE);
        int webPos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT);

        while (cu.moveToNext()) {
            String id = cu.getString(idPos);
            String firstName = cu.getString(firstNamePos);
            String lastName = cu.getString(lastNamePos);
            String add = cu.getString(addPos);
            String city = cu.getString(cityPos);
            String state = cu.getString(statePos);
            String zipCode = cu.getString(zipCodePos);
            String email = cu.getString(emailPos);
            String primPhone = cu.getString(primPhonePos);
            String title = cu.getString(titlePos);
            String website = cu.getString(webPos);
            String pt = cu.getString(prePos);

            c1._id = id;
            c1.firstname = firstName;
            c1.lastname = lastName;
            c1.streetaddress = add;
            c1.city = city;
            c1.state = state;
            c1.zipcode = zipCode;
            c1.email = email;
            c1.phone = primPhone;
            c1.title = title;
            c1.website = website;
            c1.zipcode = zipCode;
        }

        cu.close();
        return c1;
    }

    public long insertContact(ServiceRequestDbHelper dbHelper, contact P) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME, P.firstname);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME, P.lastname);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS, P.streetaddress);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL, P.email);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE, P.phone);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY, P.city);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE, P.state);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE, P.zipcode);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT, P.precinct);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE, P.title);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE, P.website);


        return db.insert(ServiceRequestDbContract.ContactEntry.TABLE_NAME, null, v);
    }

    public int updateContact(ServiceRequestDbHelper dbHelper, contact P) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME, P.firstname);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME, P.lastname);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS, P.streetaddress);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL, P.email);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE, P.phone);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY, P.city);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE, P.state);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE, P.zipcode);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE, P.title);
        v.put(ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE, P.website);

        String selection = ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT + " LIKE ? ";
        String[] selectionArgs = new String[]{P.precinct};

        return db.update(ServiceRequestDbContract.ContactEntry.TABLE_NAME, v, selection, selectionArgs);
    }

    public static void insertUpdateContacts(ServiceRequestDbHelper dbHelper, Context context, Boolean foreceUpdate) {
        AppPreferences _appPrefs = new AppPreferences(context, "UserProfile");
        Boolean contactsSet = _appPrefs.getBoolean("ContactsSet");

        if (!contactsSet || foreceUpdate) {
            ContactDataManager cdatamanager = new ContactDataManager();
            List<contact> fCon = getContacts();
            if (fCon != null && fCon.size() > 0) {
                for (int i = 0; i < fCon.size(); i++) {
                    contact jCon = (contact) fCon.get(i);
                    contact dbCon = cdatamanager.getContactByPrecinct(dbHelper, jCon.precinct);

                    if (dbCon._id != null && !dbCon._id.equals("")) {
                        cdatamanager.updateContact(dbHelper, jCon);
                    } else {
                        cdatamanager.insertContact(dbHelper, jCon);
                    }
                }

                _appPrefs.putBoolean("ContactsSet", true);
            }
        }
    }

}
