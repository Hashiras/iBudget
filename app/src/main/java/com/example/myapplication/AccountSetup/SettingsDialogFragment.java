package com.example.myapplication.AccountSetup;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.Finance.HomeFragment;
import com.example.myapplication.R;

public class SettingsDialogFragment extends DialogFragment {
    public SettingsDialogFragment() {
        super(R.layout.account_setting_dialog_fragment);
    }

    private AccountHelper helper;
    private Account account;
    private EditText fullName, email;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = new AccountHelper(getContext());
        account = helper.get(AuthenticationActivity.preferences.getInt("accountID", 0));
        fullName = view.findViewById(R.id.password_old);
        email = view.findViewById(R.id.password_new);

        initValues();

        Button button = view.findViewById(R.id.account_update);
        button.setOnClickListener(v-> launchUpdate());
    }

    @Override
    public void onResume() {
        // Set the width of the dialog proportional to 90% of the screen width
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
    private void initValues(){
        fullName.setText(account.getFullName());
        email.setText(account.getEmail());
    }
    private void launchUpdate(){
        if(!validFields())
            return;
        account.setFullName(fullName.getText().toString());
        account.setEmail(email.getText().toString());
        dismiss();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.app_container, new HomeFragment())
                .commit();
    }

    private boolean validFields(){
        boolean fieldsAreValid = true;
        if(fullName.getText().toString().isEmpty()){
            fullName.setText("Please provide a valid full name!");
            fieldsAreValid = false;
        }
        if(email.getText().toString().isEmpty() && !email.getText().toString().contains("@")){
            email.setText("Please provide a valid email!");
            fieldsAreValid = false;
        }

        return fieldsAreValid;
    }
}
