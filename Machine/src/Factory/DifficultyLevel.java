package Factory;

public enum DifficultyLevel {
    Easy,
    Medium,
    Hard,
    Impossible;

    public static DifficultyLevel getDufficultyByStr(String level) {
        level = level.toUpperCase();
        DifficultyLevel res = Easy;
        switch (level)
        {
            case "EASY":
                res = Easy;
                break;
            case "MEDIUM":
                res = Medium;
                break;
            case "HARD":
                res = Hard;
                break;
            case "IMPOSSIBLE":
                res = Impossible;
                break;
        }
        return res;
    }
}
