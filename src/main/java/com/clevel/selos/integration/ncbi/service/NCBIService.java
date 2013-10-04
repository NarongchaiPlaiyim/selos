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
    NCBIImp ncbiImp;

    @Inject
    public NCBIService() {
    }

    public void process(){
        System.out.println("process()");
        try {
            ArrayList<String> path = null;

            path = ncbiImp.XMLFormatRequestTransaction(exportImp.get());

            for(String string : path){
                System.out.println("Paht : "+string);
            }

        } catch (Exception e) {
            System.out.println("NCBI : "+e.getMessage());
        }
    }
}
