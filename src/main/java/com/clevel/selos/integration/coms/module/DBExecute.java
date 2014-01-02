package com.clevel.selos.integration.coms.module;

import com.clevel.selos.exception.COMSInterfaceException;
import com.clevel.selos.integration.COMS;
import com.clevel.selos.integration.coms.db.CollateralDecisionDetail;
import com.clevel.selos.integration.coms.db.CollateralJobLevel;
import com.clevel.selos.integration.coms.db.HeadCollateral;
import com.clevel.selos.integration.coms.db.SubCollateral;
import com.clevel.selos.integration.coms.tool.DBContext;
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

    private String getAddress(String colId, String headColId, String colType, String subColType, String onlType){
        String SQL_TYPE_1 = "SELECT " +
                                "APPR_LAND.DEED_NO, " +
                                "APPR_LAND.COORDINATE_NO, " +
                                "APPR_LAND.LAND_NO, " +
                                "APPR_LAND.SURVEY_NO, " +
                                "APPR_LAND.BOOK_NO, " +
                                "APPR_LAND.PAGE_NO, " +
                                "APPR_LAND.CONDO_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_LAND " +
                            "LEFT JOIN CITY ON APPR_LAND.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_LAND.COL_ID = ? AND APPR_LAND.HEAD_COL_ID = ?";

        String SQL_TYPE_2 = "SELECT " +
                                "APPR_LAND.DEED_NO, " +
                                "APPR_LAND.LAND_NO, " +
                                "APPR_LAND.BOOK_NO, " +
                                "APPR_LAND.PAGE_NO, " +
                                "APPR_LAND.CONDO_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_LAND " +
                            "LEFT JOIN CITY ON APPR_LAND.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_LAND.COL_ID = ? AND APPR_LAND.HEAD_COL_ID = ?";

        String SQL_TYPE_3 = "SELECT " +
                                "APPR_LAND.DEED_NO, " +
                                "APPR_LAND.BOOK_NO, " +
                                "APPR_LAND.PAGE_NO, " +
                                "APPR_LAND.LAND_NO, " +
                                "APPR_LAND.AIRIAL_PHOTO_NO, " +
                                "APPR_LAND.COORDINATE_NO, " +
                                "APPR_LAND.SHEET_NO, " +
                                "APPR_LAND.CONDO_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_LAND " +
                            "LEFT JOIN CITY ON APPR_LAND.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_LAND.COL_ID = ? AND APPR_LAND.HEAD_COL_ID = ?";

        String SQL_TYPE_4 = "SELECT " +
                                "APPR_LAND.DEED_NO, " +
                                "APPR_LAND.BOOK_NO, " +
                                "APPR_LAND.PAGE_NO, " +
                                "APPR_LAND.LAND_NO, " +
                                "APPR_LAND.SURVEY_NO, " +
                                "APPR_LAND.COORDINATE_NO, " +
                                "APPR_LAND.CONDO_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_LAND " +
                            "LEFT JOIN CITY ON APPR_LAND.CONDO_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_LAND.COL_ID = ? AND APPR_LAND.HEAD_COL_ID = ?";

        String SQL_TYPE_5 = "SELECT " +
                                "APPR_BUILDING.ADD_NO, " +
                                "APPR_BUILDING.ADD_MOO, " +
                                "APPR_BUILDING.ADD_ROAD, " +
                                "APPR_BUILDING.ADD_BANN, " +
                                "APPR_BUILDING.ADD_SOI, " +
                                "APPR_BUILDING.ADD_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_BUILDING " +
                            "LEFT JOIN CITY ON APPR_BUILDING.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_BUILDING.COL_ID = ? AND APPR_BUILDING.HEAD_COL_ID = ?";

        String SQL_TYPE_6 = "SELECT " +
                                "APPR_LAND.ROOM_NO, " +
                                "APPR_LAND.FLOOR_NO, " +
                                "APPR_LAND.LAND_NO, " +
                                "APPR_LAND.BUILDING_NAME, " +
                                "APPR_LAND.CONDO_NO, " +
                                "APPR_LAND.NO_OF_FLOOR, " +
                                "APPR_LAND.DEED_NO, " +
                                "APPR_LAND.CONDO_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME, " +
                                "APPR_LAND.AREA_METER, " +
                                "APPR_LAND.BALCONY_METER " +
                            "FROM APPR_LAND " +
                            "LEFT JOIN CITY ON APPR_LAND.CONDO_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_LAND.COL_ID = ? AND APPR_LAND.HEAD_COL_ID = ?";

        String SQL_TYPE_7 = "SELECT " +
                                "APPR_MACHINE.ADD_NO, " +
                                "APPR_MACHINE.ADD_MOO, " +
                                "APPR_MACHINE.ADD_BANN, " +
                                "APPR_MACHINE.ADD_SOI, " +
                                "APPR_MACHINE.ADD_ROAD, " +
                                "APPR_MACHINE.ADD_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_MACHINE " +
                            "LEFT JOIN CITY ON APPR_MACHINE.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_MACHINE.COL_ID = ? AND APPR_MACHINE.HEAD_COL_ID = ?";

        String SQL_TYPE_8 = "SELECT " +
                                "APPR_GOODS.ADD_NO, " +
                                "APPR_GOODS.ADD_MOO, " +
                                "APPR_GOODS.ADD_BANN, " +
                                "APPR_GOODS.ADD_SOI, " +
                                "APPR_GOODS.ADD_ROAD, " +
                                "APPR_GOODS.ADD_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_GOODS " +
                            "LEFT JOIN CITY ON APPR_GOODS.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_GOODS.COL_ID = ? AND APPR_GOODS.HEAD_COL_ID = ?";

        String SQL_TYPE_9 = "SELECT " +
                                "SET_COUNTRY.NAME_THAI, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_CAR " +
                            "LEFT JOIN SET_COUNTRY ON APPR_CAR.REGISTRATION_PLACE_COUNTRY = SET_COUNTRY.CODE " +
                            "LEFT JOIN PROVINCE ON APPR_CAR.REGISTRATION_PROVINCE_LOCAL = PROVINCE.PROV_ID " +
                            "WHERE APPR_CAR.COL_ID = ? AND APPR_CAR.HEAD_COL_ID = ?";

        String SQL_TYPE_10 = "SELECT " +
                                "SET_COUNTRY.NAME_THAI " +
                            "FROM APPR_SHIP " +
                            "LEFT JOIN SET_COUNTRY ON APPR_SHIP.REGISTRATION_PLACE = SET_COUNTRY.CODE " +
                            "WHERE APPR_SHIP.COL_ID = ? AND APPR_SHIP.HEAD_COL_ID = ?";

        String SQL_TYPE_11 = "SELECT " +
                                "APPR_RENT.ADD_NO, " +
                                "APPR_RENT.ADD_MOO, " +
                                "APPR_RENT.ADD_BANN, " +
                                "APPR_RENT.ADD_SOI, " +
                                "APPR_RENT.ADD_ROAD, " +
                                "APPR_RENT.ADD_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_RENT " +
                            "LEFT JOIN CITY ON APPR_RENT.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_RENT.COL_ID = ? AND APPR_RENT.HEAD_COL_ID = ?";

        String SQL_TYPE_12 = "SELECT " +
                                "APPR_OTHERS.ADD_NO, " +
                                "APPR_OTHERS.ADD_MOO, " +
                                "APPR_OTHERS.ADD_BANN, " +
                                "APPR_OTHERS.ADD_SOI, " +
                                "APPR_OTHERS.ADD_ROAD, " +
                                "APPR_OTHERS.ADD_DISTRICT, " +
                                "CITY.CITY_ID, " +
                                "CITY.CITY, " +
                                "CITY.PROVINCE_ID, " +
                                "PROVINCE.PROV_ID, " +
                                "PROVINCE.PROV_NAME " +
                            "FROM APPR_OTHERS " +
                            "LEFT JOIN CITY ON APPR_OTHERS.ADD_CITY = CITY.CITY_ID " +
                            "LEFT JOIN PROVINCE ON CITY.PROVINCE_ID = PROVINCE.PROV_ID " +
                            "WHERE APPR_OTHERS.COL_ID = ? AND APPR_OTHERS.HEAD_COL_ID = ?";

        try{
            conn = dbContext.getConnection(connCOMS, comsUser, comsPassword);
        } catch (COMSInterfaceException ex){
            throw ex;
        }
        return null;
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
