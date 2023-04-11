package com.example.myapplication.Finance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Balance;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.Data.Helper.BalanceHelper;
import com.example.myapplication.Finance.Dialog.ExpenseDialogFragment;
import com.example.myapplication.Finance.Dialog.IncomeDialogFragment;
import com.example.myapplication.R;

import java.util.concurrent.atomic.AtomicReference;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        super(R.layout.home_fragment);
    }

    private TextView balance, account_username2;
    private Button budget, income, expense, history, reports, profile;

    private BalanceHelper balanceHelper;
    private AccountHelper accountHelper;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        account_username2 = view.findViewById(R.id.account_username2);
        balance = view.findViewById(R.id.home_balance);
        budget = view.findViewById(R.id.home_set_budget);
        income = view.findViewById(R.id.home_income);
        expense = view.findViewById(R.id.home_expense);
        history = view.findViewById(R.id.home_history);
        reports = view.findViewById(R.id.home_reports);
        profile = view.findViewById(R.id.home_profile);
        balanceHelper = new BalanceHelper(getContext());
        accountHelper = new AccountHelper(getContext());

        initValues();

        AtomicReference<DialogFragment> fragment = new AtomicReference<>();

        budget.setOnClickListener(v->{

        });
        income.setOnClickListener(v->{
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            AtomicReference<Fragment> prev = new AtomicReference<>();
            prev.set(getParentFragmentManager().findFragmentByTag("income"));
            if (prev.get() != null) {
                ft.remove(prev.get());
            }
            ft.addToBackStack(null);
            fragment.set(new IncomeDialogFragment());
            fragment.get().show(ft, "income");
            initValues();
        });
        expense.setOnClickListener(v->{
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            AtomicReference<Fragment> prev = new AtomicReference<>();
            prev.set(getParentFragmentManager().findFragmentByTag("expense"));
            if (prev.get() != null) {
                ft.remove(prev.get());
            }
            ft.addToBackStack(null);
            fragment.set(new ExpenseDialogFragment());
            fragment.get().show(ft, "expense");
            initValues();
        });
        history.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.app_container, new HistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });
        reports.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.app_container, new ReportFragment())
                    .addToBackStack(null)
                    .commit();
        });
        profile.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.app_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    public void initValues(){
        Account account = accountHelper.get(AuthenticationActivity.preferences.getInt("accountID", 0));
        Balance bal = balanceHelper.get(account.getBalanceID());
        balance.setText("P " + bal.getBalance());
        account_username2.setText("Hi " + account.getUsername() + "!");
    }
}
