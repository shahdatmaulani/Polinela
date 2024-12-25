package com.example.polinelapeduli.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.polinelapeduli.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    private static final String TAG = "TransactionRepository";
    private final DatabaseHelper dbHelper;

    public TransactionRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Maps a Cursor to a Transaction object.
     */
    private Transaction mapCursorToTransaction(Cursor cursor) {
        return new Transaction(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRANSACTION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONATION_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT))
        );
    }

    /**
     * Inserts a new transaction into the database.
     */
    public boolean insertTransaction(Transaction transaction) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_USER_ID, transaction.getUserId());
            values.put(DatabaseHelper.COLUMN_DONATION_ID, transaction.getDonationId());
            values.put(DatabaseHelper.COLUMN_AMOUNT, transaction.getAmount());
            values.put(DatabaseHelper.COLUMN_CREATED_AT, transaction.getCreatedAt());

            long result = database.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting transaction: ", e);
            return false;
        }
    }

    /**
     * Retrieves a transaction by its ID.
     */
    public Transaction getTransactionById(int transactionId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TRANSACTIONS + " WHERE " + DatabaseHelper.COLUMN_TRANSACTION_ID + " = ?";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(transactionId)})) {

            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToTransaction(cursor);
            } else {
                Log.w(TAG, "No transaction found with ID: " + transactionId);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching transaction with ID: " + transactionId, e);
            return null;
        }
    }

    /**
     * Get the last transaction ID from the database.
     */
    public int getLastTransactionId() {
        String query = "SELECT MAX(" + DatabaseHelper.COLUMN_TRANSACTION_ID + ") AS lastId FROM " + DatabaseHelper.TABLE_TRANSACTIONS;
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow("lastId"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching last transaction ID", e);
        }
        return -1; // Return -1 if no transaction ID found
    }

    /**
     * Retrieves all transactions for a specific user ID.
     */
    public List<Transaction> getTransactionsByUserId(int userId) {
        List<Transaction> transactionList = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TRANSACTIONS + " WHERE " + DatabaseHelper.COLUMN_USER_ID + " = ?";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(userId)})) {

            if (cursor.moveToFirst()) {
                do {
                    transactionList.add(mapCursorToTransaction(cursor));
                } while (cursor.moveToNext());
            } else {
                Log.w(TAG, "No transactions found for user ID: " + userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching transactions for user ID: " + userId, e);
        }
        return transactionList;
    }
}
