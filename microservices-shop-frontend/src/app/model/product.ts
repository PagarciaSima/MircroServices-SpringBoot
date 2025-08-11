/**
 * Represents a product with details such as SKU code, name, description, and price.
 */
export interface Product {
  /**
   * The unique identifier of the product.
   * Optional field.
   */
  id?: string;

  /**
   * The SKU (Stock Keeping Unit) code of the product.
   */
  skuCode: string;

  /**
   * The name of the product.
   */
  name: string;

  /**
   * A detailed description of the product.
   */
  description: string;

  /**
   * The price of the product.
   */
  price: number;
}
