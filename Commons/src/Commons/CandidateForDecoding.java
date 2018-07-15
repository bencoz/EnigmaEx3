package Commons;



import Machine.Secret;

import java.io.Serializable;

public class CandidateForDecoding implements Serializable {
    private String decoding;
    private Secret secret;
    private String agentName;


    public CandidateForDecoding(String _decoding, Secret _position, String _agentName)
    {
        this.decoding = _decoding;
        this.secret = _position;
        this.agentName = _agentName;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent: ").append(agentName).append(" Decoded: \'").append(decoding).append("\'");
        return sb.toString();
    }

    public String getDecoding() {
        return decoding;
    }

    public void setDecoding(String decoding) {
        this.decoding = decoding;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
        this.secret = secret;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

}
