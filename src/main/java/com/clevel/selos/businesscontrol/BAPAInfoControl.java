package com.clevel.selos.businesscontrol;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.integration.SELOS;

@Stateless
public class BAPAInfoControl extends BusinessControl {
	 private static final long serialVersionUID = -4625744349595576091L;
	 @Inject @SELOS Logger log;

	 public BAPAInfoControl() {
	 }
	 
	 
}
