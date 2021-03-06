package Commons;

import java.io.Serializable;

public class DecipheringStatus implements Serializable{
    private boolean isPaused = false;
    private boolean isStopped = false;

    public synchronized boolean checkIfToContinue()
    {
        if(this.isStopped)
            return false;
        while(this.isPaused)
        {
            try{
                this.wait();
                if(this.isStopped)
                    return false;
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public synchronized void pauseDeciphering()
    {
        this.isPaused = true;
    }

    public  synchronized void continueDeciphering()
    {
        this.isPaused = false;
        this.notifyAll();
    }


    public synchronized void stopDeciphering() {
        this.isStopped = true;
    }

    public String toString(){
        return "isPaused: "+this.isPaused+" isStoped: "+this.isStopped;
    }
}
