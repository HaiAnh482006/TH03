package com.example.myapplication;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Model đại diện cho một khoản chi tiêu cá nhân.
 */
public class Expense implements Serializable {
    private String name;        // Tên khoản chi
    private long amount;        // Số tiền chi tiêu
    private String category;    // Danh mục (Ăn uống, Đi lại, Học tập, Giải trí, Khác)
    private String date;        // Ngày chi tiêu (dd/MM/yyyy)
    private String note;        // Ghi chú (không bắt buộc)

    public Expense() {
    }

    public Expense(String name, long amount, String category, String date, String note) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note != null ? note : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Định dạng số tiền có dấu phân cách hàng nghìn (vd: 30.000 đ)
     */
    public String getFormattedAmount() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + " đ";
    }

    public static String formatMoney(long money) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(money) + " đ";
    }
}