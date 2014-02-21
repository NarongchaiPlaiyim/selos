package com.clevel.selos.integration.coms.module;

import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMS;
import com.clevel.selos.integration.coms.db.CollateralDecisionDetail;
import com.clevel.selos.integration.coms.db.CollateralJobLevel;
import com.clevel.selos.integration.coms.db.HeadCollateral;
import com.clevel.selos.integration.coms.db.SubCollateral;
import com.clevel.selos.integration.coms.model.AddressType;
import com.clevel.selos.integration.coms.tool.DBContext;
import com.clevel.selos.system.Config;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBExecute implements Serializable {
    @Inject
    @COMS
    Logger log;
    @Inject
    @Config(name = "interface.coms.oracle.conn")
    String connCOMS;
    @Inject
    @Config(name = "interface.coms.oracle.username")
    String comsUser;
    @Inject
    @Config(name = "interface.coms.oracle.password")
    String comsPassword;
    @Inject
    @Config(name = "interface.coms.oracle.schema")
    String schema;

    @Inject
    DBContext dbContext;

    @Inject
    @ExceptionMessage
    Message msg;

    Connection conn = null;
    transient ResultSet rs = null;
    private final String SPACE = " ";
    private final String COMMA = ", ";
    private final String COLON = ": ";

    @Inject
    public DBExecute() {

    }

    public CollateralJobLevel getCollateralJobLevel(String jobNo){
        log.debug("getCollateralJobLevel jobNo: {}",jobNo);
        CollateralJobLevel collateralJobLevel = null;
        String SQL_COLLATERAL_JOBLEVEL =    "SELECT " +
                                                "APPR_PRICE.JOB_NO as jobNo, " +
                                                "APPR_PRICE.CUR_APPR_DATE as curApprDate, " +
                                                "APPR_PRICE.IS_MATI as isMATI, " +
                                                "APPR_PRICE.DECISION as decision " +
                                            "FROM APPR_PRICE " +
                                            "WHERE APPR_PRICE.JOB_NO = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COLLATERAL_JOBLEVEL =   "SELECT " +
                                            "A.JOB_NO as jobNo, " +
                                            "A.CUR_APPR_DATE as curApprDate, " +
                                            "A.IS_MATI as isMATI, " +
                                            "A.DECISION as decision " +
                                        "FROM "+schema+".APPR_PRICE A " +
                                        "WHERE A.JOB_NO = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COLLATERAL_JOBLEVEL : {}",SQL_COLLATERAL_JOBLEVEL);
            PreparedStatement statement = conn.prepareStatement(SQL_COLLATERAL_JOBLEVEL);
            statement.setString(1, jobNo);
            rs = statement.executeQuery();
            while (rs.next()) {
                collateralJobLevel = new CollateralJobLevel();
                collateralJobLevel.setJobNo(rs.getString(1));
                collateralJobLevel.setCurApprDate(rs.getDate(2));
                collateralJobLevel.setMATI(rs.getString(3));
                collateralJobLevel.setDecision(rs.getString(4));
                log.debug("collateralJobLevel result : {}", collateralJobLevel);
            }
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return collateralJobLevel;
    }

    public CollateralDecisionDetail getCollateralDecisionDetail(String jobNo){
        log.debug("geCollateralDecisionDetail jobNo: {}",jobNo);
        CollateralDecisionDetail collateralDecisionDetail = null;
        String SQL_COLLATERAL_DECISIONDETAIL =  "SELECT " +
                                                    "APPR_COND.COND_NO as condNo, " +
                                                    "APPR_COND.COND_REMARK as remark, " +
                                                    "APPR_COND.COND_TYPE as condType " +
                                                "FROM APPR_COND " +
                                                "WHERE (APPR_COND.COND_TYPE = 'REA' OR APPR_COND.COND_TYPE = 'CON') " +
                                                "AND APPR_COND.JOB_NO = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COLLATERAL_DECISIONDETAIL =     "SELECT " +
                                                    "A.COND_NO as condNo, " +
                                                    "A.COND_REMARK as remark, " +
                                                    "A.COND_TYPE as condType " +
                                                "FROM "+schema+".APPR_COND A " +
                                                "WHERE (A.COND_TYPE = 'REA' OR A.COND_TYPE = 'CON') " +
                                                "AND A.JOB_NO = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COLLATERAL_DECISIONDETAIL : {}",SQL_COLLATERAL_DECISIONDETAIL);
            PreparedStatement statement = conn.prepareStatement(SQL_COLLATERAL_DECISIONDETAIL);
            statement.setString(1, jobNo);
            rs = statement.executeQuery();
            String reasonCondNo = null;
            String reasonCondRemark = null;
            String condNoCond = null;
            String condRemark = null;
            while (rs.next()) {
                String condNo = rs.getString(1);
                String remark = rs.getString(2);
                String condType = rs.getString(3);

                if("REA".equalsIgnoreCase(condType)){
                    if(reasonCondNo == null){
                        reasonCondNo = condNo;
                    } else {
                        reasonCondNo = reasonCondNo.concat(",").concat(condNo);
                    }

                    if(reasonCondRemark == null){
                        reasonCondRemark = remark;
                    } else {
                        reasonCondRemark = reasonCondRemark.concat(",").concat(remark);
                    }
                } else {
                    if(condNoCond == null){
                        condNoCond = condNo;
                    } else {
                        condNoCond = condNoCond.concat(",").concat(condNo);
                    }

                    if(condRemark == null){
                        condRemark = remark;
                    } else {
                        condRemark = condRemark.concat(",").concat(remark);
                    }
                }
            }

            if(reasonCondNo!=null || reasonCondRemark!=null || condNoCond!=null || condRemark!=null){
                collateralDecisionDetail = new CollateralDecisionDetail();
                collateralDecisionDetail.setReasonCondNo(reasonCondNo);
                collateralDecisionDetail.setReasonCondRemark(reasonCondRemark);
                collateralDecisionDetail.setCondNo(condNoCond);
                collateralDecisionDetail.setCondRemark(condRemark);
            }

            log.debug("collateralDecisionDetail result : {}",collateralDecisionDetail);

            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return collateralDecisionDetail;
    }

    public List<HeadCollateral> getHeadCollateral(String jobNo){
        log.debug("getHeadCollateral jobNo: {}",jobNo);
        List<HeadCollateral> headCollateralList = new ArrayList<HeadCollateral>();

        String SQL_COLLATERAL_HEAD =    "SELECT " +
                                            "APPR_PRICE_TR.COL_ID as colId, " +
                                            "APPR_PRICE_TR.COL_NO as colNo, " +
                                            "APPR_PRICE_TR.CRM_LOCATION as crmLocation, " +
                                            "APPR_PRICE_TR.ADD_DISTRICT as addDistrict, " +
                                            "APPR_PRICE_TR.ADD_CITY as addCity, " +
                                            "APPR_PRICE_TR.CITY_EXPAND as cityExpand, " +
                                            "APPR_PRICE_TR.PROV_EXPAND as provExpand, " +
                                            "SET_COUNTRY.CODE as countryCode, "+
                                            "SET_COUNTRY.NAME_THAI as countryNameThai, " +
                                            "CITY.CITY_ID as cityId, " +
                                            "CITY.CITY as city, " +
                                            "CITY.PROVINCE_ID as cityProvinceId, " +
                                            "PROVINCE.PROV_ID as provId, " +
                                            "PROVINCE.PROV_NAME as provName, " +
                                            "APPR_PRICE_TR.C_PRICE as cPrice, " +
                                            "APPR_PRICE_TR.MATI_PRICE as matiPrice, " +
                                            "APPR_PRICE_TR.COL_TYPE as colType, " +
                                            "APPR_PRICE_TR.COL_SUB_TYPE as colSubType " +
                                        "FROM APPR_PRICE_TR " +
                                        "LEFT JOIN SET_COUNTRY ON APPR_PRICE_TR.CRM_LOCATION = SET_COUNTRY.CODE " +
                                        "LEFT JOIN CITY ON APPR_PRICE_TR.ADD_CITY = CITY.CITY_ID " +
                                        "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                                        "WHERE APPR_PRICE_TR.JOB_NO = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COLLATERAL_HEAD =   "SELECT " +
                                        "A.COL_ID as colId, " +
                                        "A.COL_NO as colNo, " +
                                        "A.CRM_LOCATION as crmLocation, " +
                                        "A.ADD_DISTRICT as addDistrict, " +
                                        "A.ADD_CITY as addCity, " +
                                        "A.CITY_EXPAND as cityExpand, " +
                                        "A.PROV_EXPAND as provExpand, " +
                                        "B.CODE as countryCode, "+
                                        "B.NAME_THAI as countryNameThai, " +
                                        "C.CITY_ID as cityId, " +
                                        "C.CITY as city, " +
                                        "C.PROVINCE_ID as cityProvinceId, " +
                                        "D.PROV_ID as provId, " +
                                        "D.PROV_NAME as provName, " +
                                        "A.C_PRICE as cPrice, " +
                                        "A.MATI_PRICE as matiPrice, " +
                                        "A.COL_TYPE as colType, " +
                                        "A.COL_SUB_TYPE as colSubType " +
                                    "FROM "+schema+".APPR_PRICE_TR A " +
                                    "LEFT JOIN "+schema+".SET_COUNTRY B ON A.CRM_LOCATION = B.CODE " +
                                    "LEFT JOIN "+schema+".CITY C ON A.ADD_CITY = C.CITY_ID " +
                                    "LEFT JOIN "+schema+".PROVINCE D ON C.PROVINCE_ID = D.PROV_ID " +
                                    "WHERE A.JOB_NO = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COLLATERAL_HEAD : {}",SQL_COLLATERAL_HEAD);
            PreparedStatement statement = conn.prepareStatement(SQL_COLLATERAL_HEAD);
            statement.setString(1, jobNo);
            rs = statement.executeQuery();
            while (rs.next()) {
                HeadCollateral headCollateral = new HeadCollateral();
                headCollateral.setColId(rs.getString(1));
                headCollateral.setColNo(rs.getString(2));
                headCollateral.setCrmLocation(rs.getString(3));
                headCollateral.setAddDistrict(rs.getString(4));
                headCollateral.setAddCity(rs.getString(5));
                headCollateral.setCityExpand(rs.getString(6));
                headCollateral.setProvExpand(rs.getString(7));
                headCollateral.setCountryCode(rs.getString(8));
                headCollateral.setCountryNameThai(rs.getString(9));
                headCollateral.setCityId(rs.getString(10));
                headCollateral.setCity(rs.getString(11));
                headCollateral.setCityProvinceId(rs.getString(12));
                headCollateral.setProvId(rs.getString(13));
                headCollateral.setProvName(rs.getString(14));
                headCollateral.setcPrice(rs.getBigDecimal(15));
                headCollateral.setMatiPrice(rs.getBigDecimal(16));
                headCollateral.setColType(rs.getString(17));
                headCollateral.setColSubType(rs.getString(18));
                headCollateralList.add(headCollateral);
            }
            log.debug("headCollateralList result : {}", headCollateralList);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return headCollateralList;
    }

    public List<SubCollateral> getSubCollateral(String jobNo, String headColId){
        log.debug("getSubCollateral jobNo: {}, headColId: {}",jobNo, headColId);
        List<SubCollateral> subCollateralList = new ArrayList<SubCollateral>();

        String SQL_COLLATERAL_SUB = "SELECT " +
                                        "APPR_PRICE_TR_ONE.COL_ID as colId, " +
                                        "APPR_PRICE_TR_ONE.HEAD_COL_ID as headColId, " +
                                        "APPR_PRICE_TR_ONE.LINE_NO as lineNo, " +
                                        "APPR_PRICE_TR_ONE.COL_TYPE as colType, " +
                                        "APPR_PRICE_TR_ONE.COL_SUB_TYPE as colSubType, " +
                                        "APPR_PRICE_TR_ONE.OWN_DOC_NO as ownDocNo, " +
                                        "APPR_PRICE_TR_OWNER.CUS_ID as cusId, " +
                                        "PRENAME.PRE_NAME as preName, " +
                                        "ONL_PERSON.NAME as name, " +
                                        "ONL_PERSON.SIR_NAME as sirName, " +
                                        "APPR_PRICE_TR_ONE.C_PRICE as cPrice, " +
                                        "APPR_PRICE_TR_ONE.MATI_PRICE as matiPrice, " +
                                        "APPR_PRICE_TR_ONE.ONL_TYPE as onlType " +
                                    "FROM APPR_PRICE_TR_ONE " +
                                    "LEFT JOIN APPR_PRICE_TR_OWNER ON APPR_PRICE_TR_ONE.HEAD_COL_ID = APPR_PRICE_TR_OWNER.HEAD_COL_ID AND APPR_PRICE_TR_ONE.COL_ID = APPR_PRICE_TR_OWNER.ONE_COL_ID " +
                                    "LEFT JOIN ONL_PERSON ON APPR_PRICE_TR_OWNER.CUS_ID = ONL_PERSON.CUS_ID " +
                                    "LEFT JOIN PRENAME ON ONL_PERSON.PRE_NAME = PRENAME.PRE_ID " +
                                    "WHERE APPR_PRICE_TR_ONE.HEAD_COL_ID = ? " +
                                    "AND APPR_PRICE_TR_ONE.JOB_NO = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_COLLATERAL_SUB =    "SELECT " +
                                        "A.COL_ID as colId, " +
                                        "A.HEAD_COL_ID as headColId, " +
                                        "A.LINE_NO as lineNo, " +
                                        "A.COL_TYPE as colType, " +
                                        "A.COL_SUB_TYPE as colSubType, " +
                                        "A.OWN_DOC_NO as ownDocNo, " +
                                        "B.CUS_ID as cusId, " +
                                        "D.PRE_NAME as preName, " +
                                        "C.NAME as name, " +
                                        "C.SIR_NAME as sirName, " +
                                        "A.C_PRICE as cPrice, " +
                                        "A.MATI_PRICE as matiPrice, " +
                                        "A.ONL_TYPE as onlType " +
                                    "FROM "+schema+".APPR_PRICE_TR_ONE A " +
                                    "LEFT JOIN "+schema+".APPR_PRICE_TR_OWNER B ON A.HEAD_COL_ID = B.HEAD_COL_ID AND A.COL_ID = B.ONE_COL_ID " +
                                    "LEFT JOIN "+schema+".ONL_PERSON C ON B.CUS_ID = C.CUS_ID " +
                                    "LEFT JOIN "+schema+".PRENAME D ON C.PRE_NAME = D.PRE_ID " +
                                    "WHERE A.HEAD_COL_ID = ? " +
                                    "AND A.JOB_NO = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("open connection.");
            log.debug("SQL_COLLATERAL_SUB : {}",SQL_COLLATERAL_SUB);
            PreparedStatement statement = conn.prepareStatement(SQL_COLLATERAL_SUB);
            statement.setString(1, headColId);
            statement.setString(2, jobNo);
            rs = statement.executeQuery();
            while (rs.next()) {
                SubCollateral subCollateral = new SubCollateral();
                subCollateral.setColId(rs.getString(1));
                subCollateral.setHeadColId(rs.getString(2));
                subCollateral.setLineNo(rs.getLong(3));
                subCollateral.setColType(rs.getString(4));
                subCollateral.setColSubType(rs.getString(5));
                subCollateral.setOwnDocNo(rs.getString(6));
                subCollateral.setCusId(rs.getString(7));
                subCollateral.setPreName(rs.getString(8));
                subCollateral.setName(rs.getString(9));
                subCollateral.setSirName(rs.getString(10));
                subCollateral.setcPrice(rs.getBigDecimal(11));
                subCollateral.setMatiPrice(rs.getBigDecimal(12));
                subCollateral.setOnlType(rs.getString(13));
                subCollateralList.add(subCollateral);
            }
            log.debug("subCollateralList result : {}", subCollateralList);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }
        return subCollateralList;
    }

    public String getAddressType1(String colId, String headColId){
        log.debug("getAddressType1 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_1 = "SELECT " +
                                "A.DEED_NO as deedNo, " +
                                "A.COORDINATE_NO as coordinateNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.SURVEY_NO as surveyNo, " +
                                "A.BOOK_NO as bookNo, " +
                                "A.PAGE_NO as pageNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_LAND A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_1 = "SELECT " +
                            "A.DEED_NO as deedNo, " +
                            "A.COORDINATE_NO as coordinateNo, " +
                            "A.LAND_NO as landNo, " +
                            "A.SURVEY_NO as surveyNo, " +
                            "A.BOOK_NO as bookNo, " +
                            "A.PAGE_NO as pageNo, " +
                            "A.CONDO_DISTRICT as condoDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+".APPR_LAND A " +
                        "LEFT JOIN "+schema+".CITY B ON A.ADD_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+".PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_1 : {}",SQL_TYPE_1);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_1);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String deedNo = Util.getStringNotNull(rs.getString("deedNo"));
                String coordinateNo = Util.getStringNotNull(rs.getString("coordinateNo"));
                String landNo = Util.getStringNotNull(rs.getString("landNo"));
                String surveyNo = Util.getStringNotNull(rs.getString("surveyNo"));
                String bookNo = Util.getStringNotNull(rs.getString("bookNo"));
                String pageNo = Util.getStringNotNull(rs.getString("pageNo"));
                String condoDistrict = Util.getStringNotNull(rs.getString("condoDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = deedNo.concat(COMMA)
                            .concat(coordinateNo).concat(COMMA)
                            .concat(landNo).concat(COMMA)
                            .concat(surveyNo).concat(COMMA)
                            .concat(bookNo).concat(COMMA)
                            .concat(pageNo).concat(COMMA)
                            .concat(condoDistrict).concat(SPACE)
                            .concat(city).concat(SPACE)
                            .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType2(String colId, String headColId){
        log.debug("getAddressType2 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_2 = "SELECT " +
                                "A.DEED_NO as deedNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.BOOK_NO as bookNo, " +
                                "A.PAGE_NO as pageNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_LAND A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_2 = "SELECT " +
                                "A.DEED_NO as deedNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.BOOK_NO as bookNo, " +
                                "A.PAGE_NO as pageNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM "+schema+".APPR_LAND A " +
                            "LEFT JOIN "+schema+".CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN "+schema+".PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_2 : {}",SQL_TYPE_2);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_2);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String deedNo = Util.getStringNotNull(rs.getString("deedNo"));
                String landNo = Util.getStringNotNull(rs.getString("landNo"));
                String bookNo = Util.getStringNotNull(rs.getString("bookNo"));
                String pageNo = Util.getStringNotNull(rs.getString("pageNo"));
                String condoDistrict = Util.getStringNotNull(rs.getString("condoDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = deedNo.concat(COMMA)
                        .concat(landNo).concat(COMMA)
                        .concat(bookNo).concat(COMMA)
                        .concat(pageNo).concat(COMMA)
                        .concat(condoDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
                log.debug("-- address result : {}", address);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType3(String colId, String headColId){
        log.debug("getAddressType3 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_3 = "SELECT " +
                                "A.DEED_NO as deedNo, " +
                                "A.BOOK_NO as bookNo, " +
                                "A.PAGE_NO as pageNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.AIRIAL_PHOTO_NO as airialPhotoNo, " +
                                "A.COORDINATE_NO as coordinateNo, " +
                                "A.SHEET_NO as sheetNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_LAND A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_3 = "SELECT " +
                            "A.DEED_NO as deedNo, " +
                            "A.BOOK_NO as bookNo, " +
                            "A.PAGE_NO as pageNo, " +
                            "A.LAND_NO as landNo, " +
                            "A.AIRIAL_PHOTO_NO as airialPhotoNo, " +
                            "A.COORDINATE_NO as coordinateNo, " +
                            "A.SHEET_NO as sheetNo, " +
                            "A.CONDO_DISTRICT as condoDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+".APPR_LAND A " +
                        "LEFT JOIN "+schema+".CITY B ON A.ADD_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+".PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_3 : {}",SQL_TYPE_3);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_3);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String deedNo = Util.getStringNotNull(rs.getString("deedNo"));
                String bookNo = Util.getStringNotNull(rs.getString("bookNo"));
                String pageNo = Util.getStringNotNull(rs.getString("pageNo"));
                String landNo = Util.getStringNotNull(rs.getString("landNo"));
                String airialPhotoNo = Util.getStringNotNull(rs.getString("airialPhotoNo"));
                String coordinateNo = Util.getStringNotNull(rs.getString("coordinateNo"));
                String sheetNo = Util.getStringNotNull(rs.getString("sheetNo"));
                String condoDistrict = Util.getStringNotNull(rs.getString("condoDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = deedNo.concat(COMMA)
                        .concat(bookNo).concat(COMMA)
                        .concat(pageNo).concat(COMMA)
                        .concat(landNo).concat(COMMA)
                        .concat(airialPhotoNo).concat(COMMA)
                        .concat(coordinateNo).concat(COMMA)
                        .concat(sheetNo).concat(COMMA)
                        .concat(condoDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType4(String colId, String headColId){
        log.debug("getAddressType4 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_4 = "SELECT " +
                                "A.DEED_NO as deedNo, " +
                                "A.BOOK_NO as bookNo, " +
                                "A.PAGE_NO as pageNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.SURVEY_NO as surveyNo, " +
                                "A.COORDINATE_NO as coordinateNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_LAND A " +
                            "LEFT JOIN CITY B ON A.CONDO_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_4 = "SELECT " +
                            "A.DEED_NO as deedNo, " +
                            "A.BOOK_NO as bookNo, " +
                            "A.PAGE_NO as pageNo, " +
                            "A.LAND_NO as landNo, " +
                            "A.SURVEY_NO as surveyNo, " +
                            "A.COORDINATE_NO as coordinateNo, " +
                            "A.CONDO_DISTRICT as condoDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+".APPR_LAND A " +
                        "LEFT JOIN "+schema+".CITY B ON A.CONDO_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+".PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_4 : {}",SQL_TYPE_4);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_4);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String deedNo = Util.getStringNotNull(rs.getString("deedNo"));
                String bookNo = Util.getStringNotNull(rs.getString("bookNo"));
                String pageNo = Util.getStringNotNull(rs.getString("pageNo"));
                String landNo = Util.getStringNotNull(rs.getString("landNo"));
                String surveyNo = Util.getStringNotNull(rs.getString("surveyNo"));
                String coordinateNo = Util.getStringNotNull(rs.getString("coordinateNo"));
                String condoDistrict = Util.getStringNotNull(rs.getString("condoDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = deedNo.concat(COMMA)
                        .concat(bookNo).concat(COMMA)
                        .concat(pageNo).concat(COMMA)
                        .concat(landNo).concat(COMMA)
                        .concat(surveyNo).concat(COMMA)
                        .concat(coordinateNo).concat(COMMA)
                        .concat(condoDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType5(String colId, String headColId){
        log.debug("getAddressType5 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_5 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_BUILDING A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_5 = "SELECT " +
                            "A.ADD_NO as addNo, " +
                            "A.ADD_MOO as addMoo, " +
                            "A.ADD_ROAD as addRoad, " +
                            "A.ADD_BANN as addBann, " +
                            "A.ADD_SOI as addSoi, " +
                            "A.ADD_DISTRICT as addDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+".APPR_BUILDING A " +
                        "LEFT JOIN "+schema+".CITY B ON A.ADD_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+".PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_5 : {}",SQL_TYPE_5);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_5);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String addNo = Util.getStringNotNull(rs.getString("addNo"));
                String addMoo = Util.getStringNotNull(rs.getString("addMoo"));
                String addRoad = Util.getStringNotNull(rs.getString("addRoad"));
                String addBann = Util.getStringNotNull(rs.getString("addBann"));
                String addSoi = Util.getStringNotNull(rs.getString("addSoi"));
                String addDistrict = Util.getStringNotNull(rs.getString("addDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = addNo.concat(SPACE)
                        .concat(addMoo).concat(SPACE)
                        .concat(addRoad).concat(SPACE)
                        .concat(addBann).concat(SPACE)
                        .concat(addSoi).concat(SPACE)
                        .concat(addDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType6(String colId, String headColId){
        log.debug("getAddressType6 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_6 = "SELECT " +
                                "A.ROOM_NO as roomNo, " +
                                "A.FLOOR_NO as floorNo, " +
                                "A.LAND_NO as landNo, " +
                                "A.BUILDING_NAME as buildingName, " +
                                "A.CONDO_NO as condoNo, " +
                                "A.NO_OF_FLOOR as noOfFloor, " +
                                "A.DEED_NO as deedNo, " +
                                "A.CONDO_DISTRICT as condoDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName, " +
                                "A.AREA_METER as areaMeter, " +
                                "A.BALCONY_METER as balconyMeter " +
                            "FROM APPR_LAND A " +
                            "LEFT JOIN CITY B ON A.CONDO_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_6 = "SELECT " +
                            "A.ROOM_NO as roomNo, " +
                            "A.FLOOR_NO as floorNo, " +
                            "A.LAND_NO as landNo, " +
                            "A.BUILDING_NAME as buildingName, " +
                            "A.CONDO_NO as condoNo, " +
                            "A.NO_OF_FLOOR as noOfFloor, " +
                            "A.DEED_NO as deedNo, " +
                            "A.CONDO_DISTRICT as condoDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName, " +
                            "A.AREA_METER as areaMeter, " +
                            "A.BALCONY_METER as balconyMeter " +
                        "FROM "+schema+"APPR_LAND A " +
                        "LEFT JOIN "+schema+"CITY B ON A.CONDO_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+"PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_6 : {}",SQL_TYPE_6);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_6);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String roomNo = Util.getStringNotNull(rs.getString("roomNo"));
                String floorNo = Util.getStringNotNull(rs.getString("floorNo"));
                String landNo = Util.getStringNotNull(rs.getString("landNo"));
                String buildingName = Util.getStringNotNull(rs.getString("buildingName"));
                String condoNo = Util.getStringNotNull(rs.getString("condoNo"));
                String noOfFloor = Util.getStringNotNull(rs.getString("noOfFloor"));
                String deedNo = Util.getStringNotNull(rs.getString("deedNo"));
                String condoDistrict = Util.getStringNotNull(rs.getString("condoDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));
                BigDecimal areaMeter = rs.getBigDecimal("areaMeter");
                BigDecimal balconyMeter = rs.getBigDecimal("balconyMeter");

                address = roomNo.concat(COMMA)
                        .concat(floorNo).concat(COMMA)
                        .concat(landNo).concat(COMMA)
                        .concat(buildingName).concat(COMMA)
                        .concat(condoNo).concat(COMMA)
                        .concat(noOfFloor).concat(COMMA)
                        .concat(deedNo).concat(COMMA)
                        .concat(condoDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
                if(areaMeter!=null){
                    address = address.concat(COMMA).concat(areaMeter.toString());
                }
                if(balconyMeter!=null){
                    address = address.concat(COMMA).concat(balconyMeter.toString());
                }
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType7(String colId, String headColId){
        log.debug("getAddressType7 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_7 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_MACHINE A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_7 = "SELECT " +
                            "A.ADD_NO as addNo, " +
                            "A.ADD_MOO as addMoo, " +
                            "A.ADD_BANN as addBann, " +
                            "A.ADD_SOI as addSoi, " +
                            "A.ADD_ROAD as addRoad, " +
                            "A.ADD_DISTRICT as addDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+"APPR_MACHINE A " +
                        "LEFT JOIN "+schema+"CITY B ON A.ADD_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+"PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_7 : {}",SQL_TYPE_7);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_7);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String addNo = Util.getStringNotNull(rs.getString("addNo"));
                String addMoo = Util.getStringNotNull(rs.getString("addMoo"));
                String addBann = Util.getStringNotNull(rs.getString("addBann"));
                String addSoi = Util.getStringNotNull(rs.getString("addSoi"));
                String addRoad = Util.getStringNotNull(rs.getString("addRoad"));
                String addDistrict = Util.getStringNotNull(rs.getString("addDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = addNo.concat(SPACE)
                        .concat(addMoo).concat(SPACE)
                        .concat(addBann).concat(SPACE)
                        .concat(addSoi).concat(SPACE)
                        .concat(addRoad).concat(SPACE)
                        .concat(addDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType8(String colId, String headColId){
        log.debug("getAddressType8 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_8 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_GOODS A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_8 = "SELECT " +
                            "A.ADD_NO as addNo, " +
                            "A.ADD_MOO as addMoo, " +
                            "A.ADD_BANN as addBann, " +
                            "A.ADD_SOI as addSoi, " +
                            "A.ADD_ROAD as addRoad, " +
                            "A.ADD_DISTRICT as addDistrict, " +
                            "B.CITY_ID as cityId, " +
                            "B.CITY as city, " +
                            "B.PROVINCE_ID as provinceId, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+"APPR_GOODS A " +
                        "LEFT JOIN "+schema+"CITY B ON A.ADD_CITY = B.CITY_ID " +
                        "LEFT JOIN "+schema+"PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_8 : {}",SQL_TYPE_8);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_8);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String addNo = Util.getStringNotNull(rs.getString("addNo"));
                String addMoo = Util.getStringNotNull(rs.getString("addMoo"));
                String addBann = Util.getStringNotNull(rs.getString("addBann"));
                String addSoi = Util.getStringNotNull(rs.getString("addSoi"));
                String addRoad = Util.getStringNotNull(rs.getString("addRoad"));
                String addDistrict = Util.getStringNotNull(rs.getString("addDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = addNo.concat(SPACE)
                        .concat(addMoo).concat(SPACE)
                        .concat(addBann).concat(SPACE)
                        .concat(addSoi).concat(SPACE)
                        .concat(addRoad).concat(SPACE)
                        .concat(addDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType9(String colId, String headColId){
        log.debug("getAddressType9 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_9 = "SELECT " +
                                "B.NAME_THAI as nameThai, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_CAR A " +
                            "LEFT JOIN SET_COUNTRY B ON A.REGISTRATION_PLACE_COUNTRY = B.CODE " +
                            "LEFT JOIN PROVINCE C ON A.REGISTRATION_PROVINCE_LOCAL = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_9 = "SELECT " +
                            "B.NAME_THAI as nameThai, " +
                            "C.PROV_ID as provId, " +
                            "C.PROV_NAME as provName " +
                        "FROM "+schema+"APPR_CAR A " +
                        "LEFT JOIN "+schema+"SET_COUNTRY B ON A.REGISTRATION_PLACE_COUNTRY = B.CODE " +
                        "LEFT JOIN "+schema+"PROVINCE C ON A.REGISTRATION_PROVINCE_LOCAL = C.PROV_ID " +
                        "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_9 : {}",SQL_TYPE_9);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_9);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String nameThai = Util.getStringNotNull(rs.getString("nameThai"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = nameThai.concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType10(String colId, String headColId){
        log.debug("getAddressType10 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_10 = "SELECT " +
                                "B.NAME_THAI as nameThai " +
                            "FROM APPR_SHIP A " +
                            "LEFT JOIN SET_COUNTRY B ON A.REGISTRATION_PLACE = B.CODE " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_10 = "SELECT " +
                                "B.NAME_THAI as nameThai " +
                            "FROM "+schema+"APPR_SHIP A " +
                            "LEFT JOIN "+schema+"SET_COUNTRY B ON A.REGISTRATION_PLACE = B.CODE " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_10 : {}",SQL_TYPE_10);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_10);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String nameThai = rs.getString("nameThai");
                address = nameThai;
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType11(String colId, String headColId){
        log.debug("getAddressType11 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_11 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_RENT A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_11 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM "+schema+"APPR_RENT A " +
                            "LEFT JOIN "+schema+"CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN "+schema+"PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_11 : {}",SQL_TYPE_11);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_11);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String addNo = Util.getStringNotNull(rs.getString("addNo"));
                String addMoo = Util.getStringNotNull(rs.getString("addMoo"));
                String addBann = Util.getStringNotNull(rs.getString("addBann"));
                String addSoi = Util.getStringNotNull(rs.getString("addSoi"));
                String addRoad = Util.getStringNotNull(rs.getString("addRoad"));
                String addDistrict = Util.getStringNotNull(rs.getString("addDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = addNo.concat(SPACE)
                        .concat(addMoo).concat(SPACE)
                        .concat(addBann).concat(SPACE)
                        .concat(addSoi).concat(SPACE)
                        .concat(addRoad).concat(SPACE)
                        .concat(addDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
    }

    public String getAddressType12(String colId, String headColId){
        log.debug("getAddressType12 colId: {}, headColId: {}",colId, headColId);
        String address = "";
        String SQL_TYPE_12 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM APPR_OTHERS A " +
                            "LEFT JOIN CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";

        if(schema!=null && !schema.trim().equalsIgnoreCase("")){
            SQL_TYPE_12 = "SELECT " +
                                "A.ADD_NO as addNo, " +
                                "A.ADD_MOO as addMoo, " +
                                "A.ADD_BANN as addBann, " +
                                "A.ADD_SOI as addSoi, " +
                                "A.ADD_ROAD as addRoad, " +
                                "A.ADD_DISTRICT as addDistrict, " +
                                "B.CITY_ID as cityId, " +
                                "B.CITY as city, " +
                                "B.PROVINCE_ID as provinceId, " +
                                "C.PROV_ID as provId, " +
                                "C.PROV_NAME as provName " +
                            "FROM "+schema+"APPR_OTHERS A " +
                            "LEFT JOIN "+schema+"CITY B ON A.ADD_CITY = B.CITY_ID " +
                            "LEFT JOIN "+schema+"PROVINCE C ON B.PROVINCE_ID = C.PROV_ID " +
                            "WHERE A.COL_ID = ? AND A.HEAD_COL_ID = ?";
        }

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }

        try {
            log.debug("SQL_TYPE_12 : {}",SQL_TYPE_12);
            PreparedStatement statement = conn.prepareStatement(SQL_TYPE_12);
            statement.setString(1, colId);
            statement.setString(2, headColId);
            rs = statement.executeQuery();
            while (rs.next()) {
                String addNo = Util.getStringNotNull(rs.getString("addNo"));
                String addMoo = Util.getStringNotNull(rs.getString("addMoo"));
                String addBann = Util.getStringNotNull(rs.getString("addBann"));
                String addSoi = Util.getStringNotNull(rs.getString("addSoi"));
                String addRoad = Util.getStringNotNull(rs.getString("addRoad"));
                String addDistrict = Util.getStringNotNull(rs.getString("addDistrict"));
                String cityId = Util.getStringNotNull(rs.getString("cityId"));
                String city = Util.getStringNotNull(rs.getString("city"));
                String provinceId = Util.getStringNotNull(rs.getString("provinceId"));
                String provId = Util.getStringNotNull(rs.getString("provId"));
                String provName = Util.getStringNotNull(rs.getString("provName"));

                address = addNo.concat(SPACE)
                        .concat(addMoo).concat(SPACE)
                        .concat(addBann).concat(SPACE)
                        .concat(addSoi).concat(SPACE)
                        .concat(addRoad).concat(SPACE)
                        .concat(addDistrict).concat(SPACE)
                        .concat(city).concat(SPACE)
                        .concat(provName);
            }
            log.debug("address result : {}", address);
            rs.close();
            conn.close();
            conn = null;
            log.debug("connection closed.");
        } catch (SQLException e) {
            log.error("execute query exception!",e);
            throw new COMSInterfaceException(e, ExceptionMapping.COMS_GETDATA_ERROR, msg.get(ExceptionMapping.COMS_GETDATA_ERROR));
        } finally {
            closeConnection();
        }

        return address;
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
