package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.PreDisbursementData;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreDisbursementDataDAO extends GenericDAO<PreDisbursementData, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public PreDisbursementDataDAO() {
    }
    
    public List<PreDisbursementData> findPreDisbursementDataByGropuName(String groupName) {
        log.info("-- findPreDisbursementDataByGropuName ::: {}", groupName);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("groupName", groupName));
        List<PreDisbursementData> preDisbursementDataList = (List<PreDisbursementData>) criteria.list();
        if (preDisbursementDataList != null)
        	log.info("-- preDisbursementDataList ::: size : {}", preDisbursementDataList.size());
        return preDisbursementDataList;
    }
    
    
    public Set<String> findGropuName() {
        log.info("-- findGropuName ::: {}", "");
        Criteria criteria = createCriteria();
        List<PreDisbursementData> preDisbursementDataList = (List<PreDisbursementData>) criteria.list();
        Set<String> groupNameSet = new HashSet<String>();
        for ( PreDisbursementData preDisbursementData : preDisbursementDataList){
        	groupNameSet.add(preDisbursementData.getGroupName());
        }
        if (preDisbursementDataList != null)
        	log.info("-- preDisbursementDataList ::: size : {}", preDisbursementDataList.size());
        return groupNameSet;
    }

}
