package hei.school.arith.endpoint.rest.model;

public record ImageSubmissionCreated(
    ImageSubmissionResponse response,
    String originalS3Key,
    String blackAndWhiteS3Key,
    String contentType) {}
