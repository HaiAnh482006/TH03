package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * Màn hình Thêm khoản chi (AddExpenseActivity)
 */
public class AddExpenseActivity extends AppCompatActivity {

    public static final String EXTRA_NEW_EXPENSE = "EXTRA_NEW_EXPENSE";

    private EditText etExpenseName;
    private EditText etExpenseAmount;
    private Spinner spCategory;
    private DatePicker datePicker;
    private EditText etExpenseNote;
    private Button btnSaveExpense;
    private ImageView btnBackAdd;

    // Danh sách 5 danh mục theo yêu cầu đề bài
    private final String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Xử lý System Bar Insets để tiêu đề không bị đè bởi Status Bar (tai thỏ / camera)
        WindowInsetsHelper.applyHeaderAndFooterInsets(this,
                findViewById(R.id.headerLayout),
                findViewById(R.id.scrollAddExpense));

        // Ánh xạ View
        etExpenseName = findViewById(R.id.etExpenseName);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        spCategory = findViewById(R.id.spCategory);
        datePicker = findViewById(R.id.datePicker);
        etExpenseNote = findViewById(R.id.etExpenseNote);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnBackAdd = findViewById(R.id.btnBackAdd);

        // Thiết lập Spinner danh mục
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // Khởi tạo ngày hiện tại cho DatePicker
        Calendar calendar = Calendar.getInstance();
        datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );

        // Sự kiện nút Quay lại
        btnBackAdd.setOnClickListener(v -> finish());

        // Sự kiện nút Lưu khoản chi
        btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String name = etExpenseName.getText().toString().trim();
        String amountStr = etExpenseAmount.getText().toString().trim();
        String note = etExpenseNote.getText().toString().trim();

        // 1. Kiểm tra tên khoản chi không được để trống
        if (TextUtils.isEmpty(name)) {
            etExpenseName.setError(getString(R.string.err_empty_name));
            etExpenseName.requestFocus();
            Toast.makeText(this, getString(R.string.err_empty_name), Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra số tiền: phải là số hợp lệ và lớn hơn 0
        if (TextUtils.isEmpty(amountStr)) {
            etExpenseAmount.setError(getString(R.string.err_invalid_amount));
            etExpenseAmount.requestFocus();
            Toast.makeText(this, getString(R.string.err_invalid_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(amountStr);
            if (amount <= 0) {
                etExpenseAmount.setError(getString(R.string.err_invalid_amount));
                etExpenseAmount.requestFocus();
                Toast.makeText(this, getString(R.string.err_invalid_amount), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            etExpenseAmount.setError(getString(R.string.err_invalid_amount));
            etExpenseAmount.requestFocus();
            Toast.makeText(this, getString(R.string.err_invalid_amount), Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Lấy danh mục từ Spinner
        String category = spCategory.getSelectedItem().toString();

        // 4. Lấy ngày chi tiêu từ DatePicker (định dạng dd/MM/yyyy)
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1; // Month bắt đầu từ 0
        int year = datePicker.getYear();
        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        // 5. Tạo đối tượng Expense
        Expense newExpense = new Expense(name, amount, category, date, note);

        // 6. Trả kết quả về màn hình danh sách (MainActivity)
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_NEW_EXPENSE, newExpense);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, getString(R.string.msg_expense_added), Toast.LENGTH_SHORT).show();
        finish();
    }
}