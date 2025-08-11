import {HttpInterceptorFn} from "@angular/common/http";
import {inject} from "@angular/core";
import {OidcSecurityService} from "angular-auth-oidc-client";
import { switchMap } from "rxjs";

/**
 * HTTP interceptor that attaches a Bearer token to outgoing requests when available.
 *
 * This interceptor retrieves the current access token from the OidcSecurityService
 * and, if present, clones the original request adding an `Authorization` header.
 * If no token is found, the request is forwarded unchanged.
 *
 * `switchMap` is used because `next(...)` returns an Observable of the HTTP event stream.
 * This operator replaces the original `Observable<string>` (token) with the
 * `Observable<HttpEvent>` returned by `next(...)`, ensuring the interceptor outputs
 * the correct observable type.
 *
 * @param req - The outgoing HTTP request.
 * @param next - The function to pass the request to the next interceptor or the backend.
 * @returns An Observable of the HTTP event stream for the modified or original request.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(OidcSecurityService);

  return authService.getAccessToken().pipe(
    switchMap(token => {
      if (token) {
        const headers = req.headers.set('Authorization', `Bearer ${token}`);
        const authReq = req.clone({ headers });
        return next(authReq);
      }
      return next(req);
    })
  );
}
