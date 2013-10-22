package com.clevel.selos.integration.ncb.nccrs.service;

import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;

import java.util.ArrayList;

public interface NCCRS {
    public ArrayList<NCCRSOutputModel> requestOnline(NCCRSInputModel inputModel)throws Exception;
    public ArrayList<NCCRSOutputModel> requestOffline(NCCRSInputModel inputModel)throws Exception;
}
