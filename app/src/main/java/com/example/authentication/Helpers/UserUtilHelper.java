package com.example.authentication.Helpers;

import com.example.authentication.MyApp;
import com.example.authentication.Objects.Account;

public class UserUtilHelper {
    public static String getLoggedInUsername() {
        Account acc = MyApp.getInstance().getCurrentUser().getAcc();
        return (acc != null) ? acc.getUsername() : null;
    }
}

