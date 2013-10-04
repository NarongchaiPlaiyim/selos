package com.clevel.selos.integration.ncbi.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.clevel.selos.integration.ncbi.model.NcbiRequestModel;
import com.clevel.selos.integration.ncbi.model.RequestModel;
import com.clevel.selos.model.db.export.NCBIExport;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NCBIImp implements Serializable{
    @Inject
    @NCB
    Logger log;

    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    public NCBIImp() {
    }

    public void process(){
        log.debug("call process()");
        System.out.println("call process()");
        List<NCBIExport> ncbiExportList = exportImp.get();



        String xml = null;
        String result = null;
        XStream xStream = null;
        NcbiRequestModel requestModel = null;

        xStream = new XStream();
        xStream.processAnnotations(NcbiRequestModel.class);
        ArrayList<RequestModel> modelArrayList = new ArrayList<RequestModel>();



        for(NCBIExport ncbiExport : ncbiExportList){
            log.debug("NCBI : ", ncbiExport.toString());
            System.out.println("NCBI : "+ ncbiExport.toString());
            modelArrayList.add(new RequestModel(ncbiExport.getStaffId(), ncbiExport.getRequestNo()));
        }
        requestModel = new NcbiRequestModel(modelArrayList);
        xml = xStream.toXML(requestModel);

        System.out.println("NCBI : "+xml);


    }
}
