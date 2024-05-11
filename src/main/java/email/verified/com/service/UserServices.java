package email.verified.com.service;

import email.verified.com.entity.Users;
import email.verified.com.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServices {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JavaMailSender mailSender;

    public List<Users> listAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void register(@NotNull Users users, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);

//        String randomCode = RandomString.make(64);
        String randomCode = RandomStringUtils.randomAlphanumeric(64);
        users.setVerificationCode(randomCode);
        users.setEnabled(false);

        userRepository.save(users);

        sendVerificationEmail(users, siteURL);
    }


    private void sendVerificationEmail(@NotNull Users user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
//        String fromAddress = "your email address";
//        String senderName = "your company name";
        String fromAddress = "no-reply@jvm.my.id";
        String senderName = "PRABHAKAR K";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "SYNYCS Private Ltd.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

        log.info("Email has been sent");
    }

    public boolean verify(String verificationCode) {
        Users user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }
}
