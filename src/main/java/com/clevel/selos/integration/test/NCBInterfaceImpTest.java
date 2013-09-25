package com.clevel.selos.integration.test;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.NCBInterface;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.nccrs.service.NCCRSModel;
import com.clevel.selos.integration.ncrs.models.response.NCRSResponseModel;
import com.clevel.selos.integration.ncrs.ncrsmodel.NCRSModel;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

public class NCBInterfaceImpTest implements NCBInterface, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    public NCBInterfaceImpTest() {
    }

    @Override
    public NCRSResponseModel request(NCRSModel ncrsModel) throws Exception {
        log.debug("NCRS request(NCRSModel : {})",ncrsModel.toString());
        String path = "D:\\Response.TXT".replace("\\","/");
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder stringBuffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        XStream xStream = null;
        xStream = new XStream();
        NCRSResponseModel ncrsResponse = null;
        xStream.processAnnotations(NCRSResponseModel.class);
        ncrsResponse = (NCRSResponseModel)xStream.fromXML(stringBuffer.toString());
        return ncrsResponse;
    }

    @Override
    public NCCRSResponseModel request(NCCRSModel nccrsModel) throws Exception {
        log.debug("NCCRS request(NCRSModel : {})",nccrsModel.toString());
        String path = "D:\\Response.XML".replace("\\","/");
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder stringBuffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        XStream xStream = null;
        xStream = new XStream();
        NCCRSResponseModel responseModel = null;
        xStream.processAnnotations(NCCRSResponseModel.class);
        responseModel = (NCCRSResponseModel)xStream.fromXML(stringBuffer.toString());
        return responseModel;
    }
}
