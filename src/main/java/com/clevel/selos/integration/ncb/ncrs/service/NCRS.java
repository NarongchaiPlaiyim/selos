package com.clevel.selos.integration.ncb.ncrs.service;

import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSInputModel;
import com.clevel.selos.integration.ncb.ncrs.ncrsmodel.NCRSOutputModel;

import java.util.ArrayList;

public interface NCRS {
    public ArrayList<NCRSOutputModel> requestOnline(NCRSInputModel inputModel) throws Exception;

    public ArrayList<NCRSOutputModel> requestOffline(NCRSInputModel inputModel) throws Exception;
}
