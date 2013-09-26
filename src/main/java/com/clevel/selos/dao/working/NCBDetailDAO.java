package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.NCBDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 25/9/2556
 * Time: 13:54 น.
 * To change this template use File | Settings | File Templates.
 */
public class NCBDetailDAO extends GenericDAO<NCBDetail,Long> {
    @Inject
    private Logger log;

    @Inject
    public NCBDetailDAO() {
    }
}