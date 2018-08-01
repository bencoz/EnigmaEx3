package JAXBManager.Actual;

import Factory.DifficultyLevel;

public class Battlefield {
    private DifficultyLevel level;
    private String name;
    private int numOfAllies;
    private int numOfRounds;

    public Battlefield(String i_name, int i_numOfAllies, String i_level, Integer i_numOfRounds){
        level = DifficultyLevel.getDifficultyByStr(i_level);
        name = i_name;
        numOfAllies = i_numOfAllies;
        if (i_numOfRounds != null) {
            numOfRounds = i_numOfRounds;
        } else {
            numOfRounds = 1;
        }
    }

    public Battlefield(JAXBManager.JAXBGenerated.Battlefield i_battlefield){
        level = DifficultyLevel.getDifficultyByStr(i_battlefield.getLevel());
        name = i_battlefield.getBattleName();
        numOfAllies = i_battlefield.getAllies();
        numOfRounds = i_battlefield.getRounds();
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getNumOfAllies() {
        return numOfAllies;
    }

    public int getNumOfRounds() {
        return numOfRounds;
    }

}
