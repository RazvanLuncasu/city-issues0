package ro.galati.cityissues.repo;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import ro.galati.cityissues.model.*;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByStatus(ReportStatus s, Pageable p);
    Page<Report> findByCategory(Category c, Pageable p);
    Page<Report> findByStatusAndCategory(ReportStatus s, Category c, Pageable p);
    Page<Report> findByFlagReason(FlagReason f, Pageable p);
    Page<Report> findByFlagReasonIsNull(Pageable p);

    // New methods for "sesizările mele" / follows
    List<Report> findByReporter(User reporter);
    List<Report> findByFollowersContains(User follower);
}