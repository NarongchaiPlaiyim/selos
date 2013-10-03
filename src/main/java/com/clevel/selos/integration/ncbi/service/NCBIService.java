package com.clevel.selos.integration.ncbi.service;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncbi.model.NcbiRequestModel;
import com.clevel.selos.integration.ncbi.model.RequestModel;
import com.clevel.selos.integration.ncb.exportncbi.NCBIExportImp;
import com.thoughtworks.xstream.XStream;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class NCBIService implements Serializable {
    @Inject
    @NCB
    NCBIExportImp exportImp;

    @Inject
    public NCBIService() {
    }

    public void process(){


        String xml = null;
        String result = null;
        XStream xStream = null;
        NcbiRequestModel requestModel = null;

        xStream = new XStream();
        xStream.processAnnotations(NcbiRequestModel.class);

        //RequestModel model = new RequestModel();
//        model.setCA_NUMBER();

        ArrayList<RequestModel> modelArrayList = new ArrayList<RequestModel>();
//        modelArrayList.add()
        requestModel = new NcbiRequestModel(modelArrayList);

        xml = xStream.toXML(requestModel);

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter("D:/out.xml");
            out = new BufferedWriter(fstream);
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("XML");
            out.close();
        } catch (IOException e) {
            System.out.println("IOException : "+e.getMessage());
        } finally {
            try {
                if(null!=out){
                    out.close();
                }
            } catch (IOException e) {
                System.out.println("IOException : "+ e.getMessage());
            }
        }

    }
}
