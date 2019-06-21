package org.pac4j.demo.spring;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.saml.profile.SAML2Profile;
import org.pac4j.springframework.web.LogoutController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class Application {

    /*@Value("${salt}")
    private String salt;*/

    @Value("${pac4j.centralLogout.defaultUrl:#{null}}")
    private String defaultUrl;

    @Value("${pac4j.centralLogout.logoutUrlPattern:#{null}}")
    private String logoutUrlPattern;

    @Autowired
    private Config config;

    @Autowired
    private J2EContext webContext;

    @Autowired
    private ProfileManager profileManager;

    private LogoutController logoutController;

    @PostConstruct
    protected void afterPropertiesSet() {
        logoutController = new LogoutController();
        logoutController.setDefaultUrl(defaultUrl);
        logoutController.setLogoutUrlPattern(logoutUrlPattern);
        logoutController.setLocalLogout(false);
        logoutController.setCentralLogout(true);
        logoutController.setConfig(config);
        logoutController.setDestroySession(false);
    }

    @RequestMapping("/")
    public String root(final Map<String, Object> map) throws HttpAction {
        return index(map);
    }

    @RequestMapping("/index.html")
    public String index(final Map<String, Object> map) throws HttpAction {
        map.put("profiles", profileManager.getAll(true));
        map.put("sessionId", webContext.getSessionStore().getOrCreateSessionId(webContext));
        return "index";
    }

    /*@RequestMapping("/saml/index.html")
    public String saml(final Map<String, Object> map) {
        map.put("profiles", profileManager.getAll(true));
        return "protectedIndex";
    }*/

    @RequestMapping(value = "/saml/index.html", method = RequestMethod.GET)
    public String saml(final Map<String, Object> map, RedirectAttributes redirectAttributes) { //HttpServletResponse response) {
        List<SAML2Profile> profiles = profileManager.getAll(true);
        SAML2Profile saml2Profile = profiles.get(0);
        ArrayList mail = (ArrayList) saml2Profile.getAttribute("emailAddress");
        if (mail != null && !mail.isEmpty()) {
            /*Cookie cookie = new Cookie("emailAddress", mail.get(0).toString());
            cookie.setPath("/");
            response.addCookie(cookie);*/
            redirectAttributes.addAttribute("emailAddress", mail.get(0));
        }
        ArrayList screenName = (ArrayList) saml2Profile.getAttribute("screenName");
        if (screenName != null && !screenName.isEmpty()) {
            /*Cookie cookie = new Cookie("screenName", screenName.get(0).toString());
            cookie.setPath("/");
            response.addCookie(cookie);*/
            redirectAttributes.addAttribute("screenName", screenName.get(0));
        }
        ArrayList firstName = (ArrayList) saml2Profile.getAttribute("firstName");
        if (firstName != null && !firstName.isEmpty()) {
            /*Cookie cookie = new Cookie("firstName", firstName.get(0).toString());
            cookie.setPath("/");
            response.addCookie(cookie);*/
            redirectAttributes.addAttribute("firstName", firstName.get(0));
        }
        ArrayList lastName = (ArrayList) saml2Profile.getAttribute("lastName");
        if (lastName != null && !lastName.isEmpty()) {
            /*Cookie cookie = new Cookie("lastName", lastName.get(0).toString());
            cookie.setPath("/");
            response.addCookie(cookie);*/
            redirectAttributes.addAttribute("lastName", lastName.get(0));
        }
        map.put("profiles", profiles);
        return "redirect:http://dockerhost:8080/c/portal/login";
        //return "protectedIndex";
    }

    @RequestMapping("/centralLogout")
    @ResponseBody
    public void centralLogout() {
        logoutController.logout(webContext.getRequest(), webContext.getResponse());
    }

    @ExceptionHandler(HttpAction.class)
    public void httpAction() {
        // do nothing
    }
}
