package com.example.polinelapeduli.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.Payment;
import com.example.polinelapeduli.utils.CurrencyFormatter;

import java.util.List;

public class RiwayatDonasiAdapter extends ArrayAdapter<Payment> {

    private final Context context;

    public RiwayatDonasiAdapter(Context context, List<Payment> transactions) {
        super(context, 0, transactions);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_transaksi, parent, false);
        }

        Payment transaction = getItem(position);

        TextView tvDonasiName = convertView.findViewById(R.id.tv_donasi_name);
        TextView tvDonasiDetails = convertView.findViewById(R.id.tv_donasi_details);
        TextView tvDonasiAmount = convertView.findViewById(R.id.tv_donasi_amount);

        assert transaction != null;
        tvDonasiName.setText(transaction.getDonationName());
        tvDonasiDetails.setText(String.format("Kategori: %s | Metode: %s | Tanggal: %s",
                transaction.getCategoryName(),
                transaction.getMethod(),
                transaction.getPaidAt()));
        tvDonasiAmount.setText(CurrencyFormatter.formatCurrency(transaction.getAmount()));

        return convertView;
    }
}
