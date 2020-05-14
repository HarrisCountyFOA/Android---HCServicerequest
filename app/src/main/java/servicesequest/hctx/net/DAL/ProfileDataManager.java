package servicesequest.hctx.net.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import servicesequest.hctx.net.Model.Profile;

public class ProfileDataManager {

    public ArrayList<Profile> getAllProfiles(ServiceRequestDbHelper dbHelper)
    {
        ArrayList<Profile> profiles = new ArrayList<Profile>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

         String[] profile_columns = new String[] { ServiceRequestDbContract.ProfileEntry.COLUMN_ID, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME,
                 ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY,
                 ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL,
                 ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT};

        Cursor cu = db.query(ServiceRequestDbContract.ProfileEntry.TABLE_NAME, profile_columns, null, null, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_ID);
        int firstNamePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME);
        int lastNamePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS);
        int cityPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY);
        int statePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE);
        int zipCodePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE);
        int emailPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL);
        int primPhonePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE);
        int secPhonPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT);

        while(cu.moveToNext())
        {
            String id = cu.getString(idPos);
            String firstName = cu.getString(firstNamePos);
            String lastName = cu.getString(lastNamePos);
            String add = cu.getString(addPos);
            String city = cu.getString(cityPos);
            String state = cu.getString(statePos);
            String zipCode = cu.getString(zipCodePos);
            String email = cu.getString(emailPos);
            String primPhone = cu.getString(primPhonePos);
            String secPhon = cu.getString(secPhonPos);
            String precinct = cu.getString(prePos);

            Profile p1=new Profile();
            p1._id =id;
            p1.firstname = firstName;
            p1.lastname = lastName;
            p1.streetaddress = add;
            p1.city = city;
            p1.state = state;
            p1.zipcode = zipCode;
            p1.email = email;
            p1.primaryphone = primPhone;
            p1.secondaryphone = secPhon;
            p1.precinct = precinct;
            profiles.add(p1);
        }

        cu.close();
        return profiles;
    }

    public Profile getProfileById(ServiceRequestDbHelper dbHelper, String id)
    {
        Profile p1 = new Profile();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] profile_columns = new String[] { ServiceRequestDbContract.ProfileEntry.COLUMN_ID, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME,
                ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY,
                ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL,
                ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE, ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT};

        String selection = ServiceRequestDbContract.ProfileEntry.COLUMN_ID + " LIKE ? ";
        String[] selectionArgs = new String[]{ id };


        Cursor cu = db.query(ServiceRequestDbContract.ProfileEntry.TABLE_NAME, profile_columns, selection, selectionArgs, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_ID);
        int firstNamePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME);
        int lastNamePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS);
        int cityPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY);
        int statePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE);
        int zipCodePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE);
        int emailPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL);
        int primPhonePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE);
        int secPhonPos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT);

        while(cu.moveToNext())
        {
            String idDB = cu.getString(idPos);
            String firstName = cu.getString(firstNamePos);
            String lastName = cu.getString(lastNamePos);
            String add = cu.getString(addPos);
            String city = cu.getString(cityPos);
            String state = cu.getString(statePos);
            String zipCode = cu.getString(zipCodePos);
            String email = cu.getString(emailPos);
            String primPhone = cu.getString(primPhonePos);
            String secPhon = cu.getString(secPhonPos);
            String precinct = cu.getString(prePos);

            p1._id =idDB;
            p1.firstname = firstName;
            p1.lastname = lastName;
            p1.streetaddress = add;
            p1.city = city;
            p1.state = state;
            p1.zipcode = zipCode;
            p1.email = email;
            p1.primaryphone = primPhone;
            p1.secondaryphone = secPhon;
            p1.precinct = precinct;
        }

        cu.close();
        return p1;
    }

    public int updateProfile(ServiceRequestDbHelper dbHelper, Profile P)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME, P.firstname);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME, P.lastname);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS, P.streetaddress);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL, P.email);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE, P.primaryphone);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE, P.secondaryphone);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY, P.city);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE, P.state);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE, P.zipcode);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT, P.precinct);

        String selection = ServiceRequestDbContract.ProfileEntry.COLUMN_ID + " LIKE ? ";
        String[] selectionArgs = new String[]{ P._id };

        return db.update(ServiceRequestDbContract.ProfileEntry.TABLE_NAME, v, selection, selectionArgs);
    }

    public long insertProfile(ServiceRequestDbHelper dbHelper, Profile P)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_FIRSTNAME, P.firstname);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_LASTNAME, P.lastname);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ADDRESS, P.streetaddress);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_EMAIL, P.email);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRIMARYPHONE, P.primaryphone);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_SECONDARYPHONE, P.secondaryphone);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_CITY, P.city);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_STATE, P.state);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_ZIPCODE, P.zipcode);
        v.put(ServiceRequestDbContract.ProfileEntry.COLUMN_NAME_PRECINCT, P.precinct);


        return db.insert(ServiceRequestDbContract.ProfileEntry.TABLE_NAME, null, v);
    }

}
