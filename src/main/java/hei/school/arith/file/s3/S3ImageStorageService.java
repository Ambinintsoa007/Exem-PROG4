package hei.school.arith.file.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ImageStorageService {

  private final S3Client s3Client;
  private final String bucket;

  public S3ImageStorageService(S3Client s3Client, @Value("${aws.s3.bucket:}") String bucket) {
    this.s3Client = s3Client;
    this.bucket = bucket;
  }

  private void assertBucketConfigured() {
    if (bucket == null || bucket.isBlank()) {
      throw new IllegalStateException("S3 bucket is not configured");
    }
  }

  public void upload(String key, byte[] content, String contentType) {
    assertBucketConfigured();

    var request =
        PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentType).build();
    s3Client.putObject(request, RequestBody.fromBytes(content));
  }

  public byte[] download(String key) {
    assertBucketConfigured();

    var request = GetObjectRequest.builder().bucket(bucket).key(key).build();
    return s3Client.getObjectAsBytes(request).asByteArray();
  }

  public String getUrl(String key) {
    assertBucketConfigured();

    return s3Client
        .utilities()
        .getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build())
        .toExternalForm();
  }
}
