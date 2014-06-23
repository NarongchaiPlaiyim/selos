package com.clevel.selos.businesscontrol.isa.csv.service;

import com.clevel.selos.businesscontrol.isa.csv.model.CSVModel;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;
import org.supercsv.cellprocessor.constraint.NotNull;
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

    public void CSVImport(final String fullPath) throws Exception{
        log.debug("-- CSVImport()");
        ICsvBeanReader beanReader = null;
        CSVModel customerModel = null;
        try {
            beanReader = new CsvBeanReader(new InputStreamReader(new FileInputStream(fullPath), UTF_8), CsvPreference.STANDARD_PREFERENCE);
            final String[] header = beanReader.getHeader(true);
            while( (customerModel = beanReader.read(CSVModel.class, header)) != null ) {
                log.debug("-- Model[{}]", customerModel.toString());
            }
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
        }
    }

    public void CSVExport(final String fullPath, final List<User> userList) throws Exception{
        ICsvBeanWriter beanWriter = null;
        try {
            File fileDir = new File(fullPath);
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), UTF_8));
            beanWriter = new CsvBeanWriter(out, CsvPreference.STANDARD_PREFERENCE);
            final String[] header = new String[] {
                    "userId", "userName", "active", "role",
                    "department", "division", "region", "team",
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

    private List<User> covertCSVToModel(final List<CSVModel> csvModelList){
        List<User> userList = null;
        if(!Util.isZero(csvModelList.size())){

        }
        return userList;
    }



    private CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // userId (must be unique)
                new NotNull(), // userName
                new NotNull(), // active
                new NotNull(), // role
                new NotNull(), // department
                new NotNull(), // division
                new NotNull(), // region
                new NotNull(), // team
                new NotNull(), // active
                new NotNull(), // title
                new NotNull(), // status
        };
        return processors;
    }
}
