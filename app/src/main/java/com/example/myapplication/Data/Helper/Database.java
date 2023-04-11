package com.example.myapplication.Data.Helper;

import android.content.Context;

public class Database {
    private Context context;

    public Database(Context context) {
        this.context = context;
        init();
    }
    private void init(){
        AccountHelper accountHelper = new AccountHelper(context);
        BalanceHelper balanceHelper = new BalanceHelper(context);
        CategoryHelper categoryHelper = new CategoryHelper(context);
        TransactionHelper transactionHelper = new TransactionHelper(context);
        accountHelper.onCreate(accountHelper.getWritableDatabase());
        balanceHelper.onCreate(balanceHelper.getWritableDatabase());
        categoryHelper.onCreate(categoryHelper.getWritableDatabase());
        transactionHelper.onCreate(transactionHelper.getWritableDatabase());
    }
}
