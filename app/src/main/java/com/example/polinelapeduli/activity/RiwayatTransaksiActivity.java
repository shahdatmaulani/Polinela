package com.example.polinelapeduli.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.User;
import com.example.polinelapeduli.model.dto.HistoryTransaction;
import com.example.polinelapeduli.repository.PaymentRepository;
import com.example.polinelapeduli.repository.UserRepository;
import com.example.polinelapeduli.utils.UserValidator;

import java.util.ArrayList;
import java.util.List;

public class RiwayatTransaksiActivity extends AppCompatActivity {

    private ListView listViewRiwayatDonasi;
    private ArrayList<String> riwayatDonasiList;
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_transaksi);

        listViewRiwayatDonasi = findViewById(R.id.listViewRiwayatDonasi);
        riwayatDonasiList = new ArrayList<>();

        // Initialize userRepository
        userRepository = new UserRepository(this);

        User userLogin = UserValidator.validateUser(this);

        // Inisialisasi paymentRepository
        paymentRepository = new PaymentRepository(this);

        if (userLogin == null) {
            finish();
            return;
        }

        // Get user by email
        User user = userRepository.getUserByEmail(userLogin.getEmail());
        if (user != null) {
            int userId = user.getUserId();
            loadRiwayatDonasi(userId);
        }

        // Set adapter untuk ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, riwayatDonasiList);
        listViewRiwayatDonasi.setAdapter(adapter);

        // Set listener untuk item click
        listViewRiwayatDonasi.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDonasi = riwayatDonasiList.get(position);
            Toast.makeText(RiwayatTransaksiActivity.this, "Selected: " + selectedDonasi, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadRiwayatDonasi(int userId) {
        riwayatDonasiList.clear();
        List<HistoryTransaction> payments = paymentRepository.getPaymentsByUserId(userId);

        if (payments.isEmpty()) {
            riwayatDonasiList.add("Belum ada riwayat donasi untuk pengguna ini.");
        } else {
            for (HistoryTransaction payment : payments) {
                String donasiInfo = " | Nama Donasi: " + payment.getDonationName()
                        + " | Kategori: " + payment.getCategoryName()
                        + " | Jumlah: Rp " + payment.getPaymentAmount()
                        + " | Metode: " + payment.getMethod()
                        + " | Tanggal: " + payment.getPaidAt();
                riwayatDonasiList.add(donasiInfo);
            }
        }
    }
}
