package servicesequest.hctx.net.DAL;

import android.provider.BaseColumns;
import java.lang.Integer;

public final  class ServiceRequestDbContract {

    private ServiceRequestDbContract() {}

    public static class ProfileEntry implements BaseColumns {
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME_FIRSTNAME= "firstname";
        public static final String COLUMN_NAME_LASTNAME= "lastname";
        public static final String COLUMN_NAME_ADDRESS = "streetaddress";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_ZIPCODE = "zipcode";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PRIMARYPHONE = "primaryphone";
        public static final String COLUMN_NAME_SECONDARYPHONE = "secondaryphone";
        public static final String COLUMN_NAME_PRECINCT = "precinct";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ProfileEntry.TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME_FIRSTNAME + " TEXT, " +
                        COLUMN_NAME_LASTNAME + " TEXT, " +
                        COLUMN_NAME_ADDRESS + " TEXT, " +
                        COLUMN_NAME_CITY + " TEXT, " +
                        COLUMN_NAME_STATE + " TEXT, " +
                        COLUMN_NAME_ZIPCODE + " TEXT, " +
                        COLUMN_NAME_EMAIL + " TEXT, " +
                        COLUMN_NAME_PRIMARYPHONE + " TEXT, " +
                        COLUMN_NAME_SECONDARYPHONE + " TEXT, " +
                        COLUMN_NAME_PRECINCT + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME;
    }

    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME_FIRSTNAME= "firstname";
        public static final String COLUMN_NAME_LASTNAME= "lastname";
        public static final String COLUMN_NAME_ADDRESS = "streetaddress";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_ZIPCODE = "zipcode";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_PRIMARYPHONE = "phone";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_WEBSITE = "website";
        public static final String COLUMN_NAME_PRECINCT = "precinct";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME_FIRSTNAME + " TEXT, " +
                        COLUMN_NAME_LASTNAME + " TEXT, " +
                        COLUMN_NAME_ADDRESS + " TEXT, " +
                        COLUMN_NAME_CITY + " TEXT, " +
                        COLUMN_NAME_STATE + " TEXT, " +
                        COLUMN_NAME_ZIPCODE + " TEXT, " +
                        COLUMN_NAME_EMAIL + " TEXT, " +
                        COLUMN_NAME_PRIMARYPHONE + " TEXT, " +
                        COLUMN_NAME_TITLE + " TEXT, " +
                        COLUMN_NAME_WEBSITE + " TEXT, " +
                        COLUMN_NAME_PRECINCT + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;
    }

    public static class RequestEntry implements BaseColumns {

        public static final String TABLE_NAME = "Reports";
        public static final String COLUMN_REPORTS_ID = "_id";
        public static final String COLUMN_REPORTS_UNIQUEID = "_UniqueId";
        public static final String COLUMN_REPORTS_IMAGE = "Image";
        public static final String COLUMN_REPORTS_IMAGENAME = "Image_Name";
        public static final String COLUMN_REPORTS_LATITUDE = "Latitude";
        public static final String COLUMN_REPORTS_LONGITUDE = "Longitude";
        public static final String COLUMN_REPORTS_FULLADDRESS = "FullAddress";
        public static final String COLUMN_REPORTS_PRECINCT = "Precinct";
        public static final String COLUMN_REPORTS_REQUESTTYPE = "RequestType";
        public static final String COLUMN_REPORTS_REQUESTTYPE_VALUE = "RequestTypeValue";
        public static final String COLUMN_REPORTS_AREA = "Area";
        public static final String COLUMN_REPORTS_AREA_VALUE = "AreaValue";
        public static final String COLUMN_REPORTS_SUBDIVISION = "Subdivision";
        public static final String COLUMN_REPORTS_CROSSSTREET = "CrossStreet";
        public static final String COLUMN_REPORTS_DESCRIPTION = "Description";
        public static final String COLUMN_REPORTS_DATETIME = "DateTime";
        public static final String COLUMN_REPORTS_SENT = "Sent";


        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + RequestEntry.TABLE_NAME + " (" +
                        COLUMN_REPORTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_REPORTS_UNIQUEID + " TEXT, " +
                        COLUMN_REPORTS_IMAGE + " blob, " +
                        COLUMN_REPORTS_IMAGENAME + " TEXT, " +
                        COLUMN_REPORTS_LATITUDE + " TEXT, " +
                        COLUMN_REPORTS_LONGITUDE + " TEXT, " +
                        COLUMN_REPORTS_FULLADDRESS + " TEXT, " +
                        COLUMN_REPORTS_PRECINCT + " TEXT, " +
                        COLUMN_REPORTS_REQUESTTYPE + " TEXT, " +
                        COLUMN_REPORTS_REQUESTTYPE_VALUE + " TEXT, " +
                        COLUMN_REPORTS_AREA + " TEXT, " +
                        COLUMN_REPORTS_AREA_VALUE + " TEXT, " +
                        COLUMN_REPORTS_SUBDIVISION + " TEXT, " +
                        COLUMN_REPORTS_CROSSSTREET + " TEXT, " +
                        COLUMN_REPORTS_DESCRIPTION + " TEXT, " +
                        COLUMN_REPORTS_DATETIME + " DATE, " +
                        COLUMN_REPORTS_SENT + " boolean)";


        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + RequestEntry.TABLE_NAME;
    }
}
