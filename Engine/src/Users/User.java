package Users;

public class User {
    private String name;
    private UserType userType;

    public User(String _userName, String _userTypeStr) {
        name = _userName;
        userType  = UserType.getTypeByStr(_userTypeStr);
    }
}
