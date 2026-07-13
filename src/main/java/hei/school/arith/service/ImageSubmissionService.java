package hei.school.arith.service;

import hei.school.arith.endpoint.rest.model.ImageSubmissionResponse;
import hei.school.arith.repository.ImageSubmissionRepository;
import hei.school.arith.repository.model.ImageSubmission;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageSubmissionService {

  private final ImageSubmissionRepository imageSubmissionRepository;

  public List<ImageSubmissionResponse> findAll() {
    return imageSubmissionRepository.findAll().stream().map(this::toResponse).toList();
  }

  private ImageSubmissionResponse toResponse(ImageSubmission imageSubmission) {
    return new ImageSubmissionResponse(
        imageSubmission.getId(),
        imageSubmission.getFileName(),
        imageSubmission.getEmail(),
        imageSubmission.getCreatedAt());
  }
}
