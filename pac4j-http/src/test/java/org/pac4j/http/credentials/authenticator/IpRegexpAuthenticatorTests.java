package org.pac4j.http.credentials.authenticator;

import org.junit.Test;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.http.profile.IpProfile;

import static org.junit.Assert.*;

/**
 * This class tests the {@link IpRegexpAuthenticator}.
 * 
 * @author Jerome Leleu
 * @since 1.8.0
 */
public final class IpRegexpAuthenticatorTests implements TestsConstants {

    private final static String GOOD_IP = "127.0.0.1";
    private final static String BAD_IP = "192.168.0.1";

    private final static IpRegexpAuthenticator authenticator = new IpRegexpAuthenticator(GOOD_IP);

    @Test(expected = TechnicalException.class)
    public void testNoPattern() throws RequiresHttpAction {
        IpRegexpAuthenticator authenticator = new IpRegexpAuthenticator();
        authenticator.validate(null);
    }

    @Test
    public void testValidateGoodIP() throws RequiresHttpAction {
        final TokenCredentials credentials = new TokenCredentials(GOOD_IP, CLIENT_NAME);
        authenticator.validate(credentials);
        final IpProfile profile = (IpProfile) credentials.getUserProfile();
        assertEquals(GOOD_IP, profile.getId());
    }

    @Test
    public void testValidateBadIP() throws RequiresHttpAction {
        try {
            final TokenCredentials credentials = new TokenCredentials(BAD_IP, CLIENT_NAME);
            authenticator.validate(credentials);
            fail("Should fail");
        } catch (final CredentialsException e) {
            assertEquals("Unauthorized IP address: " + BAD_IP, e.getMessage());
        }
    }
}
