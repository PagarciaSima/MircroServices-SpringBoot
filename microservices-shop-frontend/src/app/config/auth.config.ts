import { PassedInitialConfig } from 'angular-auth-oidc-client';

/**
 * Authentication configuration object for the Angular application.
 * 
 * This configuration is passed to the authentication service to enable
 * OpenID Connect (OIDC) with the Keycloak server.
 * 
 * @see https://www.keycloak.org/ for more details on Keycloak setup
 */
export const authConfig: PassedInitialConfig = {
  config: {
    /** URL of the OIDC provider (Keycloak realm endpoint) */
    authority: 'http://localhost:8181/realms/spring-microservices-security-realm',

    /** Where the user is redirected after successful login */
    redirectUrl: window.location.origin,

    /** Where the user is redirected after logging out */
    postLogoutRedirectUri: window.location.origin,

    /** Client ID registered in Keycloak */
    clientId: 'angular-client',

    /** Requested scopes for the token */
    scope: 'openid profile offline_access',

    /** OAuth2 response type to use (Authorization Code Flow) */
    responseType: 'code',

    /** Enables automatic silent token renewal */
    silentRenew: true,

    /** Enables usage of refresh tokens */
    useRefreshToken: true,

    /** Time before token expiry to trigger a renewal (in seconds) */
    renewTimeBeforeTokenExpiresInSeconds: 30,
  }
}
