package net.rideshare_ptc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import net.rideshare_ptc.R;
import net.rideshare_ptc.Ride;

import java.util.ArrayList;
public class RideAdapter extends ArrayAdapter<Ride> {


    public RideAdapter(Context context, ArrayList<Ride> Rides) {
        super(context, R.layout.item_ride, Rides);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_ride, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Ride currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        //ImageView numbersImage = currentItemView.findViewById(R.id.imageView);
        assert currentNumberPosition != null;
        //numbersImage.setImageResource(currentNumberPosition.getNumbersImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.pickUpText);
        textView1.setText(currentNumberPosition.getPickUpLoc());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.desText);
        textView2.setText(currentNumberPosition.getDest());

        TextView textView3 = currentItemView.findViewById(R.id.dateText);
        textView3.setText(currentNumberPosition.getRideDate());

        // then return the recyclable view
        return currentItemView;
    }
}
