package servicesequest.hctx.net.DAL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;

public class RequestInfoWindowAdapater implements GoogleMap.InfoWindowAdapter {

    private Context _context;
    private View _mWindow;
    String requestId;
    ServiceRequestDbHelper dbHelper;
    RequestDataManager manager;

    public RequestInfoWindowAdapater(Context context) {
        _context = context;
        _mWindow = (View) LayoutInflater.from(context)
                .inflate(R.layout.activity_request_infolist, null);
    }

    private void renderWindow(Marker marker, View vi)
    {
        String id = marker.getSnippet();

        dbHelper = new ServiceRequestDbHelper(_context);
        manager = new RequestDataManager();
        Request p = manager.getRequestByID(dbHelper, id);

        TextView txtName = (TextView) vi.findViewById(R.id.txvName);
        txtName.setText(p.Image_Name);

        ImageView avatar = (ImageView) vi.findViewById(R.id.avatar);

        if (p.Image != null && p.Image.length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(p.Image, 0, p.Image.length);
            avatar.setImageBitmap(bmp);
            avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
            avatar.setVisibility(View.VISIBLE);
        } else {
            avatar.setVisibility(View.GONE);
        }

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            java.util.Date date = dateFormat.parse(p.DateTime);

            TextView txvDate = (TextView) vi.findViewById(R.id.txvDate);
            txvDate.setText((new SimpleDateFormat("MMM dd yyyy")).format(date));

            TextView txvTime = (TextView) vi.findViewById(R.id.txvTime);
            txvTime.setText((new SimpleDateFormat("hh.mm aa")).format(date));

            Button btnScale =(Button) vi.findViewById(R.id.btnScale);
            btnScale.setVisibility(View.GONE);

        } catch (ParseException e) {

        }

        TextView txvaddress = (TextView) vi.findViewById(R.id.txvaddress);
        txvaddress.setText(p.FullAddress);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker, _mWindow);
        return _mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker, _mWindow);
        return _mWindow;
    }
}
