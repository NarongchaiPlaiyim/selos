package com.clevel.selos.integration.rlos.csi.module;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.tool.DBContext;
import com.clevel.selos.system.Config;
import oracle.jdbc.driver.OracleTypes;
import oracle.jdbc.driver.OracleCallableStatement;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBExecute implements Serializable{
    @Inject
    @RLOS
    Logger log;
    @Inject
    @Config(name = "interface.rlos.oracle.conn")
    String connRlos;
    @Inject
    @Config(name = "interface.rlos.oracle.username")
    String rlosUser;
    @Inject
    @Config(name = "interface.rlos.oracle.password")
    String rlosPassword;
    @Inject
    @Config(name = "interface.rlos.oracle.table.csi")
    String tableName;

    @Inject
    DBContext dbContext;

    Connection conn = null;
    ResultSet rs = null;

    @Inject
    public DBExecute(){

    }

    public List<CSIData> getCSIDataByDocumentType(RLOSInterface.DocumentType documentType, String idNumber) throws Exception{
        log.debug("getCSIDataByDocumentType DocumentType: {}, citizenId: {}",documentType.toString(),idNumber);
        List<CSIData> csiDataList = null;
        String clause;
        switch (documentType){
            case CITIZEN_ID:
                clause = "CITIZEN_ID";
                break;
            case PASSPORT:
                clause = "PASSPORT_NO";
                break;
            case CORPORATE_ID:
                clause = "BUSINESS_REG_NO";
                break;
            default:
                clause = "CITIZEN_ID";
                break;
        }

        //String SQL_SELECT_CSI_BY_CID = "SELECT ID_NO, PASSPORT_NO, BUSINESS_REG, NAME_TH, NAME_EN, WARNING_CODE, SOURCE, DATA_DATE, WARNING_CODE_DATE";
        String SQL_SELECT_CSI_BY_CID = "SELECT CITIZEN_ID, PASSPORT_NO, BUSINESS_REG_NO, NAME_TH, NAME_EN, WARNING_CODE, SOURCE, DATA_DATE, DATE_WARNING_CODE "+
                                        "FROM "+tableName+" WHERE "+clause+" = ?";

        try {
            csiDataList = new ArrayList<CSIData>();
            log.debug("open connection.");
            conn = dbContext.getConnection(connRlos, rlosUser, rlosPassword);
            log.debug("get from table name : {}",tableName);
            log.debug("where clause : {}",clause);
            PreparedStatement statement = conn.prepareStatement(SQL_SELECT_CSI_BY_CID);
            statement.setString(1, idNumber);
            rs = statement.executeQuery();
            while (rs.next()) {
                CSIData csiData = new CSIData();
                csiData.setCitizenId(rs.getString(1));
                csiData.setPassportNo(rs.getString(2));
                csiData.setBusinessRegNo(rs.getString(3));
                csiData.setNameTh(rs.getString(4));
                csiData.setNameEn(rs.getString(5));
                csiData.setWarningCode(rs.getString(6));
                csiData.setSource(rs.getString(7));
                csiData.setDataDate(rs.getString(8));
                csiData.setDateWarningCode(rs.getString(9));
                csiDataList.add(csiData);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
            log.debug("Finished executing csi query , data size. ({})",csiDataList.size());
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            csiDataList = null;
        } finally {
            closeConnection();
        }

        return csiDataList;
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
