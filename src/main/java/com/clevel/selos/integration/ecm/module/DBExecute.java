package com.clevel.selos.integration.ecm.module;

import com.clevel.selos.exception.ECMInterfaceException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ecm.db.ECMCAPShare;
import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.tool.DBContext;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
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
    @NCB
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
    @Inject
    public DBExecute() {

    }

    public List<ECMDetail> findByCANumber(final String caNumber){
        log.debug("-- findByCANumber.[{}]",caNumber);
        List<ECMDetail> ecmDetailList = null;
        StringBuilder stringBuilder = null;

        stringBuilder = new StringBuilder();
        if(!Util.isNull(schema) && !Util.isZero(schema.length())){
            stringBuilder.append("SELECT ");
            stringBuilder.append("DOCUMENT.ECM_DOC_ID, ");
            stringBuilder.append("DOCUMENT.CA_NUMBER, ");
            stringBuilder.append("DOCUMENT.FN_DOC_ID, ");
            stringBuilder.append("TXDETAIL.ORG_FILENAME, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_CODE, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_NAME_TH ");
            stringBuilder.append("FROM "+schema+".WCAP_WK_DOCUMENT DOCUMENT ");
            stringBuilder.append("LEFT OUTER JOIN "+schema+".WCAP_WK_TXDETAIL TXDETAIL ON DOCUMENT.TX_DETAIL_ID = TXDETAIL.TX_DETAIL_ID ");
            stringBuilder.append("LEFT OUTER JOIN "+schema+".WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ");
            stringBuilder.append("WHERE ");
            stringBuilder.append("DOCUMENT.CA_NUMBER = ? AND ");
            stringBuilder.append("DOCUMENT.CA_NUMBER IS NULL AND DOCUMENT.TX_DETAIL_ID <> 0 ");
            stringBuilder.append("UNION ");
            stringBuilder.append("SELECT ");
            stringBuilder.append("DOCUMENT.ECM_DOC_ID, ");
            stringBuilder.append("CATOCUST.CA_NUMBER, ");
            stringBuilder.append("DOCUMENT.FN_DOC_ID, ");
            stringBuilder.append("TXDETAIL.ORG_FILENAME, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_CODE, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_NAME_TH ");
            stringBuilder.append("FROM "+schema+".WCAP_RE_CATOCUST CATOCUST ");
            stringBuilder.append("LEFT OUTER JOIN "+schema+".WCAP_WK_DOCUMENT DOCUMENT ON CATOCUST.ECM_CUS_ID = DOCUMENT.ECM_CUS_ID ");
            stringBuilder.append("LEFT OUTER JOIN "+schema+".WCAP_WK_TXDETAIL TXDETAIL ON DOCUMENT.TX_DETAIL_ID = TXDETAIL.TX_DETAIL_ID ");
            stringBuilder.append("LEFT OUTER JOIN "+schema+".WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ");
            stringBuilder.append("WHERE ");
            stringBuilder.append("CATOCUST.CA_NUMBER = ? AND ");
            stringBuilder.append("DOCUMENT.CA_NUMBER IS NULL AND DOCUMENT.TX_DETAIL_ID <> 0");
        } else {
            stringBuilder.append("SELECT ");
            stringBuilder.append("DOCUMENT.ECM_DOC_ID, ");
            stringBuilder.append("DOCUMENT.CA_NUMBER, ");
            stringBuilder.append("DOCUMENT.FN_DOC_ID, ");
            stringBuilder.append("TXDETAIL.ORG_FILENAME, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_CODE, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_NAME_TH ");
            stringBuilder.append("FROM WCAP_WK_DOCUMENT DOCUMENT ");
            stringBuilder.append("LEFT OUTER JOIN WCAP_WK_TXDETAIL TXDETAIL ON DOCUMENT.TX_DETAIL_ID = TXDETAIL.TX_DETAIL_ID ");
            stringBuilder.append("LEFT OUTER JOIN WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ");
            stringBuilder.append("WHERE ");
            stringBuilder.append("DOCUMENT.CA_NUMBER = ? AND ");
            stringBuilder.append("DOCUMENT.CA_NUMBER IS NULL AND DOCUMENT.TX_DETAIL_ID <> 0 ");
            stringBuilder.append("UNION ");
            stringBuilder.append("SELECT ");
            stringBuilder.append("DOCUMENT.ECM_DOC_ID, ");
            stringBuilder.append("CATOCUST.CA_NUMBER, ");
            stringBuilder.append("DOCUMENT.FN_DOC_ID, ");
            stringBuilder.append("TXDETAIL.ORG_FILENAME, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_CODE, ");
            stringBuilder.append("DOCUMENTTYPE.TYPE_NAME_TH ");
            stringBuilder.append("FROM WCAP_RE_CATOCUST CATOCUST ");
            stringBuilder.append("LEFT OUTER JOIN WCAP_WK_DOCUMENT DOCUMENT ON CATOCUST.ECM_CUS_ID = DOCUMENT.ECM_CUS_ID ");
            stringBuilder.append("LEFT OUTER JOIN WCAP_WK_TXDETAIL TXDETAIL ON DOCUMENT.TX_DETAIL_ID = TXDETAIL.TX_DETAIL_ID ");
            stringBuilder.append("LEFT OUTER JOIN WCAP_MS_DOCUMENTTYPE DOCUMENTTYPE ON DOCUMENT.TYPE_CODE = DOCUMENTTYPE.TYPE_CODE ");
            stringBuilder.append("WHERE ");
            stringBuilder.append("CATOCUST.CA_NUMBER = ? AND ");
            stringBuilder.append("DOCUMENT.CA_NUMBER IS NULL AND DOCUMENT.TX_DETAIL_ID <> 0");
        }

        try{
            connection = dbContext.getConnection(connECM, ecmUser, ecmPassword);
        } catch (ECMInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            String sql = stringBuilder.toString();
            log.debug("-- SQL[{}]", sql);
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, caNumber);
            statement.setString(2, caNumber);
            resultSet = statement.executeQuery();
            ECMDetail ecmDetail = null;
            ecmDetailList = new ArrayList<ECMDetail>();
            while (resultSet.next()) {
                ecmDetail = new ECMDetail();
                ecmDetail.setEcmDocId(resultSet.getString(1));
                ecmDetail.setCaNumber(resultSet.getString(2));
                ecmDetail.setFnDocId(resultSet.getString(3));
                ecmDetail.setOrgFileName(resultSet.getString(4));
                ecmDetail.setTypeCode(resultSet.getString(5));
                ecmDetail.setTypeNameTH(resultSet.getString(6));
                ecmDetailList.add(ecmDetail);
            }
            log.debug("-- ECMDetail was added to ecmDetailList[Size {}]", ecmDetailList.size());
            resultSet.close();
            connection.close();
            connection = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_GETDATA_ERROR, msg.get(ExceptionMapping.ECM_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return ecmDetailList;
    }

    public boolean updateECM(final ECMCAPShare ecmcapShare){
        log.debug("-- updateECM.[{}]");
        StringBuilder stringBuilder = null;

        stringBuilder = new StringBuilder();
        if(!Util.isNull(schemaCAPShare) && !Util.isZero(schemaCAPShare.length())){
            stringBuilder.append("UPDATE ")
                    .append(schemaCAPShare).append(".CRS_CRSLOOKUP ").append("SET")
                    .append("CRS_LASTUPDATE = ?, ")
                    .append("CRS_CANCEL_CA = ? ")
                    .append(" WHERE ")


                    .append(schemaCAPShare).append(".CRS_CRSLOOKUP");
        } else {

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

            connection.close();
            connection = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_GETDATA_ERROR, msg.get(ExceptionMapping.ECM_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return true;
    }

    public boolean insertIntoECM(final ECMCAPShare ecmcapShare){
        boolean result = false;
        log.debug("-- updateECM.[{}]");
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
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, Util.convertNullToZero(ecmcapShare.getCrsBranchCode()));
            statement.setString(2,  ecmcapShare.getCrsCancelCA());
            statement.setDate(3, ecmcapShare.getCrsCreateDate());
            statement.setString(4, ecmcapShare.getCrsCustName());
            statement.setString(5,  Util.convertNullToZero(ecmcapShare.getCrsCusType()));
            statement.setString(6,  Util.convertNullToZero(ecmcapShare.getCrsHubCode()));
            statement.setDate(7, ecmcapShare.getCrsLastUpdate());
            statement.setString(8,  Util.convertNullToZero(ecmcapShare.getCrsRegionCode()));
            statement.setString(9,  Util.convertNullToZero(ecmcapShare.getCrsRoCode()));
            statement.setString(10, ecmcapShare.getCrsUKCANumber());
            int flag = statement.executeUpdate();
            if(flag != -1){
                return !result;
            }
            connection.close();
            connection = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new ECMInterfaceException(e, ExceptionMapping.ECM_GETDATA_ERROR, msg.get(ExceptionMapping.ECM_GETDATA_ERROR));
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
