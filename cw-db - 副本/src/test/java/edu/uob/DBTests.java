package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class DBTests {

  private DBServer server;

  // we make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup(@TempDir File dbDir)  {
    // Notice the @TempDir annotation, this instructs JUnit to create a new temp directory somewhere
    // and proceeds to *delete* that directory when the test finishes.
    // You can read the specifics of this at
    // https://junit.org/junit5/docs/5.4.2/api/org/junit/jupiter/api/io/TempDir.html

    // If you want to inspect the content of the directory during/after a test run for debugging,
    // simply replace `dbDir` here with your own File instance that points to somewhere you know.
    // IMPORTANT: If you do this, make sure you rerun the tests using `dbDir` again to make sure it
    // still works and keep it that way for the submission.

    server = new DBServer(dbDir);
  }

  // Here's a basic test for spawning a new server and sending an invalid command,
  // the spec dictates that the server respond with something that starts with `[ERROR]`
  @Test
  void testInvalidCommandIsAnError() {
    assertTrue(server.handleCommand("foo").startsWith("[ERROR]"));
  }

  // Add more unit tests or integration tests here.
  // Unit tests would test individual methods or classes whereas integration tests are geared
  // towards a specific usecase (i.e. creating a table and inserting rows and asserting whether the
  // rows are actually inserted)

  @Test
  void testBasicCommand() {
    assertTrue(server.handleCommand("CREATE DATABASE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE DATABASE testDB;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("USE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (name,age,gender);").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table2 (name,grade,number);").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table2 (name,grade,number);").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DROP TABLE table2;").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Kitty',24,'female');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Peter',26,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Tom',30,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("ALTER TABLE table1 ADD grade;").startsWith("[OK]"));
    assertTrue(server.handleCommand("Update table1 SET grade=60 WHERE name=='Kitty';").startsWith("[OK]"));
    assertTrue(server.handleCommand("Update table1 SET grade=80 WHERE name=='Peter';").startsWith("[OK]"));
    assertTrue(server.handleCommand("Update table1 SET grade=73 WHERE name=='Tom';").startsWith("[OK]"));
    assertTrue(server.handleCommand("ALTER TABLE table1 DROP grade;").startsWith("[OK]"));
    assertTrue(server.handleCommand("DELETE FROM table1 WHERE age==26;").startsWith("[OK]"));
    assertTrue(server.handleCommand("DROP DATABASE testDB;").startsWith("[OK]"));
  }

  @Test
  void testIDNum() {
    assertTrue(server.handleCommand("CREATE DATABASE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (name,age,gender);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Kitty',24,'female');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Peter',26,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Tom',30,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("DELETE FROM table1 WHERE id==3;").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('John',18,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT id FROM table1 WHERE name=='John';").contains("id\t\r\n" + "4\t"));
    assertTrue(server.handleCommand("DROP TABLE table1;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (name,age,gender);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Helen',20,'female');").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT id FROM table1 WHERE name=='Helen';").contains("id\t\r\n" + "5\t"));
  }

  @Test
  void testCondition() {
    assertTrue(server.handleCommand("CREATE DATABASE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (name,age,gender);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Kitty',24,'female');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Peter',26,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Tom',30,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('John',18,'male');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Helen',20,'female');").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT id FROM table1 WHERE ((gender=='male')AND(id>=3))AND(age<20);").contains("4"));
    assertTrue(server.handleCommand("SELECT * FROM table1 WHERE (name=='Helen')OR((id<4)AND(age>25));").contains(
            "2\tPeter\t26\tmale\t\r\n"+"3\tTom\t30\tmale\t\r\n"+"5\tHelen\t20\tfemale\t\r\n"));
  }

  @Test
  void testJoin() {
    assertTrue(server.handleCommand("CREATE DATABASE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE testDB;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE people (Name,age,Email);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO people VALUES('Bob',21,'bob@bob.net');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO people VALUES('Harry',32,'harry@harry.com');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO people VALUES('Chris',42,'chris@chris.ac.uk');").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO people VALUES('peter',13,'peter@q.com');").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE sheds (Name,Height,PurchaserID);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO sheds VALUES('Dorchester',1800,3);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO sheds VALUES('Plaza',1200,1);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO sheds VALUES('Mall',1600,2);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO sheds VALUES('Excelsior',1000,2);").startsWith("[OK]"));
    assertTrue(server.handleCommand("JOIN people and sheds ON id AND PurchaserID;").contains(
            "1\tBob\t21\tbob@bob.net\tPlaza\t1200\t\r\n"+"2\tHarry\t32\tharry@harry.com\tMall\t1600\t\r\n"+
            "3\tHarry\t32\tharry@harry.com\tExcelsior\t1000\t\r\n"+"4\tChris\t42\tchris@chris.ac.uk\tDorchester\t1800\t\r\n"));
  }

  @Test
  void testExample(){
    assertTrue(server.handleCommand("CREATE DATABASE markbook;").startsWith("[OK]"));
    assertTrue(server.handleCommand("use markbook;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE marks (name, mark, pass);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Steve', 65, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Dave', 55, TRUE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Clive', 20, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks;").contains("id\tname\tmark\tpass\t\r\n" +
            "1\tSteve\t65\tTRUE\t\r\n" + "2\tDave\t55\tTRUE\t\r\n" +
            "3\tBob\t35\tFALSE\t\r\n" + "4\tClive\t20\tFALSE\t"));
    assertTrue(server.handleCommand("INSERT INTO marks VALUES ('Bob', 35, FALSE);").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE name != 'Dave';").contains(
            "id\tname\tmark\tpass\t\r\n" + "1\tSteve\t65\tTRUE\t\r\n" + "3\tBob\t35\tFALSE\t\r\n" + "4\tClive\t20\tFALSE\t"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE pass == TRUE;").contains(
            "id\tname\tmark\tpass\t\r\n" + "1\tSteve\t65\tTRUE\t\r\n" + "2\tDave\t55\tTRUE\t"));
    assertTrue(server.handleCommand("UPDATE marks SET mark = 38 WHERE name == 'Clive';").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE name =='Clive';").contains(
            "id\tname\tmark\tpass\t\r\n" + "4\tClive\t38\tFALSE"));
    assertTrue(server.handleCommand("DELETE FROM marks WHERE name == 'Dave';").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE (pass==FALSE)AND (mark > 35);").contains(
            "id\tname\tmark\tpass\t\r\n" + "4\tClive\t38\tFALSE\t"));
    assertTrue(server.handleCommand("SELECT * FROM marks WHERE name LIKE 've';").contains(
            "id\tname\tmark\tpass\t\r\n" + "1\tSteve\t65\tTRUE\t\r\n" + "4\tClive\t38\tFALSE\t\r\n"));
    assertTrue(server.handleCommand("SELECT id FROM marks WHERE pass == FALSE;").contains("id\t\r\n" + "3\t\r\n" + "4\t"));
    assertTrue(server.handleCommand("SELECT name FROM marks WHERE mark>60;").contains("name\t\r\n" + "Steve\t"));
    assertTrue(server.handleCommand("DELETE FROM marks WHERE mark<40;").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM marks;").contains(
            "id\tname\tmark\tpass\t\r\n" + "1\tSteve\t65\tTRUE\t"));
    assertTrue(server.handleCommand("SELECT * FROM marks").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT * FROM crew;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT * FROM mark").startsWith("[ERROR]"));
  }

  @Test
  void testError(){
    assertTrue(server.handleCommand("use bd!;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE DATABASE DB$R;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE TABLE Ta*R;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (n!m, ke);").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE DATABASE DBtest;").startsWith("[OK]"));
    assertTrue(server.handleCommand("USE DBtest;").startsWith("[OK]"));
    assertTrue(server.handleCommand("CREATE TABLE table1 (name, age);").startsWith("[OK]"));
    assertTrue(server.handleCommand("DROP DATABASE op(3;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DROP DATABASE noneDB;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DROP TAble ot3&;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DROP TAble nonetable;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("ALTER TABLE -5^ add name;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("ALTER TABLE table1 drop nar5^;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("ALTER TABLE table1 drop id;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("INSERT INTO ta#e1 VALUES('Tom', 34);").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Tom', 3^4);").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("INSERT INTO table2 VALUES('Tom', 34);").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Tom', 34);").startsWith("[OK]"));
    assertTrue(server.handleCommand("INSERT INTO table1 VALUES('Kitty', 28);").startsWith("[OK]"));
    assertTrue(server.handleCommand("SELECT * FROM tabl#;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table2;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT fa FROM table1;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table1 WHERE id%3;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table1 WHERE (id>=1) AND (name=='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table1 WHERE (er>=1) OR (name=='TOM');").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table1 WHERE (id>=1) RJ (name=='TOM');").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("SELECT name FROM table2 WHERE (id>=1) OR (name=='TOM');").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("UPDATE te^ SET age=18 WHere name=='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("UPDATE table2 SET age=18 WHERE name=='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("UPDATE table1 SET age?18 WHERE name=='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("UPDATE table1 SET age=18 WHERE name)='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("UPDATE table1 SET age=18 WHERE we=='TOM';").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DELETE FROM ta%# WHERE id==1;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DELETE FROM table2 WHERE id==1;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DELETE FROM table1 WHERE r==1;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("DELETE FROM table1 WHERE id==7;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("JOIN ta^*e1 AND table2 ON id and SelectID;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("CREATE TABLE table2 (name, SelectID);").startsWith("[OK]"));
    assertTrue(server.handleCommand("JOIN table1 AND table3 ON id and SelectID;").startsWith("[ERROR]"));
    assertTrue(server.handleCommand("JOIN table1 AND table2 ON id and Sele;").startsWith("[ERROR]"));
  }


}
