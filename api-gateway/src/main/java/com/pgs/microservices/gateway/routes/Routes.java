package com.pgs.microservices.gateway.routes;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    private static final URI PRODUCT_SERVICE_URI = URI.create("http://localhost:8080");
	private static final String PRODUCT_SERVICE_PATH = "/api/product";
	private static final String PRODUCT_SERVICE_ID = "product-service";
	
	private static final URI ORDER_SERVICE_URI = URI.create("http://localhost:8081");
	private static final String ORDER_SERVICE_PATH = "/api/order";
	private static final String ORDER_SERVICE_ID = "order-service";
	
	private static final URI INVENTORY_SERVICE_URI = URI.create("http://localhost:8082");
	private static final String INVENTORY_SERVICE_PATH = "/api/inventory";
	private static final String INVENTORY_SERVICE_ID = "inventory-service";
	
	/**
	 * Defines a route for forwarding requests to the Product Service.
	 * <p>
	 * This method sets up a Spring Cloud Gateway MVC route that matches incoming HTTP requests
	 * with the path {@code /api/product} and forwards them to the Product Service located at
	 * {@code http://localhost:8080}. The target URI is manually set on the request using
	 * {@link MvcUtils#setRequestUrl}, and the request is then handled by the default
	 * HTTP handler function provided by {@link HandlerFunctions#http()}.
	 * </p>
	 *
	 * @return a {@link RouterFunction} that handles routing to the Product Service
	 */
	@Bean
	RouterFunction<ServerResponse> productServiceRoute() {
	    return GatewayRouterFunctions.route(PRODUCT_SERVICE_ID)
	        .route(RequestPredicates.path(PRODUCT_SERVICE_PATH),
	            request -> {
	                MvcUtils.setRequestUrl(request, PRODUCT_SERVICE_URI);
	                return HandlerFunctions.http().handle(request);
	            })
	        .build();
	}
	
	/**
	 * Defines a route for forwarding requests to the Order Service.
	 * <p>
	 * This method sets up a Spring Cloud Gateway MVC route that matches incoming HTTP requests
	 * with the path {@code /api/order} and forwards them to the Order Service located at
	 * {@code http://localhost:8081}. The target URI is manually set on the request using
	 * {@link MvcUtils#setRequestUrl}, and the request is then handled by the default
	 * HTTP handler function provided by {@link HandlerFunctions#http()}.
	 * </p>
	 *
	 * @return a {@link RouterFunction} that handles routing to the Order Service
	 */
	@Bean
	RouterFunction<ServerResponse> orderServiceRoute() {
	    return GatewayRouterFunctions.route(ORDER_SERVICE_ID)
	        .route(RequestPredicates.path(ORDER_SERVICE_PATH),
	            request -> {
	                MvcUtils.setRequestUrl(request, ORDER_SERVICE_URI);
	                return HandlerFunctions.http().handle(request);
	            })
	        .build();
	}
	
	/**
	 * Defines a route for forwarding requests to the Inventory Service.
	 * <p>
	 * This method sets up a Spring Cloud Gateway MVC route that matches incoming HTTP requests
	 * with the path {@code /api/inventory} and forwards them to the Inventory Service located at
	 * {@code http://localhost:8082}. The target URI is manually set on the request using
	 * {@link MvcUtils#setRequestUrl}, and the request is then handled by the default
	 * HTTP handler function provided by {@link HandlerFunctions#http()}.
	 * </p>
	 *
	 * @return a {@link RouterFunction} that handles routing to the Inventory Service
	 */
	@Bean
	RouterFunction<ServerResponse> inventoryServiceRoute() {
	    return GatewayRouterFunctions.route(INVENTORY_SERVICE_ID)
	        .route(RequestPredicates.path(INVENTORY_SERVICE_PATH),
	            request -> {
	                MvcUtils.setRequestUrl(request, INVENTORY_SERVICE_URI);
	                return HandlerFunctions.http().handle(request);
	            })
	        .build();
	}
}
