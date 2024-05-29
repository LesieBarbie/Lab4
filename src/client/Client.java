package client;

import operations.*;
import models.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private int port;
    private String server;
    private Socket socket;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    public Client(String server, int port) {
        this.port = port;
        this.server = server;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(server, port), 1000);
            os = new ObjectOutputStream(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());
        } catch (InterruptedIOException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void finish() {
        try {
            os.writeObject(new StopOperation());
            os.flush();
            System.out.println(is.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void applyOperation(CardOperation op) {
        try {
            os.writeObject(op);
            os.flush();
            System.out.println(is.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Choose an operation:");
            System.out.println("1. Issue a new card");
            System.out.println("2. Get client information");
            System.out.println("3. Add money to account");
            System.out.println("4. Pay for a trip");
            System.out.println("5. Get card balance");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    issueNewCard(scanner);
                    break;
                case 2:
                    getClientInformation(scanner);
                    break;
                case 3:
                    addMoney(scanner);
                    break;
                case 4:
                    payForTrip(scanner);
                    break;
                case 5:
                    getCardBalance(scanner);
                    break;
                case 6:
                    exit = true;
                    finish();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void issueNewCard(Scanner scanner) {
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        System.out.println("Enter user surname:");
        String surName = scanner.nextLine();
        System.out.println("Enter user sex:");
        String sex = scanner.nextLine();
        System.out.println("Enter user birthday (dd.MM.yyyy):");
        String birthday = scanner.nextLine();
        System.out.println("Enter card serial number:");
        String serNum = scanner.nextLine();
        System.out.println("Enter college:");
        String college = scanner.nextLine();

        AddMetroCardOperation op = new AddMetroCardOperation();
        op.getCrd().setUsr(new User(name, surName, sex, birthday));
        op.getCrd().setSerNum(serNum);
        op.getCrd().setColledge(college);

        applyOperation(op);
    }

    private void getClientInformation(Scanner scanner) {
        System.out.println("Enter card serial number:");
        String serNum = scanner.nextLine();

        ShowClientInformationOperation op = new ShowClientInformationOperation(serNum);
        applyOperation(op);
    }

    private void addMoney(Scanner scanner) {
        System.out.println("Enter card serial number:");
        String serNum = scanner.nextLine();
        System.out.println("Enter amount to add:");
        double money = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        AddMoneyOperation op = new AddMoneyOperation(serNum, money);
        applyOperation(op);
    }

    private void payForTrip(Scanner scanner) {
        System.out.println("Enter card serial number:");
        String serNum = scanner.nextLine();
        System.out.println("Enter amount to pay:");
        double money = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        PayMoneyOperation op = new PayMoneyOperation(serNum, money);
        applyOperation(op);
    }

    private void getCardBalance(Scanner scanner) {
        System.out.println("Enter card serial number:");
        String serNum = scanner.nextLine();

        ShowBalanceOperation op = new ShowBalanceOperation(serNum);
        applyOperation(op);
    }

    public static void main(String[] args) {
        Client cl = new Client("localhost", 7891);
        cl.displayMenu();
    }
}
