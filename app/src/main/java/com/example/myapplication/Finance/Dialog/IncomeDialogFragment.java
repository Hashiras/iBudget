package com.example.myapplication.Finance.Dialog;

import static com.example.myapplication.Data.TransactionType.INCOME;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Balance;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.Data.Helper.BalanceHelper;
import com.example.myapplication.Data.Helper.TransactionHelper;
import com.example.myapplication.Data.Transaction;
import com.example.myapplication.Finance.HomeFragment;
import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class IncomeDialogFragment extends DialogFragment {

    private EditText amount, desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.income_dialog_fragment, container, false);
        amount = view.findViewById(R.id.income_amount);
        desc = view.findViewById(R.id.expense_description);
        Button button = view.findViewById(R.id.income_add);

        button.setOnClickListener(v -> launchIncome());

        return view;
    }

    private void launchIncome() {
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
        transaction.setType(INCOME);
        transaction.setValue(income);
        transaction.setDesc(desc.getText().toString());
        TransactionHelper helper = new TransactionHelper(getContext());
        bal.setBalance(bal.getBalance() + income);
        Log.i("BalanceManager", "Balance with the id of " + bal.getID() + " is: " + bal.getBalance());
        balanceHelper.update(bal);
        helper.insert(transaction);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.app_container, new HomeFragment())
                .commit();
        dismiss();
    }
}
