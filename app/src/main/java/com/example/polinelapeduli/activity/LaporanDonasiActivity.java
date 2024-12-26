package com.example.polinelapeduli.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.dto.ReportDonation;
import com.example.polinelapeduli.repository.DonationRepository;

import java.util.ArrayList;
import java.util.List;

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
            // Using the custom adapter
            LaporanDonasiAdapter adapter = new LaporanDonasiAdapter(this, donations);
            listViewLaporanDonasi.setAdapter(adapter);
        }
    }
}
