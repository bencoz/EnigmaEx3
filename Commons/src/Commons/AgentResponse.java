package Commons;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgentResponse implements Serializable {
    private Integer agentID;
    private String agentName;
    private List<CandidateForDecoding> candidacies;
    private Integer numOfCandidacies = 0;

    public AgentResponse(Integer _agentID) {
        this.agentID = _agentID;
    }
    public AgentResponse(String name) {
        this.agentName = name;
    }

    public void addDecoding(CandidateForDecoding candidate) {
        if(numOfCandidacies == 0)
            candidacies = new ArrayList<>();
        candidacies.add(candidate);
        numOfCandidacies++;
    }

    public void reset() {
        numOfCandidacies = 0;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public List<CandidateForDecoding> getCandidacies() {
        return candidacies;
    }

    public boolean isEmpty() {
        return numOfCandidacies == 0;
    }

    public String getAgentName() {
        return agentName;
    }
}
