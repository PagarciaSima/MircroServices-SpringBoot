import { Component, inject, OnInit } from '@angular/core';
import { OidcSecurityService, UserDataResult } from "angular-auth-oidc-client";
import { Product } from "../../model/product";
import { ProductService } from "../../services/product/product.service";
import { Router } from "@angular/router";
import { Order } from "../../model/order";
import { FormsModule } from "@angular/forms";
import { OrderService } from "../../services/order/order.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './home-page.component.html',
  standalone: true,
  imports: [
    FormsModule
  ],
  styleUrl: './home-page.component.css'
})
export class HomePageComponent implements OnInit {
  private readonly oidcSecurityService = inject(OidcSecurityService);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);
  private readonly router = inject(Router);
  isAuthenticated = false;
  products: Array<Product> = [];
  quantityIsNull = false;
  orderSuccess = false;
  orderFailed = false;

  ngOnInit(): void {
    this.subscribeToAuthentication();
  }

  /**
 * Subscribes to the authentication status observable from the OIDC security service.
 * Updates the local `isAuthenticated` property based on the emitted value.
 * When authentication status changes, it triggers fetching all products.
 */
  private subscribeToAuthentication() {
    this.oidcSecurityService.isAuthenticated$.subscribe({
      next: ({ isAuthenticated }) => {
        this.isAuthenticated = isAuthenticated;
        this.fetchAllProducts();
      }, error: (err) => {
        console.error(err);
      }
    });
  }

  /**
 * Fetches all products from the ProductService and updates the local `products` array.
 *
 * Subscribes to the observable returned by `getProducts` to receive the product data.
 */
  private fetchAllProducts() {
    this.productService.getProducts()
      .subscribe({
        next: (products) => {
          this.products = products;
          console.log(products);
        }, error: (err) => {
          console.log(err);
        }
      });
  }

  /**
 * Navigates the user to the product creation page.
 *
 * @remarks
 * This method uses Angular's Router to navigate to the `/add-product` route,
 * where a new product can be created.
 */
  goToCreateProductPage() {
    this.router.navigateByUrl('/add-product');
  }

  /**
 * Initiates the ordering process for a given product and quantity.
 *
 * Subscribes to the user data observable to retrieve user details,
 * then calls `manageOrder` to handle the order logic.
 *
 * @param product - The product to be ordered.
 * @param quantity - The quantity of the product as a string.
 */
  orderProduct(product: Product, quantity: string) {
    this.oidcSecurityService.userData$.subscribe({
      next: (result) => {
        const userDetails = this.getUserDetails(result);
        this.manageOrder(quantity, product, userDetails);
      }, error: (err) => {
        console.error(err)
      }
    });
  }

  /**
 * Manages the order process by validating the quantity and either updating the status for failure
 * or creating and sending a new order.
 *
 * @param quantity - The quantity of the product to order as a string.
 * @param product - The product being ordered.
 * @param userDetails - An object containing user details: email, firstName, and lastName.
 *
 * If the quantity is falsy (e.g., empty or zero), it updates the status to reflect order failure.
 * Otherwise, it generates a new order and sends it.
 */
  private manageOrder(quantity: string, product: Product, userDetails: { email: any; firstName: any; lastName: any; }) {
    if (!quantity) {
      this.updateStatusForOrderFailed();
    } else {
      const order: Order = this.generateNewOrder(product, quantity, userDetails);
      this.sendNewOrder(order);
    }
  }

  /**
 * Sends a new order to the backend service and updates the component state based on the response.
 *
 * @param order - The `Order` object to be sent.
 *
 * Sets `orderSuccess` to `true` if the order is successfully processed.
 * Sets `orderFailed` to `false` if there is an error processing the order.
 */
  private sendNewOrder(order: Order) {
    this.orderService.orderProduct(order).subscribe({
      next: () => {
        this.orderSuccess = true;
      }, error: () => {
        this.orderFailed = false;
      }
    });
  }

  /**
 * Creates and returns a new `Order` object based on the product data, quantity, and user details.
 *
 * @param product - The product being ordered.
 * @param quantity - The quantity requested as a string (will be converted to a number).
 * @param userDetails - An object containing basic user information (email, first name, and last name).
 * @returns An `Order` object with the combined information to be sent to the backend.
 */
  private generateNewOrder(product: Product, quantity: string, userDetails: { email: any; firstName: any; lastName: any; }): Order {
    return {
      skuCode: product.skuCode,
      price: product.price,
      quantity: Number(quantity),
      userDetails: userDetails
    };
  }

  /**
 * Updates the component state to reflect a failed order due to a null quantity.
 *
 * Sets `orderFailed` to true, `orderSuccess` to false, and `quantityIsNull` to true,
 * which can be used to display appropriate error messages in the UI.
 */
  private updateStatusForOrderFailed() {
    this.orderFailed = true;
    this.orderSuccess = false;
    this.quantityIsNull = true;
  }

  /**
 * Extracts and returns basic user details from a given UserDataResult object.
 *
 * @param result - The UserDataResult object containing the user data.
 * @returns An object containing the user's email, first name, and last name.
 */
  private getUserDetails(result: UserDataResult) {
    return {
      email: result.userData.email,
      firstName: result.userData.firstName,
      lastName: result.userData.lastName
    };
  }
}
