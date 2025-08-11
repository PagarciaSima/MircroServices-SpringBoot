import { Injectable } from '@angular/core';
import { Product } from "../../model/product";
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Order } from "../../model/order";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient: HttpClient) {
  }

  /**
   * Sends an order request to the backend API.
   * 
   * @param order - The order object to be sent.
   * @returns An Observable emitting the response as a string.
   */
  orderProduct(order: Order): Observable<string> {
    const httpOptions = this.getHttpOptions();
    return this.httpClient.post<string>('http://localhost:9000/api/order', order, httpOptions);
  }

  /**
   * Builds and returns the HTTP options for the request, including headers and response type.
   * 
   * @returns An object containing HTTP headers and response type configuration.
   */
  private getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      responseType: 'text' as 'json'
    };
  }
}
