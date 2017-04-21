/**
 * Created by zuoduzuodu on 17/04/2017.
 */


import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionListener implements HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public SessionListener() {

    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        String user = (String) se.getSession().getAttribute("user");
        System.out.println("[SessionListener] valueBound of : "+user);
        ActionServlet.currentUserList.add(user);
        System.out.println("[SessionListener] sessionCreated with list updated");
      /* Session is being created. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
      /* Session is being destroyed. */
        System.out.println("SessionListener : sessionDestroyed");
        String user = (String) se.getSession().getAttribute("user");
        System.out.println("[SessionListener] user "+user+" going to get removed from list");
        ActionServlet.currentUserList.remove(user);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {

    }
}
