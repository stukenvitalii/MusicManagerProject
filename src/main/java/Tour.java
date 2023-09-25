import java.util.Date;
import java.util.List;

public class Tour {
    private String name;
    private Date dateOfBeginning;
    private Date dateOfEnd;
    private List<String> cities;

    public Tour(String name,Date dateOfBeginning,Date dateOfEnd,List<String> cities) {
        this.name = name;
        this.dateOfBeginning = dateOfBeginning;
        this.dateOfEnd = dateOfEnd;
        this.cities = cities;
    }
    public String getName() {
        return name;
    }

    public Date getDateOfBeginning() {
        return dateOfBeginning;
    }

    public Date getDateOfEnd() {
        return dateOfEnd;
    }

    public List<String> getCities() {
        return cities;
    }


}
