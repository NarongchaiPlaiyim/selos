package com.clevel.selos.integration;

import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSInputModel;
import com.clevel.selos.integration.ncb.nccrs.nccrsmodel.NCCRSOutputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;

import java.util.ArrayList;

public interface NCBInterface {
    public ArrayList<NCRSOutputModel> request(NCRSInputModel inputModel)throws Exception;
    public ArrayList<NCCRSOutputModel> request(NCCRSInputModel inputModel)throws Exception;
}
