package Alies;

public class AliesResponse {
    String decoding;
    String name;

    public AliesResponse(String _candidateForDecoding, String _aliesTeamName)
    {
        decoding = _candidateForDecoding;
        name = _aliesTeamName;
    }

    public String getAnswer() {
        return decoding;
    }

    public String getAliesName() {
        return name;
    }
}
