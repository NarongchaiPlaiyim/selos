package com.clevel.selos.busiensscontrol;

import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.*;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FullAppBusinessControl extends BusinessControl {
    @Inject
    Logger log;

    public FullAppBusinessControl(){

    }

}
