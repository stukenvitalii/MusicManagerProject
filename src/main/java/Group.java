import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "my_groups")
public class Group {

    private Integer id;
    private String name;
    private Integer yearOfFoundation;
    private String mainGenre;
    private List<GroupMember> listOfMembers = new ArrayList<>();
    private List<Song> repertoire;
    private Integer placeInChart;
    private List<Tour> upcomingTours = new ArrayList<>();
    private Tour currentTour;
    private Tour lastTour;

    public Group(){

    }

    @Column(name = "group_name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "group_year_of_foundation")
    public Integer getYearOfFoundation() {
        return yearOfFoundation;
    }
    public void setYearOfFoundation(Integer yearOfFoundation) {
        this.yearOfFoundation = yearOfFoundation;
    }
    @OneToMany
    @JoinColumn(name = "group_id")
    public List<GroupMember> getListOfMembers() {
        return listOfMembers;
    }
    public void setListOfMembers(List<GroupMember> members) {
        this.listOfMembers = members;
    }
    @OneToMany
    @JoinColumn(name = "group_id")
    public List<Song> getRepertoire() {
        return repertoire;
    }
    public void setRepertoire(List<Song> repertoire) {
        this.repertoire = repertoire;
    }
    @Transient
    public Integer getPlaceInChart() {
        return placeInChart;
    }
    public void setPlaceInChart(Integer placeInChart){this.placeInChart = placeInChart;}
    @OneToMany()
    @JoinColumn(name = "group_id")
    public List<Tour> getUpcomingTours() {
        return upcomingTours;
    }
    public void setUpcomingTours(List<Tour> upcomingTours){this.upcomingTours = upcomingTours;}
    @Transient
    public Tour getCurrentTour() {
        return currentTour;
    }
    public void setCurrentTour(Tour currentTour) {this.currentTour = currentTour;}
    @Transient
    public String getMainGenre() {
        return mainGenre;
    }
    public void setMainGenre(String mainGenre) {this.mainGenre = mainGenre;}
    @Transient
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

    public void setLastTour(Tour lastTour) {
        this.lastTour = lastTour;
    }
}
