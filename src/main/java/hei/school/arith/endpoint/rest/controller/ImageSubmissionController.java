package hei.school.arith.endpoint.rest.controller;

import hei.school.arith.endpoint.event.EventProducer;
import hei.school.arith.endpoint.event.model.ImageSubmitted;
import hei.school.arith.endpoint.rest.model.ImageSubmissionResponse;
import hei.school.arith.service.ImageSubmissionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ImageSubmissionController {

  private final ImageSubmissionService imageSubmissionService;
  private final EventProducer<ImageSubmitted> eventProducer;

  @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ImageSubmissionResponse submit(
      @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
    var created = imageSubmissionService.submit(file, email);

    var event =
        ImageSubmitted.builder()
            .email(email)
            .fileName(created.response().fileName())
            .originalS3Key(created.originalS3Key())
            .blackAndWhiteS3Key(created.blackAndWhiteS3Key())
            .contentType(created.contentType())
            .build();

    eventProducer.accept(List.of(event));

    return created.response();
  }

  @GetMapping("/images")
  public List<ImageSubmissionResponse> findAll() {
    return imageSubmissionService.findAll();
  }
}
