package Alies;

public class AliesResponse {
    String candidateForDecoding;
    String aliesTeamName;

    public AliesResponse(String _candidateForDecoding, String _aliesTeamName)
    {
        candidateForDecoding = _candidateForDecoding;
        aliesTeamName = _aliesTeamName;
    }

    public String getAnswer() {
        return candidateForDecoding;
    }

    public String getAliesName() {
        return aliesTeamName;
    }
}
