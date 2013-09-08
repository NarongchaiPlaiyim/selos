package com.clevel.selos.integration.nccrs.service;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.clevel.selos.integration.nccrs.models.response.*;

public class Main {
    public static void main(String[] args) {
        XStream stream = null;
        NCCRSResponseModel response = null;
        try {
            stream = new XStream();
            stream.processAnnotations(NCCRSResponseModel.class);
            response = (NCCRSResponseModel) stream.fromXML(readFile());
            System.out.println(response.getHeader().getUser());
        } catch (Exception e) {
            System.err.println("Exception : "+e);
        }
    }
    private static String readFile() throws FileNotFoundException, IOException{
        String path = "D:\\Response.xml".replace("\\","/");
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
                //System.out.println(stringBuffer);
            }
            return stringBuffer.toString(); 
            //System.out.println(stringBuffer.toString());
    }
}
