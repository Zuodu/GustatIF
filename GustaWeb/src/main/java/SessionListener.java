/**
 * Created by zuoduzuodu on 17/04/2017.
 */


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@WebListener()
public class SessionListener implements HttpSessionListener {

    // Public constructor is required by servlet spec
    public SessionListener() {
    }
    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("SessionListener : sessionCreated");
      /* Session is created. */
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
      /* Session is destroyed. */
        System.out.println("SessionListener : sessionDestroyed");
        String user = (String) se.getSession().getAttribute("user");
        System.out.println("user "+user+" going to get removed from list");
        ActionServlet.currentUserList.remove(user);
    }
}
