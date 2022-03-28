package edu.uob;

public class sqlUseCmd extends DBcmd{

    public sqlUseCmd() {
    }

    public void existDB() throws DBException{
        boolean flag = false;
        if (!isPlainText(DBname)){
            throw new DBException("[ERROR] Illegal DataBase Name!");
        }
        for (int i=0; i<DBList.size();i++){
            String existName = DBList.get(i).getDBname();
            if (DBname.equalsIgnoreCase(existName)){
                flag = true;
            }
        }
        if (flag == false){
            throw new DBException("[ERROR] DataBase does not exist!");
        }
    }

}
