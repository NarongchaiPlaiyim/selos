package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponse;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

public class DataForTEST implements NCBInterface, Serializable{
    @Inject
    @NCB
    Logger log;

    @Inject
    public DataForTEST() {
    }

    @Override
    public NCRSResponse request(NCRSModel ncrsModel) throws Exception {
        log.debug("========================================= request(NCRSModel : {})",ncrsModel.toString());
        String path = "D:\\Response.TXT".replace("\\","/");
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder stringBuffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        XStream xStream = null;
        xStream = new XStream();
        NCRSResponse ncrsResponse = null;
        xStream.processAnnotations(NCRSResponse.class);
        ncrsResponse = (NCRSResponse)xStream.fromXML(stringBuffer.toString());
        return ncrsResponse;
    }
}
