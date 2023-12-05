import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "my_groups")
public class Group {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "group_name")
    private String name;
    @Column(name = "group_year_of_foundation")
    private Integer yearOfFoundation;
    @Column(name = "group_main_genre")
    private String mainGenre;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<GroupMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Song> songs = new ArrayList<>();
    @Column(name = "group_place_in_chart")
    private Integer placeInChart;
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Tour> tours = new ArrayList<>();

    public Group() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYearOfFoundation() {
        return yearOfFoundation;
    }

    public void setYearOfFoundation(Integer yearOfFoundation) {
        this.yearOfFoundation = yearOfFoundation;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Integer getPlaceInChart() {
        return placeInChart;
    }

    public void setPlaceInChart(Integer placeInChart) {
        this.placeInChart = placeInChart;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public void setMainGenre(String mainGenre) {
        this.mainGenre = mainGenre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
