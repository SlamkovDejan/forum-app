package mk.ukim.finki.emt.forum.usermanagement.aplication;

import mk.ukim.finki.emt.forum.sharedkernel.domain.email.NotifySubscribersMessage;
import mk.ukim.finki.emt.forum.usermanagement.domain.model.User;
import mk.ukim.finki.emt.forum.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.Email;
import mk.ukim.finki.emt.forum.usermanagement.domain.value.UserId;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {

    /*
        Using Gmail SMTP Server
        Need to set real username and password in application.properties for it to work
    */
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender emailSender, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@forum.emt.finki.ukim.mk");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendEmails(NotifySubscribersMessage notifyObj){
        List<User> users = userRepository.findAllById(
                notifyObj.getUserIds().stream().map(UserId::new).collect(Collectors.toList()));

        users.forEach(user -> sendSimpleEmail(user.getEmail().getEmail(), notifyObj.getSubject(), notifyObj.getText()));
    }

}
