package hei.school.arith.endpoint.rest.model;

import java.time.Instant;
import java.util.UUID;

public record ImageSubmissionResponse(UUID id, String fileName, String email, Instant createdAt) {}
