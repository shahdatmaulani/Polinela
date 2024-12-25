package com.example.polinelapeduli.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.polinelapeduli.model.Donation;
import com.example.polinelapeduli.utils.CurrentTime;
import com.example.polinelapeduli.utils.Enum.EStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationRepository {

    private static final String TAG = "DonationRepository";
    private final DatabaseHelper dbHelper;

    public DonationRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Inserts a new donation into the database.
     *
     * @param donation The donation object to insert.
     * @return True if insertion is successful, false otherwise.
     */
    public boolean insertDonation(Donation donation) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, donation.getName());
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, donation.getDescription());
            values.put(DatabaseHelper.COLUMN_CATEGORY_ID, donation.getCategoryId());
            values.put(DatabaseHelper.COLUMN_TARGET, donation.getTarget());
            values.put(DatabaseHelper.COLUMN_IMAGE, donation.getImage());
            values.put(DatabaseHelper.COLUMN_STATUS, donation.getStatus().toString());
            values.put(DatabaseHelper.COLUMN_IS_ACTIVE, donation.isActive() ? 1 : 0);
            values.put(DatabaseHelper.COLUMN_CREATED_AT, donation.getCreatedAt());
            values.put(DatabaseHelper.COLUMN_UPDATED_AT, donation.getUpdatedAt());

            long result = database.insert(DatabaseHelper.TABLE_DONATIONS, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting donation: ", e);
            return false;
        }
    }

    /**
     * Retrieves all active donations from the database.
     *
     * @return A list of active donations.
     */
    public List<Donation> getAllDonations() {
        List<Donation> donations = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_DONATIONS +
                " WHERE " + DatabaseHelper.COLUMN_IS_ACTIVE + " = 1";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, null)) {

            if (cursor.moveToFirst()) {
                do {
                    donations.add(mapCursorToDonation(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching donations: ", e);
        }
        return donations;
    }

    /**
     * Retrieves all donation details including category and total collected amount.
     *
     * @return A map where the key is the category name and the value is a list of donation details.
     */
    /**
     * Retrieves donation details filtered by category.
     *
     * @param category The category name to filter donations.
     * @return A map containing donation details grouped by category name.
     */
    public List<Donation> getAllDonationDetails(String category) {
        List<Donation> donations = new ArrayList<>();
        String query = "SELECT d." + DatabaseHelper.COLUMN_DONATION_ID + " AS donationId, " +
                "d." + DatabaseHelper.COLUMN_NAME + " AS donationName, " +
                "d." + DatabaseHelper.COLUMN_DESCRIPTION + " AS donationDescription, " +
                "d." + DatabaseHelper.COLUMN_CATEGORY_ID + " AS categoryId, " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_NAME + " AS categoryName, " +
                "d." + DatabaseHelper.COLUMN_TARGET + " AS donationTarget, " +
                "d." + DatabaseHelper.COLUMN_IMAGE + " AS donationImage, " +
                "d." + DatabaseHelper.COLUMN_STATUS + " AS donationStatus, " +
                "d." + DatabaseHelper.COLUMN_IS_ACTIVE + " AS isActive, " +
                "d." + DatabaseHelper.COLUMN_CREATED_AT + " AS donationCreatedAt, " +
                "d." + DatabaseHelper.COLUMN_UPDATED_AT + " AS donationUpdatedAt, " +
                "IFNULL(SUM(t." + DatabaseHelper.COLUMN_AMOUNT + "), 0) AS totalCollected " +
                "FROM " + DatabaseHelper.TABLE_DONATIONS + " d " +
                "JOIN " + DatabaseHelper.TABLE_CATEGORIES + " c " +
                "ON d." + DatabaseHelper.COLUMN_CATEGORY_ID + " = c." + DatabaseHelper.COLUMN_CATEGORY_ID + " " +
                "LEFT JOIN " + DatabaseHelper.TABLE_TRANSACTIONS + " t " +
                "ON d." + DatabaseHelper.COLUMN_DONATION_ID + " = t." + DatabaseHelper.COLUMN_DONATION_ID + " " +
                "WHERE c." + DatabaseHelper.COLUMN_CATEGORY_NAME + " = ? " +
                "AND d." + DatabaseHelper.COLUMN_IS_ACTIVE + " = 1 " + // Menambahkan filter isActive = 1
                "GROUP BY d." + DatabaseHelper.COLUMN_DONATION_ID + ", " +
                "d." + DatabaseHelper.COLUMN_NAME + ", " +
                "d." + DatabaseHelper.COLUMN_DESCRIPTION + ", " +
                "d." + DatabaseHelper.COLUMN_CATEGORY_ID + ", " +
                "c." + DatabaseHelper.COLUMN_CATEGORY_NAME + ", " +
                "d." + DatabaseHelper.COLUMN_TARGET + ", " +
                "d." + DatabaseHelper.COLUMN_IMAGE + ", " +
                "d." + DatabaseHelper.COLUMN_STATUS + ", " +
                "d." + DatabaseHelper.COLUMN_IS_ACTIVE + ", " +
                "d." + DatabaseHelper.COLUMN_CREATED_AT + ", " +
                "d." + DatabaseHelper.COLUMN_UPDATED_AT + " " +
                "ORDER BY d." + DatabaseHelper.COLUMN_DONATION_ID;

        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{category})) {

            while (cursor.moveToNext()) {
                Donation donation = new Donation(
                        cursor.getInt(cursor.getColumnIndexOrThrow("donationId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donationName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donationDescription")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("categoryId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("categoryName")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("donationTarget")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donationImage")),
                        EStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("donationStatus"))),
                        cursor.getInt(cursor.getColumnIndexOrThrow("isActive")) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow("donationCreatedAt")),
                        cursor.getString(cursor.getColumnIndexOrThrow("donationUpdatedAt")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("totalCollected"))
                );
                donations.add(donation);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching donation details by category: ", e);
        }

        return donations;
    }




    /**
     * Retrieves all donations for a specific category.
     *
     * @param category The name of the category.
     * @return A list of donations in the specified category.
     */
    public List<Donation> getAllDonationsWithCategory(String category) {
        List<Donation> donations = new ArrayList<>();
        String query = "SELECT d.*, c." + DatabaseHelper.COLUMN_CATEGORY_NAME + " AS category_name " +
                "FROM " + DatabaseHelper.TABLE_DONATIONS + " d " +
                "LEFT JOIN " + DatabaseHelper.TABLE_CATEGORIES + " c " +
                "ON d." + DatabaseHelper.COLUMN_CATEGORY_ID + " = c." + DatabaseHelper.COLUMN_CATEGORY_ID + " " +
                "WHERE d." + DatabaseHelper.COLUMN_IS_ACTIVE + " = 1 AND c." + DatabaseHelper.COLUMN_CATEGORY_NAME + " = ?";

        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{category})) {

            if (cursor.moveToFirst()) {
                do {
                    donations.add(mapCursorToDonationWithCategory(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching donations by category: ", e);
        }
        return donations;
    }

    /**
     * Retrieves a donation by its ID.
     *
     * @param donationId The ID of the donation.
     * @return The donation object, or null if not found.
     */
    public Donation getDonationById(int donationId) {
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_DONATIONS +
                " WHERE " + DatabaseHelper.COLUMN_DONATION_ID + " = ?";
        try (SQLiteDatabase database = dbHelper.getReadableDatabase();
             Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(donationId)})) {

            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToDonation(cursor);
            } else {
                Log.w(TAG, "No donation found with ID: " + donationId);
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching donation with ID: " + donationId, e);
            return null;
        }
    }

    /**
     * Updates an existing donation with a new status.
     *
     * @param name        The name of the donation.
     * @param description The description of the donation.
     * @param target      The target amount of the donation.
     * @param status      The updated status of the donation.
     * @param imagePath   The image path for the donation.
     * @return True if the update is successful, false otherwise.
     */
    public boolean updateDonation(String name, String description, int target, String status, String imagePath) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, name);
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
            values.put(DatabaseHelper.COLUMN_TARGET, target);
            values.put(DatabaseHelper.COLUMN_IMAGE, imagePath);
            values.put(DatabaseHelper.COLUMN_STATUS, status);
            values.put(DatabaseHelper.COLUMN_UPDATED_AT, CurrentTime.getCurrentTime());

            int rowsAffected = database.update(DatabaseHelper.TABLE_DONATIONS, values,
                    DatabaseHelper.COLUMN_NAME + " = ?",
                    new String[]{name});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error updating donation: ", e);
            return false;
        }
    }


    /**
     * Performs a soft delete on a donation.
     *
     * @param donationId The ID of the donation to delete.
     * @return True if the deletion is successful, false otherwise.
     */
    public boolean softDeleteDonation(int donationId) {
        try (SQLiteDatabase database = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_IS_ACTIVE, 0);
            values.put(DatabaseHelper.COLUMN_UPDATED_AT, CurrentTime.getCurrentTime());

            int rowsAffected = database.update(DatabaseHelper.TABLE_DONATIONS, values,
                    DatabaseHelper.COLUMN_DONATION_ID + " = ?",
                    new String[]{String.valueOf(donationId)});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error soft deleting donation with ID: " + donationId, e);
            return false;
        }
    }

    /**
     * Maps a Cursor to a Donation object.
     *
     * @param cursor The database cursor.
     * @return The mapped Donation object.
     */
    private Donation mapCursorToDonation(Cursor cursor) {
        return new Donation(
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DONATION_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TARGET)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMAGE)),
                EStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS))),
                cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_ACTIVE)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_AT)),
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_AT))
        );
    }

    /**
     * Maps a Cursor to a Donation object with category name.
     *
     * @param cursor The database cursor.
     * @return The mapped Donation object with category name.
     */
    private Donation mapCursorToDonationWithCategory(Cursor cursor) {
        Donation donation = mapCursorToDonation(cursor);
        String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));
        donation.setCategoryName(categoryName);
        return donation;
    }
}
