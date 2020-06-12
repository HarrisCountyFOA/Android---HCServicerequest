package servicesequest.hctx.net.DAL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import servicesequest.hctx.net.Model.Request;
import servicesequest.hctx.net.R;

public class RequestListInfoWindowAdapter extends RecyclerView.Adapter<RequestListInfoWindowAdapter.RequestListViewHolder> {
    private List<Request> _blist;
    private Context _context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Request item);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RequestListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View vi;

        public RequestListViewHolder(View v) {
            super(v);
            vi = v;
        }

        public void setData(final Request p, int position, final OnItemClickListener listener, int noOfreqs, final float density) {
            TextView txtName = (TextView) vi.findViewById(R.id.txvName);
            txtName.setText(p.Image_Name);

            Button btnScale = (Button) vi.findViewById(R.id.btnScale);
            final ImageView avatar = (ImageView) vi.findViewById(R.id.avatar);

            if (p.Image != null && p.Image.length > 0) {
                final Bitmap bmp = BitmapFactory.decodeByteArray(p.Image, 0, p.Image.length);
                final int inHeight = bmp.getHeight();
                final int inWidth = bmp.getWidth();
                avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                avatar.getLayoutParams().height = Math.round((float) 100 * density);
                if (inWidth > inHeight) {
                    btnScale.setVisibility(View.GONE);
                } else {
                    btnScale.setVisibility(View.VISIBLE);
                }

                avatar.setImageBitmap(bmp);
                avatar.setVisibility(View.VISIBLE);

                btnScale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (avatar.getScaleType().equals(ImageView.ScaleType.CENTER_CROP)) {
                            avatar.getLayoutParams().height = Math.round((float) inHeight * density);
                            avatar.setScaleType(ImageView.ScaleType.FIT_XY);
                        } else {
                            avatar.getLayoutParams().height = Math.round((float) 200 * density);
                            ;
                            avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }
                });
                btnScale.setVisibility(View.GONE);

            } else {
                avatar.setVisibility(View.GONE);
                btnScale.setVisibility(View.GONE);
            }

            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                java.util.Date date = dateFormat.parse(p.DateTime);

                TextView txvDate = (TextView) vi.findViewById(R.id.txvDate);
                txvDate.setText((new SimpleDateFormat("MMM dd, yyyy")).format(date));

                TextView txvTime = (TextView) vi.findViewById(R.id.txvTime);
                txvTime.setText((new SimpleDateFormat("hh.mm aa")).format(date));
            } catch (ParseException e) {

            }

            TextView txvaddress = (TextView) vi.findViewById(R.id.txvaddress);
            txvaddress.setText(p.FullAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(p);
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RequestListInfoWindowAdapter(List<Request> objList, Context context, OnItemClickListener listener) {
        _blist = objList;
        _context = context;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RequestListInfoWindowAdapter.RequestListViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                 int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_content, parent, false);
        RequestListInfoWindowAdapter.RequestListViewHolder vh = new RequestListInfoWindowAdapter.RequestListViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RequestListInfoWindowAdapter.RequestListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Request v = _blist.get(position);

        if (position == (_blist.size() - 1)) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            p.bottomMargin = 200;
            holder.itemView.setLayoutParams(p);
        } else {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            p.bottomMargin = 12;
            holder.itemView.setLayoutParams(p);
        }
        float density = _context.getResources().getDisplayMetrics().density;
        holder.setData(v, position, listener, _blist.size(), density);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _blist.size();
    }
}
