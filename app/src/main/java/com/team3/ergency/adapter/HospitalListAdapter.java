package com.team3.ergency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.team3.ergency.R;
import com.team3.ergency.helper.Hospital;
import com.team3.ergency.helper.NearbySearchResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by howard on 11/14/16.
 */

public class HospitalListAdapter extends ArrayAdapter<Hospital> {
    public HospitalListAdapter(Context context, ArrayList<Hospital> hospitals) {
        super(context, 0, hospitals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View hospitalListItemView = convertView;
        if (hospitalListItemView == null) {
            hospitalListItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.hospital_list_item, parent, false);
        }

        Hospital currentHospital = getItem(position);

        TextView name = (TextView) hospitalListItemView.findViewById(R.id.hli_name);
        TextView address = (TextView) hospitalListItemView.findViewById(R.id.hli_address);
        TextView area = (TextView) hospitalListItemView.findViewById(R.id.hli_area);
        TextView phone = (TextView) hospitalListItemView.findViewById(R.id.hli_phone);
        TextView eta = (TextView) hospitalListItemView.findViewById(R.id.hli_eta);
        TextView distance = (TextView) hospitalListItemView.findViewById(R.id.hli_distance);

        name.setText(currentHospital.getName());
        address.setText(currentHospital.getAddress());
        area.setText(currentHospital.getArea());
        phone.setText(currentHospital.getPhone());
        eta.setText(currentHospital.getDuration());
        distance.setText(currentHospital.getDistance());

        return hospitalListItemView;
    }

}
