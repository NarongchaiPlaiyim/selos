package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.master.QualityLevel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class QualityLevelDAO extends GenericDAO<QualityLevel, Long> {
    @Inject
    private Logger log;

    @Inject
    public QualityLevelDAO() {
    }

}
