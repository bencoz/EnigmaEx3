package Factory;

import Builder.EnigmaMachineBuilder;
import JAXBManager.Actual.*;
import JAXBManager.JAXBGenerated.Enigma;
import JAXBManager.JAXBToActual;
import Machine.EnigmaMachine;
import Machine.EnigmaMachineImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EnigmaComponentFactoryImpl implements EnigmaComponentFactory {

    private final static String JAXB_PACKAGE_NAME = "JAXBManager.JAXBGenerated";

    public EnigmaMachine createEnigmaMachineFromXMLFile(String path) throws FileNotFoundException {
        EnigmaMachineImpl enigmaMachine = null;
        JAXBManager.Actual.Enigma res = null;
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);

        try {
            JAXBManager.JAXBGenerated.Enigma enigma = deserializeFrom(inputStream);
            res = makeEnigmaFromJAXB(enigma);
            enigmaMachine = new EnigmaMachineImpl(res);
        } catch (JAXBException e) {
            e.printStackTrace();
        } finally {
            return enigmaMachine;
        }
    }
    public EnigmaMachine createEnigmaMachineFromXMLInputStream(InputStream inputStream) throws FileNotFoundException {
        EnigmaMachineImpl enigmaMachine = null;
        JAXBManager.Actual.Enigma res = null;

        try {
            JAXBManager.JAXBGenerated.Enigma enigma = deserializeFrom(inputStream);
            res = makeEnigmaFromJAXB(enigma);
            enigmaMachine = new EnigmaMachineImpl(res);
        } catch (JAXBException e) {
            e.printStackTrace();
        } finally {
            return enigmaMachine;
        }
    }

    private JAXBManager.Actual.Enigma makeEnigmaFromJAXB(JAXBManager.JAXBGenerated.Enigma enigma) {
        JAXBManager.Actual.Enigma res;
        JAXBToActual transform = JAXBToActual.getInstance();
        res = transform.change(enigma);
        return res;
    }

    private Enigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (Enigma) u.unmarshal(in);
    }


    @Override
    public EnigmaMachineBuilder buildMachine(int rotorsCount, String alphabet) {
        EnigmaMachineBuilder returnedValue = new EnigmaMachineBuilder();
        returnedValue.setRotorsCount(rotorsCount);
        returnedValue.setABC(alphabet);
        return returnedValue;
    }

    @Override
    public Rotor createFromString(int id, String source, String target) {
        int notch = (int)(Math.random() * ((source.length() + 1)));
        return createFromString(id,source,target,notch);
    }

    @Override
    public Rotor createFromString(int id, String source, String target, int notch) {
        Rotor rotor = new Rotor();
        rotor.setID(id);
        rotor.setNotch(notch);
        String newSource = source.toUpperCase();
        String newTarget = target.toUpperCase();
        for (int i = 0; i < newSource.length(); i++){
            rotor.addMapping(new Mapping(newSource.charAt(i),newTarget.charAt(i)));
        }
        return rotor;
    }

    @Override
    public Reflector createReflector(int id, byte[] input, byte[] output) {
        Reflector reflect = new Reflector();
        reflect.setID(id);
        for (int i = 0; i < input.length ; i++){
            reflect.addReflect(new Reflect(input[i],output[i]));
        }
        return reflect;
    }
}
