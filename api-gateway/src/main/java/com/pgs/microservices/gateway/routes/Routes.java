package com.pgs.microservices.gateway.routes;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Value("${product.service.url}")
    private String productServiceUrl;
    @Value("${order.service.url}")
    private String orderServiceUrl;
    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    /**
     * Configures a router function for the Product Service within the API Gateway.
     * <p>
     * This route listens for requests to the path "/api/product" and proxies them
     * to the backend Product Service URL defined by {@code productServiceUrl}.
     * <p>
     * A Circuit Breaker filter is applied to handle backend service failures gracefully.
     * If the Product Service is unavailable, the request is forwarded to a fallback route
     * ("/fallbackRoute") within the gateway.
     *
     * @return a {@link RouterFunction} that handles product service routing with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> productServiceRoute() {
        return GatewayRouterFunctions.route("product_service") // Define route group name
            .route(
                RequestPredicates.path("/api/product"),         // Match requests with path "/api/product"
                HandlerFunctions.http(productServiceUrl)         // Proxy these requests to productServiceUrl backend
            )
            .filter(
                CircuitBreakerFilterFunctions.circuitBreaker(   // Apply a circuit breaker filter
                    "productServiceCircuitBreaker",              // Circuit breaker instance name
                    URI.create("forward:/fallbackRoute")         // Fallback route in case of failure
                )
            )
            .build();                                           // Build and return the router function
    }

    /**
     * Configures a router function to proxy the Swagger/OpenAPI documentation
     * of the Product Service through the API Gateway.
     * <p>
     * This route listens for requests at "/aggregate/product-service/v3/api-docs"
     * and forwards them to the backend Product Service's Swagger endpoint.
     * <p>
     * A Circuit Breaker filter is applied to handle failures gracefully,
     * forwarding to a fallback route ("/fallbackRoute") if the Product Service is unavailable.
     * <p>
     * Additionally, a filter is applied to modify the path of the request to "/api-docs"
     * before forwarding, to conform with the expected Swagger endpoint path.
     *
     * @return a {@link RouterFunction} that handles proxying of the Product Service Swagger docs with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("product_service_swagger") // Define route group name
            .route(
                RequestPredicates.path("/aggregate/product-service/v3/api-docs"), // Match requests for swagger docs
                HandlerFunctions.http(productServiceUrl)                           // Proxy requests to backend product service
            )
            .filter(
                CircuitBreakerFilterFunctions.circuitBreaker(                     // Apply circuit breaker filter
                    "productServiceSwaggerCircuitBreaker",                        // Circuit breaker instance name
                    URI.create("forward:/fallbackRoute")                          // Fallback route in case of failure
                )
            )
            .filter(setPath("/api-docs"))                                        // Modify request path before forwarding
            .build();                                                           // Build and return the router function
    }

    /**
     * Configures a router function for the Order Service within the API Gateway.
     * <p>
     * This route listens for HTTP requests to the path "/api/order" and proxies
     * them to the backend Order Service URL defined by {@code orderServiceUrl}.
     * <p>
     * A Circuit Breaker filter is applied to provide fault tolerance. If the Order Service
     * is unavailable or fails, the request is forwarded to a fallback route ("/fallbackRoute").
     *
     * @return a {@link RouterFunction} that handles routing to the Order Service with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> orderServiceRoute() {
        return GatewayRouterFunctions.route("order_service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http(orderServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    /**
     * Configures a router function to proxy the Swagger/OpenAPI documentation
     * of the Order Service through the API Gateway.
     * <p>
     * This route listens for requests at "/aggregate/order-service/v3/api-docs"
     * and forwards them to the backend Order Service's Swagger endpoint.
     * <p>
     * A Circuit Breaker filter is applied to handle failures gracefully,
     * forwarding to a fallback route ("/fallbackRoute") if the Order Service is unavailable.
     * <p>
     * Additionally, the route applies a filter to modify the request path to "/api-docs"
     * before forwarding, matching the expected Swagger endpoint path.
     *
     * @return a {@link RouterFunction} that handles proxying of the Order Service Swagger docs with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"), HandlerFunctions.http(orderServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    /**
     * Configures a router function for the Inventory Service within the API Gateway.
     * <p>
     * This route listens for HTTP requests to the path "/api/inventory" and proxies
     * them to the backend Inventory Service URL defined by {@code inventoryServiceUrl}.
     * <p>
     * A Circuit Breaker filter is applied to provide fault tolerance. If the Inventory Service
     * is unavailable or fails, the request is forwarded to a fallback route ("/fallbackRoute").
     *
     * @return a {@link RouterFunction} that handles routing to the Inventory Service with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> inventoryServiceRoute() {
        return GatewayRouterFunctions.route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http(inventoryServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    /**
     * Configures a router function to proxy the Swagger/OpenAPI documentation
     * of the Inventory Service through the API Gateway.
     * <p>
     * This route listens for requests at "/aggregate/inventory-service/v3/api-docs"
     * and forwards them to the backend Inventory Service's Swagger endpoint.
     * <p>
     * A Circuit Breaker filter is applied to handle failures gracefully,
     * forwarding to a fallback route ("/fallbackRoute") if the Inventory Service is unavailable.
     * <p>
     * Additionally, the route applies a filter to modify the request path to "/api-docs"
     * before forwarding, matching the expected Swagger endpoint path.
     *
     * @return a {@link RouterFunction} that handles proxying of the Inventory Service Swagger docs with resilience.
     */
    @Bean
    RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), HandlerFunctions.http(inventoryServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    /**
     * Defines a fallback route that is used by the circuit breaker filters when
     * the proxied services are unavailable or fail.
     * <p>
     * When the "/fallbackRoute" endpoint is called, it returns an HTTP 503 Service Unavailable
     * response with a simple message advising the client to try again later.
     *
     * @return a {@link RouterFunction} handling the fallback response for service failures.
     */
    @Bean
    RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }
}