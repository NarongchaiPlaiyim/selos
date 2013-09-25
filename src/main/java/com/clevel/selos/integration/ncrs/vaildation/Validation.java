package com.clevel.selos.integration.ncrs.vaildation;

import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;

public interface Validation {
    public void validation(NCRSModel model)throws Exception;
    public void validation(TUEFEnquiryNameModel model)throws Exception;
    public void validation(TUEFEnquiryIdModel model)throws Exception;
}
