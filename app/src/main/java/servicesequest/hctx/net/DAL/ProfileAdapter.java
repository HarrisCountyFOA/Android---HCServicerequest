package servicesequest.hctx.net.DAL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import servicesequest.hctx.net.Model.Profile;
import servicesequest.hctx.net.R;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private List<Profile> _blist;
    private Context _context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View vi;
        public ProfileViewHolder(View v) {
            super(v);
            vi = v;
        }

        public void setData(Profile p, int position)
        {
            TextView txtName =  (TextView)vi.findViewById(R.id.txtText);
            txtName.setText(p.firstname);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProfileAdapter(List<Profile> objList, Context context) {
        _blist = objList;
        _context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProfileAdapter.ProfileViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_list_item, parent, false);
        ProfileViewHolder vh = new ProfileViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Profile v = _blist.get(position);
        holder.setData(v, position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _blist.size();
    }
}