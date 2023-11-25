import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @Column(name = "song_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "song_name")
    private String name;

    @Column(name = "song_duration")
    private Integer duration;
    @Column(name = "song_album")
    private String album;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty durationProperty() {
        return new SimpleStringProperty(duration.toString());
    }

    public StringProperty albumProperty() {
        return new SimpleStringProperty(album);
    }
}
