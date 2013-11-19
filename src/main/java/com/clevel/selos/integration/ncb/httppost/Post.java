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
            String reason = catchStatus(resCode);
            log.error("The request has failed, Error code is {} = {}", resCode, reason);
            throw new NCBInterfaceException(new Exception("The request has failed, Error code is " + resCode + " = " + reason), exception, exceptionMessage.get(exception, "" + resCode+" = "+ reason));
        }
    }

    private String catchStatus(final int resCode){
        switch (resCode){
            case 100 : return "Continue";
            case 101 : return "Switching Protocols";
            case 201 : return "Created";
            case 202 : return "Accepted";
            case 203 : return "Non-Authoritative Information";
            case 204 : return "No Content";
            case 205 : return "Reset Content";
            case 206 : return "Partial Content";
            case 300 : return "Multiple Choices";
            case 301 : return "Moved Permanently";
            case 302 : return "Found";
            case 303 : return "See Other";
            case 304 : return "Not Modified";
            case 305 : return "Use Proxy";
            case 306 : return "Unused";
            case 307 : return "Temporary Redirect";
            case 400 : return "Bad Request";
            case 401 : return "Unauthorized";
            case 402 : return "Payment Required";
            case 403 : return "Forbidden";
            case 404 : return "Not Found";
            case 405 : return "Method Not Allowed";
            case 406 : return "Not Acceptable";
            case 407 : return "Proxy Authentication Required";
            case 408 : return "Request Timeout";
            case 409 : return "Conflict";
            case 410 : return "Gone";
            case 441 : return "Length Required";
            case 412 : return "Precondition Failed";
            case 413 : return "Request Entity Too Large";
            case 414 : return "Request-URI Too Long";
            case 415 : return "Unsupported Media Type";
            case 416 : return "Requested Range Not Satisfiable";
            case 417 : return "Expectation Failed";
            case 500 : return "Internal Server Error";
            case 501 : return "Not Implemented";
            case 502 : return "Bad Gateway";
            case 503 : return "Service Unavailable";
            case 504 : return "Gateway Timeout";
            case 505 : return "HTTP Version Not Supported";
            default : return "";
        }
    }
}
