package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.BizInfoDetail;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: Rangsun
 * Date: 20/9/2556
 * Time: 16:02 น.
 * To change this template use File | Settings | File Templates.
 */
public class BizInfoDetailDAO extends GenericDAO<BizInfoDetail,Long> {
    @Inject
    private Logger log;

    @Inject
    public BizInfoDetailDAO() {
    }
}
