package com.example.polinelapeduli.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.dto.ReportDonation;
import com.example.polinelapeduli.utils.CurrencyFormatter;

import java.util.List;

public class LaporanDonasiAdapter extends ArrayAdapter<ReportDonation> {
    public LaporanDonasiAdapter(Context context, List<ReportDonation> donations) {
        super(context, 0, donations);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_donation, parent, false);
        }

        ReportDonation donation = getItem(position);

        TextView donationName = convertView.findViewById(R.id.donationName);
        TextView category = convertView.findViewById(R.id.category);
        TextView donationAmount = convertView.findViewById(R.id.donationAmount);
        TextView targetAmount = convertView.findViewById(R.id.targetAmount);

        assert donation != null;
        donationName.setText(donation.getDonationName());
        category.setText(donation.getCategory());
        donationAmount.setText(CurrencyFormatter.formatCurrency(donation.getAmount()));
        targetAmount.setText(CurrencyFormatter.formatCurrency(donation.getTargetAmount()));

        return convertView;
    }
}
