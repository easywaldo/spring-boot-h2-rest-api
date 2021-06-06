package com.approval.document.documentapproval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class DocumentApprovalApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DocumentApprovalApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

}
