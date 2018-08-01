package Users;

import java.util.*;

public class UserManager {
    private final Map<String,User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public void addUser(String userName, String userType) {
        User newUser = new User(userName, userType);
        users.put(userName,newUser);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public Set<String> getUsers(){ //TODO:: IMPLEMENT !!!
        return null;
    }
    /*final?...
    public Map<String,User> getUsers() {
        return Collections.unmodifiableSet(users);
    }*/

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }
}
