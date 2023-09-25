import java.util.List;

public class Chart {
    private String chartName;
    private Integer size;
    private List<Group> currentCondition;
    public Chart(String chartName) {
        this.chartName = chartName;
    }
    public String getChartName() {
        return chartName;
    }
    public Integer getSize() {
        return size;
    }
    public List<Group> getCurrentCondition() {
        return currentCondition;
    }
    public void AddNewGroup(Group group, Integer place) {

    }
    public List<Group> getThreeBest() {
        return null;
    }
}
