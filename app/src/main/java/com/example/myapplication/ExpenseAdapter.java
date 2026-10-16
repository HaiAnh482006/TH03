package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter cho danh sách chi tiêu trong RecyclerView.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Expense expense, int position);
    }

    private final Context context;
    private final List<Expense> expenseList;
    private final OnItemClickListener listener;

    public ExpenseAdapter(Context context, List<Expense> expenseList, OnItemClickListener listener) {
        this.context = context;
        this.expenseList = expenseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.tvName.setText(expense.getName());

        String categoryDate = expense.getCategory() + " • " + expense.getDate();
        holder.tvCategoryDate.setText(categoryDate);

        holder.tvAmount.setText(expense.getFormattedAmount());

        // Cập nhật icon và màu sắc huy hiệu theo danh mục
        setCategoryBadge(holder, expense.getCategory());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(expense, holder.getAdapterPosition());
            }
        });
    }

    private void setCategoryBadge(ExpenseViewHolder holder, String category) {
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

        holder.ivCategoryIcon.setImageResource(iconRes);
        int color = ContextCompat.getColor(context, colorRes);
        holder.flCategoryBadge.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return expenseList != null ? expenseList.size() : 0;
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flCategoryBadge;
        ImageView ivCategoryIcon;
        TextView tvName;
        TextView tvCategoryDate;
        TextView tvAmount;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            flCategoryBadge = itemView.findViewById(R.id.flCategoryBadge);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvName = itemView.findViewById(R.id.tvItemExpenseName);
            tvCategoryDate = itemView.findViewById(R.id.tvItemExpenseCategoryDate);
            tvAmount = itemView.findViewById(R.id.tvItemExpenseAmount);
        }
    }
}