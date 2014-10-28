package com.clevel.selos.integration.ecm.module;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.ECM;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.db.ECMTypeName;
import com.clevel.selos.integration.ecm.tool.DBContext;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DBExecute implements Serializable {
    @Inject
    @ECM
    private Logger log;
    @Inject
    @Config(name = "interface.ecm.oracle.conn")
    private String connECM;
    @Inject
    @Config(name = "interface.ecm.oracle.username")
    private String ecmUser;
    @Inject
    @Config(name = "interface.ecm.oracle.password")
    private String ecmPassword;
    @Inject
    @Config(name = "interface.ecm.oracle.schema")
    private String schema;
    @Inject
    private DBContext dbContext;

    //FOR UPDATE AND INSERT
    @Inject
    @Config(name = "interface.ecm.cap.share.oracle.conn")
    private String connECMCAPShare;
    @Inject
    @Config(name = "interface.ecm.cap.share.oracle.username")
    private String ecmUserCAPShare;
    @Inject
    @Config(name = "interface.ecm.cap.share.oracle.password")
    private String ecmPasswordCAPShare;
    @Inject
    @Config(name = "interface.ecm.cap.share.oracle.schema")
    private String schemaCAPShare;

    @Inject
    @ExceptionMessage
    private Message msg;
    private Connection connection = null;
    private transient ResultSet resultSet = null;
    private PreparedStatement prepStmt = null;
    private Statement stmt = null;

    @Inject
    public DBExecute() {

    }

    public Map<String, ECMTypeName> findByEcmDocId(final List<String> ecmDocTypeIdList){
        log.debug("-- findByEcmDocId.[{}]",ecmDocTypeIdList);

        String schemaDot = null;
        if(!Util.isNull(schema) && !Util.isZero(schema.length()))
            schemaDot = schema + ".";

        StringBuilder ecmDocTypeIdBuilder = new StringBuilder();
        int count = 1;
        for(String ecmDocTypeID : ecmDocTypeIdList){
            ecmDocTypeIdBuilder.append(ecmDocTypeID);
            if(count < ecmDocTypeIdList.size()){
                ecmDocTypeIdBuilder.append(", ");
            }
            count++;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DOCUMENTTYPE.TYPE_CODE, DOCUMENTTYPE.TYPE_NAME_TH, DOCUMENTTYPE.TYPE_NAME_EN ")
                .append("FROM ").append(schemaDot).append("WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ")
                .append("WHERE DOCUMENTTYPE.TYPE_CODE in (").append(ecmDocTypeIdBuilder.toString()).append(")");

        try{
            connection = dbContext.getConnection(connECM, ecmUser, ecmPassword);
        } catch (ECMInterfaceException ex){
            ex.printStackTrace();
            throw ex;
        }

        Map<String, ECMTypeName> ecmDocTypeMap = new ConcurrentHashMap<String, ECMTypeName>();
        try {
            log.debug("open connection.");
            String sql = stringBuilder.toString();
            log.debug("-- SQL[{}]", sql);
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                ECMTypeName ecmTypeName = new ECMTypeName();
                ecmTypeName.setId(resultSet.getString(1));
                ecmTypeName.setTypeNameTH(resultSet.getString(2));
                ecmTypeName.setTypeNameEN(resultSet.getString(3));
                ecmDocTypeMap.put(ecmTypeName.getId(), ecmTypeName);
            }

            if(!Util.isNull(ecmDocTypeMap)){
                log.debug("-- ECMTypeName : [{}]", ecmDocTypeMap);
            } else {
                log.debug("-- ECMTypeName is null.");
            }

            log.debug("connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_GETDATA_ERROR, msg.get(ExceptionMapping.ECM_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        log.debug("return ecmDocTypeMap: {}", ecmDocTypeMap);
        return ecmDocTypeMap;
    }

    public List<ECMDetail> findByCANumber(final String caNumber){
        log.debug("-- findByCANumber.[{}]",caNumber);
        List<ECMDetail> ecmDetailList = null;
        StringBuilder stringBuilder = null;

        String schemaDot = null;
        if(!Util.isNull(schema) && !Util.isZero(schema.length()))
            schemaDot = schema + ".";

        stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT DOCUMENT.ECM_DOC_ID, DOCUMENT.CA_NUMBER, DOCUMENT.FN_DOC_ID, DOCUMENTTYPE.TYPE_CODE, DOCUMENTTYPE.TYPE_NAME_TH, DOCUMENT.IMPORT_DATE, DOCUMENT.CREATE_DATE ")
                .append("FROM ").append(schemaDot).append("WCAP_WK_DOCUMENT DOCUMENT ")
                .append("LEFT OUTER JOIN ").append(schemaDot).append("WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ")
                .append("WHERE DOCUMENT.CA_NUMBER = ? ")
                .append("UNION ")
                .append("SELECT DOCUMENT.ECM_DOC_ID, CATOCUST.CA_NUMBER, DOCUMENT.FN_DOC_ID, DOCUMENTTYPE.TYPE_CODE, DOCUMENTTYPE.TYPE_NAME_TH, DOCUMENT.IMPORT_DATE, DOCUMENT.CREATE_DATE ")
                .append("FROM ").append(schemaDot).append("WCAP_RE_CATOCUST CATOCUST ")
                .append("LEFT OUTER JOIN ").append(schemaDot).append("WCAP_WK_DOCUMENT DOCUMENT ON CATOCUST.ECM_CUS_ID = DOCUMENT.ECM_CUS_ID ")
                .append("LEFT OUTER JOIN ").append(schemaDot).append("WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ")
                .append("WHERE CATOCUST.CA_NUMBER = ? AND DOCUMENT.CA_NUMBER IS NULL");

        try{
            connection = dbContext.getConnection(connECM, ecmUser, ecmPassword);
        } catch (ECMInterfaceException ex){
            ex.printStackTrace();
            throw ex;
        }

        try {
            log.debug("open connection.");
            String sql = stringBuilder.toString();
            log.debug("-- SQL[{}]", sql);
            prepStmt = connection.prepareStatement(sql);

            prepStmt.setString(1, caNumber);
            prepStmt.setString(2, caNumber);
            resultSet = prepStmt.executeQuery();
            ecmDetailList = new ArrayList<ECMDetail>();
            while (resultSet.next()) {
                ECMDetail ecmDetail = new ECMDetail();
                ecmDetail.setEcmDocId(resultSet.getString(1));
                ecmDetail.setCaNumber(resultSet.getString(2));
                ecmDetail.setFnDocId(resultSet.getString(3));
                ecmDetail.setTypeCode(resultSet.getString(4));
                ecmDetail.setTypeNameTH(resultSet.getString(5));
                ecmDetail.setImportDate(resultSet.getDate(6));
                ecmDetail.setCreateDate(resultSet.getDate(7));
                ecmDetailList.add(ecmDetail);
            }
            log.debug("-- ECMDetail was added to ecmDetailList[Size {}]", ecmDetailList.size());
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_GETDATA_ERROR, msg.get(ExceptionMapping.ECM_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return ecmDetailList;
    }

    public boolean updateECM(final ECMCAPShare ecmcapShare){
        log.debug("-- updateECM.[{}]", ecmcapShare.getCrsUKCANumber());
        boolean result = false;
        StringBuilder stringBuilder = null;
        stringBuilder = new StringBuilder();
        if(!Util.isNull(schemaCAPShare) && !Util.isZero(schemaCAPShare.length())){
            stringBuilder.append("UPDATE ")
                    .append(schemaCAPShare).append(".CRS_CRSLOOKUP ").append("SET ")
                    .append("CRS_LASTUPDATE = ?, ")
                    .append("CRS_CANCEL_CA = ? ")
                    .append("WHERE ")
                    .append("CRS_UK_CANUMBER = ?");
        } else {
            stringBuilder.append("UPDATE ")
                    .append("CRS_CRSLOOKUP ").append("SET ")
                    .append("CRS_LASTUPDATE = ?, ")
                    .append("CRS_CANCEL_CA = ? ")
                    .append("WHERE ")
                    .append("CRS_UK_CANUMBER = ?");
        }
        try{
            connection = dbContext.getConnection(connECMCAPShare, ecmUserCAPShare, ecmPasswordCAPShare);
        } catch (ECMInterfaceException ex){
            throw ex;
        }
        try {
            log.debug("open connection.");
            String sql = stringBuilder.toString();
            log.debug("-- SQL[{}]", sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1, ecmcapShare.getCrsLastUpdate());
            statement.setString(2, ecmcapShare.getCrsCancelCA());
            statement.setString(3, ecmcapShare.getCrsUKCANumber());
            int flag = statement.executeUpdate();
            if(flag != -1){
                return !result;
            }
            connection.close();
            connection = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_UPDATEDATA_ERROR, msg.get(ExceptionMapping.ECM_UPDATEDATA_ERROR));
        } finally {
            closeConnection();
        }
        return result;
    }

    public boolean insertIntoECM(final ECMCAPShare ecmcapShare){
        boolean result = false;
        log.debug("-- insertIntoECM.[{}]", ecmcapShare.getCrsUKCANumber());
        StringBuilder stringBuilder = null;
        stringBuilder = new StringBuilder();
        if(!Util.isNull(schemaCAPShare) && !Util.isZero(schemaCAPShare.length())){
            stringBuilder.append("INSERT INTO ")
                    .append(schemaCAPShare).append(".CRS_CRSLOOKUP")
                    .append("(CRS_BRANCHCODE, ")
                    .append("CRS_CANCEL_CA, ").append("CRS_CRATEDATE, ")
                    .append("CRS_CUSTNAME, ").append("CRS_CUSTTYPE, ")
                    .append("CRS_HUBCODE, ").append("CRS_LASTUPDATE, ")
                    .append("CRS_REGIONCODE, ").append("CRS_ROCODE, ")
                    .append("CRS_UK_CANUMBER )")
                    .append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            stringBuilder.append("INSERT INTO ")
                    .append("CRS_CRSLOOKUP")
                    .append("(CRS_BRANCHCODE, ")
                    .append("CRS_CANCEL_CA, ").append("CRS_CRATEDATE, ")
                    .append("CRS_CUSTNAME, ").append("CRS_CUSTTYPE, ")
                    .append("CRS_HUBCODE, ").append("CRS_LASTUPDATE, ")
                    .append("CRS_REGIONCODE, ").append("CRS_ROCODE, ")
                    .append("CRS_UK_CANUMBER)")
                    .append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        }
        try{
            connection = dbContext.getConnection(connECMCAPShare, ecmUserCAPShare, ecmPasswordCAPShare);
        } catch (ECMInterfaceException ex){
            throw ex;
        }
        try {
            log.debug("open connection.");
            String sql = stringBuilder.toString();
            log.debug("-- SQL[{}]", sql);
            prepStmt = connection.prepareStatement(sql);
            prepStmt.setString(1, Util.convertNullToZero(ecmcapShare.getCrsBranchCode()));
            prepStmt.setString(2,  ecmcapShare.getCrsCancelCA() == null ? "N" : ecmcapShare.getCrsCancelCA());
            prepStmt.setDate(3, ecmcapShare.getCrsCreateDate());
            prepStmt.setString(4, ecmcapShare.getCrsCustName());
            prepStmt.setString(5,  Util.convertNullToZero(ecmcapShare.getCrsCusType()));
            prepStmt.setString(6,  Util.convertNullToZero(ecmcapShare.getCrsHubCode()));
            prepStmt.setDate(7, ecmcapShare.getCrsLastUpdate());
            prepStmt.setString(8,  Util.convertNullToZero(ecmcapShare.getCrsRegionCode()));
            prepStmt.setString(9,  Util.convertNullToZero(ecmcapShare.getCrsRoCode()));
            prepStmt.setString(10, ecmcapShare.getCrsUKCANumber());
            int flag = prepStmt.executeUpdate();
            if(flag != -1){
                return !result;
            }

            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_INSERTDATA_ERROR, msg.get(ExceptionMapping.ECM_INSERTDATA_ERROR));
        } finally {
            closeConnection();
        }
        return result;
    }

    private void closeConnection() {
        if(!Util.isNull(resultSet)){
            try{
                resultSet.close();
                log.debug("result set closed. (in finally)");
            } catch (SQLException e) {
                resultSet = null;
            }
        }
        if(!Util.isNull(stmt)){
            try{
                stmt.close();
            }catch (SQLException ex){
                stmt = null;
            }
        }
        if(!Util.isNull(prepStmt)){
            try{
                prepStmt.close();
            }catch (SQLException ex){
                prepStmt = null;
            }
        }
        if (!Util.isNull(connection)) {
            try {
                connection.close();
                log.debug("connection closed. (in finally)");
            } catch (SQLException e) {
                connection = null;
            }
        }
    }
}
