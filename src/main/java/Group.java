import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "groups")
public class Group {

    private Integer id;
    private String name;
    private Integer yearOfFoundation;
    private String mainGenre;
    private List<GroupMember> listOfMembers = new ArrayList<>();
    private List<String> repertoire;
    private Integer placeInChart;
    private List<Tour> upcomingTours = new ArrayList<>();
    private Tour currentTour;
    private Tour lastTour;
    
    public Group(String name,Integer yearOfFoundation,String mainGenre) {
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.mainGenre = mainGenre;
    }
    @Column(name = "group_name")
    public String getName() {
        return name;
    }
    public Integer getYearOfFoundation() {
        return yearOfFoundation;
    }
    public List<GroupMember> getListOfMembers() {
        return listOfMembers;
    }
    public List<String> getRepertoire() {
        return repertoire;
    }
    public Integer getPlaceInChart() {
        return placeInChart;
    }
    public List<Tour> getUpcomingTours() {
        return upcomingTours;
    }
    public Tour getCurrentTour() {
        return currentTour;
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public Tour getLastTour() {
        return lastTour;
    }
    public void getReportAboutLastTour() {

    }
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
