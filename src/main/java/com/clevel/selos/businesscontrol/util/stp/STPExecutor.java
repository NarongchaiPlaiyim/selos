package com.clevel.selos.businesscontrol.util.stp;

import com.clevel.selos.businesscontrol.isa.csv.model.CSVModel;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.util.Util;
import oracle.jdbc.OracleTypes;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class STPExecutor implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @PersistenceContext
    protected EntityManager em;
    private final String EMPTY = "";
    @Inject
    public STPExecutor() {
    }

    public String getApplicationNumber(String segmentCode) {
        String applicationNumber = "";
        try {
            Query query = em.createNativeQuery("select SLOS.GETAPPLICATIONNUMBER(:param) from dual");
            query.setParameter("param", segmentCode);
            applicationNumber = (String) query.getSingleResult();

            log.debug("applicationNumber: {}", applicationNumber);
        } catch (Exception e) {
            log.error("Exception getApplicationNumber! (message: {})", e.getMessage());
        }
        return applicationNumber;
    }

    public String createFromCSV(final CSVModel csv) throws Exception {
        log.debug("-- createFromCSV(CSVModel : {})", csv.toString());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.INSERTUSERBYISA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                callStmt.setString("USERID", Util.parseString(csv.getUserId(), EMPTY));
                callStmt.setString("USER_NAME", Util.parseString(csv.getUserName(), EMPTY));
                callStmt.setInt(   "ACTIVE", Util.parseInt(csv.getActive(), 3));
                callStmt.setString("ROLE_NAME", Util.parseString(csv.getRole(), EMPTY));
                callStmt.setString("DEPARTMENT_NAME", Util.parseString(csv.getDepartment(), EMPTY));
                callStmt.setString("DIVISION_NAME", Util.parseString(csv.getDivision(), EMPTY));
                callStmt.setString("REGION_NAME", Util.parseString(csv.getRegion(), EMPTY));
                callStmt.setString("TEAM_NAME", Util.parseString(csv.getTeam(), EMPTY));
                callStmt.setString("TITLE_NAME", Util.parseString(csv.getTitle(), EMPTY));
                callStmt.setString("STATUS", Util.parseString(csv.getStatus(), EMPTY));
                callStmt.registerOutParameter("RESULT", OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString("RESULT");
            }
        });
        log.debug("-- result : {}", result[0]);
        return result[0];
    }

    public String updateFromCSV(final CSVModel csv) throws Exception {
        log.debug("-- updateFromCSV(CSVModel : {})", csv.toString());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.UPDATEUSERBYISA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                callStmt.setString("USERID", Util.parseString(csv.getUserId(), EMPTY));
                callStmt.setString("USER_NAME", Util.parseString(csv.getUserName(), EMPTY));
                callStmt.setInt(   "ACTIVE", Util.parseInt(csv.getActive(), 3));
                callStmt.setString("ROLE_NAME", Util.parseString(csv.getRole(), EMPTY));
                callStmt.setString("DEPARTMENT_NAME", Util.parseString(csv.getDepartment(), EMPTY));
                callStmt.setString("DIVISION_NAME", Util.parseString(csv.getDivision(), EMPTY));
                callStmt.setString("REGION_NAME", Util.parseString(csv.getRegion(), EMPTY));
                callStmt.setString("TEAM_NAME", Util.parseString(csv.getTeam(), EMPTY));
                callStmt.setString("TITLE_NAME", Util.parseString(csv.getTitle(), EMPTY));
                callStmt.setString("STATUS", Util.parseString(csv.getStatus(), EMPTY));
                callStmt.registerOutParameter("RESULT", OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString("RESULT");
            }
        });
        log.debug("-- result : {}", result[0]);
        return result[0];
    }

    public String deleteFromCSV(final CSVModel csv) throws Exception {
        log.debug("-- deleteFromCSV(UserId : {})", csv.getUserId());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.DELETEUSERBYISA(?, ?)}");
                callStmt.setString("USERID", csv.getUserId());
                callStmt.registerOutParameter("RESULT", OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString("RESULT");
            }
        });
        log.debug("-- result : {}", result[0]);
        return result[0];
    }

    public String addUserFromFile( final Object... params)throws ServiceException { //todo : change this , AS ( To use Hibernate )
       final String result[]=new String[1];
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement callStmt=connection.prepareCall("call SLOS.PUSERFILEUPLOAD ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

                for(int i =0;i < params.length;i++){
                    log.debug("data: {}",params[i]);
                    callStmt.setString(i + 1, params[i].toString());
                }
                    callStmt.registerOutParameter(params.length+1, OracleTypes.VARCHAR);
                    callStmt.executeUpdate();
                     result[0]=(String)callStmt.getObject(params.length+1);
                log.debug("result: {}",result[0]);
            }
        });

         return result[0];
    }

    public void duplicateData(final long workCasePreScreenId) throws Exception{
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement callStmt=connection.prepareCall("call SLOS.createWorkCase ( ? )");
                callStmt.setLong(1, workCasePreScreenId);
                callStmt.executeUpdate();

            }
        });
    }
}
