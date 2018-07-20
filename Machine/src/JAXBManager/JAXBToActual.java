package JAXBManager;

import JAXBManager.Actual.Machine;
import JAXBManager.JAXBGenerated.*;

import java.util.List;

public class JAXBToActual {
    private static JAXBToActual ourInstance = new JAXBToActual();

    public static JAXBToActual getInstance() {
        return ourInstance;
    }

    private JAXBToActual() {
    }

    public JAXBManager.Actual.Enigma change(Enigma enigma) {
        List<Reflector> JAXBReflectors;
        List<Rotor> JAXBRotors;
        Decipher decipher = enigma.getDecipher();
        Battlefield battlefield = enigma.getBattlefield();
        JAXBManager.Actual.Enigma res = new JAXBManager.Actual.Enigma();
        Machine machine = new Machine();

        machine.setAbc(enigma.getMachine().getABC().trim());
        machine.setRotorsCount(enigma.getMachine().getRotorsCount());

        JAXBRotors = enigma.getMachine().getRotors().getRotor();
        JAXBReflectors = enigma.getMachine().getReflectors().getReflector();

        for (Rotor rotor : JAXBRotors){
            JAXBManager.Actual.Rotor ActualRotor = new JAXBManager.Actual.Rotor();
            ActualRotor.setID(rotor.getId());
            ActualRotor.setNotch(rotor.getNotch()-1);
            ActualRotor.addMappingList(rotor.getMapping());
            machine.addRotor(ActualRotor);
        }

        for (Reflector reflector : JAXBReflectors) {
            JAXBManager.Actual.Reflector ActualReflector = new JAXBManager.Actual.Reflector();
            ActualReflector.setID(reflector.getId());
            ActualReflector.addReflectList(reflector.getReflect());
            machine.addReflector(ActualReflector);
        }

        if (decipher != null){
            JAXBManager.Actual.Decipher ActualDecipher = new JAXBManager.Actual.Decipher(decipher.getAgents());
            ActualDecipher.setExcludeChars(decipher.getDictionary().getExcludeChars());
            String words = decipher.getDictionary().getWords().trim();
            ActualDecipher.appedToDictionary(words.toUpperCase().split(" "));

            res.setDecipher(ActualDecipher);
        }

        if (battlefield != null){
            JAXBManager.Actual.Battlefield ActualBattlefield = new JAXBManager.Actual.Battlefield(battlefield);
            res.setBattlefield(ActualBattlefield);
        }

        res.setMachine(machine);
        return res;
    }

}
