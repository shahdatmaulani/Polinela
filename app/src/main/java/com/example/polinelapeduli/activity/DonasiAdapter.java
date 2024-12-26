package com.example.polinelapeduli.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.polinelapeduli.R;
import com.example.polinelapeduli.model.Donation;
import com.example.polinelapeduli.model.Payment;
import com.example.polinelapeduli.model.Transaction;
import com.example.polinelapeduli.repository.DonationRepository;
import com.example.polinelapeduli.repository.PaymentRepository;
import com.example.polinelapeduli.repository.TransactionRepository;
import com.example.polinelapeduli.utils.CurrencyFormatter;
import com.example.polinelapeduli.utils.CurrentTime;
import com.example.polinelapeduli.utils.InputValidator;


import java.util.ArrayList;


public class DonasiAdapter extends ArrayAdapter<Donation> {

    private final Context context;
    private final ArrayList<Donation> donationList;
    private final String userRole;
    private final Integer userId;
    private final DonationRepository donationRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    public DonasiAdapter(@NonNull Context context, @NonNull ArrayList<Donation> donationList, String userRole, Integer userId) {
        super(context, 0, donationList);
        this.context = context;
        this.donationList = donationList;
        this.userRole = userRole;
        this.userId = userId;
        this.donationRepository = new DonationRepository(context);
        this.transactionRepository = new TransactionRepository(context);
        this.paymentRepository = new PaymentRepository(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Donation donation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_donasi, parent, false);
        }

        setupView(convertView, donation, position);
        return convertView;
    }

    private void setupView(View convertView, Donation donation, int position) {
        TextView tvNamaDonasi = convertView.findViewById(R.id.tvNamaDonasi);
        TextView tvDeskripsiDonasi = convertView.findViewById(R.id.tvDeskripsiDonasi);
        TextView tvStatusDonasi = convertView.findViewById(R.id.tvStatusDonasi);
        TextView tvTargetDonasi = convertView.findViewById(R.id.tvTargetDonasi);
        TextView tvTerkumpulDonasi = convertView.findViewById(R.id.tvTerkumpulDonasi);
        ImageView imageViewDonasi = convertView.findViewById(R.id.imageViewDonasi);
        Button btnDonasiSekarang = convertView.findViewById(R.id.btnDonasiSekarang);
        EditText etJumlahDonasi = convertView.findViewById(R.id.etJumlahDonasi);

        if (donation != null) {
            Log.d("DonasiAdapter", "Setting data for position " + position + ": " + donation);

            tvNamaDonasi.setText(donation.getName());
            tvDeskripsiDonasi.setText(donation.getDescription());
            tvStatusDonasi.setText(String.format("Status: %s", donation.getStatus().toString()));
            tvTargetDonasi.setText(String.format("Target: %s", CurrencyFormatter.formatCurrency(donation.getTarget())));
            tvTerkumpulDonasi.setText(String.format("Terkumpul: %s", CurrencyFormatter.formatCurrency(donation.getTotalCollected())));

            Glide.with(context)
                    .load(donation.getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageViewDonasi);

            if ("DITUTUP".equalsIgnoreCase(donation.getStatus().toString())) {
                btnDonasiSekarang.setVisibility(View.GONE);
                etJumlahDonasi.setVisibility(View.GONE);
            } else {
                btnDonasiSekarang.setVisibility(View.VISIBLE);
                etJumlahDonasi.setVisibility(View.VISIBLE);
            }

            btnDonasiSekarang.setOnClickListener(v -> handleDonationClick(etJumlahDonasi, donation));
            convertView.setOnLongClickListener(v -> handleLongClick(donation));
        }
    }

    private void handleDonationClick(EditText etJumlahDonasi, Donation donation) {
        Integer jumlahDonasi = InputValidator.getValidatedNumberWithMinValue(
                etJumlahDonasi,
                "Donasi tidak boleh kosong",
                "Jumlah donasi minimal Rp 1.000",
                1000
        );

        if (jumlahDonasi != null) {
            showPaymentOptionsDialog(jumlahDonasi, etJumlahDonasi, donation);
        }
    }

    private void showPaymentOptionsDialog(int jumlahDonasi, EditText etJumlahDonasi, Donation donation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih Metode Pembayaran");

        String[] paymentOptions = {"Bank Transfer", "QRIS", "GoPay", "OVO", "Dana"};

        builder.setItems(paymentOptions, (dialog, which) -> {
            String selectedOption = paymentOptions[which];
            handlePaymentOption(selectedOption, jumlahDonasi, etJumlahDonasi, donation);
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void handlePaymentOption(String selectedOption, int jumlahDonasi, EditText etJumlahDonasi, Donation donation) {
        try {
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setDonationId(donation.getDonationId());
            transaction.setAmount(jumlahDonasi);
            transaction.setCreatedAt(CurrentTime.getCurrentTime());

            boolean isTransactionInserted = transactionRepository.insertTransaction(transaction);

            if (!isTransactionInserted) {
                Toast.makeText(context, "Gagal menyimpan transaksi.", Toast.LENGTH_SHORT).show();
                return;
            }

            int transactionId = transactionRepository.getLastTransactionId();

            if (transactionId == -1) {
                Toast.makeText(context, "Gagal mendapatkan ID transaksi.", Toast.LENGTH_SHORT).show();
                return;
            }

            Payment payment = new Payment();
            payment.setTransactionId(transactionId);
            payment.setAmount(jumlahDonasi);
            payment.setMethod(selectedOption);
            payment.setPaidAt(CurrentTime.getCurrentTime());

            boolean isPaymentInserted = paymentRepository.insertPayment(payment);

            if (!isPaymentInserted) {
                Toast.makeText(context, "Gagal menyimpan pembayaran.", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (selectedOption) {
                case "Bank Transfer":
                case "QRIS":
                case "GoPay":
                case "OVO":
                case "Dana":
                    Toast.makeText(context, "Terima kasih atas donasi sebesar Rp " + jumlahDonasi + " via " + selectedOption, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "Metode pembayaran tidak valid.", Toast.LENGTH_SHORT).show();
                    break;
            }
            etJumlahDonasi.setText("");
        } catch (Exception e) {
            Log.e("DonasiAdapter", "Error handling payment option: ", e);
            Toast.makeText(context, "Terjadi kesalahan. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean handleLongClick(Donation donation) {
        if ("ADMIN".equals(userRole)) {
            showAdminOptionsDialog(donation);
        } else if("DITUTUP".equals(donation.getStatus().toString())){
            Toast.makeText(context, "Donasi Telah Ditutup", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ayo Donasi Sekarang dengan cara klik tombol Donasi Sekarang", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void showAdminOptionsDialog(Donation donation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih Opsi");
        String[] options = {"Edit", "Hapus"};
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                startEditDonationIntent(donation);
            } else if (which == 1) {
                deleteDonation(donation);
            }
        });
        builder.show();
    }

    private void startEditDonationIntent(Donation donation) {
        Intent intent = new Intent(context, EditDonasiActivity.class);
        intent.putExtra("idDonation", donation.getDonationId());
        context.startActivity(intent);
    }

    private void deleteDonation(Donation donation) {
        boolean isDeleted = donationRepository.softDeleteDonation(donation.getDonationId());
        if (isDeleted) {
            donationList.remove(donation);
            notifyDataSetChanged();
            Toast.makeText(context, "Donasi berhasil dihapus.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Gagal menghapus donasi.", Toast.LENGTH_SHORT).show();
        }
    }
}
