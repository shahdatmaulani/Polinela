package com.example.polinelapeduli.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.polinelapeduli.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private static final String TAG = "PaymentRepository";
    private final DatabaseHelper dbHelper;

    public PaymentRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Maps a Cursor to a Payment object.
     */
    private Payment mapCursorToPayment(Cursor cursor) {
        return new Payment(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAYMENT_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_METHOD)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAID_AT))
        );
    }

    /**
     * Inserts a new payment into the database.
     */
    public boolean insertPayment(Payment payment) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TRANSACTION_ID, payment.getTransactionId());
            values.put(DatabaseHelper.COLUMN_AMOUNT, payment.getAmount());
            values.put(DatabaseHelper.COLUMN_METHOD, payment.getMethod());
            values.put(DatabaseHelper.COLUMN_PAID_AT, payment.getPaidAt());

            long result = database.insert(DatabaseHelper.TABLE_PAYMENTS, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting payment: ", e);
            return false;
        }
    }

    /**
     * Retrieves a payment by its ID.
     */
    public Payment getPaymentById(int paymentId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PAYMENTS + " WHERE " + DatabaseHelper.COLUMN_PAYMENT_ID + " = ?";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(paymentId)})) {

            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToPayment(cursor);
            } else {
                Log.w(TAG, "No payment found with ID: " + paymentId);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching payment with ID: " + paymentId, e);
            return null;
        }
    }

    /**
     * Retrieves all payments associated with a transaction ID.
     */
    public List<Payment> getPaymentsByTransactionId(int transactionId) {
        List<Payment> paymentList = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PAYMENTS + " WHERE " + DatabaseHelper.COLUMN_TRANSACTION_ID + " = ?";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(transactionId)})) {

            if (cursor.moveToFirst()) {
                do {
                    paymentList.add(mapCursorToPayment(cursor));
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No payments found for transaction ID: " + transactionId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching payments for transaction ID: " + transactionId, e);
        }
        return paymentList;
    }
}
