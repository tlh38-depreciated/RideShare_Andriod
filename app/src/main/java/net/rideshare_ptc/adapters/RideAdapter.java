package net.rideshare_ptc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import net.rideshare_ptc.R;
import net.rideshare_ptc.Ride;

import java.util.ArrayList;
public class RideAdapter extends ArrayAdapter<Ride> {
    private static class ViewHolder {
        TextView id;
        TextView title;
    }

    public RideAdapter(Context context, ArrayList<Ride> Rides) {
        super(context, R.layout.item_ride, Rides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ride ride = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_ride, parent, false);

            viewHolder.id = (TextView) convertView.findViewById(R.id.value_note_id);
            viewHolder.title = (TextView) convertView.findViewById(R.id.value_note_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.id.setText(ride.getPickUpLoc());
        viewHolder.title.setText(ride.getDest());

        return convertView;
    }
}
