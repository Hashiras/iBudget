package com.example.myapplication.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class PinFragment extends Fragment {
    public PinFragment() {
        super(R.layout.pin_authentication_fragment);
    }

    private SharedPreferences preferences;
    private EditText display;
    private AccountHelper helper;
    private Button zero, one, two, three, four, five, six, seven, eight, nine, clear, enter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = AuthenticationActivity.preferences;
        helper = new AccountHelper(getContext());
        display = view.findViewById(R.id.pin_edit);
        zero = view.findViewById(R.id.pin_button0);
        one = view.findViewById(R.id.pin_button1);
        two = view.findViewById(R.id.pin_button2);
        three = view.findViewById(R.id.pin_button3);
        four = view.findViewById(R.id.pin_button4);
        five = view.findViewById(R.id.pin_button5);
        six = view.findViewById(R.id.pin_button6);
        seven = view.findViewById(R.id.pin_button7);
        eight = view.findViewById(R.id.pin_button8);
        nine = view.findViewById(R.id.pin_button9);
        clear = view.findViewById(R.id.pin_clear);
        enter = view.findViewById(R.id.pin_enter);

        zero.setOnClickListener(v-> display.setText(display.getText() + "0"));
        one.setOnClickListener(v-> display.setText(display.getText() + "1"));
        two.setOnClickListener(v-> display.setText(display.getText() + "2"));
        three.setOnClickListener(v-> display.setText(display.getText() + "3"));
        four.setOnClickListener(v-> display.setText(display.getText() + "4"));
        five.setOnClickListener(v-> display.setText(display.getText() + "5"));
        six.setOnClickListener(v-> display.setText(display.getText() + "6"));
        zero.setOnClickListener(v-> display.setText(display.getText() + "0"));
        seven.setOnClickListener(v-> display.setText(display.getText() + "7"));
        eight.setOnClickListener(v-> display.setText(display.getText() + "8"));
        nine.setOnClickListener(v-> display.setText(display.getText() + "9"));
        clear.setOnClickListener(v-> display.setText(""));
        enter.setOnClickListener(v->{
            validatePin();
        });
        display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(display.getText().toString().length() == 4){
                    validatePin();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validatePin(){
        if(display.getText().toString().length() != 4){
            display.setError("Pin must have a length of four!");
            return;
        }
        new setPinTask().execute(display.getText().toString());
    }

    private class setPinTask extends AsyncTask<String, Void, Account> {
        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);
            if(account == null){
                display.setError("Invalid Pin!");
                return;
            }
            startActivity(new Intent(getContext(), MainActivity.class));
        }

        @Override
        protected Account doInBackground(String... strings) {
            for(Account acc : helper.get()){
                if(acc.getPin().equals(strings[0]))
                    return acc;
            }
            return null;
        }
    }
}
