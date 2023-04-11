package com.example.myapplication.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.R;

public class LoginFragment extends Fragment {
    public LoginFragment(){
        super(R.layout.log_in_fragment);
    }

    private SharedPreferences preferences;
    private EditText username, password;
    private TextView signUpLink;

    private Button button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = AuthenticationActivity.preferences;
        username = view.findViewById(R.id.log_in_username);
        password = view.findViewById(R.id.log_in_password);
        signUpLink = view.findViewById(R.id.log_in_sign_up);
        button = view.findViewById(R.id.log_in_button);

        if(preferences.getInt("accountID", 0) != 0){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new PinFragment())
                    .commit();
        }
        signUpLink.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_container, new RegisterFragment())
                    .commit();
        });

        button.setOnClickListener(v->{
            if(validFields())
                new validateCredentialsTask().execute(new Credential(username.getText().toString(), password.getText().toString()));
        });
    }

    private boolean validFields(){
        boolean isFieldValid = true;
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
    private class Credential{
        private String username;
        private String password;

        public Credential(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private class validateCredentialsTask extends AsyncTask<Credential, Void, Account>{
        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);
            if(account == null){
                username.setError("Invalid username");
                password.setError("Invalid password");
                Toast.makeText(getContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("accountID", account.getID());
            editor.commit();
            if(account.getPin() == null)
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_container, new PinSetupFragment())
                        .commit();
            else
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_container, new PinFragment())
                        .commit();
        }

        @Override
        protected Account doInBackground(Credential... credentials) {
            String username = credentials[0].username;
            String password = credentials[0].password;
            AccountHelper helper = new AccountHelper(getContext());
            for(Account acc : helper.get()){
                if(username.equals(acc.getUsername()) && password.equals(acc.getPassword()))
                    return acc;
            }
            return null;
        }
    }
}
