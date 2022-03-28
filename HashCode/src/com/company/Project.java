package com.company;

import com.company.Skill;

import java.util.ArrayList;

public class Project {
    private String projectName;
    private int daysRequired;
    private int scoreForCompletion;
    private int daysBestBefore;
    private int rolesNumber;
    static int currentDay = 0;
    private boolean isFinished;
    ArrayList<Skill> projectSkills = new ArrayList<Skill>();

    public Project(String Name, int required, int score, int best, int roles, ArrayList<Skill> skills) {
        projectName = Name;
        daysRequired = required;
        scoreForCompletion = score;
        daysBestBefore = best;
        rolesNumber = roles;
        for(int i = 0; i < skills.size(); i++){
            projectSkills.set(i, skills.get(i));
        }
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setDaysRequired(int daysRequired) {
        this.daysRequired = daysRequired;
    }

    public void setScoreForCompletion(int scoreForCompletion) {
        this.scoreForCompletion = scoreForCompletion;
    }

    public void setDaysBestBefore(int daysRequired) {
        this.daysBestBefore = daysBestBefore;
    }

    public void setRolesNumber(int rolesNumber) {
        this.rolesNumber = rolesNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getDaysRequired() {
        return daysRequired;
    }

    public int getScoreForCompletion() {
        return scoreForCompletion;
    }

    public int getDaysBestBefore() {
        return daysBestBefore;
    }

    public int getRolesNumber() {
        return rolesNumber;
    }

    public void setIsFinished(){
        isFinished=true;
    }
    public boolean getIsFinished(){
        return isFinished;
    }

    public void setCurrentDay(){
        Project.currentDay++;
    }

    public int getCurrentDay(){
        return currentDay;
    }
}
