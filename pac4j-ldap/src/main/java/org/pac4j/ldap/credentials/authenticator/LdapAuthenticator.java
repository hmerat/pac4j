package org.pac4j.ldap.credentials.authenticator;

import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.auth.AuthenticationRequest;
import org.ldaptive.auth.AuthenticationResponse;
import org.ldaptive.auth.AuthenticationResultCode;
import org.ldaptive.auth.Authenticator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.AccountNotFoundException;
import org.pac4j.core.exception.BadCredentialsException;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.creator.AuthenticatorProfileCreator;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.UsernamePasswordAuthenticator;
import org.pac4j.core.util.InitializableWebObject;
import org.pac4j.ldap.profile.LdapProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authenticator for LDAP based on the Ldaptive library and its core {@link org.ldaptive.auth.Authenticator} class.
 * It creates the user profile and stores it in the credentials for the {@link AuthenticatorProfileCreator}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public class LdapAuthenticator extends InitializableWebObject implements UsernamePasswordAuthenticator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Authenticator ldapAuthenticator;

    private String attributes = "";

    public LdapAuthenticator() {}

    public LdapAuthenticator(final Authenticator ldapAuthenticator) {
        this.ldapAuthenticator = ldapAuthenticator;
    }

    public LdapAuthenticator(final Authenticator ldapAuthenticator, final String attributes) {
        this.ldapAuthenticator = ldapAuthenticator;
        this.attributes = attributes;
    }

    @Override
    protected void internalInit(final WebContext context) {
        CommonHelper.assertNotNull("ldapAuthenticator", ldapAuthenticator);
        CommonHelper.assertNotNull("attributes", attributes);
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials) throws RequiresHttpAction {

        final String username = credentials.getUsername();
        final String[] ldapAttributes = attributes.split(",");
        final AuthenticationResponse response;
        try {
            logger.debug("Attempting LDAP authentication for: {}", credentials);
            final AuthenticationRequest request = new AuthenticationRequest(username,
                    new org.ldaptive.Credential(credentials.getPassword()),
                    ldapAttributes);
            response = this.ldapAuthenticator.authenticate(request);
        } catch (final LdapException e) {
            throw new TechnicalException("Unexpected LDAP error", e);
        }
        logger.debug("LDAP response: {}", response);

        if (response.getResult()) {
            final LdapProfile profile = createProfile(username, ldapAttributes, response.getLdapEntry());
            credentials.setUserProfile(profile);
            return;
        }

        if (AuthenticationResultCode.DN_RESOLUTION_FAILURE == response.getAuthenticationResultCode()) {
            throw new AccountNotFoundException(username + " not found");
        }
        throw new BadCredentialsException("Invalid credentials for: " + username);
    }

    protected LdapProfile createProfile(final String username, final String[] ldapAttributes, final LdapEntry entry) {
        final LdapProfile profile = new LdapProfile();
        profile.setId(username);
        for (String ldapAttribute: ldapAttributes) {
            final LdapAttribute entryAttribute = entry.getAttribute(ldapAttribute);
            if (entryAttribute != null) {
                logger.debug("Found attribute: {}", ldapAttribute);
                if (entryAttribute.size() > 1) {
                    profile.addAttribute(ldapAttribute, entryAttribute.getStringValues());
                } else {
                    profile.addAttribute(ldapAttribute, entryAttribute.getStringValue());
                }
            }
        }
        return profile;
    }

    public Authenticator getLdapAuthenticator() {
        return ldapAuthenticator;
    }

    public void setLdapAuthenticator(Authenticator ldapAuthenticator) {
        this.ldapAuthenticator = ldapAuthenticator;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
