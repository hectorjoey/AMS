package fhi360.it.assetverify.notification;

import fhi360.it.assetverify.asset.model.Asset;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AssetCreationNotification {
    private final JavaMailSender javaMailSender;

    public void sendAssetCreationNotification(Asset asset) throws MessagingException {
        final MimeMessage msg = javaMailSender.createMimeMessage();
        final MimeMessageHelper messageHelpers = new MimeMessageHelper(msg, true);
        messageHelpers.setTo(asset.getEmailAddress());
//        messageHelpers.addBcc("eankrah@fhi360.org");
        messageHelpers.addCc("schikeluba@epicact2.org");
//        messageHelpers.addCc("uabrahams@fhi360.org");
//        messageHelpers.addCc("ananitelamio@fhi360.org");
        messageHelpers.setSubject("Confirm Asset " + asset.getStatus());

        String htmlContent = "<br><br>Hi " + asset.getAssignee() + ",<br><br>"
                + "An Asset was " + asset.getStatus().toLowerCase() + " under your name. Please find details are below:<br><br>"
                + "<table style='border-collapse: collapse; width: 50%;'>"
                + "<tbody>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Description</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getDescription() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Manufacturer</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getManufacturer() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Asset ID</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getAssetId() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Serial Number</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getSerialNumber() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Model Number</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getModelNumber() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Assignee</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getAssignee() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Email Address</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getEmailAddress() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>State</td><td style='border: 1px solid #ddd; padding: 8px;'>" + asset.getStates() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Status</td><td style='border: 1px solid #ddd; padding: 8px; color: green;'>" + asset.getStatus() + "</td></tr>"
                + "<tr><td style='border: 1px solid #ddd; padding: 8px;'>Approved By</td><td style='border: 1px solid #ddd; padding: 8px; color: green;'>" + asset.getApprovedBy() + "</td></tr>"
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

        messageHelpers.setText(htmlContent, true);
        this.javaMailSender.send(msg);
    }
}
