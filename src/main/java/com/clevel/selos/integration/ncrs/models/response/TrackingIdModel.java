package com.clevel.selos.integration.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("trackingid")
public class TrackingIdModel {
    @XStreamAlias("trackingid")
    private String trackingid;

    public TrackingIdModel(String trackingid) {
        this.trackingid = trackingid;
    }

    public String getTrackingid() {
        return trackingid;
    }
    
}
