package banking;

import aca.proto.BankMessage;
import banking.interfaces.ICustomerServiceManager;
import banking.interfaces.IUser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Logger LOG = LoggerFactory.getLogger(Main.class);
    private static List<IUser> users = new ArrayList<>();

    public static void main(String[] args) {
        ICustomerServiceManager manager = new CustomerServiceManager("1");


        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter \"login\", \"register\" or \"exit\": ");
            String input = scanner.nextLine();

            while (!input.equals("register") && !input.equals("login") && !input.equals("exit")) {
                System.out.println("Enter \"login\" , \"register\" or \"exit\": ");
                input = scanner.nextLine();
            }

            switch (input) {
                case "login":
                    System.out.println("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.println("Enter password: ");
                    String password = scanner.nextLine();
                    BankMessage loginMessage = BankMessage.newBuilder()
                            .setLogin(BankMessage.Login.newBuilder().setUsername(username).setPassword(password).build())
                            .build();
                    BankMessage message = manager.verifyUserLogin(loginMessage);
                    if (message.hasConfirmation()) {
                        IUser loggedUser = getUser(username);
                        System.out.println("Confirmation: " + message.getConfirmation().getMessage());
                        printUserInfo(loggedUser);
                    } else if (message.hasFailure()) {
                        System.out.println("Failure: " + message.getFailure().getMessage());
                    }

                    break;
                case "register":
                    System.out.println("Enter first name: ");
                    String newUserFirstName = scanner.nextLine();
                    System.out.println("Enter last name: ");
                    String newUserLastName = scanner.nextLine();
                    System.out.println("Enter username: ");
                    String newUserName = scanner.nextLine();
                    System.out.println("Enter email: ");
                    String newUserEmail = scanner.nextLine();
                    System.out.println("Enter password: ");
                    String newUserPassword = scanner.nextLine();

                    BankMessage registrationMessage = BankMessage.newBuilder()
                            .setRegistration(
                                    BankMessage.Registration.newBuilder()
                                            .setFirstName(newUserFirstName)
                                            .setLastName(newUserLastName)
                                            .setUsername(newUserName)
                                            .setEmail(newUserEmail)
                                            .setPassword(newUserPassword)
                                            .build())
                            .build();
                    Pair<IUser, BankMessage> messageFromManager = manager.verifyRegistration(registrationMessage);

                    if (messageFromManager.getKey() != null) {
                        if (messageFromManager.getValue().hasConfirmation()) {
                            System.out.println("Confirmation: " + messageFromManager.getValue().getConfirmation().getMessage());
                        }
                        users.add(messageFromManager.getKey());
                        printUserInfo(messageFromManager.getKey());
                    } else {
                        if (messageFromManager.getValue().hasFailure()) {
                            System.out.println("Failure: " + messageFromManager.getValue().getFailure().getMessage());
                        }
                    }
                    break;

                case "exit":
                    System.exit(0);
            }
        }
    }

    private static IUser getUser(String username) {
        for (IUser user : users) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private static void printUserInfo(IUser user) {
        LOG.info("Username: " + user.getUserName() + " \nAddress: " + user.getCurrentAddress() + "\nBalance: " + user.getWallet().getBalance().toString());
    }
}
