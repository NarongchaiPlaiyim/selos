package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

@XStreamAlias("trackingid")
public class TrackingIdModel implements Serializable {
    @XStreamAlias("trackingid")
    private String trackingid;

    public TrackingIdModel(String trackingid) {
        this.trackingid = trackingid;
    }

    public String getTrackingid() {
        return trackingid;
    }
    
}
