package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Màn hình Chi tiết khoản chi (ExpenseDetailActivity)
 */
public class ExpenseDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE = "EXTRA_EXPENSE";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String ACTION_DELETE = "ACTION_DELETE";

    private TextView tvDetailName;
    private TextView tvDetailAmount;
    private TextView tvDetailCategory;
    private TextView tvDetailDate;
    private TextView tvDetailNote;
    private Button btnBack;
    private Button btnDelete;
    private ImageView btnBackHeader;
    private FrameLayout flDetailCategoryBadge;
    private ImageView ivDetailCategoryIcon;

    private Expense currentExpense;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        // Xử lý System Bar Insets để tiêu đề không bị đè bởi Status Bar (tai thỏ / camera)
        WindowInsetsHelper.applyHeaderAndFooterInsets(this,
                findViewById(R.id.headerLayout),
                findViewById(R.id.scrollDetailExpense));

        // Ánh xạ View
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailAmount = findViewById(R.id.tvDetailAmount);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        btnBackHeader = findViewById(R.id.btnBackHeader);
        flDetailCategoryBadge = findViewById(R.id.flDetailCategoryBadge);
        ivDetailCategoryIcon = findViewById(R.id.ivDetailCategoryIcon);

        // Nhận dữ liệu khoản chi và vị trí qua Intent
        Intent intent = getIntent();
        if (intent != null) {
            currentExpense = (Expense) intent.getSerializableExtra(EXTRA_EXPENSE);
            position = intent.getIntExtra(EXTRA_POSITION, -1);
        }

        if (currentExpense != null) {
            displayExpenseDetails(currentExpense);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin khoản chi!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Sự kiện nút Quay lại (Đóng màn hình và trở về danh sách)
        btnBack.setOnClickListener(v -> finish());
        btnBackHeader.setOnClickListener(v -> finish());

        // Sự kiện nút Xóa khoản chi (Xóa khỏi ArrayList, quay lại danh sách)
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void displayExpenseDetails(Expense expense) {
        tvDetailName.setText(expense.getName());
        tvDetailAmount.setText(expense.getFormattedAmount());
        tvDetailCategory.setText(expense.getCategory());
        tvDetailDate.setText(expense.getDate());

        if (!TextUtils.isEmpty(expense.getNote())) {
            tvDetailNote.setText(expense.getNote());
        } else {
            tvDetailNote.setText(getString(R.string.no_note));
        }

        // Cập nhật huy hiệu danh mục
        setCategoryBadge(expense.getCategory());
    }

    private void setCategoryBadge(String category) {
        int iconRes;
        int colorRes;

        if (category == null) category = "";

        switch (category) {
            case "Ăn uống":
                iconRes = R.drawable.ic_food;
                colorRes = R.color.cat_food;
                break;
            case "Đi lại":
                iconRes = R.drawable.ic_transport;
                colorRes = R.color.cat_transport;
                break;
            case "Học tập":
                iconRes = R.drawable.ic_education;
                colorRes = R.color.cat_education;
                break;
            case "Giải trí":
                iconRes = R.drawable.ic_entertainment;
                colorRes = R.color.cat_entertainment;
                break;
            default:
                iconRes = R.drawable.ic_other;
                colorRes = R.color.cat_other;
                break;
        }

        ivDetailCategoryIcon.setImageResource(iconRes);
        int color = ContextCompat.getColor(this, colorRes);
        flDetailCategoryBadge.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_title))
                .setMessage(getString(R.string.confirm_delete_msg))
                .setPositiveButton(getString(R.string.btn_confirm), (dialog, which) -> {
                    // Trả kết quả thông báo xóa và vị trí cần xóa về cho MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ACTION_DELETE, true);
                    resultIntent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_OK, resultIntent);

                    Toast.makeText(ExpenseDetailActivity.this, getString(R.string.msg_expense_deleted), Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }
}