package com.example.polinelapeduli.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.User;
import com.example.polinelapeduli.utils.UserValidator;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private String role;
    private TextView headerWelcome;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout kategoriBencana, kategoriPendidikan, kategoriKesehatan, kategoriKemanusiaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        User userLogin = UserValidator.validateUser(this);
        if (userLogin == null) {
            finish();
            return;
        }

        // View Initialization
        headerWelcome = findViewById(R.id.headerWelcome);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        kategoriBencana = findViewById(R.id.kategoriBencana);
        kategoriPendidikan = findViewById(R.id.kategoriPendidikan);
        kategoriKesehatan = findViewById(R.id.kategoriKesehatan);
        kategoriKemanusiaan = findViewById(R.id.kategoriKemanusiaan);

        // Setup
        setupUserDetails(userLogin);
        setupCategoryListeners();
        setupBottomNavigation();
    }

    private void setupUserDetails(User userLogin) {
            role = userLogin.getRole() != null ? String.valueOf(userLogin.getRole()) : "USER";
            String fullName = userLogin.getFullName() != null ? userLogin.getFullName() : "User";
            setupBottomNavigationMenu(role);
            setHeaderWelcome(fullName);
    }

    private void setupBottomNavigationMenu(String role) {
        bottomNavigationView.getMenu().clear(); // Clear existing menu
        if ("ADMIN".equals(role)) {
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_admin);
        } else {
            bottomNavigationView.inflateMenu(R.menu.bottom_nav_user);
        }
        // Set label visibility mode
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
    }

    private void setHeaderWelcome(String fullName) {
        headerWelcome.setText(String.format("Welcome%s!", fullName != null ? ", " + fullName : ""));
    }

    private void setupCategoryListeners() {
        kategoriBencana.setOnClickListener(v -> openCategoryActivity(BencanaActivity.class));
        kategoriPendidikan.setOnClickListener(v -> openCategoryActivity(PendidikanActivity.class));
        kategoriKesehatan.setOnClickListener(v -> openCategoryActivity(KesehatanActivity.class));
        kategoriKemanusiaan.setOnClickListener(v -> openCategoryActivity(KemanusiaanActivity.class));
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_SELECTED);
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }


    private void openCategoryActivity(Class<?> activityClass) {
        Intent intent = new Intent(HomeActivity.this, activityClass);
        startActivity(intent);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            return true;
        } else if (itemId == R.id.tambah_donasi && "ADMIN".equals(role)) {
            intent = new Intent(this, TambahDonasiActivity.class);
        } else if (itemId == R.id.laporan_donasi && "ADMIN".equals(role)) {
            intent = new Intent(this, LaporanDonasiActivity.class);
        } else if (itemId == R.id.riwayat_transaksi && "USER".equals(role)) {
            intent = new Intent(this, RiwayatTransaksiActivity.class);
        } else if (itemId == R.id.profile) {
            intent = new Intent(this, ProfileActivity.class);
        } else {
            return false;
        }

        startActivity(intent);
        return true;
    }
}
