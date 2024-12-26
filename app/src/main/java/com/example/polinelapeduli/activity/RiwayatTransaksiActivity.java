package com.example.polinelapeduli.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.User;
import com.example.polinelapeduli.model.dto.HistoryTransaction;
import com.example.polinelapeduli.repository.PaymentRepository;
import com.example.polinelapeduli.repository.UserRepository;
import com.example.polinelapeduli.utils.UserValidator;

import java.util.List;

public class RiwayatTransaksiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_transaksi);

        ListView listViewRiwayatDonasi = findViewById(R.id.listViewRiwayatDonasi);

        // Initialize userRepository
        UserRepository userRepository = new UserRepository(this);

        // Validate logged-in user
        User userLogin = UserValidator.validateUser(this);

        // Initialize paymentRepository
        PaymentRepository paymentRepository = new PaymentRepository(this);

        if (userLogin == null) {
            finish();
            return;
        }

        // Get user by email
        User user = userRepository.getUserByEmail(userLogin.getEmail());
        if (user != null) {
            int userId = user.getUserId();

            // Load donation history
            List<HistoryTransaction> payments = paymentRepository.getPaymentsByUserId(userId);

            // Set custom adapter for ListView
            RiwayatDonasiAdapter adapter = new RiwayatDonasiAdapter(this, payments);
            listViewRiwayatDonasi.setAdapter(adapter);

            // Set listener for item click
            listViewRiwayatDonasi.setOnItemClickListener((parent, view, position, id) -> {
                HistoryTransaction selectedTransaction = (HistoryTransaction) parent.getItemAtPosition(position);
                Toast.makeText(RiwayatTransaksiActivity.this, "Selected: " + selectedTransaction.getDonationName(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
