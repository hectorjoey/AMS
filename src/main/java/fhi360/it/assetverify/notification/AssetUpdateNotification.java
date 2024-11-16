
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
public class AssetUpdateNotification {
    private final JavaMailSender javaMailSender;

    public void sendAssetUpdateNotification(final Asset asset) throws MessagingException {
        final MimeMessage msg = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, true);
        mimeMessageHelper.setTo(asset.getEmailAddress());
        mimeMessageHelper.addCc("schikeluba@epicact2.org");
        mimeMessageHelper.setSubject("Confirm Asset " + asset.getStatus());

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<br><br>Hi ").append(asset.getAssignee()).append(",<br><br>")
                .append("An Asset was ").append(asset.getStatus().toLowerCase()).append(" under your name. Please find details are below:<br><br>")
                .append("<table style='border-collapse: collapse; width: 50%;'>")
                .append("<tbody>");

        appendRow(htmlContent, "Description", asset.getDescription());
        appendRow(htmlContent, "Manufacturer", asset.getManufacturer());
        appendRow(htmlContent, "Asset ID", asset.getAssetId());
        appendRow(htmlContent, "Serial Number", asset.getSerialNumber());
        appendRow(htmlContent, "Model Number", asset.getModelNumber());
        appendRow(htmlContent, "Assignee", asset.getAssignee());
        appendRow(htmlContent, "Email Address", asset.getEmailAddress());
        appendRow(htmlContent, "Country", asset.getCountry());
        appendRow(htmlContent, "States", asset.getStates());
        appendRow(htmlContent, "Date", String.valueOf(LocalDate.now()));
        appendRow(htmlContent, "Status", asset.getStatus());
        appendRow(htmlContent, "Approved By", asset.getApprovedBy());

        htmlContent.append("</tbody></table>")
                .append("<br>Thank you,<br>")
                .append("for Support contact: globalservicedesk@fhi360.org<br><br>")
                .append("<img src='https://www.atachcommunity.com/fileadmin/_processed_/f/1/csm_FHI360_logo_full_rgb_-_Asia_Pacific_Regional_Office_94475b8c42.png' alt='FHI360 Logo' style='width: 100px; height: auto; margin=20px;'><br><br>")
                .append("<a href='https://www.fhi360.org'>fhi360.org</a> | ")
                .append("<a href='https://www.facebook.com/fhi360'>FHI 360 Facebook</a> | ")
                .append("<a href='https://x.com/fhi360'>FHI 360 Twitter</a><br><br>")
                .append("<a href='https://connect.fhi360.org/sites/faq-portal/SitePageModern/12138/faq-portal'>Search F.A.Q Portal for all your IT issues >>> Search now</a><br><br>")
                .append("<a href='https://servicehub.fhi360.org/'>Explore the Service Hub for IT resources >>> Explore here</a><br><br>");

        mimeMessageHelper.setText(htmlContent.toString(), true);
        this.javaMailSender.send(msg);
    }

    private void appendRow(StringBuilder htmlContent, String label, String value) {
        if (value != null && !value.isEmpty()) {
            htmlContent.append("<tr><td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(label)
                    .append("</td><td style='border: 1px solid #ddd; padding: 8px;'>")
                    .append(value)
                    .append("</td></tr>");
        }
    }
}