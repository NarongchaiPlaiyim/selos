package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.businesscontrol.util.stp.STPExecutor;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class IsaUploadService extends BusinessControl {
    @Inject
    STPExecutor stpExecutor;


    @Inject
    @SELOS
    Logger log;
   
    String processFileName = null;
    String folder = null;
    BufferedWriter bufferedWriter = null;
    private final static String COMMA_DELIMITTED = ",";
    public final static String RESULT_FILENAME = "UPLOAD_RESULT_";

    static FileProcessor worker = null;

    protected class FileProcessor implements Runnable {

        
        public void run() {
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddmmsss");
                Date now = Calendar.getInstance().getTime();

                FileInputStream fileInputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader bufferedReader = null;

                File uploadRSTFile = null;
                FileOutputStream fileOutputStream = null;
                OutputStreamWriter outputStreamWriter = null;

                try {
                    uploadRSTFile = new File(folder, RESULT_FILENAME + dateFormat.format(now) + ".tmp");
                    fileOutputStream = new FileOutputStream(uploadRSTFile);
                    outputStreamWriter = new OutputStreamWriter(fileOutputStream, "MS874");
                    bufferedWriter = new BufferedWriter(outputStreamWriter);

                    if (processFileName.indexOf(".csv") < 0 && processFileName.indexOf(".CSV") < 0) {

                        logResult("File Format", UploadResult.FAILED, "File " + processFileName + " has wrong format. The file should be in COMMA Delimited format(.csv).");
                    } else {

                        fileInputStream = new FileInputStream(new File(folder, processFileName));
                        inputStreamReader = new InputStreamReader(fileInputStream, "MS874");
                        bufferedReader = new BufferedReader(inputStreamReader);

                        // First line for column header
                        String line = bufferedReader.readLine();
                        try {
                            bufferedWriter.write(line);
                            bufferedWriter.write(COMMA_DELIMITTED);
                            bufferedWriter.write("Result");
                            bufferedWriter.write(COMMA_DELIMITTED);
                            bufferedWriter.write("Description");
                            bufferedWriter.write("\n");
                            bufferedWriter.flush();
                        } catch (Exception ex) {
                            log.warn("Cannot write result of upload failed into file.");
                        }

                        line = bufferedReader.readLine();

                        while (line != null) {
                            System.out.println("LINE : = "+line);
                            try {
                                List<String> params = getToken(line);
                                System.out.println("1");
                                String importResult = executeScript(params);
                                System.out.println("2");
                                log.debug("IMPORT RESULT :: {}", importResult);
                                if (importResult != null && importResult.length() > 0) {
                                    System.out.println("3");
                                    if (importResult.startsWith(UploadResult.FAILED.getCode())) {
                                        logResult(line, UploadResult.FAILED, importResult.substring((UploadResult.FAILED.getCode().length() + 1)));
                                    } else if (importResult.startsWith(UploadResult.SUCCESS.getCode())) {
                                        logResult(line, UploadResult.SUCCESS, importResult.substring((UploadResult.SUCCESS.getCode().length() + 1)));
                                    } else {
                                        logResult(line, UploadResult.FAILED, "Unidentified Result " + importResult);
                                    }
                                } else {
                                    logResult(line, UploadResult.FAILED, "importResult is not return");
                                }

                            } catch (ArrayIndexOutOfBoundsException e) {
                                logResult(line, UploadResult.FAILED, "Failed to import user. Variable is missing. Please check variable.");
                                log.warn("Upload User", e);
                            } catch (NullPointerException e) {
                                logResult(line, UploadResult.FAILED, "Failed to import user. Variable is missing. Please check variable.");
                                log.warn("Upload User", e);
                            } catch (SQLException e) {
                                logResult(line, UploadResult.FAILED, "Failed to import user. An sql exception occurred.");
                                log.warn("Upload User", e);
                            } catch (Exception e) {
                                logResult(line, UploadResult.FAILED, "Failed to import user. Unexpected Exception, Please contact administrator to validate the system.");
                                log.warn("Upload User", e);
                            }
                            line = bufferedReader.readLine();
                        }
                    }
                } catch (IOException io) {
                    log.warn("Upload User", io);
                    try {
                        logResult("ALL User", UploadResult.FAILED, "Exception before Read User Profile information.");
                    } catch (Exception wex) {
                        log.error("Cannot write result of upload failed into file.");
                    }
                    log.debug("Process File Exception ::", io);
                } finally {
                    try {
                        fileInputStream.close();
                        inputStreamReader.close();
                        bufferedReader.close();
                    } catch (Exception iex) {
                        log.error("Close File Exception", iex);
                    }
                }

                try {
                    fileInputStream.close();
                    bufferedWriter.close();
                } catch (Exception wex) {
                    log.warn("Cannot close file writer", wex);
                }
                uploadRSTFile.renameTo(new File(folder, RESULT_FILENAME + dateFormat.format(now) + ".csv"));

            } catch (Exception ex) {
                log.error("Upload User, Unexpected:", ex);

            } catch (Error err) {
                log.error("Upload User, Unexpected:", err);

            } finally {
                worker = null;
            }
        }

        private void logResult(String line, UploadResult result, String description) {
            try {
                bufferedWriter.write(line);
                bufferedWriter.write(COMMA_DELIMITTED);
                bufferedWriter.write(result.desc);
                bufferedWriter.write(COMMA_DELIMITTED);
                bufferedWriter.write(description);
                bufferedWriter.write("\n");
                bufferedWriter.flush();
            } catch (Exception ex) {
                log.warn("Cannot write result of upload failed into file.");
            }
        }


        public String executeScript(List<String> params) throws SQLException, Exception {
            String result = "";
            //todo
//            String create_by = getCurrentUserID();

//            params.add(create_by);
            String[] stringParams = params.toArray(new String[params.size()]);
            System.out.println(stringParams.length);
            for(String ssd:stringParams){
                System.out.println("--- "+ssd);
            }
            log.debug("parameters ", stringParams);

//            String params[] = new String[] {command, userid, username, userbu, usergroup, userrole, userImportRole, userActive, create_by};
            try {
//                todo
                result = stpExecutor.addUserFromFile("{CALL USERPROFILE_PACKAGE.pUserprofileUpload(?,?,?,?,?,?,?,?,?,?)}", stringParams);
            }catch (Exception e) {
                log.error("Exception", e);
                throw new Exception(e.getMessage());
            }
            return result;
        }

        public List<String> getToken(String input) {
            log.debug("===== begin getToken =====");
            String _tmp = input;
            List<String> tokens = new ArrayList<String>();
            System.out.println("11");
            while (_tmp.length() > 0) {
                System.out.println("22");
                _tmp = _tmp.trim();
                String token = null;
                if (_tmp.charAt(0) == '\"') {
                    System.out.println("33");
                    int dIdx = _tmp.indexOf('\"', 1);
                    token = _tmp.substring(1, dIdx);
                    int cIdx = _tmp.indexOf(",", dIdx + 1);
                    _tmp = _tmp.substring(cIdx + 1);

                } else {
                    System.out.println("44");
                    int cIdx = _tmp.indexOf(",");
                    if (cIdx < 0) {
                        System.out.println("55");
                        token = _tmp;
                        _tmp = "";
                    } else {
                        System.out.println("66");
                        token = _tmp.substring(0, cIdx);
                        _tmp = _tmp.substring(cIdx + 1);
                    }
                }
                tokens.add(token.trim());
                log.debug("_tmp value after subString {}", _tmp);
            }
            while(tokens.size() < 8){
                System.out.println("77");
                tokens.add("");
            }

            log.debug("===== end getToken ====={}", tokens);

            return tokens;
        }
    }

    public static boolean isFileProcessing() {
        if (worker != null)
            return true;
        return false;
    }

    public void processUserUploadFiles(String folder, String fileName) {
        processFileName = fileName;
        this.folder = folder;
        FileProcessor fileProcessor = new FileProcessor();
        this.worker = fileProcessor;
        fileProcessor.run();
    }

    private enum UploadResult {
        FAILED("Failed", "FAILED"),
        SUCCESS("Success", "SUCCESS");

        String desc = null;
        String code = null;

        private UploadResult(String message, String code) {
            desc = message;
            this.code = code;
        }

        private String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return desc;
        }
    }
}
