package hei.school.arith.service.event;

import hei.school.arith.endpoint.event.model.ImageSubmitted;
import hei.school.arith.file.image.BlackAndWhiteImageProcessor;
import hei.school.arith.file.s3.S3ImageStorageService;
import hei.school.arith.mail.Email;
import hei.school.arith.mail.Mailer;
import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageSubmittedService implements Consumer<ImageSubmitted> {

  private final S3ImageStorageService s3ImageStorageService;
  private final BlackAndWhiteImageProcessor blackAndWhiteImageProcessor;
  private final Mailer mailer;

  @SneakyThrows
  @Override
  public void accept(ImageSubmitted imageSubmitted) {
    var originalImage = s3ImageStorageService.download(imageSubmitted.getOriginalS3Key());
    var format = imageSubmitted.getContentType().equals("image/png") ? "png" : "jpg";
    var blackAndWhiteImage = blackAndWhiteImageProcessor.toBlackAndWhite(originalImage, format);

    s3ImageStorageService.upload(
        imageSubmitted.getBlackAndWhiteS3Key(),
        blackAndWhiteImage,
        imageSubmitted.getContentType());

    var blackAndWhiteImageUrl =
        s3ImageStorageService.getUrl(imageSubmitted.getBlackAndWhiteS3Key());

    var email =
        new Email(
            new InternetAddress(imageSubmitted.getEmail()),
            List.of(),
            List.of(),
            "Votre image noir et blanc est prête",
            "<p>Bonjour,</p>"
                + "<p>Votre image <strong>"
                + imageSubmitted.getFileName()
                + "</strong> a été traitée en noir et blanc.</p>"
                + "<p>Lien S3 : <a href=\""
                + blackAndWhiteImageUrl
                + "\">"
                + blackAndWhiteImageUrl
                + "</a></p>",
            List.of());

    mailer.accept(email);
  }
}
