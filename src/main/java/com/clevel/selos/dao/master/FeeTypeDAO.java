package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.FeeType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class FeeTypeDAO  extends GenericDAO<FeeType,Integer>{
	private static final long serialVersionUID = 8051678631586999828L;
	@Inject @SELOS
    private Logger logger;

    public FeeType findByBRMSCode(String brmsCode){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("brmsCode", brmsCode));
        FeeType feeType = (FeeType) criteria.uniqueResult();
        return feeType;
    }
    public FeeType findByDescription(String description) {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("description", description));
        FeeType feeType = (FeeType) criteria.uniqueResult();
        return feeType;
    }
}
