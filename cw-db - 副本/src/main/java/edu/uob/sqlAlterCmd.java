package edu.uob;

public class sqlAlterCmd extends DBcmd{
    String alterTName;  //table name
    String alterType;    //ADD / DROP
    String attriName;

    public sqlAlterCmd() {
    }

    public void runAlter() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        if (!isPlainText(alterTName) || !isPlainText(attriName)){
            throw new DBException("[ERROR] Illegal Table Name!");
        }
        boolean existTable = repeatOp.existTable(DBPos,alterTName);
        if (!existTable){
            throw new DBException("[ERROR] Table does not exist!");
        }
        if (alterType.equalsIgnoreCase("ADD")){
            addCol();
        }else if(alterType.equalsIgnoreCase("DROP")){
            dropCol();
        }
    }

    public void addCol() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,alterTName);
        boolean existName = repeatOp.existAttri(DBPos,TPos,attriName);
        if (existName){
            throw new DBException("[ERROR] Attribute Name Already exists!");
        }
        DBList.get(DBPos).tables.get(TPos).addCol();
        int col =  DBList.get(DBPos).tables.get(TPos).getColNumber();
        DBList.get(DBPos).tables.get(TPos).writeValue(0,col-1,attriName);
        result = "[OK] " + attriName + " is added in "+alterTName;
    }

    public void dropCol() throws DBException{
        RepeatOp repeatOp = new RepeatOp(DBList);
        int DBPos = repeatOp.findCurrentDB(currentDB);
        int TPos = repeatOp.findTable(currentDB,alterTName);
        boolean existName = repeatOp.existAttri(DBPos,TPos,attriName);
        if (!existName){
            throw new DBException("[ERROR] Attribute Name does not exist!");
        }
        if(attriName.equalsIgnoreCase("id")){
            throw new DBException("[ERROR] 'ID' cannot be dropped!");
        }
        int col = DBList.get(DBPos).tables.get(TPos).getColNumber();
        int deleteNum=0;
        for (int j=0; j<col;j++){
            String tmpName = DBList.get(DBPos).tables.get(TPos).tableValue.get(0).get(j);
            if (tmpName.equalsIgnoreCase(attriName)){
                deleteNum = j;
            }
        }
        DBList.get(DBPos).tables.get(TPos).deleteCol(deleteNum);
        result = "[OK] " + attriName + " is dropped in "+alterTName;
    }
}
