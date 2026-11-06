package org.rewear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReWearApplication {

    public static void main(String[] args) {
        System.out.println("\n============================================================");
        System.out.println("  [ReWear] Java Spring Boot Sustainable Fashion Platform");
        System.out.println("  Access the platform at: http://127.0.0.1:5000");
        System.out.println("  Demo Admin Login:  admin@rewear.org / admin123");
        System.out.println("  Demo User Logins:  sarah@example.com / user123");
        System.out.println("                     david@example.com / user123");
        System.out.println("============================================================\n");

        SpringApplication.run(ReWearApplication.class, args);
    }
}
