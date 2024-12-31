package com.example.polinelapeduli.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.Donation;
import com.example.polinelapeduli.utils.CurrencyFormatter;

import java.util.List;

public class LaporanDonasiAdapter extends ArrayAdapter<Donation> {
    public LaporanDonasiAdapter(Context context, List<Donation> donations) {
        super(context, 0, donations);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_donation, parent, false);
        }

        Donation donations = getItem(position);

        TextView donationName = convertView.findViewById(R.id.donationName);
        TextView category = convertView.findViewById(R.id.category);
        TextView donationAmount = convertView.findViewById(R.id.donationAmount);
        TextView targetAmount = convertView.findViewById(R.id.targetAmount);

        assert donations != null;
        donationName.setText(donations.getName());
        category.setText(donations.getCategoryName());
        donationAmount.setText(CurrencyFormatter.formatCurrency(donations.getTotalCollected()));
        targetAmount.setText(CurrencyFormatter.formatCurrency(donations.getTarget()));

        return convertView;
    }
}
