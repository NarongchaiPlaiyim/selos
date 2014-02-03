package com.clevel.selos.model;

public enum Month {

    //    January(1), February(2), March(3), April(4), May(5), June(6), July(7), August(8), September(9), October(10), November(11), December(12);
    January(1,"มกราคม"),
    February(2,"กุมภาพันธ์"),
    March(3,"มีนาคม"),
    April(4,"เมษายน"),
    May(5,"พฤษภาคม"),
    June(6,"มิถุนายน"),
    July(7,"กรกฎาคม"),
    August(8,"สิงหาคม"),
    September(9,"กันยายน"),
    October(10,"ตุลาคม"),
    November(11,"พฤศจิกายน"),
    December(12,"ธันวาคม");

    int value;
    String nameTH;

    private Month(int value, String nameTH) {
        this.value = value;
        this.nameTH = nameTH;
    }

    public int value() {
        return this.value;
    }

    public String nameTH() {
        return this.nameTH;
    }
}
