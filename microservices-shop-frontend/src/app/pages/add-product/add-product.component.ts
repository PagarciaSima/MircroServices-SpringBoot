import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Product } from "../../model/product";
import { ProductService } from "../../services/product/product.service";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  addProductForm!: FormGroup;
  private readonly productService = inject(ProductService);
  productCreated = false;

  constructor(private fb: FormBuilder) {
    this.bindProductForm();
  }

  /**
 * Initializes the reactive form `addProductForm` with the necessary form controls
 * and their respective validators.
 * 
 * The form contains the following fields:
 * - `skuCode`: required text input.
 * - `name`: required text input.
 * - `description`: required text input.
 * - `price`: required numeric input, initialized to 0.
 */
  private bindProductForm() {
    this.addProductForm = this.fb.group({
      skuCode: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [0, [Validators.required]]
    });
  }

  /**
 * Handles the form submission event.
 * 
 * If the form is valid, it creates a new `Product` object from the form values
 * and calls the service to create the product.
 * Otherwise, it logs a message indicating the form is not valid.
 */
  onSubmit(): void {
    if (this.addProductForm.valid) {
      const product: Product = this.bindNewProductFromForm();
      this.createNewProduct(product);
    } else {
      console.log('Form is not valid');
    }
  }

  /**
 * Calls the ProductService to create a new product and handles the response.
 * 
 * Subscribes to the createProduct observable:
 * - On success (`next`), sets `productCreated` flag to true and resets the form.
 * - On error (`error`), logs the error to the console.
 * 
 * @param product - The Product object to be created.
 */
  private createNewProduct(product: Product) {
    this.productService.createProduct(product).subscribe({
      next: () => {
        this.productCreated = true;
        this.addProductForm.reset();
      }, error: (err) => {
        console.log(err);
      }
    });
  }

  /**
 * Creates a new `Product` object by extracting the current values
 * from the form controls.
 * 
 * @returns A `Product` object populated with the form data.
 */
  private bindNewProductFromForm(): Product {
    return {
      skuCode: this.addProductForm.get('skuCode')?.value,
      name: this.addProductForm.get('name')?.value,
      description: this.addProductForm.get('description')?.value,
      price: this.addProductForm.get('price')?.value
    };
  }

  /**
  * Gets the FormControl for the SKU code field.
  */
  get skuCode() {
    return this.addProductForm.get('skuCode');
  }

  /**
   * Gets the FormControl for the name field.
   */
  get name() {
    return this.addProductForm.get('name');
  }

  /**
   * Gets the FormControl for the description field.
   */
  get description() {
    return this.addProductForm.get('description');
  }

  /**
   * Gets the FormControl for the price field.
   */
  get price() {
    return this.addProductForm.get('price');
  }

}
