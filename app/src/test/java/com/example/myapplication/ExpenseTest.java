package com.example.myapplication;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExpenseTest {

    @Test
    public void testExpenseCreationAndFormatting() {
        Expense expense = new Expense("Ăn sáng", 30000, "Ăn uống", "15/10/2023", "Ăn phở bò");
        assertEquals("Ăn sáng", expense.getName());
        assertEquals(30000, expense.getAmount());
        assertEquals("Ăn uống", expense.getCategory());
        assertEquals("15/10/2023", expense.getDate());
        assertEquals("Ăn phở bò", expense.getNote());
        assertEquals("30.000 đ", expense.getFormattedAmount());
    }

    @Test
    public void testSampleDataTotal() {
        List<Expense> list = new ArrayList<>();
        list.add(new Expense("Ăn sáng", 30000, "Ăn uống", "15/10/2023", "Ăn phở bò"));
        list.add(new Expense("Đổ xăng", 50000, "Đi lại", "15/10/2023", "Đổ xăng xe máy"));
        list.add(new Expense("Mua sách", 120000, "Học tập", "15/10/2023", "Sách lập trình Android"));

        long total = 0;
        for (Expense e : list) {
            total += e.getAmount();
        }

        assertEquals(200000, total);
        assertEquals("200.000 đ", Expense.formatMoney(total));
    }

    @Test
    public void testDeleteExpenseUpdatesTotal() {
        List<Expense> list = new ArrayList<>();
        list.add(new Expense("Ăn sáng", 30000, "Ăn uống", "15/10/2023", "Ăn phở bò"));
        list.add(new Expense("Đổ xăng", 50000, "Đi lại", "15/10/2023", "Đổ xăng xe máy"));
        list.add(new Expense("Mua sách", 120000, "Học tập", "15/10/2023", "Sách lập trình Android"));

        // Xóa khoản chi đầu tiên (Ăn sáng 30.000)
        list.remove(0);

        long total = 0;
        for (Expense e : list) {
            total += e.getAmount();
        }

        assertEquals(170000, total);
        assertEquals("170.000 đ", Expense.formatMoney(total));
    }
}