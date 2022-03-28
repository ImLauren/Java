package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class DBServer {

    private static final char END_OF_TRANSMISSION = 4;

    public static void main(String[] args) throws IOException {
        System.out.println(Paths.get(".").toAbsolutePath().toFile());
        new DBServer(Paths.get(".").toAbsolutePath().toFile()).blockingListenOn(8888);
    }


    public DBServer(File databaseDirectory){
        // TODO implement your server logic here
        String name = databaseDirectory + File.separator + "people.tab";  //获取文件名
        File fileToOpen = new File(name);

        try{
            if(fileToOpen.exists()){
                FileReader reader = new FileReader(fileToOpen);
                BufferedReader buffReader = new BufferedReader(reader);
                String line = null;
                List<List<String>> tableValue = new ArrayList<>();  //新建存放table的二维数组
                int count=0;  //指针
                while((line = buffReader.readLine()) != null){   //逐行读取
                    List<String> rowValue = new ArrayList<>();  //新建存放一行值的二维数组
                    String tmpStr[] = line.split(" ");  //用空格将一行字符串分开
                    for (int i=0; i<tmpStr.length;i++){    //将数组中的值传入动态数组
                        rowValue.add(i,tmpStr[i]);
                    }
                    tableValue.add(count, rowValue);
                    count++;
                    System.out.println(line);
                }
                buffReader.close();   //关闭文件

                //新建table的class，将数据存入
                DBTable sqlTable = new DBTable("people");  //手动输入-->键盘输入??
                sqlTable.setTableValue(tableValue);
                int rowNumber = tableValue.size();
                sqlTable.setRowNumber(rowNumber);
                int colNumber = tableValue.get(0).size();
                sqlTable.setColNumber(colNumber);
                sqlTable.setKeys("id");   //手动输入??
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public String handleCommand(String command) {
        // TODO implement your server logic here
        List<String> commandValue = new ArrayList<>();
        sqlToken inputToken = new sqlToken(command, commandValue);  //新建一个类，在里面解析空格
        try{
            String handledCmd = inputToken.splitSpace();
            System.out.println(handledCmd);
            inputToken.addCmdList(handledCmd);
            commandValue = inputToken.commandValue;
            System.out.println(commandValue);
        }catch (sqlException e){
            e.printStackTrace();
            return "[ERROR] "+e.getMessage();
        }


        //int count=0;
        //sqlCommand inputCommand = new sqlCommand(commandWord, count);


        return "[OK] Thanks for your message: " + command;
    }

    public void saveTable(DBTable sqlTable){
        //保存文件????
    }


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
