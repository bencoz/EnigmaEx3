package Enigma;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.management.BufferPoolMXBean;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import Builder.SecretBuilder;
import Machine.*;
import Factory.*;
import JAXBManager.Actual.*;

public class EnigmaManager
{
    private EnigmaMachine machine;
    private EnigmaComponentFactoryImpl factory;
    private String errorInMachineBuilding;
    private List<String> dictionary;

    public EnigmaManager() {
        this.machine = null;
        this.factory = new EnigmaComponentFactoryImpl();
    }


    /*In case of success create the wanted machine and save it (member) and return true.
        In case of failure updates the error string (member) and return false*/
    public boolean createEnigmaMachineFromXMLInputStream(InputStream stream) {
        try {
            machine = factory.createEnigmaMachineFromXMLInputStream(stream);
        } catch (FileNotFoundException e){
            errorInMachineBuilding = "Could not find XML file.";
        }
        if (machine == null) {
            errorInMachineBuilding = "Could not load Machine - File is no good";
            return false;
        }
        if (!isMachineABCEven()) {
            errorInMachineBuilding = "Machine ABC is not Even";
            return false;
        }
        if (!isMachineRotorsCountOK())
            return false;
        if (!isMachineRotorsOK())
            return false;
        if (!isMachineReflectorsOK())
            return false;
        if(machine.getBattlefield() == null) {
            errorInMachineBuilding = "Machine Battlefield Uninitialized";
            return false;
        }
        dictionary = machine.getDecipher().getDictionary();
        return true;
    }

    public String process(String code)
    {
        return machine.process(code);
    }

    //if we want to do the bonus (tournament)
    public void resetSystem()
    {
        machine.resetToInitialPosition();
    }

    private boolean isMachineReflectorsOK() {
        if (!isMachineReflectorsDoubleMapping()) {
            errorInMachineBuilding = "One or more of Machines reflectors contains double mapping";
            return false;
        }
        List<Reflector> reflectors = machine.getReflectors();
        reflectors.sort(Comparator.comparingInt(Reflector::getID));
        for (int i = 0; i< reflectors.size(); i++){
            Reflector reflect = reflectors.get(i);
            if (reflect.getID() != i+1) {
                errorInMachineBuilding = "Machine reflectors id's are not in sequential order";
                return false;
            }
            if (reflect.getReflectLength() != getABC().length()) {
                errorInMachineBuilding = "One of machine's reflectors mapping is not the same size as abc";
                return false;
            }
        }
        return true;
    }

    private boolean isMachineReflectorsDoubleMapping() {
        Reflector reflector = null;
        reflector = getMachine().getReflectors().stream().
                filter(Reflector::containsDoubleMapping).
                findAny().
                orElse(null);
        if (reflector == null)
            return true;
        else
            return false;
    }

    private boolean isMachineRotorsOK() {
        if (!isMachineRotorsNotchPositionOK()){
            errorInMachineBuilding = "One of machine's rotors has bad notch position";
            return false;
        }
        if (!isMachineRotorsMappingSizeOK()) {
            errorInMachineBuilding = "One of machine's rotors mapping is not the same size as abc";
            return false;
        }
        if(!isMachineRotorsMappingOK())
        {
            errorInMachineBuilding = "One of machine's rotors mapping letters doesn't contains in the ABC";
            return false;
        }
        if(!isMachineReflectorsMappingOK())
        {
            errorInMachineBuilding = "One of machine's reflector mapping in not valid";
            return false;
        }

        List<Rotor> rotors = machine.getRotors();
        rotors.sort(Comparator.comparingInt(Rotor::getID));
        for (int i = 0; i< rotors.size(); i++){
            if (rotors.get(i).getID() != i+1) {
                errorInMachineBuilding = "Machine rotors id's are not in sequential order";
                return false;
            }
        }
        return true;
    }

    private boolean isMachineRotorsMappingOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors) {
            for(char mapCh : rotor.getMappingABC().toCharArray())
            {
                if(!machine.getABC().contains(String.valueOf(mapCh)))
                    return false;
            }
        }
        return true;
    }


    private boolean isMachineReflectorsMappingOK() {
        List<Reflector> reflectors = getMachine().getReflectors();
        for (Reflector reflector : reflectors) {
            for(int mapInt : reflector.getReflectorMapping())
            {
                if(!((mapInt >= 0) && (mapInt < machine.getABC().length() )))
                    return false;
            }
        }
        return true;
    }

    private boolean isMachineRotorsNotchPositionOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors){
            if (rotor.getNotch() < 0 || rotor.getNotch() > getMachine().getABC().length())
                return false;
        }
        return true;
    }

    private boolean isMachineRotorsMappingSizeOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors) {
            if (rotor.getMappingLength() != getABC().length())
                return false;
        }
        return true;
    }

    private boolean isMachineRotorsCountOK() {
        int numOfRotors = machine.getNumOfRotors();
        int rotorsCount = machine.getRotorsCount();
        if (rotorsCount < 2 || rotorsCount > numOfRotors) {
            errorInMachineBuilding = "Machine's 2 <= rotors-count < the total number of rotors";
            return false;
        }
        else
            return true;
    }

    private boolean isMachineABCEven() {
        return (machine.getABC().length() % 2 == 0);
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public String getABC()
    {
        return machine.getABC();
    }

    public String getErrorInMachineBuilding() {
        return errorInMachineBuilding;
    }

    public boolean isInDictionary(String userInput) {
        boolean found;
        String[] words = userInput.split(" ");
        for (String word : words){
            found = false;
            for (String permittedWord : dictionary){
                if (permittedWord.equals(word)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }

    public Battlefield getBattlefield() {
        return machine.getBattlefield();
    }

    public String getBattleName()
    {
        return machine.getBattlefield().getName();
    }

    public void setMachineConfig(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        SecretBuilder secretBuilder = machine.createSecret();
        for(int i = 0; i < chosenRotorsID.size(); i++) {
            secretBuilder.selectRotor(chosenRotorsID.get(i), chosenRotorsLoc.get(i));
        }
        secretBuilder.selectReflector(chosenReflectorID);
        secretBuilder.create();
        //currentCodeFormat = new CodeFormat(chosenRotorsID, chosenRotorsLoc, toRoman(chosenReflectorID));
    }
}
