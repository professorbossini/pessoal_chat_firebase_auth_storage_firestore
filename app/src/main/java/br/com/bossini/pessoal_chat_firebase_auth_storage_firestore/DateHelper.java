package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import java.text.SimpleDateFormat;
import java.util.Date;

class DateHelper {
    private static SimpleDateFormat sdf =  new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
    public static String format (Date date){
        return sdf.format(date);
    }
}
