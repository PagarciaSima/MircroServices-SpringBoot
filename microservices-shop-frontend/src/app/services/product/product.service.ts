import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product } from "../../model/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) {
  }

  /**
   * Retrieves the list of products from the backend API.
   * @returns An Observable emitting an array of Product objects.
   */
  getProducts(): Observable<Array<Product>> {
    return this.httpClient.get<Array<Product>>('http://localhost:9000/api/product');
  }

  /**
   * Sends a new product to the backend API to be created.
   * @param product The Product object to create.
   * @returns An Observable emitting the created Product object.
   */
  createProduct(product: Product): Observable<Product> {
    return this.httpClient.post<Product>('http://localhost:9000/api/product', product);
  }
}
