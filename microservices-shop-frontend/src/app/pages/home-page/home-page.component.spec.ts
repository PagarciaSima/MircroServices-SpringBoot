import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomePageComponent } from './home-page.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { ProductService } from '../../services/product/product.service';
import { OrderService } from '../../services/order/order.service';

// Mock implementation for OidcSecurityService with observable properties
const oidcSecurityServiceMock = {
  isAuthenticated$: of({ isAuthenticated: true }), // Simulate authenticated user
  userData$: of({
    userData: {
      email: 'test@example.com',
      firstName: 'Test',
      lastName: 'User'
    }
  }) // Simulate user data returned by the service
};

// Mock ProductService with a spy on the getProducts method returning an observable of a test product list
const productServiceMock = {
  getProducts: jasmine.createSpy('getProducts').and.returnValue(of([
    { skuCode: '123', price: 10 }
  ]))
};

// Mock OrderService with a spy on orderProduct method that returns an observable for successful order placement
const orderServiceMock = {
  orderProduct: jasmine.createSpy('orderProduct').and.returnValue(of({}))
};

// Mock Router with a spy on navigateByUrl method to test navigation calls without actually navigating
const routerMock = {
  navigateByUrl: jasmine.createSpy('navigateByUrl')
};

describe('HomePageComponent', () => {
  let component: HomePageComponent;
  let fixture: ComponentFixture<HomePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomePageComponent], // Import the standalone component directly
      providers: [
        provideHttpClientTesting(),  // Provides the testing HttpClient without the deprecated module
        { provide: OidcSecurityService, useValue: oidcSecurityServiceMock }, // Use mock instead of real service
        { provide: ProductService, useValue: productServiceMock },           // Provide mock ProductService
        { provide: OrderService, useValue: orderServiceMock },               // Provide mock OrderService
        { provide: Router, useValue: routerMock }                            // Provide mock Router
      ]
    }).compileComponents();

    // Create component fixture and instance before each test
    fixture = TestBed.createComponent(HomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Trigger Angular change detection
  });

  it('should create the component', () => {
    // Test that the component instance is created successfully
    expect(component).toBeTruthy();
  });

  it('should fetch products on init when authenticated', () => {
    // Verify that getProducts() was called during initialization when user is authenticated
    expect(productServiceMock.getProducts).toHaveBeenCalled();
    // Confirm that products array in component is populated with test data
    expect(component.products.length).toBeGreaterThan(0);
  });

  it('should navigate to /add-product when goToCreateProductPage is called', () => {
    // Call the method to trigger navigation
    component.goToCreateProductPage();
    // Expect the router to navigate to the '/add-product' URL
    expect(routerMock.navigateByUrl).toHaveBeenCalledWith('/add-product');
  });
});
