package JAXBManager.Actual;

import java.io.Serializable;

public class Enigma implements Serializable {
    private Machine machine;
    private Decipher decipher;
    private Battlefield battlefield;

    public Enigma() {
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public void setDecipher(Decipher decipher) {
        this.decipher = decipher;
    }

    public Decipher getDecipher(){
        return decipher;
    }

    public void setBattlefield(Battlefield battlefield) { this.battlefield = battlefield; }

    public Battlefield getBattlefield() { return battlefield; }
}