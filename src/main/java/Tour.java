import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "tours")
public class Tour {
    private Integer tourId;
    private String name;
    private Date dateOfBeginning;
    private Date dateOfEnd;
    private List<String> cities;

    public Tour() {

    }
    @Column(name = "tour_name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "tour_date_of_beginning")
    @Temporal(TemporalType.DATE)
    public Date getDateOfBeginning() {
        return dateOfBeginning;
    }
    public void setDateOfBeginning(Date dateOfBeginning) {
        this.dateOfBeginning = dateOfBeginning;
    }
    @Column(name = "tour_date_of_end")
    @Temporal(TemporalType.DATE)
    public Date getDateOfEnd() {
        return dateOfEnd;
    }
    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

//    public List<String> getCities() {
//        return cities;
//    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @Id
    @Column(name = "tour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }
}
