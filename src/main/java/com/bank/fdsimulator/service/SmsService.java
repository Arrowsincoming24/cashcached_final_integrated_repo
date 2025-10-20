package com.bank.fdsimulator.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    
    @Value("${twilio.account-sid:your-twilio-account-sid}")
    private String accountSid;
    
    @Value("${twilio.auth-token:your-twilio-auth-token}")
    private String authToken;
    
    @Value("${twilio.phone-number:your-twilio-phone-number}")
    private String fromPhoneNumber;
    
    private boolean twilioEnabled = false;
    
    @PostConstruct
    public void init() {
        // Only initialize Twilio if valid credentials are provided
        if (accountSid != null && !accountSid.isEmpty() && 
            !accountSid.startsWith("your-") && 
            authToken != null && !authToken.isEmpty() && 
            !authToken.startsWith("your-") &&
            fromPhoneNumber != null && !fromPhoneNumber.isEmpty() && 
            !fromPhoneNumber.startsWith("your-")) {
            
            try {
                Twilio.init(accountSid, authToken);
                twilioEnabled = true;
                logger.info("Twilio initialized successfully with phone number: {}", fromPhoneNumber);
            } catch (Exception e) {
                logger.error("Failed to initialize Twilio: {}", e.getMessage());
                twilioEnabled = false;
            }
        } else {
            logger.warn("Twilio is not configured. SMS functionality will be disabled. " +
                       "Please set TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN, and TWILIO_PHONE_NUMBER environment variables.");
            twilioEnabled = false;
        }
    }
    
    public void sendOtp(String phoneNumber, String otp) {
        if (!twilioEnabled) {
            logger.warn("Twilio is not enabled. OTP would be sent to {}: {}", phoneNumber, otp);
            // In development mode, just log the OTP instead of throwing an error
            System.out.println("=================================================");
            System.out.println("DEVELOPMENT MODE - OTP NOT SENT VIA SMS");
            System.out.println("Phone: " + phoneNumber);
            System.out.println("OTP: " + otp);
            System.out.println("=================================================");
            return;
        }
        
        try {
            String messageBody = "Your OTP for Fixed Deposit Simulator is: " + otp + 
                               ". This OTP is valid for 5 minutes. Do not share it with anyone.";
            
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody
            ).create();
            
            logger.info("SMS sent successfully. SID: {}", message.getSid());
        } catch (Exception e) {
            logger.error("Error sending SMS: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP", e);
        }
    }
    
    public void sendNotification(String phoneNumber, String message) {
        if (!twilioEnabled) {
            logger.warn("Twilio is not enabled. Notification would be sent to {}: {}", phoneNumber, message);
            // In development mode, just log the notification instead of throwing an error
            System.out.println("=================================================");
            System.out.println("DEVELOPMENT MODE - NOTIFICATION NOT SENT VIA SMS");
            System.out.println("Phone: " + phoneNumber);
            System.out.println("Message: " + message);
            System.out.println("=================================================");
            return;
        }
        
        try {
            Message smsMessage = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();
            
            logger.info("Notification sent successfully. SID: {}", smsMessage.getSid());
        } catch (Exception e) {
            logger.error("Error sending notification: {}", e.getMessage());
            throw new RuntimeException("Failed to send notification", e);
        }
    }
    
    public boolean isTwilioEnabled() {
        return twilioEnabled;
    }
}
