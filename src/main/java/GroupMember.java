public class GroupMember extends Person {
    private String role;
    public GroupMember(String name, Integer age, String gender,String role) {
        super(name,age,gender);
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String newRole) {
        role = newRole;
    }
}