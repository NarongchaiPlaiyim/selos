package com.clevel.selos.businesscontrol.util.stp;

import com.clevel.selos.integration.SELOS;
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
import java.sql.ResultSet;
import java.sql.SQLException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class STPExecutor implements Serializable {
    @Inject
    @SELOS
    Logger log;
    @PersistenceContext
    protected EntityManager em;

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

    public ResultSet getLogonOver90(){
        final ResultSet[] rs = {null};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt=connection.prepareCall("call SLOS.logonover90 ( ? )");
                callStmt.registerOutParameter("cursor_out",OracleTypes.CURSOR);
                callStmt.execute();

                rs[0] = (ResultSet) callStmt.getObject("cursor_out");

            }
        });
        return rs[0];
    }

    public ResultSet getViolation(final String fromdate,final String todate){
        final ResultSet[] rs = {null};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt=connection.prepareCall("call SLOS.violation ( ?,?,? )");
                callStmt.setString("fromdate",fromdate);
                callStmt.setString("todate",todate);
                callStmt.registerOutParameter("cursor_out",OracleTypes.CURSOR);
                callStmt.execute();

                rs[0] = (ResultSet) callStmt.getObject("cursor_out");

            }
        });
        return rs[0];
    }
}
