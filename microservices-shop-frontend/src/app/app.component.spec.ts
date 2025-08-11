import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { of } from 'rxjs';

describe('AppComponent', () => {
  // Create a mock for OidcSecurityService with a spy for the checkAuth method
  // that returns an observable with isAuthenticated set to true
  const oidcSecurityServiceMock = {
    checkAuth: jasmine.createSpy('checkAuth').and.returnValue(of({ isAuthenticated: true }))
  };

  beforeEach(async () => {
    // Configure the testing module for this test suite
    // Import the AppComponent and provide the mock service instead of the real one
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: OidcSecurityService, useValue: oidcSecurityServiceMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    // Create an instance of the AppComponent
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    // Verify that the component instance is created successfully
    expect(app).toBeTruthy();
  });

  it(`should have the 'microservices-shop-frontend' title`, () => {
    // Create an instance of the AppComponent
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    // Check that the title property is set correctly
    expect(app.title).toEqual('microservices-shop-frontend');
  });

});
