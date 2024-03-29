package edu.uob;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** This class implements the DB server. */
public final class DBServer {

  private static final char END_OF_TRANSMISSION = 4;

  static List<DataBase> DBList;
  static String currentDB;
  File databaseDirectory;
  static String result;
  DBParser inputCommand;
  static List<DBidCount> idCounts;

  public static void main(String[] args) throws IOException {
    new DBServer(Paths.get(".").toAbsolutePath().toFile()).blockingListenOn(8888);

    //new DBServer(Paths.get("allDB").toAbsolutePath().toFile()).blockingListenOn(8888);
  }

  /**
   * KEEP this signature (i.e. {@code edu.uob.DBServer(File)}) otherwise we won't be able to mark
   * your submission correctly.
   *
   * <p>You MUST use the supplied {@code databaseDirectory} and only create/modify files in that
   * directory; it is an error to access files outside that directory.
   *
   * @param databaseDirectory The directory to use for storing any persistent database files such
   *     that starting a new instance of the server with the same directory will restore all
   *     databases. You may assume *exclusive* ownership of this directory for the lifetime of this
   *     server instance.
   */
  public DBServer(File databaseDirectory) {
    // TODO implement your server logic here
    this.databaseDirectory = databaseDirectory;
    DBList = new ArrayList<>();
    try{
      fileSystem loadDBs = new fileSystem(databaseDirectory);
      DBList = loadDBs.loadDB();
      idCounts = loadDBs.setIDs(DBList);
    }catch (FileNotFoundException e){
      e.printStackTrace();
    }catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * KEEP this signature (i.e. {@code edu.uob.DBServer.handleCommand(String)}) otherwise we won't be
   * able to mark your submission correctly.
   *
   * <p>This method handles all incoming DB commands and carry out the corresponding actions.
   */
  public String handleCommand(String command) {
    // TODO implement your server logic here
    try{
      List<String> commandWord = new ArrayList<>();
      DBTokenizer inputToken = new DBTokenizer(command, commandWord);
      String handledCmd = inputToken.splitSpace();
      inputToken.addCmdList(handledCmd);
      commandWord = inputToken.commandValue;
      int pointer = 0;
      inputCommand = new DBParser(commandWord, pointer,currentDB, DBList);
      inputCommand.setDitect(databaseDirectory);
      inputCommand.setIdCounts(idCounts);
      inputCommand.runCmd();
      currentDB = inputCommand.getCurrentDB();
      DBList = inputCommand.getDBList();
      idCounts = inputCommand.getIdCounts();
      result = inputCommand.getResult();
      //save files
      fileSystem saveDBs = new fileSystem(databaseDirectory);
      idCounts = saveDBs.resetIDs(DBList,idCounts);   //resetID
      saveDBs.saveDB(DBList);
    }catch (DBException e){
      e.printStackTrace();
      return e.getMessage();
    }catch (IOException e){
      e.printStackTrace();
    }

    return inputCommand.query(this);
  }


  //  === Methods below are there to facilitate server related operations. ===

  /**
   * Starts a *blocking* socket server listening for new connections. This method blocks until the
   * current thread is interrupted.
   *
   * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
   * you want to.
   *
   * @param portNumber The port to listen on.
   * @throws IOException If any IO related operation fails.
   */
  public void blockingListenOn(int portNumber) throws IOException {
    try (ServerSocket s = new ServerSocket(portNumber)) {
      System.out.println("Server listening on port " + portNumber);
      while (!Thread.interrupted()) {
        try {
          blockingHandleConnection(s);
        } catch (IOException e) {
          System.err.println("Server encountered a non-fatal IO error:");
          e.printStackTrace();
          System.err.println("Continuing...");
        }
      }
    }
  }

  /**
   * Handles an incoming connection from the socket server.
   *
   * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
   * * you want to.
   *
   * @param serverSocket The client socket to read/write from.
   * @throws IOException If any IO related operation fails.
   */
  private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
    try (Socket s = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {

      System.out.println("Connection established: " + serverSocket.getInetAddress());
      while (!Thread.interrupted()) {
        String incomingCommand = reader.readLine();
        System.out.println("Received message: " + incomingCommand);
        String result = handleCommand(incomingCommand);
        writer.write(result);
        writer.write("\n" + END_OF_TRANSMISSION + "\n");
        writer.flush();
      }
    }
  }
}
