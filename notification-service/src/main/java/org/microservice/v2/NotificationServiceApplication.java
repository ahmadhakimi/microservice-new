package org.microservice.v2;

import lombok.extern.slf4j.Slf4j;
import org.microservice.v2.event.OrderPlacedEvent;

import org.microservice.v2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.logging.Logger;


@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    @Autowired
    private EmailService emailService;

    public static void main (String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    //add kafka listener annotation
    @KafkaListener(topics = "notificationTopic" , groupId = "notificationId")

    //method take the OrderPlacedEvent object as the parameter
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {

        //sending out the email notification
        String orderNumber = orderPlacedEvent.getOrderNumber();
        String email = "hakimihakimi611@gmail.com"; // Replace with the recipient's email
        String subject = "Order Placed Successfully";
        String message = "Your order with number " + orderNumber + " has been successfully placed.";
        emailService.sendEmail(email, subject, message);

        log.info("Received notification for Order Number: {}", orderPlacedEvent.getOrderNumber());
        //give logs if the notification success
    }
}
