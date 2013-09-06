package com.clevel.selos.integration.ncrs.httppost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;

import javax.inject.Inject;

public class Post implements Serializable {
    @Inject
    @NCB
    Logger log;

    @Inject
    public Post() {
    }

    public String sendPost(String xml, String url) throws Exception {
        log.debug("========================================= sendPost(xml : {}, url : {})", xml, url);
        String result = "";
        DefaultHttpClient client = null;
        HttpPost post = null;
        List<NameValuePair> urlParameters = null;
        HttpResponse response = null;

        if(!Util.isNull(xml)&&!Util.isNull(url)){
            client = new DefaultHttpClient();
            post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0");
            urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("q", xml));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            response = client.execute(post);
            int resCode = response.getStatusLine().getStatusCode();
            if (resCode==200) {
                log.debug("=========================================sendPost. The request has succeeded");
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();
                log.debug("=========================================sendPost. The result is ",result);
                return result;
            }else{
                log.error("=========================================sendPost. The request has failed, Error code is ",resCode);
                return result;
            }
        }else {
            throw new ValidationException("sendPost() : xml is null or url is null");
        }
    }

}
