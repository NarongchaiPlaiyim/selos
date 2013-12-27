package com.clevel.selos.integration.bpm.module;

import com.clevel.selos.exception.BPMInterfaceException;
import com.clevel.selos.integration.BPM;
import com.clevel.selos.integration.bpm.model.BPMInbox;
import com.clevel.selos.integration.bpm.model.FieldName;
import com.clevel.selos.integration.bpm.model.OrderType;
import com.clevel.selos.integration.bpm.tool.DBContext;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBExecute implements Serializable {
    @Inject
    @BPM
    Logger log;
    @Inject
    @Config(name = "interface.bpm.oracle.conn")
    String connBPM;
    @Inject
    @Config(name = "interface.bpm.oracle.username")
    String bpmUser;
    @Inject
    @Config(name = "interface.bpm.oracle.password")
    String bpmPassword;
    @Inject
    @Config(name = "interface.bpm.oracle.schema")
    String schema;
    @Inject
    @Config(name = "interface.bpm.oracle.table.prefix")
    String prefix;

    @Inject
    DBContext dbContext;

    @Inject
    @ExceptionMessage
    Message msg;

    Connection conn = null;
    transient ResultSet rs = null;

    @Inject
    public DBExecute() {

    }

    public List<BPMInbox> getMyBox(String userId, FieldName fieldName, OrderType orderType, int startRecord, int endRecord){
        log.debug("getMyBox (userId: {}, fieldName: {}, orderType: {}, startRecord: {}, endRecord: {})",userId,fieldName,orderType,startRecord,endRecord);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();

        if(startRecord!=0 || endRecord!=0){
            String SQL_SELECT = "SELECT * FROM " +
                    "(SELECT ROWNUM rec_no, A.* FROM " +
                    "(SELECT * FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                    ") " +
                    "WHERE rec_no >= ? AND rec_no <= ?";
            if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ? AND rec_no <= ?";
            }

            if(endRecord==0){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ?";
                if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                    SQL_SELECT = "SELECT * FROM " +
                            "(SELECT ROWNUM rec_no, A.* FROM " +
                            "(SELECT * FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                            ") " +
                            "WHERE rec_no >= ?";
                }
            }

            try{
                conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
            } catch (BPMInterfaceException ex){
                throw ex;
            }

            try {
                log.debug("open connection.");
                log.debug("SQL_SELECT : {}",SQL_SELECT);
                PreparedStatement statement = conn.prepareStatement(SQL_SELECT);
                statement.setString(1, userId);
                statement.setInt(2, startRecord);
                if(endRecord!=0){
                    statement.setInt(3, endRecord);
                }
                rs = statement.executeQuery();
                while (rs.next()) {
                    BPMInbox bpmInbox = new BPMInbox();
                    bpmInbox.setReceivedTime(rs.getInt("RECEIVEDTIME"));
                    bpmInbox.setAppNumber(rs.getString("APPNUMBER"));
                    bpmInbox.setBorrowerName(rs.getString("BORROWERNAME"));
                    bpmInbox.setProductGroup(rs.getString("PRODUCTGROUP"));
                    bpmInbox.setRequestType(rs.getInt("REQUESTTYPE"));
                    bpmInbox.setfStepName(rs.getString("F_STEPNAME"));
                    bpmInbox.setStatus(rs.getString("STATUS"));
                    bpmInbox.setPreviousUser(rs.getString("PREVIOUSUSER"));
                    bpmInbox.setAppointmentDate(rs.getInt("APPOINTMENTDATE"));
                    bpmInbox.setDoaLevel(rs.getString("APPOINTMENTDATE"));
                    bpmInbox.setPreviousAction(rs.getString("PREVIOUSACTION"));
                    bpmInbox.setSlaEndTime(rs.getInt("SLAENDTIME"));
                    bpmInbox.setSlaPeriod(rs.getInt("SLAPERIOD"));
                    bpmInbox.setTotalTimeAtUser(rs.getInt("TOTALTIMEATUSER"));
                    bpmInbox.setTotalTimeAtProcess(rs.getInt("TOTALTIMEATPROCESS"));
                    bpmInbox.setCurrentUser(rs.getString("CURRENTUSER"));
                    bpmInbox.setReturnFlag(rs.getString("RETURNFLAG"));
                    bpmInbox.setTeamName(rs.getString("TEAMNAME"));
                    bpmInboxList.add(bpmInbox);
                }
                rs.close();
                conn.close();
                conn = null;
                log.debug("connection closed.");
            } catch (SQLException e) {
                log.error("execute query exception!",e);
                throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
            } finally {
                closeConnection();
            }
        }
        return bpmInboxList;
    }

    public List<BPMInbox> getReturnBox(String userId, FieldName fieldName, OrderType orderType, int startRecord, int endRecord){
        log.debug("getReturnBox (userId: {}, fieldName: {}, orderType: {}, startRecord: {}, endRecord: {})",userId,fieldName,orderType,startRecord,endRecord);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();

        if(startRecord!=0 || endRecord!=0){
            String SQL_SELECT = "SELECT * FROM " +
                    "(SELECT ROWNUM rec_no, A.* FROM " +
                    "(SELECT * FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                    ") " +
                    "WHERE rec_no >= ? AND rec_no <= ?";
            if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ? AND rec_no <= ?";
            }

            if(endRecord==0){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ?";
                if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                    SQL_SELECT = "SELECT * FROM " +
                            "(SELECT ROWNUM rec_no, A.* FROM " +
                            "(SELECT * FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y' ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                            ") " +
                            "WHERE rec_no >= ?";
                }
            }

            try{
                conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
            } catch (BPMInterfaceException ex){
                throw ex;
            }

            try {
                log.debug("open connection.");
                log.debug("SQL_SELECT : {}",SQL_SELECT);
                PreparedStatement statement = conn.prepareStatement(SQL_SELECT);
                statement.setString(1, userId);
                statement.setInt(2, startRecord);
                if(endRecord!=0){
                    statement.setInt(3, endRecord);
                }
                rs = statement.executeQuery();
                while (rs.next()) {
                    BPMInbox bpmInbox = new BPMInbox();
                    bpmInbox.setReceivedTime(rs.getInt("RECEIVEDTIME"));
                    bpmInbox.setAppNumber(rs.getString("APPNUMBER"));
                    bpmInbox.setBorrowerName(rs.getString("BORROWERNAME"));
                    bpmInbox.setProductGroup(rs.getString("PRODUCTGROUP"));
                    bpmInbox.setRequestType(rs.getInt("REQUESTTYPE"));
                    bpmInbox.setfStepName(rs.getString("F_STEPNAME"));
                    bpmInbox.setStatus(rs.getString("STATUS"));
                    bpmInbox.setPreviousUser(rs.getString("PREVIOUSUSER"));
                    bpmInbox.setAppointmentDate(rs.getInt("APPOINTMENTDATE"));
                    bpmInbox.setDoaLevel(rs.getString("APPOINTMENTDATE"));
                    bpmInbox.setPreviousAction(rs.getString("PREVIOUSACTION"));
                    bpmInbox.setSlaEndTime(rs.getInt("SLAENDTIME"));
                    bpmInbox.setSlaPeriod(rs.getInt("SLAPERIOD"));
                    bpmInbox.setTotalTimeAtUser(rs.getInt("TOTALTIMEATUSER"));
                    bpmInbox.setTotalTimeAtProcess(rs.getInt("TOTALTIMEATPROCESS"));
                    bpmInbox.setCurrentUser(rs.getString("CURRENTUSER"));
                    bpmInbox.setReturnFlag(rs.getString("RETURNFLAG"));
                    bpmInbox.setTeamName(rs.getString("TEAMNAME"));
                    bpmInboxList.add(bpmInbox);
                }
                rs.close();
                conn.close();
                conn = null;
                log.debug("connection closed.");
            } catch (SQLException e) {
                log.error("execute query exception!",e);
                throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
            } finally {
                closeConnection();
            }
        }

        return bpmInboxList;
    }

    public List<BPMInbox> getBDMUWBox(String userId, FieldName fieldName, OrderType orderType, int startRecord, int endRecord){
        log.debug("getBDMUWBox (userId: {}, fieldName: {}, orderType: {}, startRecord: {}, endRecord: {})",userId,fieldName,orderType,startRecord,endRecord);
        List<BPMInbox> bpmInboxList = new ArrayList<BPMInbox>();

        if(startRecord!=0 || endRecord!=0){
            String SQL_SELECT = "SELECT * FROM " +
                    "(SELECT ROWNUM rec_no, A.* FROM " +
                    "(SELECT * FROM "+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ? ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                    ") " +
                    "WHERE rec_no >= ? AND rec_no <= ?";
            if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+schema+"."+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ? ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ? AND rec_no <= ?";
            }

            if(endRecord==0){
                SQL_SELECT = "SELECT * FROM " +
                        "(SELECT ROWNUM rec_no, A.* FROM " +
                        "(SELECT * FROM "+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ? ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                        ") " +
                        "WHERE rec_no >= ?";
                if(schema!=null && !schema.trim().equalsIgnoreCase("")){
                    SQL_SELECT = "SELECT * FROM " +
                            "(SELECT ROWNUM rec_no, A.* FROM " +
                            "(SELECT * FROM "+schema+"."+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ? ORDER BY "+fieldName.value()+" "+orderType.value()+") A" +
                            ") " +
                            "WHERE rec_no >= ?";
                }
            }

            try{
                conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
            } catch (BPMInterfaceException ex){
                throw ex;
            }

            try {
                log.debug("open connection.");
                log.debug("SQL_SELECT : {}",SQL_SELECT);
                PreparedStatement statement = conn.prepareStatement(SQL_SELECT);
                userId = "%"+userId+"%";
                statement.setString(1, userId);
                statement.setInt(2, startRecord);
                if(endRecord!=0){
                    statement.setInt(3, endRecord);
                }
                rs = statement.executeQuery();
                while (rs.next()) {
                    BPMInbox bpmInbox = new BPMInbox();
                    bpmInbox.setReceivedTime(rs.getInt("RECEIVEDTIME"));
                    bpmInbox.setAppNumber(rs.getString("APPNUMBER"));
                    bpmInbox.setBorrowerName(rs.getString("BORROWERNAME"));
                    bpmInbox.setProductGroup(rs.getString("PRODUCTGROUP"));
                    bpmInbox.setRequestType(rs.getInt("REQUESTTYPE"));
                    bpmInbox.setfStepName(rs.getString("F_STEPNAME"));
                    bpmInbox.setStatus(rs.getString("STATUS"));
                    bpmInbox.setPreviousUser(rs.getString("PREVIOUSUSER"));
                    bpmInbox.setAppointmentDate(rs.getInt("APPOINTMENTDATE"));
                    bpmInbox.setDoaLevel(rs.getString("APPOINTMENTDATE"));
                    bpmInbox.setPreviousAction(rs.getString("PREVIOUSACTION"));
                    bpmInbox.setSlaEndTime(rs.getInt("SLAENDTIME"));
                    bpmInbox.setSlaPeriod(rs.getInt("SLAPERIOD"));
                    bpmInbox.setTotalTimeAtUser(rs.getInt("TOTALTIMEATUSER"));
                    bpmInbox.setTotalTimeAtProcess(rs.getInt("TOTALTIMEATPROCESS"));
                    bpmInbox.setCurrentUser(rs.getString("CURRENTUSER"));
                    bpmInbox.setReturnFlag(rs.getString("RETURNFLAG"));
                    bpmInbox.setTeamName(rs.getString("TEAMNAME"));
                    bpmInboxList.add(bpmInbox);
                }
                rs.close();
                conn.close();
                conn = null;
                log.debug("connection closed.");
            } catch (SQLException e) {
                log.error("execute query exception!",e);
                throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
            } finally {
                closeConnection();
            }
        }

        return bpmInboxList;
    }

    public int getMyBoxCount(String userId){
        log.debug("getMyBoxCount userId: {}", userId);
        int count = 0;
        String SQL_COUNT = "SELECT COUNT(*) FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N'";
        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COUNT = "SELECT COUNT(*) FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'N'";
        }
        try{
            conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
        } catch (BPMInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COUNT : {}",SQL_COUNT);
            PreparedStatement statement = conn.prepareStatement(SQL_COUNT);
            statement.setString(1, userId);
            rs = statement.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return count;
    }

    public int getReturnBoxCount(String userId){
        log.debug("getMyBoxCount userId: {}", userId);
        int count = 0;
        String SQL_COUNT = "SELECT COUNT(*) FROM "+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y'";
        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COUNT = "SELECT COUNT(*) FROM "+schema+"."+prefix+"_INBOX WHERE CURRENTUSER = ? AND RETURNFLAG = 'Y'";
        }
        try{
            conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
        } catch (BPMInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COUNT : {}",SQL_COUNT);
            PreparedStatement statement = conn.prepareStatement(SQL_COUNT);
            statement.setString(1, userId);
            rs = statement.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return count;
    }

    public int getBDMUWBoxCount(String userId){
        log.debug("getMyBoxCount userId: {}", userId);
        int count = 0;
        String SQL_COUNT = "SELECT COUNT(*) FROM "+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ?";
        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COUNT = "SELECT COUNT(*) FROM "+schema+"."+prefix+"_Return_UW_BDM_Q WHERE CURRENTUSER LIKE ?";
        }
        try{
            conn = dbContext.getConnection(connBPM, bpmUser, bpmPassword);
        } catch (BPMInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COUNT : {}",SQL_COUNT);
            PreparedStatement statement = conn.prepareStatement(SQL_COUNT);
            userId = "%"+userId+"%";
            statement.setString(1, userId);
            rs = statement.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new BPMInterfaceException(e, ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR, msg.get(ExceptionMapping.BPM_GET_INBOX_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return count;
    }

    private void closeConnection() {
        if(rs != null){
            try{
                rs.close();
                log.debug("result set closed. (in finally)");
            } catch (SQLException e) {
                rs = null;
            }
        }
        if (conn != null) {
            try {
                conn.close();
                log.debug("connection closed. (in finally)");
            } catch (SQLException e) {
                conn = null;
            }
        }
    }
}
