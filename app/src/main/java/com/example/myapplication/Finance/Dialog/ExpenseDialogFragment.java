package com.example.myapplication.Finance.Dialog;

import static com.example.myapplication.Data.TransactionType.EXPENSE;
import static com.example.myapplication.Data.TransactionType.INCOME;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Balance;
import com.example.myapplication.Data.Category;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.Data.Helper.BalanceHelper;
import com.example.myapplication.Data.Helper.CategoryHelper;
import com.example.myapplication.Data.Helper.TransactionHelper;
import com.example.myapplication.Data.Transaction;
import com.example.myapplication.Finance.HomeFragment;
import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDialogFragment extends DialogFragment {

    private EditText amount, desc;
    private Spinner category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expense_dialog_fragment, container, false);
        amount = view.findViewById(R.id.expense_amount);
        desc = view.findViewById(R.id.expense_description);
        category = view.findViewById(R.id.expense_category);
        Button button = view.findViewById(R.id.expense_add);
        Button cancel = view.findViewById(R.id.expense_cancel);

        populateCategories();

        button.setOnClickListener(v -> launchExpense());
        cancel.setOnClickListener(v -> dismiss());

        return view;
    }

    private void populateCategories(){
        List<String> categories = new ArrayList<>();
        categories.add("General Category");
        for(Category cat: new CategoryHelper(getContext()).get()){
            if(cat.getAccountID() == AuthenticationActivity.preferences.getInt("accountID", 0))
                categories.add(cat.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        category.setAdapter(adapter);
    }

    private void launchExpense() {
        Account account = new AccountHelper(getContext()).get(AuthenticationActivity.preferences.getInt("accountID", 0));
        BalanceHelper balanceHelper =  new BalanceHelper(getContext());
        Balance bal = balanceHelper.get(account.getBalanceID());
        Transaction transaction = new Transaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            transaction.setDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        else
            transaction.setDate(0);
        double income = Double.valueOf(amount.getText().toString());
        transaction.setAccountID(AuthenticationActivity.preferences.getInt("accountID", 0));
        transaction.setCategoryID(0);
        transaction.setType(EXPENSE);
        transaction.setValue(income);
        transaction.setDesc(desc.getText().toString());
        TransactionHelper helper = new TransactionHelper(getContext());
        bal.setBalance(bal.getBalance() - income);
        Log.i("BalanceManager", "Balance with the id of " + bal.getID() + " is: " + bal.getBalance());
        balanceHelper.update(bal);
        helper.insert(transaction);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.app_container, new HomeFragment())
                .commit();
        dismiss();
    }
}
