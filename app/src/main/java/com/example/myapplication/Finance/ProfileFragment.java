package com.example.myapplication.Finance;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.AccountSetup.PasswordDialogFragment;
import com.example.myapplication.AccountSetup.SettingsDialogFragment;
import com.example.myapplication.Auth.AuthenticationActivity;
import com.example.myapplication.Data.Account;
import com.example.myapplication.Data.Helper.AccountHelper;
import com.example.myapplication.R;


public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    private AccountHelper accountHelper;
    private TextView fullName, email;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountHelper = new AccountHelper(getContext());
        fullName = view.findViewById(R.id.profile_full_name);
        email = view.findViewById(R.id.profile_email);
        Button modify = view.findViewById(R.id.profile_account_setting);
        Button password = view.findViewById(R.id.profile_change_password);
        Button signOut = view.findViewById(R.id.profile_sign_out);

        modify.setOnClickListener(v -> launchModify());

        password.setOnClickListener(v -> launchChangePass());

        signOut.setOnClickListener(v -> launchSignOut());

        initValues();
    }
    private void initValues(){
        Account account = accountHelper.get(AuthenticationActivity.preferences.getInt("accountID", 0));
        fullName.setText(account.getFullName());
        email.setText(account.getEmail());
    }

    private void launchModify(){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("modify");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment fragment = new SettingsDialogFragment();
        fragment.show(ft, "modify");
    }

    private void launchChangePass(){
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("password");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment fragment = new PasswordDialogFragment();
        fragment.show(ft, "password");
    }

    private void launchSignOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SharedPreferences.Editor editor = AuthenticationActivity.preferences.edit();
        builder.setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    editor.putInt("accountID", 0);
                    editor.commit();
                    startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // User cancelled the dialog
                }).create().show();
    }
}
