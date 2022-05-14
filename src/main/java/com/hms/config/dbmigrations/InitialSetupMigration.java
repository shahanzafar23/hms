package com.hms.config.dbmigrations;

import com.hms.config.Constants;
import com.hms.domain.Authority;
import com.hms.domain.User;
import com.hms.security.AuthoritiesConstants;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.time.Instant;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    public InitialSetupMigration(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        template.save(createPatientAuthority());
        template.save(createDoctorAuthority());
        template.save(createInsurerAuthority());
        template.save(createERAuthority());
        Authority adminAuthority = createAdminAuthority();
        template.save(adminAuthority);
        addUsers(adminAuthority);
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createDoctorAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.DOCTOR);
        return userAuthority;
    }

    private Authority createInsurerAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.INSURER);
        return userAuthority;
    }

    private Authority createERAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.ER);
        return userAuthority;
    }

    private Authority createPatientAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.PATIENT);
        return userAuthority;
    }

    private void addUsers(Authority adminAuthority) {
        User admin = createAdmin(adminAuthority);
        template.save(admin);
    }

    private User createUser(Authority userAuthority) {
        User userUser = new User();
        userUser.setId("user-2");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(Constants.SYSTEM);
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        return userUser;
    }

    private User createAdmin(Authority adminAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        return adminUser;
    }
}
