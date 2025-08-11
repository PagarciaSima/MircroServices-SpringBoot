/**
 * Represents an order made by a user.
 */
export interface Order {
  /** Unique identifier for the order */
  id?: number;

  /** The order number */
  orderNumber?: string;

  /** SKU code of the product ordered */
  skuCode: string;

  /** Price of a single unit */
  price: number;

  /** Quantity of units ordered */
  quantity: number;

  /** Details of the user who placed the order */
  userDetails: UserDetails;
}

/**
 * Details about a user placing an order.
 */
export interface UserDetails {
  /** User's email address */
  email: string;

  /** User's first name */
  firstName: string;

  /** User's last name */
  lastName: string;
}
