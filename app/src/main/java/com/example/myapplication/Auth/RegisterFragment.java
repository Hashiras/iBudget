package com.example.myapplication.Auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Balance;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.Data.Helper.BalanceHelper;
import com.example.myapplication.R;

public class RegisterFragment extends Fragment {
    public RegisterFragment() {
        super(R.layout.register_fragment);
    }
    private TextView loginLink;
    private EditText fullname, email, username, password;
    private Button button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginLink = view.findViewById(R.id.log_in_sign_up);
        fullname = view.findViewById(R.id.register_full_name);
        email = view.findViewById(R.id.register_email);
        username = view.findViewById(R.id.register_username);
        password = view.findViewById(R.id.register_password);
        button = view.findViewById(R.id.log_in_button);

        loginLink.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new LoginFragment())
                    .commit();
        });

        button.setOnClickListener(v->{
            if(validFields()){
                createAccount();
                Toast.makeText(getContext(), "Account successfully created!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_container, new LoginFragment())
                        .commit();
            }
        });
    }
    private boolean validFields(){
        boolean isFieldValid = true;
        if(fullname.getText().toString().isEmpty()){
            fullname.setError("Field is required!");
            isFieldValid = false;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Field is required!");
            isFieldValid = false;
        }
        if(username.getText().toString().isEmpty()){
            username.setError("Field is required!");
            isFieldValid = false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Field is required!");
            isFieldValid = false;
        }
        if(username.getText().toString().contains(" ")){
            password.setError("Username may not contain white spaces!");
            isFieldValid = false;
        }
        return isFieldValid;
    }

    private void createAccount(){
        AccountHelper accountHelper = new AccountHelper(getContext());
        BalanceHelper balanceHelper = new BalanceHelper(getContext());
        Account data = new Account();
        Balance balance = new Balance();
        data.setFullName(fullname.getText().toString());
        data.setEmail(email.getText().toString());
        data.setUsername(username.getText().toString());
        data.setPassword(password.getText().toString());
        data.setBalanceID(balanceHelper.getNextID());

        balance.setBalance(0.00);

        balanceHelper.insert(balance);
        accountHelper.insert(data);
    }
}
