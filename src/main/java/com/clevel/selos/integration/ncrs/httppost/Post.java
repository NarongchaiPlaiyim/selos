package com.clevel.selos.integration.ncrs.httppost;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.clevel.selos.integration.Integration;
import com.clevel.selos.integration.NCB;
import com.clevel.selos.integration.ncrs.exception.ValidationException;
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

    private static Post post = null;
    private Post(){};

    public static Post getInstance() {
        return post == null ? post =new Post() : post;
    }
    public String sendPost(String xml, String url) throws Exception {
        System.out.println("========================================= sendPost");
        //log.debug("sendPost. (xml: {}, url: {})",xml,url);
        System.out.println("========================================= XML : "+xml+" "+"URL : "+url);
        String result = "";

        Util util = new Util();
        if(util.isNotNullString(xml)&&util.isNotNullString(url)){
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0");
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("q", xml));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            int resCode = response.getStatusLine().getStatusCode();
            if (resCode==200) {
                //log.debug("The request has succeeded");
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    builder.append(line);
                }
                result = builder.toString();

                //log.debug("sendPost. (result: {})",result);

                System.out.println("========================================= Result : "+result);

                return result;
            }else{
                System.out.println("========================================= Error code : "+ resCode);
                //log.error("sendPost. (Error code : {})",resCode);
                return result;
            }
        }else {
            throw new ValidationException("sendPost() : xml is null or url is null");
        }



    }

}
