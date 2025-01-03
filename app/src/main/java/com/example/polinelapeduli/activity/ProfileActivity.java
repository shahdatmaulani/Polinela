package com.example.polinelapeduli.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.polinelapeduli.R;
import com.example.polinelapeduli.repository.UserRepository;
import com.example.polinelapeduli.model.User;
import com.example.polinelapeduli.utils.CurrentTime;
import com.example.polinelapeduli.utils.UserValidator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail;
    private ImageView imageViewProfile;
    private FirebaseAuth mAuth;
    private UserRepository userRepository;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User userLogin = UserValidator.validateUser(this);

        if (userLogin == null) {
            finish();
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        userRepository = new UserRepository(this);

        initViews();

        initImagePickerLauncher();

        loadUserData(userLogin.getEmail());
    }

    private void initViews() {
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        Button buttonUpload = findViewById(R.id.buttonUpload);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonListView = findViewById(R.id.goListUser);

        buttonUpload.setOnClickListener(v -> openFileChooser());
        buttonListView.setOnClickListener(v -> getListUser());
        buttonLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        updateProfilePicture(imageUri);
                    }
                }
        );
    }

    private void updateProfilePicture(Uri imageUri) {
        if (imageUri != null) {
            Glide.with(this).load(imageUri).into(imageViewProfile);

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                User dbUser = userRepository.getUserByEmail(user.getEmail());
                if (dbUser != null) {
                    dbUser.setProfilePicture(imageUri.toString());
                    dbUser.setUpdatedAt(CurrentTime.getCurrentTime());
                    userRepository.updateUser(dbUser);
                }
            }
        }
    }

    private void redirectToSignIn() {
        Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadUserData(String email) {
        if (email == null) {
            Toast.makeText(this, "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            String role = String.valueOf(user.getRole());
            Button buttonListView = findViewById(R.id.goListUser);
            textViewName.setText(String.format("Full Name: %s", user.getFullName()));
            textViewEmail.setText(String.format("Email: %s", user.getEmail()));

            if ("ADMIN".equals(role)) {
                buttonListView.setVisibility(View.VISIBLE);  // Show the button
            } else {
                buttonListView.setVisibility(View.GONE);  // Hide the button
            }

            if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
                String profilePicture = user.getProfilePicture();
                if (profilePicture.startsWith("file://")) {
                    imageViewProfile.setImageURI(Uri.parse(profilePicture));
                } else {
                    Glide.with(this).load(profilePicture).into(imageViewProfile);
                }
            } else {
                imageViewProfile.setImageResource(R.drawable.pic21); // Gambar default
            }
        } else {
            Toast.makeText(this, "Data pengguna tidak ditemukan di database", Toast.LENGTH_SHORT).show();
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Pilih Gambar"));
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> logout())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void logout() {
        mAuth.signOut();
        redirectToSignIn();
    }

    private void navigateToListUserView() {
        Intent intent = new Intent(this, GetUsersActivity.class);
        startActivity(intent);
    }
    private void getListUser() {
        navigateToListUserView();
    }
}
