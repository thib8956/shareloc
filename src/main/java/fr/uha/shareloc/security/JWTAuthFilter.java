package fr.uha.shareloc.security;

import fr.uha.shareloc.api.AuthenticationServices;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.util.List;


@Provider
@SigninNeeded
public class JWTAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) throw new NotAuthorizedException("Bearer");

        // récupération du JWT et vérification de la signature
        if (authHeader.startsWith("Bearer")) {
            try {
                // test de validation de la signature et décodage du contenu du token
                final String subject = validate(authHeader.split(" ")[1]);

                // définition du contexte de sécurité
                final SecurityContext securityContext = requestContext.getSecurityContext();
                if (subject != null) {
                    requestContext.setSecurityContext(new MySecurityContext(subject, securityContext));
                }
            } catch (InvalidJwtException ex) {
                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        } else {
            requestContext.setProperty("auth-failed", true);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    /**
     * Méthode de validation d'un JWT
     *
     * @param jwt le token à tester
     * @return le contenu du token décryté s'il est valide, null sinon
     * @throws InvalidJwtException si le token n'est pas valide
     */
    private String validate(String jwt) throws InvalidJwtException {
        String subject;
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();

        // construction du décodeur de JWT
        JwtConsumer jwtConsumer = new JwtConsumerBuilder().setRequireSubject()
                .setVerificationKey(rsaJsonWebKey.getKey())
                .build();

        // validation du JWT et récupération du contenu
        JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
        subject = (String) jwtClaims.getClaimValue("sub");

        return subject;
    }

    private static final class MySecurityContext implements SecurityContext {
        private final String subject;
        private final SecurityContext securityContext;

        MySecurityContext(String subject, SecurityContext securityContext) {
            this.subject = subject;
            this.securityContext = securityContext;
        }

        @Override
        public Principal getUserPrincipal() {
            return () -> subject;
        }

        @Override
        public boolean isUserInRole(String role) {
            List<String> roles = AuthenticationServices.findUserRoles(subject);
            if (roles != null) return roles.contains(role);
            return false;
        }

        @Override
        public boolean isSecure() {
            return securityContext.isSecure();
        }

        @Override
        public String getAuthenticationScheme() {
            return securityContext.getAuthenticationScheme();
        }
    }
}
