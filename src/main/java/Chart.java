import jakarta.persistence.*;

import java.util.List;
@Entity
public class Chart {
    @Id
    @Column(name = "chart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "chart_name")
    private String chartName;
    @Column(name = "chart_size")
    private Integer size;
    @Transient
    private List<Group> currentCondition;
    public Chart(String chartName) {
        this.chartName = chartName;
    }

    public Chart() {

    }

    public String getChartName() {
        return chartName;
    }
    public void setChartName(String chartName) {this.chartName = chartName;}
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {this.size = size;}
    public List<Group> getCurrentCondition() {
        return currentCondition;
    }
    public void setCurrentCondition(List<Group> currentCondition) {this.currentCondition = currentCondition;}
    public void AddNewGroup(Group group, Integer place) {

    }
}
