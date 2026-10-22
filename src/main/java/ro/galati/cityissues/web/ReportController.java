package ro.galati.cityissues.web;

import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import ro.galati.cityissues.model.*;
import ro.galati.cityissues.service.ReportService;
import ro.galati.cityissues.repo.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;
    private final UserRepository userRepo;

    public ReportController(ReportService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping
    public Page<Report> list(@RequestParam(required=false) String status,
                             @RequestParam(required=false) String category,
                             @RequestParam(required=false) String flag,
                             @RequestParam(defaultValue="0") int page) {
        return service.search(status, category, flag,
                PageRequest.of(page, 50, Sort.by(Sort.Direction.DESC, "urgent").and(Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    // --- Funcționalitatea recuperată din Proiectul 1 ---
    @GetMapping("/my")
    public List<Report> myReports(Principal principal) {
        return service.getMyReportsAndFollows(principal.getName(), userRepo);
    }

    @PostMapping("/{id}/follow")
    public Report follow(@PathVariable Long id, Principal p) {
        User u = userRepo.findByEmail(p.getName()).orElseThrow();
        return service.followReport(id, u);
    }

    @PatchMapping("/{id}/status")
    public Report updateStatus(@PathVariable Long id, @RequestBody ro.galati.cityissues.dto.UpdateStatusRequest req) {
        return service.updateStatus(id, ro.galati.cityissues.model.ReportStatus.valueOf(req.getStatus()));
    }

    @PatchMapping("/{id}/assign")
    public Report assign(@PathVariable Long id, @RequestBody ro.galati.cityissues.dto.AssignRequest req) {
        return service.assign(id, req.getDepartment());
    }

    @PatchMapping("/{id}/response")
    public Report resp(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.addResponse(id, body.get("message"));
    }

    @PatchMapping("/{id}/flag")
    public Report flag(@PathVariable Long id, @RequestBody Map<String, String> b) {
        String r = b.get("reason");
        return service.flagReport(id, (r==null||r.isEmpty()||r.equals("null")) ? null : FlagReason.valueOf(r));
    }

    @PostMapping
    public Report create(@RequestBody Report r, Principal p) {
        User u = userRepo.findByEmail(p.getName()).orElseThrow();
        return service.create(r, u);
    }

    @PostMapping("/{id}/vote")
    public void vote(@PathVariable Long id) {
        service.vote(id);
    }
}