
public class Controller {
    Model model;
    int totalScores;
    int numInWork = 0;
    int totalNumInWork=0;
    int projSkillIndex = 0;

    public Controller(Model MATModel) {
        model = MATModel;
    }

    public void assignContributor() {
        int projNum = model.projects.size();
        Project curProj;
        for (int i = 0; i < projNum; i++) {
            curProj = model.projects.get(i);
            if(!curProj.getIsFinished()) {
                // every contributor
                for(int j = 0; j < model.contributors.size(); j++){
                    if(model.contributors.get(j).getIsFree()){
                        if(skillMatch(model.contributors.get(j).conSkills, curProj.projectSkills)) {
                            NumInWork++;

                        }
                        else if (numInWork= CurProj.getRoleNumber()){
                            numInWork=0;
                            totalNumInWork+=CurProj.getRoleNumber();
                        }

                    }

                }

            }

        }

        // 遍历每个项目。
        // 当前项目下，去匹配所有员工。
        // 如果没有员工符合角色的技能要求，那么这个项目跳过。
        // 再去下一个项目，
    }

    public boolean skillMatch(ArrayList<Skill> a, ArrayList<Skill> b) {
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if (a.get(i).getSkillName() == b.get(j).getSkillName()) {
                    if (a.get(i).getSkillLevel() >= b.get(j).getSkillLevel()) {
                        projSkillIndex = j;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void levelUP(Skill curSkill) {
        int aimLevel = curSkill.levelUP();
        curSkill.setSkillLevel(aimLevel);
    }
}