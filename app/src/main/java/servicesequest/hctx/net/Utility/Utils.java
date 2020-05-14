package servicesequest.hctx.net.Utility;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import servicesequest.hctx.net.R;

public class Utils {

    public static void customPopMessge(Context context, String Title, String Results, String IconType) {
        Drawable icon = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alertdetails, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(Html.fromHtml(Results));

        if (IconType.equals("error"))
            icon = ContextCompat.getDrawable(context, R.drawable.ic_highlight_off_black_24dp);
        else if (IconType.equals("instructions"))
            icon = ContextCompat.getDrawable(context, R.drawable.ic_info_outline_black_24dp);

        new android.app.AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setTitle(Title)
                .setView(layout)
                .setCancelable(false)
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // do nothing
                            }
                        }).show();
    }

}
