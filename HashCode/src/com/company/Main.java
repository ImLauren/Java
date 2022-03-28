package com.company;

import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader readin = new BufferedReader(new FileReader("test.log"));
            String str;
            int numContributors = 0;
            int numProjects = 0;

            str = readin.readLine();
            String tmp1 = str.substring(0,str.indexOf(" "));
            String tmp2 = str.substring(str.indexOf(" ")+1,str.indexOf('\0'));
            numContributors = Integer.parseInt(tmp1);
            numProjects = Integer.parseInt(tmp2);
            Model gameModel = new Model(numContributors,numProjects);

            int i = 0;
            while ((str = readin.readLine()) != null && i < numContributors) {
                String nameContributor = str.substring(0, str.indexOf(" ")); // should be Contributor's name.
                String skillNumS = str.substring(str.indexOf(" ") + 1, str.indexOf('\0'));
                int skillNum = Integer.parseInt(String.valueOf(skillNumS));
                Contributor newContributor = new Contributor(nameContributor, skillNum);
                for (int j = 0; j < skillNum; j++) {
                    if ((str = readin.readLine()) != null) {
                        String skillName = str.substring(0, str.indexOf(" ")); // skill name
                        String skillLevelS = str.substring(str.indexOf(" ") + 1, str.indexOf('\0'));
                        int skillLevel = Integer.parseInt(String.valueOf(skillLevelS));
                        Skill newSkill = new Skill(skillName, skillLevel);
                        newContributor.conSkills.set(i, newSkill);
                    }
                }
                gameModel.addContributor(newContributor);
            }

            i=0;
            while ((str = readin.readLine()) != null && i < numProjects) {
                String nameProject = str.substring(0, str.indexOf(" ")); // should be Contributor's name.
                String strTmp = str.substring(str.indexOf(" ")+1);
                String numTmp = strTmp.substring(0,str.indexOf(" "));
                int required = Integer.parseInt(numTmp);   //required day
                strTmp = strTmp.substring(strTmp.indexOf(" ")+1);
                numTmp = strTmp.substring(0,str.indexOf(" "));
                int score = Integer.parseInt(numTmp);     //project score
                strTmp = strTmp.substring(strTmp.indexOf(" ")+1);
                numTmp = strTmp.substring(0,str.indexOf(" "));
                int bestDay = Integer.parseInt(numTmp);     //best day
                strTmp = strTmp.substring(strTmp.indexOf(" ")+1);
                numTmp = strTmp.substring(0,str.indexOf("\0"));
                int roles = Integer.parseInt(numTmp);     //roles number

                ArrayList<Skill> projectSkillsTmp = new ArrayList<Skill>();
                for (int j = 0; j < roles; j++) {
                    if ((str = readin.readLine()) != null) {
                        String skillName = str.substring(0, str.indexOf(" ")); // skill name
                        String skillLevelS = str.substring(str.indexOf(" ") + 1, str.indexOf('\0'));
                        int skillLevel = Integer.parseInt(String.valueOf(skillLevelS));
                        Skill newSkill = new Skill(skillName, skillLevel);
                        projectSkillsTmp.add(newSkill);
                    }
                }
                Project newProject = new Project (nameProject,required,score,bestDay,roles,projectSkillsTmp);
                gameModel.addProject(newProject);
            }


            System.out.println(str);
        } catch (IOException e) {
        }




    }
}