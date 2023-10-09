import jakarta.persistence.*;

@MappedSuperclass
@Table(name = "persons")
public class Person {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;

    public Person(String name,Integer age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "person_gender")
    public String getGender() {
        return gender;
    }
    @Column(name = "person_age")
    public Integer getAge() {
        return age;
    }
    @Column(name = "person_name")
    public String getName() {
        return name;
    }

}
