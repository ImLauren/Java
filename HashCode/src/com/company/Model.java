package com.company;

import java.util.ArrayList;

public class Model {
    ArrayList<Contributor> contributors = new ArrayList<Contributor>();
    ArrayList<Project> projects = new ArrayList<Project>();
    private int numContributors;
    private int numProjects;

    public Model(int numProjects, int numContributors){
        this.numContributors = numContributors;
        this.numProjects = numProjects;

    }

    public void addContributor(Contributor contributor) {

        contributors.add(contributor);
    }

    public void addProject(Project project){
        projects.add(project);
    }
}
