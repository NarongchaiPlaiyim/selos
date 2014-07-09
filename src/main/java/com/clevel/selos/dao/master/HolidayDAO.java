package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Holiday;
import com.clevel.selos.util.DateTimeUtil;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Date;

public class HolidayDAO extends GenericDAO<Holiday, Long> {
    @Inject
    @SELOS
    private Logger log;
    @Inject
    public HolidayDAO() {

    }

    public boolean isHoliday(final Date HOLIDAY){
        log.info("-- isHoliday() Date : {}", DateTimeUtil.convertToStringDDMMYYYY(HOLIDAY));
        return isRecordExist(Restrictions.eq("holidayDate", HOLIDAY));
    }
}
