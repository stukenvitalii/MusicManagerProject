import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class GroupMember {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Group group;
    private String role;


    public GroupMember() {
    }
    @Column(name = "member_role")
    public String getRole() {
        return role;
    }

    public void setRole(String newRole) {
        role = newRole;
    }

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @ManyToOne
    @JoinColumn(name = "group_id")
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    @Column(name = "member_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "member_age",insertable=false, updatable=false)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    @Column(name = "member_age")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}