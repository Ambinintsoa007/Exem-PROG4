package hei.school.arith.service;

import hei.school.arith.endpoint.rest.model.ImageSubmissionCreated;
import hei.school.arith.endpoint.rest.model.ImageSubmissionResponse;
import hei.school.arith.file.image.ImageValidator;
import hei.school.arith.file.s3.S3ImageStorageService;
import hei.school.arith.repository.ImageSubmissionRepository;
import hei.school.arith.repository.model.ImageSubmission;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageSubmissionService {

  private final ImageSubmissionRepository imageSubmissionRepository;
  private final ImageValidator imageValidator;
  private final S3ImageStorageService s3ImageStorageService;

  @SneakyThrows
  @Transactional
  public ImageSubmissionCreated submit(MultipartFile file, String email) {
    var imageType = imageValidator.validate(file);
    var id = UUID.randomUUID();
    var fileName = sanitizeFileName(file.getOriginalFilename());
    var createdAt = Instant.now();
    var originalKey = "images/original/" + id + "-" + fileName;
    var blackAndWhiteKey = "images/black-and-white/" + id + "-bw." + imageType.extension();

    var saved =
        imageSubmissionRepository.save(
            ImageSubmission.builder()
                .id(id)
                .fileName(fileName)
                .email(email)
                .createdAt(createdAt)
                .build());

    s3ImageStorageService.upload(originalKey, file.getBytes(), imageType.contentType());

    return new ImageSubmissionCreated(
        toResponse(saved), originalKey, blackAndWhiteKey, imageType.contentType());
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
