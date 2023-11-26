import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

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
    private LocalDate dateOfBeginning;
    @Column(name = "tour_date_of_end")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfEnd;

    public void setGroup(Group group) {
        this.group = group;
    }

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBeginning() {
        return dateOfBeginning;
    }

    public void setDateOfBeginning(LocalDate dateOfBeginning) {
        this.dateOfBeginning = dateOfBeginning;
    }

    public LocalDate getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(LocalDate dateOfEnd) {
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
