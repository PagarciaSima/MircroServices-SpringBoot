import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddProductComponent } from './add-product.component';
import { ProductService } from '../../services/product/product.service';
import { ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('AddProductComponent', () => {
  let component: AddProductComponent;
  let fixture: ComponentFixture<AddProductComponent>;
  let productServiceMock: any;

  // Setup TestBed and create component instance before each test
  beforeEach(async () => {
    // Mock ProductService with a spy on createProduct method
    productServiceMock = {
      createProduct: jasmine.createSpy('createProduct').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [AddProductComponent, ReactiveFormsModule],
      providers: [
        { provide: ProductService, useValue: productServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Basic sanity check: component instance should be created
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Verify that the form is invalid when no values are set
  it('form should be invalid when empty', () => {
    expect(component.addProductForm.valid).toBeFalse();
  });

  // Check that the skuCode form control is invalid if set to an empty string
  it('skuCode field should be invalid when empty', () => {
    const skuCode = component.skuCode;
    skuCode?.setValue('');
    expect(skuCode?.valid).toBeFalse();
  });

  // Verify that the form becomes valid when all required fields have valid values
  it('form should be valid when all required fields are filled', () => {
    component.addProductForm.setValue({
      skuCode: 'SKU123',
      name: 'Product Name',
      description: 'Product Description',
      price: 100
    });
    expect(component.addProductForm.valid).toBeTrue();
  });

  // Test that onSubmit calls createProduct service method when form is valid
  // and sets productCreated to true
  it('should call createProduct on valid form submission', () => {
    component.addProductForm.setValue({
      skuCode: 'SKU123',
      name: 'Product Name',
      description: 'Product Description',
      price: 100
    });

    component.onSubmit();

    expect(productServiceMock.createProduct).toHaveBeenCalledWith({
      skuCode: 'SKU123',
      name: 'Product Name',
      description: 'Product Description',
      price: 100
    });

    expect(component.productCreated).toBeTrue();
  });

  // Test that onSubmit does not call createProduct when the form is invalid
  // and logs a message to the console
  it('should not call createProduct on invalid form submission', () => {
    spyOn(console, 'log');
    component.addProductForm.setValue({
      skuCode: '',
      name: '',
      description: '',
      price: 0
    });

    component.onSubmit();

    expect(productServiceMock.createProduct).not.toHaveBeenCalled();
    expect(console.log).toHaveBeenCalledWith('Form is not valid');
  });

  // Verify that after a successful product creation, the form is reset,
  // and productCreated flag is set to true
  it('should reset form and set productCreated to true on successful creation', () => {
    component.addProductForm.setValue({
      skuCode: 'SKU123',
      name: 'Product Name',
      description: 'Product Description',
      price: 100
    });

    component.onSubmit();

    expect(component.productCreated).toBeTrue();
    expect(component.addProductForm.pristine).toBeTrue();
    expect(component.addProductForm.value).toEqual({
      skuCode: null,
      name: null,
      description: null,
      price: null
    });
  });

  // Test error handling: logs an error if createProduct observable throws an error
  it('should log error if createProduct fails', () => {
    spyOn(console, 'log');
    productServiceMock.createProduct.and.returnValue(throwError(() => new Error('Error')));

    component.addProductForm.setValue({
      skuCode: 'SKU123',
      name: 'Product Name',
      description: 'Product Description',
      price: 100
    });

    component.onSubmit();

    expect(console.log).toHaveBeenCalledWith(jasmine.any(Error));
  });
});
