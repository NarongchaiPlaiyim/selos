package com.clevel.selos.businesscontrol.isa;

import com.clevel.selos.businesscontrol.BusinessControl;
import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class ImportService extends BusinessControl {

    @Inject
    @SELOS
    Logger log;

    public String uploadDocFiles(HttpServletRequest request, String clientId){
//        try{
            if(clientId == null || clientId.length() == 0)
                return "ERROR:: client id is not identified";
            else {
//                File clientIdDir = new File(getTmpFolder(clientId));
                File clientIdDir = new File("C:\\Users\\sahawat\\Desktop\\test");
                if(!clientIdDir.exists())
                    clientIdDir.mkdir();

            }
//            boolean beLastChunk = LAST_CHUNK_FLAG_ATTR_VALUE.equals(request.getParameter(LAST_CHUNK_FLAG_ATTR_NAME));
//            int chunkNumber = Integer.parseInt(request.getParameter(NUMBER_OF_CHUNK_ATTR_NAME) == null?"0":request.getParameter(NUMBER_OF_CHUNK_ATTR_NAME));
//
//            DiskFileItemFactory factory = new DiskFileItemFactory(Integer.valueOf(systemConfigProvider.getValue(Config.IMPORT_MAX_MEM_SIZE)), new File(getTempDirectory()));
//
//            ServletFileUpload upload = new ServletFileUpload(factory);
//
//            // Set overall request size constraint
//            upload.setSizeMax(Long.valueOf(userService.getUserDetails().getAllowImportSize()) + 8192);
//
//            List<FileItem> fileItems = upload.parseRequest(request);
//            // Process the uploaded items
//            Iterator fileItemsIterator = fileItems.iterator();
//
//            while (fileItemsIterator.hasNext()) {
//                FileItem fileItem = (FileItem) fileItemsIterator.next();
//                if(!fileItem.isFormField()){
//                    String fileName = fileItem.getName();
//
//                    File fileOutput = new File(getTmpFolder(clientId), getFileNameOut(fileName, chunkNumber));
//                    log.debug("File Out: {}", fileOutput.getAbsolutePath());
//                    fileItem.write(fileOutput);
//
//                    if (beLastChunk) {
//                        log.debug(" Last chunk received: let's rebuild the complete file (" + fileName + ")");
//                        //First: construct the final filename.
//                        FileInputStream fis;
//                        File completedFile = new File(getTmpFolder(clientId), getFileNameOut(fileName,0));
//                        FileOutputStream fos = new FileOutputStream(completedFile);
//                        int nbBytes;
//                        byte[] byteBuff = new byte[BYTE_TRANSFER_BUFFER];
//
//                        for (int i = 1; i <= chunkNumber; i++) {
//                            log.debug("start completed file:: {}", i);
//                            File partFile = new File(getTmpFolder(clientId), getFileNameOut(fileName,i));
//                            fis = new FileInputStream(partFile);
//                            while ((nbBytes = fis.read(byteBuff)) >= 0) {
//                                fos.write(byteBuff, 0, nbBytes);
//                            }
//                            fis.close();
//                            partFile.delete();
//                        }
//
//                        fos.flush();
//                        fos.close();
//                    }
//                    fileItem.delete();
//                    //change mode to support doc processing to read file to perform doc processing.
//                    if(chunkNumber == 0 || beLastChunk){
//                        try{
//                            String completeFileName = getTmpFolder(clientId) + "/" + getFileNameOut(fileName, 0);
//                            Runtime.getRuntime().exec("chmod 777 " + completeFileName);
//                            log.debug("===== end change mode ====== chmod 777 {}", completeFileName);
//                        }catch(Exception ex){
//                            log.error("Exception while change mod from file: ", ex);
//                        }
//                    }
//                }
//            }//while
//        } catch (Exception ex){
//            log.error("ERROR:: Exception e return {} ", ex);
//            return "ERROR:: Exception e = " + ex.toString();
//        } catch (Error err){
//            log.error("ERROR:: Error {}", err);
//            return "ERROR:: Exception e {} " + err.toString();
//        }
        return "SUCCESS";
    }

}
