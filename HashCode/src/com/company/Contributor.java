package com.company;

import java.util.ArrayList;

public class Contributor {

    private String contributorName;
    private int numSkills;
    ArrayList<Skill> conSkills = new ArrayList<>();

    public Contributor(String name, int skills) {
        contributorName = name;
        numSkills = skills;
        for (int i = 0; i < numSkills; i++) {
            conSkills.add(null);
        }
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getContributorName() {
        return contributorName;
    }


}

