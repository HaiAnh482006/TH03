package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Màn hình Đăng nhập (LoginActivity)
 * Tài khoản cố định: admin / 123456
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định khai báo trong code theo yêu cầu đề bài
    private static final String FIXED_USERNAME = "admin";
    private static final String FIXED_PASSWORD = "123456";

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvErrorMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Xử lý System Bar Insets để tiêu đề không bị đè bởi Status Bar (tai thỏ / camera)
        WindowInsetsHelper.applyHeaderAndFooterInsets(this,
                findViewById(R.id.headerLayout),
                findViewById(R.id.scrollLogin));

        // Ánh xạ các thành phần View
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);

        // Xử lý sự kiện nhấn nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Kiểm tra không cho phép bỏ trống tên đăng nhập hoặc mật khẩu
        if (TextUtils.isEmpty(username)) {
            showError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return;
        }

        // 2. So sánh thông tin nhập với tài khoản cố định trong code
        if (FIXED_USERNAME.equals(username) && FIXED_PASSWORD.equals(password)) {
            // Ẩn thông báo lỗi nếu có
            tvErrorMessage.setVisibility(View.GONE);

            Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

            // Chuyển sang MainActivity bằng Intent
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            // Gọi finish() để người dùng không quay lại màn hình đăng nhập bằng nút Back
            finish();
        } else {
            // 3. Sai tên đăng nhập hoặc mật khẩu: hiển thị thông báo lỗi và ở lại màn hình đăng nhập
            showError(getString(R.string.err_invalid_credentials));
            Toast.makeText(LoginActivity.this, getString(R.string.err_invalid_credentials), Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
}