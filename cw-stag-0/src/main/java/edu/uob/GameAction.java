package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;

public class GameAction
{
    //name in TreeMap String
    HashSet<String> subjects;
    HashSet<String> consumed;
    HashSet<String> produced;
    String narration;

    public GameAction(HashSet<String> subjects, HashSet<String> consumed,
                      HashSet<String> produced, String narration) {
        this.subjects = subjects;
        this.consumed = consumed;
        this.produced = produced;
        this.narration = narration;
    }

    public String getNarration() { return narration; }

    public HashSet<String> getSubjects() { return subjects; }

    public HashSet<String> getConsumed() { return consumed; }

    public HashSet<String> getProduced() { return produced; }

}
