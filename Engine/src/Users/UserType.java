package Users;

public enum UserType {
    UBOAT,
    ALIES;

    public static UserType getTypeByStr(String userTypeStr){
        UserType res = UserType.UBOAT;
        switch (userTypeStr){
            case "Uboat":
                res = UserType.UBOAT;
                break;
            case "Alies":
                res = UserType.ALIES;

        }
        return res;
    }
}
