package com.clevel.selos.controller.isa;

import com.clevel.selos.businesscontrol.isa.ImportService;
import com.clevel.selos.businesscontrol.isa.IsaUploadService;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.view.isa.UserUploadView;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@ViewScoped
@ManagedBean(name = "isaUpload")
public class IsaUpload implements Serializable {

    @Inject
    @SELOS
    Logger log;

    @Inject
    IsaUploadService isaUploadService;

    @Inject
    ImportService importService;


    public IsaUpload() {

    }

    @PostConstruct
    public void onCreate() {
        initForm();
    }


    private List<UserUploadView> userUploadViews;
    private String downloadFilePart;
    private StreamedContent file;
    private final static String UPLOAD_FOLDER = "_userUpload";
    public final static String RESULT_FILENAME = "UPLOAD_RESULT_";
    private final static String PATH_FILE = "C:\\Users\\sahawat\\Desktop\\FileUploadTest";
    private final static SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    public void initForm() {
        log.debug("initForm()");

        if(PATH_FILE==""||PATH_FILE.length()==0){
            log.debug("PATH_FILE IS NULL");
        }else{
            if(!new File(PATH_FILE).isDirectory()){
                new File(PATH_FILE).mkdir();
            }
        }

        FilenameFilter dirFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                if (name.endsWith(UPLOAD_FOLDER+".csv"))
                    return true;
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        String tmpDirStr = PATH_FILE;

        File tmpFolder = new File(tmpDirStr);
        File[] files = tmpFolder.listFiles(dirFilter);

        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });

        userUploadViews = new ArrayList<UserUploadView>();
        System.out.println("files : " + files.length);
        if (files.length > 0) {
            System.out.println("oo");
            try {
                int count = 0;
                int index = files.length - 1;

                do {
                    File subFolder = files[index];
                    System.out.println(subFolder.getPath());
//                    File[] subFiles = subFolder.listFiles();
                    File[] subFiles = new File(PATH_FILE).listFiles();
                    System.out.println("subFiles : "+subFiles.length);
                    if (subFiles.length > 0) {
                        System.out.println("o");
                        UserUploadView userUploadView = new UserUploadView();

                        userUploadView.setUploadTime(DateTimeUtil.dateToStringWT(new Date(subFolder.lastModified())));
                        for (int subidx = 0; subidx < subFiles.length; subidx++) {
                            System.out.println("nb "+subFiles[subidx].getName());
                            if (subFiles[subidx].getName().startsWith(IsaUpload.RESULT_FILENAME)) {
//                                userUploadView.setResultFile(subFolder.getName() + "/" + subFiles[subidx].getName());
                                userUploadView.setResultFile(subFiles[subidx].getName());
//                                System.out.println("1 "+userUploadView.getResultFile());
                            }
//                            System.out.println("2 " + userUploadView.getResultFile());
                        }
                        if (userUploadView.getResultFile() == null) {
                            userUploadView.setResultFile("");
                        }

                        userUploadViews.add(userUploadView);
                    }
                    count++;
                    index--;
                    System.out.println(count);
                } while (count <= 5 && index >= 0);
            } catch (Exception ex) {
                log.warn("Exception when list upload and Result Files", ex);
            }
        }
        System.out.println("n " + userUploadViews.size());

        RequestContext.getCurrentInstance().update(":okl");

    }


    public void uploadUserFile(FileUploadEvent event) {
        log.debug("uploadUserFile()");


        if (event != null) {
            Date now = Calendar.getInstance().getTime();
            String userUploadId = dateFormatFile.format(now) + UPLOAD_FOLDER;
            UploadedFile file = event.getFile();
            log.info("file: {} (size: {} KB)", file.getFileName(), file.getSize() / 1024);

            System.out.println(event.getFile().getFileName());


            // Prepare filename prefix and suffix for an unique filename in upload folder.
            String prefix = FilenameUtils.getBaseName(userUploadId);
            String suffix = "."+FilenameUtils.getExtension(file.getFileName());

            try {
                File fileDir = new File(PATH_FILE);

                if (file.getSize() > 10) {
                    log.debug("file too big");
                    System.out.println("FILE TOO BIG");
                }

                if(PATH_FILE==""||PATH_FILE.length()==0){
                     log.debug("PATH_FILE IS NULL");
                }else{
                      if(!fileDir.isDirectory()){
                          fileDir.mkdir();
                      }
                }

                String filePath = PATH_FILE +"\\"+ prefix+suffix;

                if (new File(PATH_FILE).isDirectory()) {
                    System.out.println("filepAth : "+filePath);
                    FileUtils.copyInputStreamToFile(file.getInputstream(), new File(filePath));
                } else {
                    System.out.println("NOT DIRECTORY");
                }
                log.debug("copy file to: {}", filePath);


                //            String result = importService.uploadDocFiles(request, userUploadId);
                isaUploadService.processingUploadFile(PATH_FILE, fileDir.listFiles()[0].getName());
//                initForm();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        } else {
            System.out.println("file is null");
        }
    }

    public StreamedContent getFile() {
        log.debug("downloadFile()");

        String filename="C:\\Users\\sahawat\\Desktop\\FileUploadTest\\"+downloadFilePart;
        File file1 =new File(filename);
        System.out.println(filename);
        InputStream stream = null;
        try {
            stream = new FileInputStream(file1);
            file = new DefaultStreamedContent(stream,"text/csv",downloadFilePart);
        } catch (FileNotFoundException e) {

        }

        return file;
    }


    public List<UserUploadView> getUserUploadViews() {
        return userUploadViews;
    }

    public void setUserUploadViews(List<UserUploadView> userUploadViews) {
        this.userUploadViews = userUploadViews;
    }

    public String getDownloadFilePart() {
        return downloadFilePart;
    }

    public void setDownloadFilePart(String downloadFilePart) {
        this.downloadFilePart = downloadFilePart;
    }


}
