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

    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){
        log.info("Got Message from order-placed topic {}", orderPlacedEvent);
        MimeMessagePreparator messagePreparator = generateOrderEmail(orderPlacedEvent);
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
     * <p>
     * The email will be sent from "springshop@email.com" to the email address
     * provided in the {@link OrderPlacedEvent}. The subject of the email will
     * indicate the order number, and the body will greet the customer by their
     * first and last name and confirm that their order has been placed successfully.
     *
     * @param orderPlacedEvent the event containing order details and customer information
     * @return a {@link MimeMessagePreparator} configured to send the order confirmation email
     */
	private MimeMessagePreparator generateOrderEmail(OrderPlacedEvent orderPlacedEvent) {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Your Order with OrderNumber %s is placed successfully", orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                            Hi

                            Your order with order number %s is now placed successfully.
                            
                            Best Regards
                            Spring Shop
                            """,

                    orderPlacedEvent.getOrderNumber()));
        };
		return messagePreparator;
	}
}