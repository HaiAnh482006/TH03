package com.example.myapplication;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Màn hình 2: Danh sách chi tiêu (MainActivity)
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvTotalExpense;
    private TextView tvExpenseCount;
    private RecyclerView rvExpenses;
    private View layoutEmptyState;
    private Button btnAddExpense;
    private ImageView btnLogout;

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    // Launcher mở màn hình Thêm khoản chi (AddExpenseActivity)
    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Expense newExpense = (Expense) result.getData()
                            .getSerializableExtra(AddExpenseActivity.EXTRA_NEW_EXPENSE);
                    if (newExpense != null) {
                        expenseList.add(newExpense);
                        adapter.notifyItemInserted(expenseList.size() - 1);
                        rvExpenses.scrollToPosition(expenseList.size() - 1);
                        updateUI();
                    }
                }
            }
    );

    // Launcher mở màn hình Chi tiết khoản chi (ExpenseDetailActivity)
    private final ActivityResultLauncher<Intent> detailExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    boolean isDeleted = result.getData()
                            .getBooleanExtra(ExpenseDetailActivity.ACTION_DELETE, false);
                    int position = result.getData()
                            .getIntExtra(ExpenseDetailActivity.EXTRA_POSITION, -1);

                    // Xóa khoản chi khỏi ArrayList khi người dùng nhấn nút Xóa
                    if (isDeleted && position >= 0 && position < expenseList.size()) {
                        expenseList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, expenseList.size() - position);
                        updateUI();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Xử lý System Bar Insets để tiêu đề không bị đè bởi Status Bar (tai thỏ / camera)
        WindowInsetsHelper.applyHeaderAndFooterInsets(this,
                findViewById(R.id.headerLayout),
                findViewById(R.id.bottomButtonLayout));

        // Ánh xạ View
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvExpenseCount = findViewById(R.id.tvExpenseCount);
        rvExpenses = findViewById(R.id.rvExpenses);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnLogout = findViewById(R.id.btnLogout);

        // Khởi tạo danh sách dữ liệu mẫu theo đúng yêu cầu đề bài
        initSampleData();

        // Thiết lập RecyclerView và Adapter
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter(this, expenseList, (expense, position) -> {
            // Nhấn vào một dòng để mở màn hình chi tiết
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra(ExpenseDetailActivity.EXTRA_EXPENSE, expense);
            intent.putExtra(ExpenseDetailActivity.EXTRA_POSITION, position);
            detailExpenseLauncher.launch(intent);
        });
        rvExpenses.setAdapter(adapter);

        // Sự kiện nút Thêm khoản chi (Mở AddExpenseActivity bằng Intent)
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addExpenseLauncher.launch(intent);
        });

        // Sự kiện nút Đăng xuất
        btnLogout.setOnClickListener(v -> confirmLogout());

        // Cập nhật tổng số tiền và trạng thái ban đầu
        updateUI();
    }

    /**
     * Dữ liệu mẫu ban đầu theo đề bài:
     * 1. Ăn sáng - 30.000 đ - Ăn uống
     * 2. Đổ xăng - 50.000 đ - Đi lại
     * 3. Mua sách - 120.000 đ - Học tập
     */
    private void initSampleData() {
        expenseList = new ArrayList<>();
        expenseList.add(new Expense("Ăn sáng", 30000, "Ăn uống", "15/10/2023", "Ăn phở bò buổi sáng"));
        expenseList.add(new Expense("Đổ xăng", 50000, "Đi lại", "15/10/2023", "Đổ xăng xe máy đi làm"));
        expenseList.add(new Expense("Mua sách", 120000, "Học tập", "15/10/2023", "Sách lập trình Android cơ bản"));
    }

    /**
     * Cộng dồn số tiền của tất cả khoản chi và cập nhật lên TextView tổng tiền.
     * Cập nhật số lượng khoản chi và trạng thái danh sách trống.
     */
    private void updateUI() {
        long totalAmount = 0;
        for (Expense expense : expenseList) {
            totalAmount += expense.getAmount();
        }

        // Cập nhật hiển thị tổng số tiền có dấu phân cách hàng nghìn
        tvTotalExpense.setText(Expense.formatMoney(totalAmount));

        // Cập nhật số lượng
        int count = expenseList.size();
        tvExpenseCount.setText(count + " khoản chi");

        // Hiển thị giao diện rỗng nếu không có dữ liệu
        if (count == 0) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvExpenses.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvExpenses.setVisibility(View.VISIBLE);
        }
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có muốn đăng xuất khỏi ứng dụng không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}