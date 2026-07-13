package hei.school.arith.file.image;

import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ImageValidator {

  public ImageType validate(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required");
    }

    var contentType = file.getContentType();
    var fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
    var normalizedFileName = fileName.toLowerCase(Locale.ROOT);

    if ("image/jpeg".equals(contentType)
        && (normalizedFileName.endsWith(".jpg") || normalizedFileName.endsWith(".jpeg"))) {
      return new ImageType("image/jpeg", "jpg", "jpg");
    }

    if ("image/png".equals(contentType) && normalizedFileName.endsWith(".png")) {
      return new ImageType("image/png", "png", "png");
    }

    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST, "Only JPEG and PNG images are accepted");
  }
}
