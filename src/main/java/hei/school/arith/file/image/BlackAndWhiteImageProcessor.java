package hei.school.arith.file.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.springframework.stereotype.Component;

@Component
public class BlackAndWhiteImageProcessor {

  public byte[] toBlackAndWhite(byte[] originalImage, String imageIoFormat) {
    try {
      var source = ImageIO.read(new ByteArrayInputStream(originalImage));
      if (source == null) {
        throw new IllegalArgumentException("Unsupported image content");
      }

      var blackAndWhite =
          new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D graphics = blackAndWhite.createGraphics();
      graphics.drawImage(source, 0, 0, null);
      graphics.dispose();

      var output = new ByteArrayOutputStream();
      ImageIO.write(blackAndWhite, imageIoFormat, output);
      return output.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Unable to convert image to black and white", e);
    }
  }
}
