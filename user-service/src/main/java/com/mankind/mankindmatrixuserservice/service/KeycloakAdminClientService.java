package com.mankind.mankindmatrixuserservice.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminClientService {
    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminClientService.class);

    private final Keycloak keycloak;
    private final String realm;

    public KeycloakAdminClientService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public String createUser(String username, String email, String password, String firstName, String lastName, Map<String, String> customAttributes) {

        UsersResource usersResource = keycloak.realm(realm).users();

        List<UserRepresentation> found = usersResource.search(username);
        if (!found.isEmpty()) {
            throw new IllegalStateException("User already exists in Keycloak with username=" + username);
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(true);

        if (customAttributes != null && !customAttributes.isEmpty()) {
            user.setAttributes(customAttributes.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> List.of(e.getValue())
                    )));
        }

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);
        user.setCredentials(Collections.singletonList(cred));

        if (customAttributes != null) {
            customAttributes.forEach(user::singleAttribute);
        }

        log.debug("Creating Keycloak user '{}' in realm '{}'", username, realm);

        try (Response resp = keycloak.realm(realm).users().create(user)) {
            int status = resp.getStatus();
            if (status != Response.Status.CREATED.getStatusCode()) {
                throw new IllegalStateException("Keycloak create user failed: HTTP " + status + "): " +
                        resp.getStatusInfo().getReasonPhrase());
            }

            log.debug("Keycloak user '{}' created", username);
            return CreatedResponseUtil.getCreatedId(resp);
        }
    }
}
