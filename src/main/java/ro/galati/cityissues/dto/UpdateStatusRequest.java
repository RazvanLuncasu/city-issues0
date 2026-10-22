package ro.galati.cityissues.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateStatusRequest {
    @NotBlank
    private String status; // NEW, VALIDATED, IN_PROGRESS, RESOLVED, REJECTED

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}