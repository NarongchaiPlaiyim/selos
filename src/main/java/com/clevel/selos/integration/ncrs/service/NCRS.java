package com.clevel.selos.integration.ncrs.service;

import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.ResponseNCRSModel;

import java.util.ArrayList;

public interface NCRS {
    public ArrayList<ResponseNCRSModel> requestOnline(NCRSModel ncrsModel)throws Exception;
    public ArrayList<ResponseNCRSModel> requestOffline(NCRSModel ncrsModel)throws Exception;
}
