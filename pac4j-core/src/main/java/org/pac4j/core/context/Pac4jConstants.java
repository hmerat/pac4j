package org.pac4j.core.context;

/**
 * Common constants.
 *
 * @author Jerome Leleu
 * @since 1.6.0
 */
public interface Pac4jConstants {

    /* Original requested url to save before redirect to Identity Provider */
    String REQUESTED_URL = "pac4jRequestedUrl";

    /* User Profile object saved in session */
    String USER_PROFILE = "pac4jUserProfile";

    /* CSRF token name saved in session */
    String CSRF_TOKEN = "pac4jCsrfToken";

    /* Session ID */
    String SESSION_ID = "pac4jSessionId";

    @Deprecated
    String CLIENT_NAME = "clientName";

    /* Client names configuration parameter */
    String CLIENTS = "clients";

    /* An AJAX parameter name to dynamically set a HTTP request as an AJAX one. */
    String IS_AJAX_REQUEST = "is_ajax_request";

    @Deprecated
    String AUTHORIZER_NAME = "authorizerName";

    /* Authorizers names configuration parameter */
    String AUTHORIZERS = "authorizers";

    /* The default url parameter */
    String DEFAULT_URL = "defaultUrl";

    /* The default url, the root path */
    String DEFAULT_URL_VALUE = "/";

    /* The url parameter */
    String URL = "url";

    /* The element (client or authorizer) separator */
    String ELEMENT_SEPRATOR = ",";

    /* The logout pattern for url */
    String LOGOUT_URL_PATTERN = "logoutUrlPattern";

    /* The default value for the logout url pattern, meaning only relative urls are allowed */
    String DEFAULT_LOGOUT_URL_PATTERN_VALUE = "/.*";

    /* The config factory parameter */
    String CONFIG_FACTORY = "configFactory";

    @Deprecated
    String MATCHER_NAME = "matcherName";

    /* Matcher names configuration parameter */
    String MATCHERS = "matchers";

    String USERNAME = "username";

    String PASSWORD = "password";
}
