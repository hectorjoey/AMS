package fhi360.it.assetverify.notification;

import fhi360.it.assetverify.user.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class UserAccountCreationNotification {
    private final JavaMailSender javaMailSender;

    public void sendAccountCreationNotification(Users users) throws MessagingException {
        final MimeMessage msg = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true);
        mimeMessageHelper.setTo(users.getEmail());
//        mimeMessageHelper.addBcc("eankrah@fhi360.org");
        mimeMessageHelper.addCc("schikeluba@epicact2.org");
//        mimeMessageHelper.addCc("uabrahams@fhi360.org");
//        mimeMessageHelper.addCc("ananitelamio@fhi360.org");
        mimeMessageHelper.setSubject("Account Creation for " + users.getFirstname() + " " + users.getLastname());

        String htmlContent = "<br><br>Hi " + users.getFirstname() + "  " + users.getLastname() + ",<br>"
                + "Welcome to FHI360 Asset Management System, Your Account Has Been Created successfully" + "<br>"
                + "Below are your account details: "
                + "<table style='border-collapse: collapse; width: 100%;'>"
                + "<tbody>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Date</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getCreatedDate() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>First Name</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getFirstname() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Last Name</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getLastname() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Email Address</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getEmail() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Department</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getDepartment() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Country</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getCountry() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>State</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getStates() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Role</td><td style='border: 1px solid #ddd; padding: 8px;'>" + users.getRole() + "</td></tr>"
                + "</tbody>"
                + "</table>"
                + "<br>Thank you,<br>"
                + "for Support contact: globalservicedesk@fhi360.org<br><br>"
                + "<img src='https://www.atachcommunity.com/fileadmin/_processed_/f/1/csm_FHI360_logo_full_rgb_-_Asia_Pacific_Regional_Office_94475b8c42.png' alt='FHI360 Logo' style='width: 100px; height: auto; margin=20px;'><br><br>"
                + "<a href='https://www.fhi360.org'>fhi360.org</a> | "
                + "<a href='https://www.facebook.com/fhi360'>FHI 360 Facebook</a> | "
                + "<a href='https://x.com/fhi360'>FHI 360 Twitter</a><br><br>"
                + "<a href='https://connect.fhi360.org/sites/faq-portal/SitePageModern/12138/faq-portal'>Search F.A.Q Portal for all your IT issues >>> Search now</a><br><br>"
                + "<a href='https://servicehub.fhi360.org/'>Explore the Service Hub for IT resources >>> Explore here</a><br><br>";

        mimeMessageHelper.setText(htmlContent, true);
        this.javaMailSender.send(msg);
    }
}