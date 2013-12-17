package com.clevel.selos.controller;


import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean(name = "disbursementInfo")
public class DisbursementInfo implements Serializable{

    @Inject
    @SELOS
    Logger log;





}
