package com.pgs.microservices.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.pgs.microservices.order.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;
    
    /**
     * Listens for {@link com.pgs.microservices.order.event.OrderPlacedEvent} messages
     * on the "order-placed" Kafka topic and sends an order confirmation email.
     *
     * <p>When a new order event is received, this method:
     * <ul>
     *     <li>Logs the received message.</li>
     *     <li>Generates the email content using {@link #generateEmailMessage(OrderPlacedEvent)}.</li>
     *     <li>Sends the email via {@link org.springframework.mail.javamail.JavaMailSender}.</li>
     *     <li>Logs success or throws a {@link RuntimeException} if email sending fails.</li>
     * </ul></p>
     *
     * @param orderPlacedEvent the event containing customer and order details
     * @throws RuntimeException if there is an error sending the email
     */
    @KafkaListener(topics = "order-placed")
    public void listen(com.pgs.microservices.order.event.OrderPlacedEvent orderPlacedEvent){
        log.info("Got Message from order-placed topic {}", orderPlacedEvent);
        MimeMessagePreparator messagePreparator = generateEmailMessage(orderPlacedEvent);
        try {
            javaMailSender.send(messagePreparator);
            log.info("Order Notifcation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }
    }
    
    /**
     * Generates a {@link MimeMessagePreparator} for sending an order confirmation email.
     *
     * <p>The email is sent to the customer who placed the order, using the details
     * from the provided {@link com.pgs.microservices.order.event.OrderPlacedEvent} object.
     * The email includes the customer's first and last name, email address, and order number.</p>
     *
     * @param orderPlacedEvent the event object containing customer and order details
     * @return a {@link MimeMessagePreparator} that can be used by a {@link org.springframework.mail.javamail.JavaMailSender}
     *         to send the email
     */
	private MimeMessagePreparator generateEmailMessage(
			com.pgs.microservices.order.event.OrderPlacedEvent orderPlacedEvent) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject(
            		String.format(
            				"Your Order with OrderNumber %s is placed successfully", orderPlacedEvent.getOrderNumber()
    				)
    		);
            messageHelper.setText(String.format("""
                            Hi %s,%s

                            Your order with order number %s is now placed successfully.
                            
                            Best Regards
                            Spring Shop
                            """,
                    orderPlacedEvent.getFirstName().toString(),
                    orderPlacedEvent.getLastName().toString(),
                    orderPlacedEvent.getOrderNumber()));
        };
		return messagePreparator;
	}
}