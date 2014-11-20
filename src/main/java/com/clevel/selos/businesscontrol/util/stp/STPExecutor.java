package com.clevel.selos.businesscontrol.util.stp;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ManageUserActive;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.isa.CSVModel;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

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

    public String getApplicationNumber(String segmentCode, String refAppNumber, int requestType) {
        String applicationNumber = "";
        try {
            Query query = em.createNativeQuery("select SLOS.GETAPPLICATIONNUMBER(:param1, :param2, :param3) from dual");
            query.setParameter("param1", segmentCode);
            query.setParameter("param2", refAppNumber);
            query.setParameter("param3", requestType);
            applicationNumber = (String) query.getSingleResult();

            log.debug("applicationNumber: {}", applicationNumber);
        } catch (Exception e) {
            log.error("Exception getApplicationNumber! (message: {})", e.getMessage());
        }
        return applicationNumber;
    }

    public String createFromCSV(final CSVModel csv, final User user) throws Exception {
        log.debug("-- createFromCSV(CSVModel : {})", csv.toString());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.INSERTUSERBYISA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                callStmt.setString(1, Util.parseString(csv.getUserId().trim(), EMPTY));
                log.debug("-- IN_USER_ID :  {}", Util.parseString(csv.getUserId(), EMPTY));
                callStmt.setString(2, Util.parseString(csv.getUserName().trim(), EMPTY));
                log.debug("-- IN_USER_NAME :  {}", Util.parseString(csv.getUserName(), EMPTY));
                if ("ACTIVE".equalsIgnoreCase(csv.getActive().trim())){
                    callStmt.setInt(3,1);
                    log.debug("-- IN_ACTIVE :  {}", Util.parseString(csv.getActive(), EMPTY));
                } else if ("INACTIVE".equalsIgnoreCase(csv.getActive().trim())){
                    callStmt.setInt(3,0);
                    log.debug("-- IN_ACTIVE :  {}", Util.parseString(csv.getActive(), EMPTY));
                }
                callStmt.setString(4, Util.parseString(csv.getRole().trim(), EMPTY));
                log.debug("-- IN_ROLE_NAME :  {}", Util.parseString(csv.getRole(), EMPTY));
                callStmt.setString(5, Util.parseString(csv.getTeam().trim(), EMPTY));
                log.debug("-- IN_TEAM_NAME :  {}", Util.parseString(csv.getTeam(), EMPTY));
                callStmt.setString(6, "NORMAL");
                log.debug("-- IN_TEAM_NAME :  {NORMAL}");
                callStmt.setString(7, Util.parseString(csv.getDepartment().trim(), EMPTY));
                log.debug("-- IN_DEPARTMENT_NAME :  {}", Util.parseString(csv.getDepartment(), EMPTY));
                callStmt.setString(8, Util.parseString(csv.getDivision().trim(), EMPTY));
                log.debug("-- IN_DIVISION_NAME :  {}", Util.parseString(csv.getDivision(), EMPTY));
                callStmt.setString(9, Util.parseString(csv.getRegion().trim(), EMPTY));
                log.debug("-- IN_REGION_NAME :  {}", Util.parseString(csv.getRegion(), EMPTY));
                callStmt.setString(10, Util.parseString(csv.getTitle().trim(), EMPTY));
                log.debug("-- IN_TITLE_NAME :  {}", Util.parseString(csv.getTitle(), EMPTY));
                callStmt.setString(11, user.getId());
                log.debug("-- IN_CREATE_BY :  {}", user.getId());
                callStmt.registerOutParameter(12, OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString(12);
            }
        });
        log.debug("-- result : {}", result[0]);
        return result[0];
    }

    public String updateFromCSV(final CSVModel csv, final User user) throws Exception {
        log.debug("-- updateFromCSV(CSVModel : {})", csv.toString());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.UPDATEUSERBYISA(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                callStmt.setString(1, Util.parseString(csv.getUserId().trim(), EMPTY));
                log.debug("-- IN_USER_ID :  {}", Util.parseString(csv.getUserId(), EMPTY));
                callStmt.setString(2, Util.parseString(csv.getUserName().trim(), EMPTY));
                log.debug("-- IN_USER_NAME :  {}", Util.parseString(csv.getUserName(), EMPTY));
                if ("ACTIVE".equalsIgnoreCase(csv.getActive())){
                    callStmt.setInt(3, 1);
                    log.debug("-- IN_ACTIVE :  {}", Util.parseString(csv.getActive(), EMPTY));
                } else if ("INACTIVE".equalsIgnoreCase(csv.getActive())){
                    callStmt.setInt(3, 0);
                    log.debug("-- IN_ACTIVE :  {}", Util.parseString(csv.getActive(), EMPTY));
                }
                callStmt.setString(4, Util.parseString(csv.getRole().trim(), EMPTY));
                log.debug("-- IN_ROLE_NAME :  {}", Util.parseString(csv.getRole(), EMPTY));
                callStmt.setString(5, Util.parseString(csv.getTeam().trim(), EMPTY));
                log.debug("-- IN_TEAM_NAME :  {}", Util.parseString(csv.getTeam(), EMPTY));
                callStmt.setString(6, "NORMAL");
                log.debug("-- IN_TEAM_NAME :  {NORMAL}");
                callStmt.setString(7, Util.parseString(csv.getDepartment().trim(), EMPTY));
                log.debug("-- IN_DEPARTMENT_NAME :  {}", Util.parseString(csv.getDepartment(), EMPTY));
                callStmt.setString(8, Util.parseString(csv.getDivision().trim(), EMPTY));
                log.debug("-- IN_DIVISION_NAME :  {}", Util.parseString(csv.getDivision(), EMPTY));
                callStmt.setString(9, Util.parseString(csv.getRegion().trim(), EMPTY));
                log.debug("-- IN_REGION_NAME :  {}", Util.parseString(csv.getRegion(), EMPTY));
                callStmt.setString(10, Util.parseString(csv.getTitle().trim(), EMPTY));
                log.debug("-- IN_TITLE_NAME :  {}", Util.parseString(csv.getTitle(), EMPTY));
//                callStmt.setString(10, Util.parseString(csv.getStatus(), EMPTY));
//                log.debug("-- IN_STATUS :  {}", Util.parseString(csv.getStatus(), EMPTY));
                callStmt.setString(11, user.getId());
                log.debug("-- IN_CREATE_BY :  {}", user.getId());
                callStmt.registerOutParameter(12, OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString(12);
            }
        });
        log.debug("-- result : {}", result[0]);
        return result[0];
    }

    public String deleteFromCSV(final CSVModel csv, final User user) throws Exception {
        log.debug("-- deleteFromCSV(UserId : {})", csv.getUserId());
        final String[] result = {""};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                CallableStatement callStmt = connection.prepareCall("{call SLOS.DELETEUSERBYISA(?, ?, ?)}");
                callStmt.setString(1, Util.parseString(csv.getUserId().trim(), EMPTY));
                log.debug("-- IN_USER_ID :  {}", Util.parseString(csv.getUserId(), EMPTY));
                callStmt.setString(2, user.getId());
                log.debug("-- IN_CREATE_BY :  {}", user.getId());
                callStmt.registerOutParameter(3, OracleTypes.VARCHAR);
                callStmt.executeUpdate();
                result[0] =callStmt.getString(3);
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

    public void duplicateFacilityData(final long workCaseId) throws Exception{
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement callStmt=connection.prepareCall("call SLOS.duplicateFacilityData ( ? )");
                callStmt.setLong(1, workCaseId);
                callStmt.executeUpdate();

            }
        });
    }

    public void duplicateCollateralData(final long workCaseId, final long workCasePreScreenId, final String method) throws Exception{
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement callStmt=connection.prepareCall("call SLOS.duplicateCollateralData ( ?, ?, ? )");
                callStmt.setLong(1, workCaseId);
                callStmt.setLong(2, workCasePreScreenId);
                callStmt.setString(3, method);
                callStmt.executeUpdate();

            }
        });
    }

    public void deleteCollateralData(final long workCaseId, final long workCasePreScreenId) throws Exception{
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement callStmt=connection.prepareCall("call SLOS.deleteCollateralData ( ?, ? )");
                callStmt.setLong(1, workCaseId);
                callStmt.setLong(2, workCasePreScreenId);
                callStmt.executeUpdate();

            }
        });
    }

    public ResultSet getActivity(final Map<String, Object> map){
        log.debug("on getViolation.");
        final ResultSet[] rs = {null};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try{
                    CallableStatement callStmt = connection.prepareCall("call SLOS.activity (?, ?, ?)");
                    int round = 1;
                    for (String key : map.keySet()){
                        callStmt.setObject(round, map.get(key).toString());
                        round++;
                    }
                    callStmt.registerOutParameter(3,OracleTypes.CURSOR);
                    callStmt.executeUpdate();
                    rs[0] = (ResultSet) callStmt.getObject(3);
                } catch (Exception e){
                    log.debug("Exception Error. {}",e);
                }
            }
        });
        log.debug("--rs. {}",rs[0]);
        return rs[0];
    }

    public ResultSet getUserProfileByUserMaster(){
        log.debug("on getViolation.");
        final ResultSet[] rs = {null};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try{
                    CallableStatement callStmt = connection.prepareCall("call SLOS.userprofilebyusermaster (?)");
                    callStmt.registerOutParameter(1,OracleTypes.CURSOR);
                    callStmt.executeUpdate();
                    rs[0] = (ResultSet) callStmt.getObject(1);
                } catch (Exception e){
                    log.debug("Exception Error. {}",e);
                }
            }
        });
        log.debug("--rs. {}",rs[0]);
        return rs[0];
    }

    public ResultSet getMatrix(final int roleId){
        log.debug("on getMatrix.");
        final ResultSet[] rs = {null};
        ((Session) em.getDelegate()).doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                try{
                    CallableStatement callStmt = connection.prepareCall("call SLOS.userAccressMatrix (?, ?)");
                    callStmt.setInt(1, roleId);
                    callStmt.registerOutParameter(2, OracleTypes.CURSOR);
                    callStmt.executeUpdate();
                    rs[0] = (ResultSet) callStmt.getObject(2);
                } catch (Exception e){
                    log.debug("Exception Error. {}",e);
                }
            }
        });
        log.debug("--rs. {}",rs[0]);
        return rs[0];
    }
}
