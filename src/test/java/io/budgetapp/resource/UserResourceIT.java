package io.budgetapp.resource;


import com.sun.jersey.api.client.ClientResponse;
import io.budgetapp.BudgetApplication;
import io.budgetapp.configuration.AppConfiguration;
import io.budgetapp.model.User;
import io.budgetapp.model.form.LoginForm;
import io.budgetapp.model.form.SignUpForm;
import io.budgetapp.model.form.user.Password;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 *
 */
public class UserResourceIT extends ResourceIT {

    @ClassRule
    public static final DropwizardAppRule<AppConfiguration> RULE =
            new DropwizardAppRule<>(BudgetApplication.class, resourceFilePath("config-test.yml"));

    @Override
    protected int getPort() {
        return RULE.getLocalPort();
    }

    @Test
    public void shouldAbleCreateUser() {

        // given
        SignUpForm signUp = new SignUpForm();

        // when
        signUp.setUsername(randomEmail());
        signUp.setPassword(randomAlphabets());
        ClientResponse response = post("/api/users", signUp);

        // then
        assertCreated(response);
        Assert.assertNotNull(response.getLocation());
    }


    @Test
    public void shouldAbleChangePassword() {
        // given
        Password password = new Password();
        password.setOriginal(defaultUser.getPassword());
        password.setPassword(randomAlphabets());
        password.setConfirm(password.getPassword());

        // when
        put(Resources.CHANGE_PASSWORD, password);
        LoginForm login = new LoginForm();
        login.setUsername(defaultUser.getUsername());
        login.setPassword(password.getPassword());
        ClientResponse authResponse = post(Resources.USER_AUTH, login);

        // then
        assertOk(authResponse);
        Assert.assertNotNull(authResponse.getEntity(User.class).getToken());
    }
}
