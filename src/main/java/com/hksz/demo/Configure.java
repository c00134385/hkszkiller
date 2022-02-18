package com.hksz.demo;

import com.hksz.demo.models.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class Configure {
//    public static String host = "http://fanyi.youdao.com/";
    public static String host = "https://hk.sz.gov.cn:8118/";
    public static long timeDelay = 50;
    public static long startTime = 10 * 60 * 60 * 1000;  //"10:00:00";
    private static String[] userAccounts =
            {
            "4,H09471876,ed521126",
//            "4,H04304428,a63061977",
//            "4,H08853500,461745phl",
//            "4,H03608252,Asd59386915",
//            "2,C77240437,tata2022",
//            "4,H08853561,abcd1234",
//            "3,G49480256,c00134385"
    };

    public static List<UserAccount> getUserAccounts() {
        List<UserAccount> accounts = new ArrayList<>();
        for(int i = 0; i < userAccounts.length; i++) {
            UserAccount account = UserAccount.parseFromString(userAccounts[i]);
            accounts.add(account);
        }
        return accounts;
    }
}
