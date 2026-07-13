package hei.school.arith.endpoint.rest.controller;

import hei.school.arith.endpoint.rest.model.ImageSubmissionResponse;
import hei.school.arith.service.ImageSubmissionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ImageSubmissionController {

  private final ImageSubmissionService imageSubmissionService;

  @GetMapping("/images")
  public List<ImageSubmissionResponse> findAll() {
    return imageSubmissionService.findAll();
  }
}
