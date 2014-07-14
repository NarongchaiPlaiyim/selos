package com.clevel.selos.integration.filenet.ce.connection;

import com.clevel.selos.integration.SELOS;
import com.filenet.wcm.api.ObjectFactory;
import com.filenet.wcm.api.Session;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.URLEncoder;

public class CESessionToken {

	/*public static void main(String[] args) throws Exception {
		CESessionToken obj = new CESessionToken();
		System.out.println(obj.getTokenFromSession("administrator", "filenet"));
	}*/

    @Inject
    @SELOS
    private Logger log;
    @Inject
    public CESessionToken() {

    }

    // getToken from session
    public String getTokenFromSession(String username, String password) throws Exception {
        try {
            Session session = ObjectFactory.getSession("UserToken", Session.CLEAR, username, password);
            log.debug("getTokenFromSession , session : {}", session);
            String token = session.getToken(false);
            String encToken = URLEncoder.encode(token, "UTF-8");
            return encToken;
        } catch (Exception e) {
            log.debug("CESessionToken Exception",e);
            throw e;
        }
    }
}
