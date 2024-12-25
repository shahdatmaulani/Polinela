package com.example.polinelapeduli.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.dto.ReportDonation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class LaporanDonasiAdapter extends ArrayAdapter<ReportDonation> {
    public LaporanDonasiAdapter(Context context, List<ReportDonation> donations) {
        super(context, 0, donations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_donation, parent, false);
        }

        ReportDonation donation = getItem(position);

        TextView donationName = convertView.findViewById(R.id.donationName);
        TextView category = convertView.findViewById(R.id.category);
        TextView donationAmount = convertView.findViewById(R.id.donationAmount);
        TextView targetAmount = convertView.findViewById(R.id.targetAmount);

        donationName.setText(donation.getDonationName());
        category.setText(donation.getCategory());
        donationAmount.setText(formatCurrency(donation.getAmount()));
        targetAmount.setText(formatCurrency(donation.getTargetAmount()));

        return convertView;
    }

    private String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
}
