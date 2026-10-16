package com.example.myapplication;

import android.app.Activity;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Tiện ích xử lý Window Insets để tránh giao diện bị tai thỏ / camera / thanh điều hướng đè lên.
 */
public class WindowInsetsHelper {

    public static void applyHeaderAndFooterInsets(Activity activity, View headerView, View footerView) {
        // Đảm bảo icon status bar (giờ, pin, sóng) có màu trắng tương phản trên nền xanh primary
        new WindowInsetsControllerCompat(activity.getWindow(), activity.getWindow().getDecorView())
                .setAppearanceLightStatusBars(false);

        if (headerView != null) {
            final int initialPaddingLeft = headerView.getPaddingLeft();
            final int initialPaddingRight = headerView.getPaddingRight();
            final int initialPaddingBottom = headerView.getPaddingBottom();
            ViewCompat.setOnApplyWindowInsetsListener(headerView, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(initialPaddingLeft, insets.top, initialPaddingRight, initialPaddingBottom);
                return windowInsets;
            });
        }

        if (footerView != null) {
            final int initialPaddingLeft = footerView.getPaddingLeft();
            final int initialPaddingTop = footerView.getPaddingTop();
            final int initialPaddingRight = footerView.getPaddingRight();
            final int initialPaddingBottom = footerView.getPaddingBottom();
            ViewCompat.setOnApplyWindowInsetsListener(footerView, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(initialPaddingLeft, initialPaddingTop, initialPaddingRight, initialPaddingBottom + insets.bottom);
                return windowInsets;
            });
        }
    }
}
