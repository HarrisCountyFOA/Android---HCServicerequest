package servicesequest.hctx.net.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

public class ServiceRequestDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ServiceRequest";
    Context _context;

    public ServiceRequestDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this._context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServiceRequestDbContract.ProfileEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.ContactEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.RequestEntry.SQL_CREATE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.PointEntry.SQL_CREATE_ENTRIES);

//        db.execSQL("INSERT INTO " + ServiceRequestDbContract.ContactEntry.TABLE_NAME + " (" +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT +
//                ") VALUES('Lina','Hidalgo','1001 Preston, Suite 911','Houston','Texas','77002','judge.hidalgo@cjo.hctx.net','(713) 274-7000','Harris County Judge','http://cjo.harriscountytx.gov/','Judge')");
//
//
//        db.execSQL("INSERT INTO " + ServiceRequestDbContract.ContactEntry.TABLE_NAME + " (" +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT +
//                ") VALUES('Rodney','Ellis','1001 Preston, 9th Floor','Houston','Texas','77002','Comm_Ellis@cp1.hctx.net','(713) 274-1000','Precinct 1 Commissioner','http://www.hcp1.net','Precinct 1')");
//
//        db.execSQL("INSERT INTO " + ServiceRequestDbContract.ContactEntry.TABLE_NAME + " (" +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT +
//                ") VALUES('Adrian','Garcia','1001 Preston, 9th Floor','Houston','Texas','77002','Comm_Ellis@cp1.hctx.net','(713) 274-2222','Precinct 2 Commissioner','https://www.hcp2.com/','Precinct 2')");
//
//        db.execSQL("INSERT INTO " + ServiceRequestDbContract.ContactEntry.TABLE_NAME + " (" +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT +
//                ") VALUES('Steve','Radack','1001 Preston, 9th Floor','Houston','Texas','77002','pct3@pct3.com','(713) 755-6306','Precinct 3 Commissioner','http://www.pct3.com/','Precinct 3')");
//
//        db.execSQL("INSERT INTO " + ServiceRequestDbContract.ContactEntry.TABLE_NAME + " (" +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_FIRSTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_LASTNAME + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ADDRESS + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_CITY + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_STATE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_ZIPCODE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_EMAIL + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRIMARYPHONE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_TITLE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_WEBSITE + ", " +
//                ServiceRequestDbContract.ContactEntry.COLUMN_NAME_PRECINCT +
//                ") VALUES('R. Jack','Cagle','1001 Preston, Suite 950','Houston','Texas','77002','cadir@hcp4.net','(832) 927-4444','Precinct 4 Commissioner','https://www.hcp4.net/','Precinct 4')");


//        Random generator = new Random();
//
//        int START = 100000000;
//        int END = 199999999;
//
//        long range = (long)START - (long)END + 1;
//        long fraction = (long)(range * generator.nextDouble());
//        int randomNumber =  (int)(fraction + START);
//
//        String un = Integer.toString(randomNumber);
//
//        Bitmap bitmap = ((BitmapDrawable)this._context.getResources().getDrawable(R.drawable.thumb_1_0)).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] Bitmap_Image = stream.toByteArray();
//
//        Bitmap bitmap2 = ((BitmapDrawable)this._context.getResources().getDrawable(R.drawable.thumb_1_6)).getBitmap();
//        ByteArrayOutputStream stream2= new ByteArrayOutputStream();
//        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
//        byte[] Bitmap_Image2 = stream2.toByteArray();
//
//        Bitmap bitmap3 = ((BitmapDrawable)this._context.getResources().getDrawable(R.drawable.thumb_1_3)).getBitmap();
//        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
//        bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, stream3);
//        byte[] Bitmap_Image3 = stream3.toByteArray();
//
//
//
//        java.util.Date date = new java.util.Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // insertReports(un,Bitmap_Image,"Pothole on Main St","29.766083","-95.358810","500 Main st, Houston, tx 77002","Precinct 1","Bridges/R&B","1091228d-fe77-e011-9cee-005056877f02","", "", "", "", "My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311 My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311 My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311.My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311 My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311 My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311.)",true, db);
        // insertReports(un,null,"Trash Pickup","29.6","-95.3","406 caroline, Houston, tx 77002","Precinct 1","Bridges/R&B","1091228d-fe77-e011-9cee-005056877f02","", "", "", "", "My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311.)",true, db);
        //  insertReports(un,Bitmap_Image2,"Broken stop sign","29.8","-95.6","406 caroline, Houston, tx 77002","Precinct 1","Bridges/R&B","1091228d-fe77-e011-9cee-005056877f02","", "", "", "", "My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311.)",true, db);
        //  insertReports(un,Bitmap_Image3,"Animal Pickup","29.8","-95.4","406 caroline, Houston, tx 77002","Precinct 1","Bridges/R&B","1091228d-fe77-e011-9cee-005056877f02","", "", "", "", "My pothole repair initiative is the first step in gaining your trust on this issue. ... It's the fastest way to report potholes and other street conditions. You may also call 311 to submit a report. (If outside Houston, dial 713.837. 0311.)",true, db);


    }

    public long insertReports(String UniquedID, byte[] Image, String ImageName, String Latitude, String Longitude,
                              String FullAddress, String Precinct, String RequestType, String RequestTypeValue,
                              String Area, String AreaValue, String Subdivision, String CrossStreet,
                              String Description, boolean Sent, SQLiteDatabase db) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();

        ContentValues values = new ContentValues();
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_UNIQUEID, UniquedID);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGE, Image);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGENAME, ImageName);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE, Latitude);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LONGITUDE, Longitude);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_FULLADDRESS, FullAddress);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_PRECINCT, Precinct);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE, RequestType);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE_VALUE, RequestTypeValue);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA, Area);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA_VALUE, AreaValue);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SUBDIVISION, Subdivision);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_CROSSSTREET, CrossStreet);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DESCRIPTION, Description);
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME, dateFormat.format(date));
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT, Sent);

        return db.insert(ServiceRequestDbContract.RequestEntry.TABLE_NAME, null, values);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ServiceRequestDbContract.ProfileEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.ContactEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.RequestEntry.SQL_DELETE_ENTRIES);
        db.execSQL(ServiceRequestDbContract.PointEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
