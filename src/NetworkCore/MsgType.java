package NetworkCore;

public enum MsgType {
    MATCH,
    YOURTURN,
    MOVE,
    CHALLENGE,
    CHALLENGE_CANCELLED,
    WIN,
    LOSS,
    DRAW,
    ERR,
    NULL;

    public static MsgType translateMessage(String type) {
        switch (type.toUpperCase()) {
            case "MATCH":
                return MATCH;
            case "YOURTURN":
                return YOURTURN;
            case "MOVE":
                return MOVE;
            case "CHALLENGE":
                return CHALLENGE;
            case "CHALLENGE_CANCELLED":
                return CHALLENGE_CANCELLED;
            case "WIN":
                return WIN;
            case "LOSS":
                return LOSS;
            case "DRAW":
                return DRAW;
            case "ERR":
                return ERR;
            default:
            case "NULL":
                return NULL;
        }
    }
    public static String translateMessage(MsgType type) {
        switch(type) {
            case MATCH: return "MATCH";
            case YOURTURN: return "YOURTURN";
            case MOVE: return "MOVE";
            case CHALLENGE: return "CHALLENGE";
            case CHALLENGE_CANCELLED: return "CHALLENGE_CANCELLED";
            case WIN: return "WIN";
            case LOSS: return "LOSS";
            case DRAW: return "DRAW";
            case ERR: return "ERR";
            default: case NULL: return "NULL";
        }
    }
}