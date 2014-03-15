package com.clevel.selos.integration.filenet.ce.connection;

import java.net.URLEncoder;

import com.filenet.wcm.api.ObjectFactory;
import com.filenet.wcm.api.Session;

import javax.inject.Inject;

public class CESessionToken {

	/*public static void main(String[] args) throws Exception {
		CESessionToken obj = new CESessionToken();
		System.out.println(obj.getTokenFromSession("administrator", "filenet"));
	}*/

    @Inject
    public CESessionToken() {

    }

    // getToken from session
    public String getTokenFromSession(String username, String password) throws Exception {
        Session session = ObjectFactory.getSession("UserToken", Session.CLEAR, username, password);
        String token = session.getToken(false);
       
        String encToken = URLEncoder.encode(token, "UTF-8");
        
        return encToken;
    }
}
