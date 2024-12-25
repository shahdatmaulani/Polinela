package com.example.polinelapeduli.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.dto.HistoryTransaction;

import java.util.List;

public class RiwayatDonasiAdapter extends ArrayAdapter<HistoryTransaction> {

    private Context context;
    private List<HistoryTransaction> transactions;

    public RiwayatDonasiAdapter(Context context, List<HistoryTransaction> transactions) {
        super(context, 0, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_riwayat_transaksi, parent, false);
        }

        HistoryTransaction transaction = getItem(position);

        TextView tvDonasiName = convertView.findViewById(R.id.tv_donasi_name);
        TextView tvDonasiDetails = convertView.findViewById(R.id.tv_donasi_details);
        TextView tvDonasiAmount = convertView.findViewById(R.id.tv_donasi_amount);

        tvDonasiName.setText(transaction.getDonationName());
        tvDonasiDetails.setText("Kategori: " + transaction.getCategoryName() + " | Metode: " + transaction.getMethod() + " | Tanggal: " + transaction.getPaidAt());
        tvDonasiAmount.setText("Rp " + transaction.getPaymentAmount());

        return convertView;
    }
}
