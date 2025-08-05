package com.pgs.microservices.order.exception;

public class ProductNotInStockException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductNotInStockException(String skuCode) {
        super("Product with skuCode " + skuCode + " is not in stock");
    }
}
