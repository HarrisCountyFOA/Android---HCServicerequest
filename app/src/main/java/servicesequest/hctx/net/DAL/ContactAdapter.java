package servicesequest.hctx.net.DAL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.Model.contact;
import servicesequest.hctx.net.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<contact> _blist;
    private Context _context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View vi;
        public ContactViewHolder(View v) {
            super(v);
            vi = v;
        }

        public void setData(contact p, int position)
        {
            TextView txtName =  (TextView)vi.findViewById(R.id.name);
            txtName.setText(p.firstname);

            TextView txtTitle =  (TextView)vi.findViewById(R.id.title);
            txtTitle.setText(p.title);

            TextView txtEmail =  (TextView)vi.findViewById(R.id.email);
            txtEmail.setText(p.email);

            TextView txtPhone =  (TextView)vi.findViewById(R.id.phone);
            txtPhone.setText(p.phone);

            TextView website =  (TextView)vi.findViewById(R.id.website);
            website.setText(p.website);

            TextView txtaddress =  (TextView)vi.findViewById(R.id.address);
            txtaddress.setText(p.streetaddress);

            TextView txtcity =  (TextView)vi.findViewById(R.id.city);
            txtcity.setText(p.city );
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(List<contact> objList, Context context) {
        _blist = objList;
        _context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_content, parent, false);
        ContactAdapter.ContactViewHolder vh = new ContactAdapter.ContactViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        contact v = _blist.get(position);

        if (position == (_blist.size() - 1)) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            p.bottomMargin = 240;
            holder.itemView.setLayoutParams(p);
        } else {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            p.bottomMargin = 12;
            holder.itemView.setLayoutParams(p);
        }

        holder.setData(v, position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _blist.size();
    }
}
