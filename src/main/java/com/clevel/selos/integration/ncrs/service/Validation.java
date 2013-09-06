package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;

public interface Validation {
    public void validation(NCRSModel model)throws Exception;
    public void validation(TUEFEnquiryNameModel model)throws Exception;
    public void validation(TUEFEnquiryIdModel model)throws Exception;
}
