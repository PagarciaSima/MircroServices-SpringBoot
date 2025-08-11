import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Subject } from 'rxjs';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {

  private readonly oidcSecurityService = inject(OidcSecurityService);
  isAuthenticated = false;
  username = "";
  private readonly destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.subscribeToAuthStatus();
    this.subscribeToUserData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
 * Subscribes to the `userData$` observable from the `OidcSecurityService`
 * to retrieve the current user's data.
 *
 * This method updates the local `username` property with the user's preferred username
 * whenever new user data is emitted.
 *
 * Any errors encountered during subscription are logged to the console.
 *
 * @remarks The observable does not complete automatically, so consider
 * using operators like `take(1)` or `takeUntil` to avoid memory leaks
 * in larger applications.
 */
  subscribeToUserData() {
    this.oidcSecurityService.userData$.subscribe({
      next: ({ userData }) => {
        this.username = userData.preferred_username;
      },
      error: (err) => {
        console.error('Error fetching user data:', err);
      }
    });
  }

  /**
 * Subscribes to the `isAuthenticated$` observable from the `OidcSecurityService`
 * to determine whether the user is authenticated via OAuth2.
 *
 * The authentication is handled using OpenID Connect. This method updates the local
 * `isAuthenticated` property based on the emitted value, which can be used to control
 * access to features or UI elements.
 *
 * It also handles any potential errors by logging them to the console.
 *
 * @remarks The observable does not complete automatically, so it's recommended to use
 * operators like `take(1)` or `takeUntil` to prevent memory leaks in larger applications.
 */
  subscribeToAuthStatus() {
    this.oidcSecurityService.isAuthenticated$.subscribe({
      next: ({ isAuthenticated }) => {
        this.isAuthenticated = isAuthenticated;
      }, error: (err) => {
        console.error(err);
      }
    });
  }

  /**
   * Initiates the login process by redirecting to the authorization endpoint.
   */
  login(): void {
    this.oidcSecurityService.authorize();
  }

  /**
   * Logs out the current user by calling the logoff endpoint,
   * and logs the result to the console.
   */
  logout(): void {
    this.oidcSecurityService
      .logoff()
      .subscribe((result) => console.log(result));
  }
}
