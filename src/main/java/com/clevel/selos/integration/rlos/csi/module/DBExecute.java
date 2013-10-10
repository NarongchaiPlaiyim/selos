package com.clevel.selos.integration.rlos.csi.module;

import com.clevel.selos.integration.RLOS;
import com.clevel.selos.integration.RLOSInterface;
import com.clevel.selos.integration.rlos.csi.model.CSIData;
import com.clevel.selos.integration.rlos.csi.tool.DBContext;
import com.clevel.selos.model.DocumentType;
import com.clevel.selos.system.Config;
import com.clevel.selos.util.Util;
import com.filenet.wcm.api.TransactionTimeoutException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    transient ResultSet rs = null;

    @Inject
    public DBExecute(){

    }

    public Map<String,CSIData> getWarningCodeListFullyMatched(DocumentType documentType, String idNumber) throws Exception{
        log.debug("getWarningCodeFullyMatched DocumentType: {}, citizenId: {}",documentType.toString(),idNumber);
        Map<String,CSIData> warningCodeMap = null;
        String clause;
        switch (documentType){
            case CITIZEN_ID:
                clause = "ID_NO";
                break;
            case PASSPORT:
                clause = "PASSPORT_NO";
                break;
            case CORPORATE_ID:
                clause = "BUSINESS_REG";
                break;
            default:
                clause = "ID_NO";
                break;
        }

        String SQL_SELECT = "SELECT ID_NO, PASSPORT_NO, BUSINESS_REG, NAME_TH, NAME_EN, WARNING_CODE, SOURCE, DATA_DATE, WARNING_CODE_DATE "+
                "FROM "+tableName+" WHERE "+clause+" = ?";

        try {
            log.debug("open connection.");
            log.debug("SQL_SELECT : {}",SQL_SELECT);
            conn = dbContext.getConnection(connRlos, rlosUser, rlosPassword);
            log.debug("where clause : {}",clause);
            PreparedStatement statement = conn.prepareStatement(SQL_SELECT);
            statement.setString(1, idNumber);
            rs = statement.executeQuery();
            warningCodeMap = new HashMap<String, CSIData>();
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
                log.debug("csi data : {}",csiData.toString());
                warningCodeMap.put(csiData.getWarningCode(),csiData);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
        } finally {
            closeConnection();
        }
        return warningCodeMap;
    }

    public Map<String,CSIData> getWarningCodeListPartialMatched(String nameTh, String nameEn) throws Exception{
        log.debug("getWarningCodeListPartialMatched nameTh: {}, nameEn: {}",nameTh,nameEn);
        Map<String,CSIData> warningCodeMap = null;

        String SQL_SELECT = "SELECT ID_NO, PASSPORT_NO, BUSINESS_REG, NAME_TH, NAME_EN, WARNING_CODE, SOURCE, DATA_DATE, WARNING_CODE_DATE "+
                "FROM "+tableName+" WHERE NAME_TH like ? OR NAME_EN like ? AND ID_NO is null AND PASSPORT_NO is null AND BUSINESS_REG is null";

        try {
            log.debug("open connection.");
            log.debug("SQL_SELECT : {}",SQL_SELECT);
            conn = dbContext.getConnection(connRlos, rlosUser, rlosPassword);
            PreparedStatement statement = conn.prepareStatement(SQL_SELECT);
            if(!Util.isEmpty(nameTh)){
                nameTh = "%"+nameTh+"%";
            } else {
                nameTh = null;
            }

            if(!Util.isEmpty(nameEn)){
                nameEn = "%"+nameEn+"%";
            } else {
                nameEn = null;
            }

            statement.setString(1, nameTh);
            statement.setString(2, nameEn);
            rs = statement.executeQuery();
            warningCodeMap = new HashMap<String, CSIData>();
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
                log.debug("csi data : {}",csiData.toString());
                warningCodeMap.put(csiData.getWarningCode(),csiData);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
        } finally {
            closeConnection();
        }
        return warningCodeMap;
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
