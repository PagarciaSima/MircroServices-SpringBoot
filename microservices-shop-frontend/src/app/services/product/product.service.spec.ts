import { TestBed } from '@angular/core/testing';
import { ProductService } from './product.service';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Product } from '../../model/product';

describe('ProductService', () => {
  let service: ProductService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProductService,          // Provide the service under test
        provideHttpClient(),     // Provide the real HttpClient instance (needed by the service)
        provideHttpClientTesting() // Provide HttpTestingController for mocking HTTP requests
      ],
    });

    // Inject the service instance to test
    service = TestBed.inject(ProductService);

    // Inject the HttpTestingController to control and assert HTTP requests
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Ensure that no unexpected HTTP requests are left pending after each test
    httpTestingController.verify();
  });

  it('should be created', () => {
    // Basic test to check the service is instantiated correctly
    expect(service).toBeTruthy();
  });

  it('should retrieve a list of products', () => {
    // Mock array of products to be returned by the fake backend
    const mockProducts: Product[] = [
      {
        id: '1',
        skuCode: 'SKU123',
        name: 'Product 1',
        description: 'Description for Product 1',
        price: 100
      },
      {
        id: '2',
        skuCode: 'SKU456',
        name: 'Product 2',
        description: 'Description for Product 2',
        price: 200
      }
    ];

    // Call the service method and subscribe to the Observable
    service.getProducts().subscribe(products => {
      // Assert the received products match the mock data exactly
      expect(products.length).toBe(2);
      expect(products).toEqual(mockProducts);
    });

    // Expect that a single GET request has been made to the specified URL
    const req = httpTestingController.expectOne('http://localhost:9000/api/product');

    // Verify that the HTTP method was GET
    expect(req.request.method).toBe('GET');

    // Respond to the request by flushing the mockProducts array as the fake response
    req.flush(mockProducts);
  });

  it('should create a new product and return it', () => {
    // Mock product object to send and expect as response
    const newProduct: Product = {
      id: '3',
      skuCode: 'SKU789',
      name: 'New Product',
      description: 'New product description',
      price: 150
    };

    // Call the service's createProduct method and subscribe to its Observable
    service.createProduct(newProduct).subscribe(product => {
      // Assert that the response matches the sent product
      expect(product).toEqual(newProduct);
    });

    // Expect a single POST request to the given URL
    const req = httpTestingController.expectOne('http://localhost:9000/api/product');

    // Assert that the HTTP method is POST
    expect(req.request.method).toBe('POST');

    // Assert that the request body matches the newProduct object
    expect(req.request.body).toEqual(newProduct);

    // Respond to the request with the mock newProduct as if the backend returned it
    req.flush(newProduct);
  });
});
