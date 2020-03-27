package com.example.memox.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NoteHelpers {
    public static String dateFromLong(long time) {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy 'à' hh:mm aaa", Locale.FRANCE);
        return format.format(new Date(time));
    }
}
