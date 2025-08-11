import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { of, Subject } from 'rxjs';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  // Mock service and Subjects
  let oidcSecurityServiceMock: Partial<OidcSecurityService>;
  let isAuthenticatedSubject: Subject<{ isAuthenticated: boolean; allConfigsAuthenticated: any[] }>;
  let userDataSubject: Subject<{ userData: any; allUserData: any[] }>;

  beforeEach(async () => {
    isAuthenticatedSubject = new Subject<{ isAuthenticated: boolean; allConfigsAuthenticated: any[] }>();
    userDataSubject = new Subject<{ userData: any; allUserData: any[] }>();

    oidcSecurityServiceMock = {
      isAuthenticated$: isAuthenticatedSubject.asObservable(),
      userData$: userDataSubject.asObservable(),
      authorize: jasmine.createSpy('authorize'),
      logoff: jasmine.createSpy('logoff').and.returnValue(of('logged out')),
    };

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        { provide: OidcSecurityService, useValue: oidcSecurityServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update isAuthenticated based on isAuthenticated$.isAuthenticated', () => {
    component.ngOnInit();

    isAuthenticatedSubject.next({ isAuthenticated: true, allConfigsAuthenticated: [] });
    expect(component.isAuthenticated).toBeTrue();

    isAuthenticatedSubject.next({ isAuthenticated: false, allConfigsAuthenticated: [] });
    expect(component.isAuthenticated).toBeFalse();
  });

  it('should update username based on userData$.userData.preferred_username', () => {
    component.ngOnInit();

    userDataSubject.next({ userData: { preferred_username: 'testuser' }, allUserData: [] });
    expect(component.username).toBe('testuser');
  });

  it('should call authorize() on login()', () => {
    component.login();
    expect(oidcSecurityServiceMock.authorize).toHaveBeenCalled();
  });

  it('should call logoff() and log result on logout()', () => {
    spyOn(console, 'log');

    component.logout();

    expect(oidcSecurityServiceMock.logoff).toHaveBeenCalled();
    expect(console.log).toHaveBeenCalledWith('logged out');
  });

  it('should call destroy$.next and complete on ngOnDestroy()', () => {
    component.ngOnInit();

    spyOn(component['destroy$'], 'next').and.callThrough();
    spyOn(component['destroy$'], 'complete').and.callThrough();

    component.ngOnDestroy();

    expect(component['destroy$'].next).toHaveBeenCalled();
    expect(component['destroy$'].complete).toHaveBeenCalled();
  });
});
