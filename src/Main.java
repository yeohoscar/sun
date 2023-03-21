
public class Main {
    public static void main(String[] args) {
        String[] names = {"Human", "Tom", "Dick", "Harry"};

        System.out.println("\nWelcome to the Automated Poker Machine ...\n\n");

        System.out.print("\nWhat is your name?  ");

        byte[] input = new byte[100];

        try {
            System.in.read(input);

            names[0] = new String(input);
        }
        catch (Exception e){};

        int startingBank = 10;

        System.out.println("\nLet's play POKER ...\n\n");

        GameOfBlackJack game = new GameOfBlackJack(names, startingBank);

        game.play();
    }
}