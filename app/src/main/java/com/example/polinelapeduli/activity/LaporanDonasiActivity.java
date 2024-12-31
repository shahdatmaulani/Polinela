package com.example.polinelapeduli.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.Donation;
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
        List<Donation> donations = donationRepository.getAllReportDonations();
        for (Donation donation : donations) {
            Log.d("LaporanDonasiActivity", "Donation Report: " +
                    "\nName: " + donation.getName() +
                    "\nCategory: " + donation.getCategoryName() +
                    "\nTotal Collected: " + donation.getTotalCollected() +
                    "\nTarget: " + donation.getTarget()
            );

            if (donations.isEmpty()) {
                laporanDonasiList.add("Belum ada laporan donasi.");
            } else {
                // Using the custom adapter
                LaporanDonasiAdapter adapter = new LaporanDonasiAdapter(this, donations);
                listViewLaporanDonasi.setAdapter(adapter);
            }
        }
    }
}
