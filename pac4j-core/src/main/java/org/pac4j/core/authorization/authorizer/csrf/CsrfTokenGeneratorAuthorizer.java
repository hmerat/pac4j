package org.pac4j.core.authorization.authorizer.csrf;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.CommonHelper;

/**
 * Authorizer which creates a new CSRF token and adds it as a request attribute and as a cookie (AngularJS).
 *
 * @author Jerome Leleu
 * @since 1.8.1
 */
public class CsrfTokenGeneratorAuthorizer implements Authorizer<UserProfile> {

    private final CsrfTokenGenerator csrfTokenGenerator;

    public CsrfTokenGeneratorAuthorizer(final CsrfTokenGenerator csrfTokenGenerator) {
        this.csrfTokenGenerator = csrfTokenGenerator;
    }

    @Override
    public boolean isAuthorized(final WebContext context, final UserProfile profile) {
        CommonHelper.assertNotNull("csrfTokenGenerator", csrfTokenGenerator);
        final String token = csrfTokenGenerator.get(context);
        context.setRequestAttribute(Pac4jConstants.CSRF_TOKEN, token);
        final Cookie cookie = new Cookie(Pac4jConstants.CSRF_TOKEN, token);
        cookie.setDomain(context.getServerName());
        context.addResponseCookie(cookie);
        return true;
    }
}
