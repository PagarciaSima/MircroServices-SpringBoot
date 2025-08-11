import { TestBed } from '@angular/core/testing';
import { OrderService } from './order.service';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Order } from '../../model/order';
import { provideHttpClient } from '@angular/common/http';

describe('OrderService', () => {
  let service: OrderService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        OrderService,
        provideHttpClient(),          // Provides the real HttpClient to the testing module
        provideHttpClientTesting()    // Provides the HttpTestingController mock for intercepting HTTP requests
      ],
    });

    // Inject the OrderService instance from the testing injector
    service = TestBed.inject(OrderService);

    // Inject the HttpTestingController to mock and assert HTTP requests
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verify that no unmatched HTTP requests are pending after each test
    httpTestingController.verify();
  });

  it('should be created', () => {
    // Basic test to verify the service was created successfully
    expect(service).toBeTruthy();
  });

  it('should send an order and return response text', () => {
    // Prepare a mock order object that matches the Order interface
    const mockOrder: Order = {
      skuCode: '123-ABC',
      price: 99.99,
      quantity: 3,
      userDetails: {
        email: 'test@example.com',
        firstName: 'Test',
        lastName: 'User'
      }
    };

    // Mock response expected from the API when order is placed
    const mockResponse = 'Order placed successfully';

    // Call the service method and subscribe to the Observable response
    service.orderProduct(mockOrder).subscribe(response => {
      // Assert that the response from the service matches the mock response
      expect(response).toBe(mockResponse);
    });

    // Expect that a single HTTP POST request has been made to the API URL
    const req = httpTestingController.expectOne('http://localhost:9000/api/order');

    // Assert that the HTTP method used was POST
    expect(req.request.method).toBe('POST');

    // Assert that the request body matches the mock order object
    expect(req.request.body).toEqual(mockOrder);

    // Assert that the Content-Type header is set to application/json
    expect(req.request.headers.get('Content-Type')).toBe('application/json');

    // Respond to the HTTP request with the mock response
    req.flush(mockResponse);
  });
});
