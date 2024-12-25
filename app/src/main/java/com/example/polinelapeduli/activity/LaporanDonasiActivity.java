package com.example.polinelapeduli.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.Donation;
import com.example.polinelapeduli.model.dto.ReportDonation;
import com.example.polinelapeduli.repository.DonationRepository;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanDonasiActivity extends AppCompatActivity {

    private ListView listViewLaporanDonasi;
    private ArrayList<String> laporanDonasiList;
    private DonationRepository donationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_donasi);

        listViewLaporanDonasi = findViewById(R.id.listViewLaporanDonasi);
        laporanDonasiList = new ArrayList<>();

        donationRepository = new DonationRepository(this);

        loadLaporanDonasi();
    }

    private void loadLaporanDonasi() {
        laporanDonasiList.clear();
        List<ReportDonation> donations = donationRepository.getAllReportDonations();

        if (donations.isEmpty()) {
            laporanDonasiList.add("Belum ada laporan donasi.");
        } else {
            for (ReportDonation donation : donations) {
                // Corrected: using the getter methods from ReportDonation
                String donasiInfo = "Donasi " + donation.getDonationName() + " - " +
                        "Kategori: " + donation.getCategory() + " - " +
                        "Jumlah Donasi: " + formatCurrency(donation.getAmount()) + " - " +
                        "Target: " + formatCurrency(donation.getTargetAmount());
                laporanDonasiList.add(donasiInfo);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, laporanDonasiList);
        listViewLaporanDonasi.setAdapter(adapter);
    }


    private String formatCurrency(int amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
}
