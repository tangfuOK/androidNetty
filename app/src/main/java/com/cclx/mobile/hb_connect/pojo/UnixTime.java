package com.cclx.mobile.hb_connect.pojo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UnixTime {
    private final String value;

    public UnixTime() {
        this(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
    }

    public UnixTime(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
