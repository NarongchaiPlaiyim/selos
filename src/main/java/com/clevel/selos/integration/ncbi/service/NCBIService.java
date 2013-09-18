package com.clevel.selos.integration.ncbi.service;

import javax.inject.Inject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class NCBIService implements Serializable {

    @Inject
    public NCBIService() {
    }

    public void process(){
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
