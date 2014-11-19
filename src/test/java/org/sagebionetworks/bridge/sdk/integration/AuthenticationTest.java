package org.sagebionetworks.bridge.sdk.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sagebionetworks.bridge.Tests;
import org.sagebionetworks.bridge.sdk.ClientProvider;
import org.sagebionetworks.bridge.sdk.Config;
import org.sagebionetworks.bridge.sdk.TestApiCaller;
import org.sagebionetworks.bridge.sdk.TestUserHelper;
import org.sagebionetworks.bridge.sdk.TestUserHelper.TestUser;
import org.sagebionetworks.bridge.sdk.UserClient;
import org.sagebionetworks.bridge.sdk.exceptions.BadRequestException;
import org.sagebionetworks.bridge.sdk.exceptions.InvalidEntityException;
import org.sagebionetworks.bridge.sdk.models.users.SignInCredentials;

public class AuthenticationTest {

    private Properties properties;
    private TestUser user;

    @Before
    public void before() {
        user = TestUserHelper.createAndSignInUser(AuthenticationTest.class, true);
        properties = Tests.getApplicationProperties();
    }

    @After
    public void after() {
        user.signOutAndDeleteUser();
    }

    @Test
    public void signInNoCredentialsFailsWith400() {
        try {
            user.getSession().signOut();
            ClientProvider.signIn(new SignInCredentials(null, null));
            fail("Should have thrown an exception");
        } catch(InvalidEntityException e) {
            assertEquals("Exception is a 400 Bad Request", 400, e.getStatusCode());
        }
    }

    @Test
    @Ignore
    public void signInGarbageCredentialsFailsWith400() {
        try {
            TestApiCaller caller = new TestApiCaller(null);
            String url = properties.getProperty(Config.Props.AUTH_SIGNIN_API.getPropertyName());

            HttpResponse response = caller.post(url, "username=bob&password=foo");
            assertEquals("Response should be 400 Bad Request", 400, response.getStatusLine().getStatusCode());
            fail("Should have thrown an exception");
        } catch(BadRequestException e) {
            assertEquals("Exception is a 400 Bad Request", 400, e.getStatusCode());
        }
    }

    @Test(expected=IllegalStateException.class)
    public void signOutUpdatesSession() {
        UserClient client = user.getSession().getUserClient();

        user.getSession().signOut();
        assertFalse("User is signed out", user.getSession().isSignedIn());

        client.getProfile(); // throws Exception
    }

}
