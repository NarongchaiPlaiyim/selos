package com.clevel.selos.integration.ncb.httppost;


import com.clevel.selos.exception.NCBInterfaceException;
import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.system.message.*;
import com.clevel.selos.util.ValidationUtil;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    @ValidationMessage
    Message validationMessage;

    @Inject
    @ExceptionMessage
    Message exceptionMessage;

    private final String required = ValidationMapping.NCB_DATA_REQUIRED;
    private final String exception = ExceptionMapping.NCB_EXCEPTION;

    public Post() {
    }

    public String sendPost(String xml, String url, int timeOut) throws Exception {
        log.debug("Call : sendPost(url : {}, timeOut : {})", url, timeOut);
        if (ValidationUtil.isNull(xml)) {
            throw new ValidationException(required, validationMessage.get(required, "XML"));
        }
        if (ValidationUtil.isNull(url)) {
            throw new ValidationException(required, validationMessage.get(required, "URL"));
        }
        if (timeOut <= 0) {
            throw new ValidationException(required, validationMessage.get(required, "Time Out"));
        }

        DefaultHttpClient client = null;
        HttpPost post = null;
        List<NameValuePair> urlParameters = null;
        HttpResponse response = null;
        HttpParams params = null;
        BufferedReader rd = null;
        StringBuilder builder = null;

        params = new BasicHttpParams();
//        int minute = 60000;
        int sec = 1000;
        HttpConnectionParams.setSoTimeout(params, sec * timeOut);
        HttpConnectionParams.setConnectionTimeout(params, sec * timeOut);

        client = new DefaultHttpClient(params);
        post = new HttpPost(url);
        post.setHeader("User-Agent", "Mozilla/5.0");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");

        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("q", xml));
        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

        response = client.execute(post);
        int resCode = response.getStatusLine().getStatusCode();
        if (resCode == 200) {
            log.debug("The request has succeeded");
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            builder = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                builder.append(line);
            }
            return builder != null ? builder.toString() : "";
        } else {
            log.error("The request has failed, Error code is {}", resCode);
            throw new NCBInterfaceException(new Exception("The request has failed, Error code is " + resCode), exception, exceptionMessage.get(exception, "" + resCode));
        }
    }
}
