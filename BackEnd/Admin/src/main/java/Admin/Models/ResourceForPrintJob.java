package Admin.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_for_print_job")
public class ResourceForPrintJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "resource_property_detail_id", foreignKey = @ForeignKey(name = "fk_detail_resourceforprintjob"))
    @JsonIgnoreProperties("resourcePropertyDetail")
    private ResourcePropertyDetail resourcePropertyDetail;

    @ManyToOne
    @JoinColumn(name = "print_job_id", foreignKey = @ForeignKey(name = "fk_resource_print"))
    @JsonIgnoreProperties("printJobs")
    private PrintJobs printJobs;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResourcePropertyDetail getResourcePropertyDetail() {
        return resourcePropertyDetail;
    }

    public void setResourcePropertyDetail(ResourcePropertyDetail resourcePropertyDetail) {
        this.resourcePropertyDetail = resourcePropertyDetail;
    }

    public PrintJobs getPrintJobs() {
        return printJobs;
    }

    public void setPrintJobs(PrintJobs printJobs) {
        this.printJobs = printJobs;
    }
}
