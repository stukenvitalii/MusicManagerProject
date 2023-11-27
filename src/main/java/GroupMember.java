import jakarta.persistence.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "members")
public class GroupMember {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "member_name")
    private String name;
    @Column(name = "member_age")
    private Integer age;
    @Column(name = "member_role")
    private String role;
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public GroupMember() {
    }
    public String getRole() {
        return role;
    }
    public void setRole(String newRole) {
        role = newRole;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setGroup(Group group) {
        this.group = group;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public StringProperty roleProperty() {
        return new SimpleStringProperty(role);
    }

    public StringProperty ageProperty() {
        return new SimpleStringProperty(age.toString());
    }
}