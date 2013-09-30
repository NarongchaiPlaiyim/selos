package com.clevel.selos.integration.ncb.vaildation;

import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryIdModel;
import com.clevel.selos.integration.ncb.ncrs.models.request.TUEFEnquiryNameModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSModel;

public interface Validation {
    public void validation(NCRSModel model)throws Exception;
    public void validation(TUEFEnquiryNameModel model)throws Exception;
    public void validation(TUEFEnquiryIdModel model)throws Exception;

    public void validation(NCCRSInputModel model)throws Exception;
}
