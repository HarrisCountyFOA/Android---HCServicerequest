package servicesequest.hctx.net.DAL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import servicesequest.hctx.net.Model.GeoPoint;


public class PointDataManager {

    public ArrayList<GeoPoint> getAllPoints(ServiceRequestDbHelper dbHelper) {
        ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] point_columns = new String[]{ServiceRequestDbContract.PointEntry.COLUMN_ID, ServiceRequestDbContract.PointEntry.COLUMN_NAME_LAT,
                ServiceRequestDbContract.PointEntry.COLUMN_NAME_LONG};

        Cursor cu = db.query(ServiceRequestDbContract.PointEntry.TABLE_NAME, point_columns, null, null, null, null, null);

        int idPos = cu.getColumnIndex(ServiceRequestDbContract.PointEntry.COLUMN_ID);
        int latPos = cu.getColumnIndex(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LAT);
        int longPos = cu.getColumnIndex(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LONG);


        while (cu.moveToNext()) {
            String id = cu.getString(idPos);
            String lat = cu.getString(latPos);
            String longit = cu.getString(longPos);

            GeoPoint p1 = new GeoPoint();
            p1._id = id;
            p1.lat = lat;
            p1.longit = longit;

            points.add(p1);
        }

        cu.close();
        return points;
    }

    public void add_points(ServiceRequestDbHelper dbHelper, List<GeoPoint> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (GeoPoint p : list) {
                values.put(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LAT, p.lat);
                values.put(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LONG, p.longit);
                db.insert(ServiceRequestDbContract.PointEntry.TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public long insertPoint(ServiceRequestDbHelper dbHelper, GeoPoint P) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LAT, P.lat);
        v.put(ServiceRequestDbContract.PointEntry.COLUMN_NAME_LONG, P.longit);


        return db.insert(ServiceRequestDbContract.PointEntry.TABLE_NAME, null, v);
    }

}
