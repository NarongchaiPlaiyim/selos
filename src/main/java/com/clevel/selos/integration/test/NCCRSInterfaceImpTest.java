package com.clevel.selos.integration.test;

import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.NCCRSInterface;
import com.clevel.selos.integration.nccrs.models.response.NCCRSResponseModel;
import com.clevel.selos.integration.nccrs.service.NCCRSModel;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

public class NCCRSInterfaceImpTest implements NCCRSInterface, Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    public NCCRSInterfaceImpTest() {
    }

    @Override
    public NCCRSResponseModel request(NCCRSModel nccrsModel) throws Exception {
        log.debug("=========================================NCCRS request(NCRSModel : {})",nccrsModel.toString());
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
