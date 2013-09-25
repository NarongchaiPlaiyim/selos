package com.clevel.selos.integration.ncrs.service;


import com.clevel.selos.dao.ext.ncb.NCBResultDAO;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.model.db.ext.ncb.NCBResult;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
@NCB
public class NCBResultImp implements Serializable {
    @Inject
    Logger log;

    @Inject
    NCBResultDAO ncbResult;

    @Inject
    public NCBResultImp() {
    }

    public void add(String appRefNumber, String customerType, String customerId, Date inquiryDate, String result, String reason){
        ncbResult.persist(new NCBResult(appRefNumber, customerType, customerId, inquiryDate, result, reason));
    }

    public List<NCBResult> getListByAppRefNumber(String appRefNumber){
        log.debug("NCRS Call checkAppRefNumber({})", appRefNumber);
        return ncbResult.findByCriteria(Restrictions.eq("appRefNumber", appRefNumber));
    }

    public boolean isChecked(String appRefNumber){
        log.debug("NCRS Call isChecked({})", appRefNumber);
        return  ncbResult.isExist(appRefNumber);
    }

    public void update(String appRefNumber, String customerId, String result){
        NCBResult model = ncbResult.findOneByCriteria(Restrictions.and(Restrictions.eq("appRefNumber", appRefNumber), Restrictions.eq("customerId", customerId)));
        model.setResult(result);
        ncbResult.save(model);
    }

    public void test(String appRefNumber, String result){
//        ncbResult.findByCriteria(Restrictions.eq());
    }
}
