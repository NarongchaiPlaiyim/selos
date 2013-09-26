package com.clevel.selos.integration.ncrs.httppost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.ValidationMessage;
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
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

import javax.inject.Inject;

public class Post implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    @ValidationMessage
    Message message;

    @Inject
    public Post() {

    }

    public String sendPost(String xml, String url, int timeOut) throws Exception {
        log.debug("NCRS Call : sendPost()");
        if (ValidationUtil.isNull(xml)) throw new ValidationException(message.get("validation.102"));
        if (ValidationUtil.isNull(url)) throw new ValidationException(message.get("validation.103"));

        log.debug("NCRS url : {}", url);
        DefaultHttpClient client = null;
        HttpPost post = null;
        List<NameValuePair> urlParameters = null;
        HttpResponse response = null;
        HttpParams params = null;
        BufferedReader rd = null;
        StringBuilder builder = null;

        params = new BasicHttpParams();
        int minute = 60000;
        HttpConnectionParams.setSoTimeout(params, minute*timeOut);
        HttpConnectionParams.setConnectionTimeout(params, minute*timeOut);

        client = new DefaultHttpClient(params);
        post = new HttpPost(url);
        post.setHeader(HTTP.USER_AGENT, "Mozilla/5.0");
        post.setHeader("Accept-Charset", HTTP.UTF_8);
        urlParameters = new ArrayList<NameValuePair>();

        //ByteBuffer encode = Charset.forName("UTF-8").encode(XML);


        urlParameters.add(new BasicNameValuePair("q", xml));
        //Charset.forName("UTF-8").encode(myString)
        post.setEntity(new UrlEncodedFormEntity(urlParameters, HTTP.UTF_8));
//        post.setEntity(new UrlEncodedFormEntity(urlParameters, Charset.forName(HTTP.UTF_8)));
        response = client.execute(post);
        int resCode = response.getStatusLine().getStatusCode();
        if (resCode==200) {
            log.debug("NCRS The request has succeeded");
//            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-11"), 8);
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            builder = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                builder.append(line);
            }
            return builder!=null?builder.toString():"";
        }else{
            throw new Exception("The request has failed, Error code is "+resCode);
        }


    }

}
