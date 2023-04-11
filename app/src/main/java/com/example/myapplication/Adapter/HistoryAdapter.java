package com.example.myapplication.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Data.Category;
import com.example.myapplication.Data.Helper.CategoryHelper;
import com.example.myapplication.Data.Transaction;
import com.example.myapplication.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<Transaction> list;
    private Context context;
    private CategoryHelper helper;
    public HistoryAdapter(List<Transaction> list, Context context) {
        this.list = list;
        this.context = context;
        helper = new CategoryHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_card_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Transaction data = list.get(position);

        viewHolder.getType().setText(data.getType().toString());
        new setCategoryTask().execute(new CategoryParam(data.getCategoryID(),viewHolder));
        viewHolder.getAmount().setText("" + data.getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView type, category, amount;
        public ViewHolder(View view) {
            super(view);
            type = view.findViewById(R.id.card_type);
            category = view.findViewById(R.id.card_category);
            amount = view.findViewById(R.id.card_amount);
        }

        public TextView getType() {
            return type;
        }

        public TextView getCategory() {
            return category;
        }

        public TextView getAmount() {
            return amount;
        }
    }

    private class CategoryParam{
        private int category;
        private ViewHolder viewHolder;

        public CategoryParam(int category, ViewHolder viewHolder) {
            this.category = category;
            this.viewHolder = viewHolder;
        }
    }

    private class CategoryReturn{
        private Category category;
        private ViewHolder viewHolder;

        public CategoryReturn(Category category, ViewHolder viewHolder) {
            this.category = category;
            this.viewHolder = viewHolder;
        }
    }

    private class setCategoryTask extends AsyncTask<CategoryParam, Void, CategoryReturn>{
        @Override
        protected void onPostExecute(CategoryReturn categoryReturn) {
            super.onPostExecute(categoryReturn);
            categoryReturn.viewHolder.getCategory().setText(categoryReturn.category.getName());
        }

        @Override
        protected CategoryReturn doInBackground(CategoryParam... categoryParams) {
            Category category = helper.get(categoryParams[0].category);
            CategoryReturn categoryParam = new CategoryReturn(category, categoryParams[0].viewHolder);
            return categoryParam;
        }
    }
}

