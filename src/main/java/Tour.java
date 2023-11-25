import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

@Entity
@Table(name = "tours")
public class Tour {
    public Tour() {
    }

    @Id
    @Column(name = "tour_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tourId;
    @Column(name = "tour_name")
    private String name;
    @Column(name = "tour_date_of_beginning")
    @Temporal(TemporalType.DATE)
    private Date dateOfBeginning;
    @Column(name = "tour_date_of_end")
    @Temporal(TemporalType.DATE)
    private Date dateOfEnd;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBeginning() {
        return dateOfBeginning;
    }

    public void setDateOfBeginning(Date dateOfBeginning) {
        this.dateOfBeginning = dateOfBeginning;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(Date dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }


    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public StringProperty dateOfBeginningProperty() {
        return new SimpleStringProperty(dateOfBeginning.toString());
    }

    public StringProperty dateOfEndProperty() {
        return new SimpleStringProperty(dateOfEnd.toString());
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }
}
