package org.lantern;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.junit.Test;
import org.lantern.http.GoogleOauth2CallbackServlet;
import org.lantern.state.Model;
import org.lantern.util.HttpClientFactory;

public class GoogleOauth2CallbackServletTest {


    @Test
    public void testGoogleApis() throws Exception {
        int code = TestingUtils.doWithWithGetModeProxy(new Callable<Integer>() {
           @Override
            public Integer call() throws Exception {
               final Censored censored = new DefaultCensored();
               final HttpClientFactory factory = new HttpClientFactory(censored);
               final GoogleOauth2CallbackServlet servlet = 
                   new GoogleOauth2CallbackServlet(null, null, null, null, null, 
                       null, factory, null, new Messages(new Model()));
               
               final Map<String, String> allToks = new HashMap<String, String>();
               allToks.put("access_token", "invalidcode");
               return servlet.fetchEmail(allToks, factory.newClient());
            } 
        });
        
        // Expected to be unauthorized with a bogus token -- we want to 
        // make sure it gets there.
        assertEquals(401, code);
    }
}
