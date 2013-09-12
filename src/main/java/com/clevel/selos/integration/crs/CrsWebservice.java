package com.clevel.selos.integration.crs;


import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class CrsWebservice {

    public CrsWebservice() {

    }

    @WebMethod
    public String sayHello(){
        return "sahawat";
    }

}
