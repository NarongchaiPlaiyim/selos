package com.clevel.selos.model;

public enum Month {

    January(1), February(2), March(3), April(4), May(5), June(6), July(7), August(8), September(9), October(10), November(11), December(12);
//    มกราคม(1), กุมภาพันธ์(2), มีนาคม(3), เมษายน(4), พฤษภาคม(5), มิถุนายน(6), กรกฎาคม(7), สิงหาคม(8), กันยายน(9), ตุลาคม(10), พฤศจิกายน(11), ธันวาคม(12);
    int value;


    Month(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int value() {
        return this.value;
    }
}
