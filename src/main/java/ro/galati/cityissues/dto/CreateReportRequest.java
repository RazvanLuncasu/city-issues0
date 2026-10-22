package ro.galati.cityissues.dto;

import jakarta.validation.constraints.*;

public class CreateReportRequest {
    @NotBlank
    private String reporterName;

    @Email @NotBlank
    private String reporterEmail;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String category; // ROADS, LIGHTING, WASTE, PARKS, OTHER

    // opționale
    private Double latitude;
    private Double longitude;
    private String address;

    // NOU: Câmpul pentru urgență
    private boolean isUrgent;

    // getters/setters
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }

    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public boolean isUrgent() { return isUrgent; }
    public void setUrgent(boolean urgent) { isUrgent = urgent; }
}