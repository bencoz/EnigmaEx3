package Commons;

import java.io.Serializable;

public class AgentDetails implements Serializable {
    public String name;
    public Integer candidates;
    public Integer codesleft;

    public AgentDetails(String i_name, Integer i_candidates, Integer i_codesLeft){
        name = i_name;
        candidates = i_candidates;
        codesleft = i_codesLeft;
    }
}
