package com.clevel.selos.transform;

import com.clevel.selos.integration.brms.model.request.PreScreenRequest;
import com.clevel.selos.model.view.CustomerInfoView;

import javax.inject.Inject;
import java.util.List;

public class PreScreenRequestTransform extends Transform {

    @Inject
    public PreScreenRequestTransform(){

    }

    public PreScreenRequest transformToRequest(List<CustomerInfoView> customerInfoViewList){
        PreScreenRequest preScreenRequest = new PreScreenRequest();
        //TODO Transform to preScreenRequest
        return preScreenRequest;
    }
}
