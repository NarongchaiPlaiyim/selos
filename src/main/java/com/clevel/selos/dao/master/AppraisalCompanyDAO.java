package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.AppraisalCompany;
import com.clevel.selos.util.Util;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

public class AppraisalCompanyDAO extends GenericDAO<AppraisalCompany,Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public AppraisalCompanyDAO() {

    }

    public List<AppraisalCompany> findAllASC(){
        log.debug("-- findAllASC()");
        List<AppraisalCompany> appraisalCompanyList = null;
        Criteria criteria = createCriteria();
        criteria.addOrder(Order.asc("name"));
        appraisalCompanyList = Util.safetyList((List<AppraisalCompany>) criteria.list());
        int round = 0;
        if(Util.isSafetyList(appraisalCompanyList)){
            for(final AppraisalCompany appraisalCompany : appraisalCompanyList){
                if(appraisalCompany.getCode() == 34){
                    Collections.swap(appraisalCompanyList, 0, round);
                }
                round++;
            }
        }
        log.debug("-- AppraisalCompanyList.size()[{}]", appraisalCompanyList.size());
        return appraisalCompanyList;
    }
}
