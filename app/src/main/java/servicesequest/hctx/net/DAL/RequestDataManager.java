package servicesequest.hctx.net.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.Model.contact;

public class RequestDataManager {

    public ArrayList<Request> getAllRequests(ServiceRequestDbHelper dbHelper)
    {
        ArrayList<Request> requests = new ArrayList<Request>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] request_columns = new String[] { ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID, ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_UNIQUEID, ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGENAME,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LONGITUDE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_FULLADDRESS,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_PRECINCT,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE_VALUE,ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA_VALUE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SUBDIVISION,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_CROSSSTREET,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DESCRIPTION,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT
        };

        Cursor cu = db.query(ServiceRequestDbContract.RequestEntry.TABLE_NAME, request_columns, null, null, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID);
        int uniqueIdPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_UNIQUEID);
        int imagePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGE);
        int imageNamePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGENAME);
        int latPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE);
        int lonPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LONGITUDE);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_FULLADDRESS);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_PRECINCT);
        int reqTypePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE);
        int reqTypevaluePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE_VALUE);
        int reportsAreaPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA);
        int areaValPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA_VALUE);
        int subDivPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SUBDIVISION);
        int crossStreetPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_CROSSSTREET);
        int descPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DESCRIPTION);
        int dateTimepos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME);
        int sentPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT);

        while(cu.moveToNext())
        {
            String id = cu.getString(idPos);
            String uniqueId = cu.getString(uniqueIdPos);
            byte[] image = cu.getBlob(imagePos);
            String Image_Name = cu.getString(imageNamePos);
            String Latitude = cu.getString(latPos);
            String Longitude = cu.getString(lonPos);
            String FullAddress = cu.getString(addPos);
            String Precinct = cu.getString(prePos);
            String RequestType = cu.getString(reqTypePos);
            String RequestTypeValue = cu.getString(reqTypevaluePos);
            String Description = cu.getString(descPos);
            String Area = cu.getString(reportsAreaPos);
            String AreaValue = cu.getString(areaValPos);
            String Subdivision = cu.getString(subDivPos);
            String CrossStreet = cu.getString(crossStreetPos);
            String DateTimeval = cu.getString(dateTimepos);
            boolean Sent = (cu.getInt(sentPos) == 0) ? false : true;


            Request p1=new Request();
            p1._id =id;
            p1._UniqueId = uniqueId;
            p1.Image = image;
            p1.Image_Name = Image_Name;
            p1.Latitude = Latitude;
            p1.Longitude = Longitude;
            p1.FullAddress = FullAddress;
            p1.Precinct = Precinct;
            p1.RequestType = RequestType;
            p1.RequestTypeValue = RequestTypeValue;
            p1.Description = Description;
            p1.Area=Area;
            p1.AreaValue = AreaValue;
            p1.Subdivision = Subdivision;
            p1.CrossStreet = CrossStreet;
            p1.DateTime = DateTimeval;
            p1.Sent = Sent;
            requests.add(p1);
        }

        cu.close();

        ArrayList<Request> sortedrequests = new ArrayList<Request>();
        for(int i = requests.size() - 1; i >= 0; i--)
        {
            sortedrequests.add(requests.get(i));
        }

        return sortedrequests;
    }

    public Request getRequestByID(ServiceRequestDbHelper dbHelper, String Id)
    {
        Request p1 = new Request();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] request_columns = new String[] { ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID, ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_UNIQUEID, ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGENAME,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LONGITUDE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_FULLADDRESS,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_PRECINCT,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE_VALUE,ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA_VALUE,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SUBDIVISION,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_CROSSSTREET,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DESCRIPTION,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME,
                ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT
        };

        String selection = ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID + " LIKE ? ";
        String[] selectionArgs = new String[]{ Id };


        Cursor cu = db.query(ServiceRequestDbContract.RequestEntry.TABLE_NAME, request_columns, selection, selectionArgs, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_ID);
        int uniqueIdPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_UNIQUEID);
        int imagePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGE);
        int imageNamePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_IMAGENAME);
        int latPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE);
        int lonPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LONGITUDE);
        int addPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_FULLADDRESS);
        int prePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_PRECINCT);
        int reqTypePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE);
        int reqTypevaluePos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_REQUESTTYPE_VALUE);
        int reportsAreaPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA);
        int areaValPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_AREA_VALUE);
        int subDivPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SUBDIVISION);
        int crossStreetPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_CROSSSTREET);
        int descPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DESCRIPTION);
        int dateTimepos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME);
        int sentPos = cu.getColumnIndex(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT);

        while(cu.moveToNext())
        {
            String id = cu.getString(idPos);
            String uniqueId = cu.getString(uniqueIdPos);
            byte[] image = cu.getBlob(imagePos);
            String Image_Name = cu.getString(imageNamePos);
            String Latitude = cu.getString(latPos);
            String Longitude = cu.getString(lonPos);
            String FullAddress = cu.getString(addPos);
            String Precinct = cu.getString(prePos);
            String RequestType = cu.getString(reqTypePos);
            String RequestTypeValue = cu.getString(reqTypevaluePos);
            String Description = cu.getString(descPos);
            String Area = cu.getString(reportsAreaPos);
            String AreaValue = cu.getString(areaValPos);
            String Subdivision = cu.getString(subDivPos);
            String CrossStreet = cu.getString(crossStreetPos);
            String DateTimeval = cu.getString(dateTimepos);
            boolean Sent = (cu.getInt(sentPos) == 0) ? false : true;


            p1=new Request();
            p1._id =id;
            p1._UniqueId = uniqueId;
            p1.Image = image;
            p1.Image_Name = Image_Name;
            p1.Latitude = Latitude;
            p1.Longitude = Longitude;
            p1.FullAddress = FullAddress;
            p1.Precinct = Precinct;
            p1.RequestType = RequestType;
            p1.RequestTypeValue = RequestTypeValue;
            p1.Description = Description;
            p1.Area=Area;
            p1.AreaValue = AreaValue;
            p1.Subdivision = Subdivision;
            p1.CrossStreet = CrossStreet;
            p1.DateTime = DateTimeval;
            p1.Sent = Sent;
        }

        cu.close();
        return p1;
    }

    public long insertRequest(ServiceRequestDbHelper dbHelper, Request req)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int START = 100000000;
        int END = 199999999;
        Random generator = new Random();

        long range = (long)START - (long)END + 1;
        long fraction = (long)(range * generator.nextDouble());
        int randomNumber =  (int)(fraction + START);

        String un = Integer.toString(randomNumber);

        return  insertReports(un, req.Image, req.Image_Name, req.Latitude, req.Longitude, req.FullAddress, req.Precinct, req.RequestType, req.RequestTypeValue,"","", "", "", req.Description, req.Sent, db);
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
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_LATITUDE , Latitude);
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
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_DATETIME , dateFormat.format(date));
        values.put(ServiceRequestDbContract.RequestEntry.COLUMN_REPORTS_SENT , Sent);

        return db.insert(ServiceRequestDbContract.RequestEntry.TABLE_NAME, null, values);
    }


}
