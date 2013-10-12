package com.clevel.selos.integration.ncb.vaildation;

import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;

public interface Validation {
    public void validation(NCRSInputModel model)throws Exception;
    public void validation(NCCRSInputModel model)throws Exception;
}
