package com.example.polinelapeduli.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    public static String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount);
    }
}

