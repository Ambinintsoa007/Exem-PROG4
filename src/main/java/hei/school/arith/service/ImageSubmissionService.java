package hei.school.arith.service;

import hei.school.arith.endpoint.rest.model.ImageSubmissionResponse;
import hei.school.arith.file.image.ImageValidator;
import hei.school.arith.repository.ImageSubmissionRepository;
import hei.school.arith.repository.model.ImageSubmission;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageSubmissionService {

  private final ImageSubmissionRepository imageSubmissionRepository;
  private final ImageValidator imageValidator;

  @Transactional
  public ImageSubmissionResponse submit(MultipartFile file, String email) {
    imageValidator.validate(file);

    var saved =
        imageSubmissionRepository.save(
            ImageSubmission.builder()
                .id(UUID.randomUUID())
                .fileName(sanitizeFileName(file.getOriginalFilename()))
                .email(email)
                .createdAt(Instant.now())
                .build());

    return toResponse(saved);
  }

  public List<ImageSubmissionResponse> findAll() {
    return imageSubmissionRepository.findAll().stream().map(this::toResponse).toList();
  }

  private String sanitizeFileName(String fileName) {
    if (fileName == null || fileName.isBlank()) {
      return "image";
    }
    return Paths.get(fileName).getFileName().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  private ImageSubmissionResponse toResponse(ImageSubmission imageSubmission) {
    return new ImageSubmissionResponse(
        imageSubmission.getId(),
        imageSubmission.getFileName(),
        imageSubmission.getEmail(),
        imageSubmission.getCreatedAt());
  }
}
