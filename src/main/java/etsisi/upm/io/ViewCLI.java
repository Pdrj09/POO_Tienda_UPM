package etsisi.upm.io;

public class ViewCLI {


    /// messages and help
    //This is the welcome message it is printed when you start the program
    private static final String WELCOME_MESSAGE = """
            Welcome to the ticket module App.
            Ticket module. Type 'help' to see commands.
        """;


    //printed when you exit the program
    private static final String BYE = """
            Closing application.
            Goodbye!
            """;


    //this is printed when you call 'help'
    private static final String COMMANDS_LIST = """
            Commands:
                    client add "<nombre>" <DNI> <email> <cashId>
                    client remove <DNI>
                    client list
                    cash add [<id>] "<nombre>"<email>
                    cash remove <id>
                    cash list
                    cash tickets <id>
                    ticket new [<id>] <cashId> <userId>
                    ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]\s
                    ticket remove <ticketId><cashId> <prodId>\s
                    ticket print <ticketId> <cashId>\s
                    ticket list
                    prod add <id> "<name>" <category> <price>
                    prod update <id> NAME|CATEGORY|PRICE <value>
                    prod addFood [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                    prod addMeeting [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
                    prod list
                    prod remove <id>
                    help
                    echo “<text>”
                    exit
            
            """;
    public static void print(String results){
        System.out.println(results);
    }

    public static void printWellcomeMessage(){
        System.out.println(WELCOME_MESSAGE);
    }

    public static void printHelp(){
        System.out.println(COMMANDS_LIST);
    }

    public static void printExit(){
        System.out.println(BYE);
    }

    public static void echoCommand(String command) {
        System.out.println(command);
    }




}
