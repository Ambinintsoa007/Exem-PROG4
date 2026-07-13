package hei.school.arith.repository;

import hei.school.arith.repository.model.ImageSubmission;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageSubmissionRepository extends JpaRepository<ImageSubmission, UUID> {}
