package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class ParserErrorTest {

    private DBServer server;

    @BeforeEach
    void setup(@TempDir File dbDir)  {
        server = new DBServer(dbDir);
    }

    @Test
    void basicError(){
        server.handleCommand(";").startsWith("[ERROR]");
        server.handleCommand("Ua ;").startsWith("[ERROR]");
    }

    @Test
    void useError(){
        server.handleCommand("USE ;").startsWith("[ERROR]");
    }

    @Test
    void createError() {
        server.handleCommand("CREATE DATABASE ;").startsWith("[ERROR]");
        server.handleCommand("CREATE TABLE ;").startsWith("[ERROR]");
        server.handleCommand("CREATE ;").startsWith("[ERROR]");
        server.handleCommand("CREATE Table tablename ();").startsWith("[ERROR]");
        server.handleCommand("CREATE Table tablename (id,);").startsWith("[ERROR]");
    }

    @Test
    void dropError (){
        server.handleCommand("Drop;").startsWith("[ERROR]");
        server.handleCommand("Drop Table ;").startsWith("[ERROR]");
    }

    @Test
    void alterError (){
        server.handleCommand("ALTER TABLa ;").startsWith("[ERROR]");
        server.handleCommand("ALTER TABLE tablename AD id ;").startsWith("[ERROR]");
        server.handleCommand("ALTER TABLE tablename    ADD ;").startsWith("[ERROR]");
    }

    @Test
    void insertError () {
        server.handleCommand("INSERT ;").startsWith("[ERROR]");
        server.handleCommand("INSERT INTO tablename ;").startsWith("[ERROR]");
        server.handleCommand("INSERT INTO tablename VALUES;").startsWith("[ERROR]");
        server.handleCommand("INSERT INTO tablename VALUES();").startsWith("[ERROR]");
        server.handleCommand("INSERT INTO tablename VALUES('Tom',);").startsWith("[ERROR]");
    }

    @Test
    void selectError (){
        server.handleCommand("SELECT * ;").startsWith("[ERROR]");
        server.handleCommand("SELECT * from ;").startsWith("[ERROR]");
        server.handleCommand("SELECT * from table1 id==1;").startsWith("[ERROR]");
    }

    @Test
    void updateError (){
        server.handleCommand("Update table1 ;").startsWith("[ERROR]");
        server.handleCommand("Update table1 SET age=12;").startsWith("[ERROR]");
    }

    @Test
    void deleteError (){
        server.handleCommand("DELETE FRam;").startsWith("[ERROR]");
        server.handleCommand("DELETE FROM table1 ;").startsWith("[ERROR]");
    }

    @Test
    void joinError (){
        server.handleCommand("JOIN table1 ;").startsWith("[ERROR]");
        server.handleCommand("JOIN table1 and table2 ;").startsWith("[ERROR]");
        server.handleCommand("JOIN table1 and table2 ON gender;").startsWith("[ERROR]");
    }

}
