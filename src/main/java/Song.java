import jakarta.persistence.*;

@Entity
@Table(name = "songs")
public class Song {
    @Column(name = "song_id")
    private Integer id;
    @Column(name = "song_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    private Integer duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "song_duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
