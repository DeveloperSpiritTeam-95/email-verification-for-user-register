package email.verified.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailVerificationForUserRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailVerificationForUserRegisterApplication.class, args);
		System.out.println("Email Verification Application Running");
	}

}
