package ro.galati.cityissues.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.galati.cityissues.model.*;
import ro.galati.cityissues.repo.*;

import java.util.*;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepo;
    private final NotificationRepository notifRepo;

    public ReportService(ReportRepository reportRepo, NotificationRepository notifRepo) {
        this.reportRepo = reportRepo;
        this.notifRepo = notifRepo;
    }

    public Page<Report> search(String status, String category, String flag, Pageable pageable) {
        if ("NONE".equals(flag)) return reportRepo.findByFlagReasonIsNull(pageable);
        if (flag != null && !flag.isEmpty()) return reportRepo.findByFlagReason(FlagReason.valueOf(flag), pageable);

        ReportStatus s = (status != null && !status.isEmpty()) ? ReportStatus.valueOf(status) : null;
        Category c = (category != null && !category.isEmpty()) ? Category.valueOf(category) : null;

        if (s != null && c != null) return reportRepo.findByStatusAndCategory(s, c, pageable);
        if (s != null) return reportRepo.findByStatus(s, pageable);
        if (c != null) return reportRepo.findByCategory(c, pageable);
        return reportRepo.findAll(pageable);
    }

    public Report followReport(Long id, User user) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.getFollowers().add(user);
        return reportRepo.save(r);
    }

    private void notifyInterested(Report r, String msg) {
        notifRepo.save(new Notification(msg, r.getReporter()));
        r.getFollowers().forEach(f -> {
            if (!f.getId().equals(r.getReporter().getId()))
                notifRepo.save(new Notification(msg, f));
        });
    }

    public Report updateStatus(Long id, ReportStatus s) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.setStatus(s);
        r.setFlagReason(null);
        notifyInterested(r, "Status nou: '" + r.getTitle() + "' este acum " + s);
        return reportRepo.save(r);
    }

    public Report addResponse(Long id, String text) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.setOperatorResponse(text);
        notifyInterested(r, "Răspuns oficial pentru: " + r.getTitle());
        return reportRepo.save(r);
    }

    public Report assign(Long id, String dept) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.setAssignedDepartment(dept);
        notifyInterested(r, "Sesizarea '" + r.getTitle() + "' trimisă la " + dept);
        return reportRepo.save(r);
    }

    public Report flagReport(Long id, FlagReason reason) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.setFlagReason(reason);
        return reportRepo.save(r);
    }

    public Report create(Report r, User u) {
        r.setReporter(u);
        return reportRepo.save(r);
    }

    public void vote(Long id) {
        Report r = reportRepo.findById(id).orElseThrow();
        r.setVotes(r.getVotes() + 1);
        reportRepo.save(r);
    }

    // ✅ NEW: Get my reports and followed reports
    public List<Report> getMyReportsAndFollows(String email, UserRepository userRepo) {
        User u = userRepo.findByEmail(email).orElseThrow();
        List<Report> created = reportRepo.findByReporter(u);
        List<Report> followed = reportRepo.findByFollowersContains(u);

        Set<Report> all = new LinkedHashSet<>();
        all.addAll(created);
        all.addAll(followed);

        return new ArrayList<>(all);
    }
}