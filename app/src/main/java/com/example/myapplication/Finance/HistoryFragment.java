package com.example.myapplication.Finance;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.HistoryAdapter;
import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Helper.TransactionHelper;
import com.example.myapplication.Data.Transaction;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    public HistoryFragment() {
        super(R.layout.history_fragment);
    }
    private SharedPreferences preferences;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = AuthenticationActivity.preferences;
        recyclerView = view.findViewById(R.id.history_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        new getHistoryTask().execute();
    }

    private class getHistoryTask extends AsyncTask<Void, Void, List<Transaction>>{
        @Override
        protected void onPostExecute(List<Transaction> transactions) {
            super.onPostExecute(transactions);
            adapter = new HistoryAdapter(transactions, getContext());
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            TransactionHelper helper = new TransactionHelper(getContext());
            List<Transaction> list = new ArrayList<>();
            for(Transaction data: helper.get()){
                if(data.getAccountID() == preferences.getInt("accountID", 0))
                    list.add(data);
            }
            return list;
        }
    }
}
