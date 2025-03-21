import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String red = "\u001B[31m";   // Red text
        String green = "\u001B[32m"; // Green text
        String reset = "\u001B[0m";  // Reset color
        String yellow = "\u001B[33m"; // Yellow text
        String outputColor;


        Scanner s = new Scanner(System.in);
        Random rand = new Random();

        while (true) {
            System.out.println(yellow + "!!! Starting new Game !!!" + reset);
            char player;
            Grid field = new Grid(6, 7);
            field.gridPrint();

            if (rand.nextBoolean()) {
                player = 'G';
                outputColor = green;
            } else {
                player = 'R';
                outputColor = red;
            }


            while (true) {
                System.out.println();
                System.out.print("Player " + outputColor + player + reset + " turn : ");
                int nextThrow = s.nextInt();

                char winner = field.throwCoin(player, nextThrow);

                if (winner != 'X') {
                    System.out.println("Winner is : " + outputColor + winner + reset);
                    break;
                }

                if (player == 'G') {
                    outputColor = red;
                    player = 'R';
                } else {
                    outputColor = green;
                    player = 'G';
                }
                if (field.freeSlots <= 0) {
                    System.out.println(red + "!!! No free space left !!! \n !!!DRAW!!!" + reset);
                    break;
                }

            }

            String ans = "";
            while (!Objects.equals(ans, "Y") && !Objects.equals(ans, "N")){
                System.out.println("Dow you want to play again? Y/N : ");
                ans = s.nextLine();
            }

            if(ans.equals("N")){
                System.out.println("Thank you for playing!");
                break;
            }

        }

    }

    public static class Grid {

        int freeSlots;
        char[][] slots;

        public Grid(int x, int y) {
            this.freeSlots = x * y;
            this.slots = gridInit(x, y);
        }

        public char[][] gridInit(int x, int y){
            char[][] slots = new char[y][x];

            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    slots[i][j] = 'x';
                }
            }

            return slots;
        }


        public void gridPrint(){

            String red = "\u001B[31m";   // Red text
            String green = "\u001B[32m"; // Green text
            String reset = "\u001B[0m";  // Reset color
            String yellow = "\u001B[33m"; // Yellow text
            String blue = "\u001B[34m";  // Blue text
            String cyan = "\u001B[36m";

            int row = 0;
            System.out.print(blue + "  |" + reset);
            for(int i = 1; i <= this.slots[0].length; i++){
                System.out.print(yellow + i + blue + "|" + reset);
            }

            System.out.println();

            for (char[] x1 : this.slots){
                row++;
                System.out.print(yellow + row + blue + ">" + reset);
                for (char y1 : x1){
                    if (y1 == 'R'){
                        System.out.print(blue + "|" + red + y1 + reset+ "");
                    } else if (y1 == 'G'){
                        System.out.print(blue + "|" + green + y1 + reset + "");
                    } else {
                        System.out.print(blue + "|" + cyan + y1 + reset + "");
                    }
                }
                System.out.println(blue + "|" + reset);

            }
        }

        public char throwCoin(char colour, int x){

            x--;

            if(x >= this.slots[0].length){ return 'X'; }

            for (int i = this.slots[0].length; i >= 0; i--){
                if (this.slots[i][x] == 'x'){
                    this.slots[i][x] = colour;
                    this.freeSlots--;
                    System.out.println("Putting " + colour + " to " + (x+1) + " column, " + (this.slots[0].length - i + 1) + " row.");
                    gridPrint();
                    return checkForWin(x, i, colour);
                }
            }
            System.out.println("Can't Throw!");
            this.freeSlots--;
            return 'X';
        }

        public char checkForWin(int lastThrowX, int lastThrowY, char player){
            int inRowNum = 0;

            // Check Verticals:

            for (char slot : this.slots[lastThrowY]){

                if (slot == player){
                    inRowNum++;
                    if(inRowNum >= 4){ return player; }
                }
                else { inRowNum = 0; }
            }

            inRowNum = 1;

            // Check Horizontals:
            for (char[] slot : this.slots) {
                if (slot[lastThrowX] == player) {
                    inRowNum++;
                    if (inRowNum >= 4) { return player; }
                }
                else { inRowNum = 0; }
            }
            inRowNum = 0;

            // Check Diagonals:
            int startingX = Math.max(lastThrowX - lastThrowY, 0);
            int startingY = Math.max(lastThrowY - lastThrowX, 0);

            // L to R :
            while (startingY < slots.length && startingX < slots[0].length){
                if(slots[startingY][startingX] == player){
                    inRowNum++;
                    if (inRowNum >= 4) { return player; }
                }
                else { inRowNum = 0; }
                startingX++;
                startingY++;
            }

            // R to L :
            startingX = Math.min(lastThrowX + lastThrowY, slots[0].length - 1);
            startingY = Math.max(lastThrowY - (slots[0].length - lastThrowX - 1), 0);
            System.out.println("StartingX: " + startingX + " | StartingY: " + startingY);

            while (startingY < slots.length && startingX >= 0){
                if(slots[startingY][startingX] == player){
                    inRowNum++;
                    System.out.println("InRowNum: " + inRowNum);
                    if (inRowNum >= 4) { return player; }
                }
                else { inRowNum = 0; }
                startingX--;
                startingY++;
            }

            return 'X';
        }

    }

}

