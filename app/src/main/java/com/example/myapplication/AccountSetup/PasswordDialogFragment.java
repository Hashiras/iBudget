package com.example.myapplication.AccountSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.R;

public class PasswordDialogFragment extends DialogFragment {
    public PasswordDialogFragment() {
        super(R.layout.change_password_dialog_fragment);
    }

    private AccountHelper helper;
    private Account account;
    private EditText oldPass, newPass, newPass2;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new AccountHelper(getContext());
        account = helper.get(AuthenticationActivity.preferences.getInt("accountID", 0));
        oldPass = view.findViewById(R.id.password_old);
        newPass = view.findViewById(R.id.password_new);
        newPass2 = view.findViewById(R.id.password_confirm);

        Button button = view.findViewById(R.id.password_button);
        button.setOnClickListener(v-> launchUpdate());
    }
    private void launchUpdate(){
        if(!validFields())
            return;
        account.setPassword(newPass.getText().toString());
        Toast.makeText(getContext(), "Please log in to continue!", Toast.LENGTH_SHORT).show();
        getActivity().finish();
        startActivity(new Intent(getContext(), AuthenticationActivity.class));
    }

    private boolean validFields(){
        boolean fieldsAreValid = true;
        if(!oldPass.getText().toString().equals(account.getPassword())){
            oldPass.setError("Password is incorrect!");
            fieldsAreValid = false;
        }
        if(oldPass.getText().toString().isEmpty()){
            oldPass.setError("Field is required");
            fieldsAreValid = false;
        }
        if(newPass.getText().toString().isEmpty()){
            newPass.setError("Field is required");
            fieldsAreValid = false;
        }
        if(!newPass.getText().toString().equals(newPass2.getText().toString())){
            newPass2.setError("Password does not match!");
            fieldsAreValid = false;
        }
        return fieldsAreValid;
    }
}
