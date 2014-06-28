package com.clevel.selos.businesscontrol.isa.csv.service;

import com.clevel.selos.businesscontrol.isa.csv.model.CSVModel;
import com.clevel.selos.businesscontrol.isa.csv.model.ResultModel;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVService {
    @Inject
    @SELOS
    private Logger log;
    private final String EMPTY = "";
    private final String UTF_8 = "UTF-8";
    @Inject
    public CSVService() {

    }

    public List<CSVModel> CSVImport(final InputStream inputStream) throws Exception{
        log.debug("-- CSVImport()");
        ICsvBeanReader beanReader = null;
        CSVModel csvModel = null;
        List<CSVModel> csvModelList = null;
        try {
            beanReader = new CsvBeanReader(new InputStreamReader(inputStream, UTF_8), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = beanReader.getHeader(true);
            csvModelList = new ArrayList<CSVModel>();
            while( (csvModel = beanReader.read(CSVModel.class, header, getProcessorsss())) != null ) {
                csvModelList.add(csvModel);
            }
            return csvModelList;
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if(!Util.isNull(beanReader)){
                try {
                    beanReader.close();
                } catch (Exception e) {
                    log.error("",e);
                }
            }
            if(!Util.isNull(inputStream)){
                inputStream.close();
            }
        }
    }

    public void CSVExport(final String fullPath, final List<User> userList) throws Exception{
        log.debug("-- CSVExport(fullPath : {} List<User>.size() {})", fullPath, userList.size());
        ICsvBeanWriter beanWriter = null;
        try {
            File fileDir = new File(fullPath);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), UTF_8));
            beanWriter = new CsvBeanWriter(out, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = new String[] {
                    "userId", "userName", "active", "role",
                    "team", "department", "division", "region",
                    "title", "status" };
            beanWriter.writeHeader(header);
            List<CSVModel> csvModelList = covertModelToCSV(userList);
            for (final CSVModel csvModel : csvModelList) {
                beanWriter.write(csvModel, header, getProcessors());
            }
            beanWriter.flush();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if(!Util.isNull(beanWriter)){
                try {
                    beanWriter.close();
                } catch (Exception e) {
                    log.error("",e);
                }
            }
        }
    }

    public void CSVExport(final String fullPath, final List<ResultModel> resultModelList, final String test) throws Exception{
        log.debug("-- CSVExport(fullPath : {} List<ResultModel>.size() {})", fullPath, resultModelList.size());
        ICsvBeanWriter beanWriter = null;
        try {
            File fileDir = new File(fullPath);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), UTF_8));
            beanWriter = new CsvBeanWriter(out, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = new String[] {
                    "command", "id", "result", "detail"};
            beanWriter.writeHeader(header);
            for (final ResultModel resultModel : resultModelList) {
                beanWriter.write(resultModel, header, getProcessor());
            }
            beanWriter.flush();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            if(!Util.isNull(beanWriter)){
                try {
                    beanWriter.close();
                } catch (Exception e) {
                    log.error("",e);
                }
            }
        }
    }

    private List<CSVModel> covertModelToCSV(final List<User> userList) throws Exception{
        List<CSVModel> csvModelList = null;
        CSVModel csvModel = null;
        if(!Util.isZero(userList.size())){
            csvModelList = new ArrayList<CSVModel>();
            for(final User model : userList){
                csvModel = new CSVModel();
                csvModel.setUserId(model.getId());
                csvModel.setUserName(model.getUserName());
                csvModel.setActive(model.getActive()+EMPTY);
                if(!Util.isNull(model.getRole())){
                    csvModel.setRole(model.getRole().getName());
                } else {
                    csvModel.setRole(EMPTY);
                }
                if(!Util.isNull(model.getDepartment())){
                    csvModel.setTitle(model.getDepartment().getName());
                } else {
                    csvModel.setDepartment(EMPTY);
                }
                if(!Util.isNull(model.getDivision())){
                    csvModel.setTitle(model.getDivision().getName());
                } else {
                    csvModel.setDivision(EMPTY);
                }
                if(!Util.isNull(model.getRegion())){
                    csvModel.setRegion(model.getRegion().getName());
                } else {
                    csvModel.setRegion(EMPTY);
                }
                if(!Util.isNull(model.getTeam())){
                    csvModel.setTeam(model.getTeam().getTeam_name());
                } else {
                    csvModel.setTeam(EMPTY);
                }
                if(!Util.isNull(model.getTitle())){
                    csvModel.setTitle(model.getTitle().getName());
                } else {
                    csvModel.setTitle(EMPTY);
                }
                if(!Util.isNull(model.getUserStatus())){
                    csvModel.setStatus(model.getUserStatus().name());
                } else {
                    csvModel.setStatus(EMPTY);
                }
                csvModelList.add(csvModel);
            }
        }
        return csvModelList;
    }
    private CellProcessor[] getProcessor() {
        final CellProcessor[] processors = new CellProcessor[] {
                null, // commandType
                null, // id
                null, // result
                null, // detail
        };
        return processors;
    }
    private CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // userId (must be unique)
                null, // userName
                null, // active
                null, // role
                null, // department
                null, // division
                null, // region
                null, // team
                null, // title
                null, // status
        };
        return processors;
    }

    private CellProcessor[] getProcessorsss() {
        final CellProcessor[] processors = new CellProcessor[] {
                null, // commandType
                null, // userID
                null, // userName
                null, // active
                null, // role
                null, // department
                null, // division
                null, // region
                null, // team
                null, // title
                null, // status
        };
        return processors;
    }
}