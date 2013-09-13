package com.clevel.selos.integration.nccrs.httppost;


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
    Message message;

    @Inject
    public Post() {

    }

    public String sendPost(String xml, String url, int timeOut) throws Exception {
        if (ValidationUtil.isNull(xml)) throw new ValidationException(message.get("validation.102"));
        if (ValidationUtil.isNull(url)) throw new ValidationException(message.get("validation.103"));

        log.debug("NCCRS sendPost. url : {}", url);
        String result = "";
        DefaultHttpClient client = null;
        HttpPost post = null;
        List<NameValuePair> urlParameters = null;
        HttpResponse response = null;
        HttpParams params = null;

        int minute = 60000;
        params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, minute * timeOut);
        HttpConnectionParams.setSoTimeout(params, minute*timeOut);

        client = new DefaultHttpClient(params);

        post = new HttpPost(url);
        post.setHeader("User-Agent", "Mozilla/5.0");
        urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("q", xml));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        response = client.execute(post);
        int resCode = response.getStatusLine().getStatusCode();
        if (resCode==200) {
            log.debug("NCCRS sendPost. The request has succeeded");
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                builder.append(line);
            }
            result = builder.toString();
            return result;
        }else{
            log.error("NCCRS sendPost. The request has failed, Error code is ",resCode);
            return result;
        }
    }

}
