package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import it.polimi.ingsw.model.exceptions.CoordinatesAreNotValidException;
import it.polimi.ingsw.utilities.Printer;

import java.io.IOException;
import java.util.*;

public class FieldPrinter {
    public static final String RESET = Printer.RESET;
    public static final String FungiCard = Printer.RED_BACKGROUND;
    public static final String AnimalCard = Printer.BLUE_BACKGROUND;
    public static final String InsectCard = Printer.PURPLE_BACKGROUND;
    public static final String PlantCard = Printer.GREEN_BACKGROUND;

    public static final String FUNGI = Printer.RED+"█";
    public static final String ANIMAL = Printer.BLUE+"█";
    public static final String INSECT = Printer.PURPLE+"█";
    public static final String PLANT = Printer.GREEN+"█";
    public static final String INKWELL = Printer.BLACK+"I";
    public static final String QUILL = Printer.BLACK+"Q";
    public static final String MANUSCRIPT = Printer.BLACK+"M";
    public static final String SymbolCorner = Printer.YELLOW+"█";
    public static final String NeutralCorner = Printer.YELLOW+"█";
    public static final String PlayableCell = Printer.CYAN_BACKGROUND;
    public static final String StarterCard = Printer.WHITE_BACKGROUND;

    private final Field field;
    private final Coordinates topLeftBound;
    private final Coordinates botRightBound;
    private static List<PlayableCard> cardPool;
    private static Map<Integer, Coordinates> validPlays;
    private final Scanner scanner;
    private int availablePlays;
    public FieldPrinter(){
        this.scanner = new Scanner(System.in);
        field = new Field();
        cardPool = new ArrayList<>();
        Coordinates coordinates = new Coordinates(41,41);
        topLeftBound = new Coordinates(36,36);
        botRightBound = new Coordinates(46,46);
        //create field
        Board board;
        try {
            board = new Board();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //draw cards
        cardPool.add(board.getStarterDeck().draw());
        for(int i=0; i < 5; i++){
            cardPool.add(board.getResourceDeck().draw());
        }
        //position cards
        try {
            field.addCard(cardPool.getFirst(), true, coordinates);
            cardPool.removeFirst();
        } catch (CoordinatesAreNotValidException e) {
            throw new RuntimeException(e);
        }
    }

    public void printField(){
        Coordinates cell;
        List<Coordinates> validCoords = new ArrayList<>(field.getAllValidCoords());
        validPlays = new HashMap<>();
        String cardColor = null, leftCorner = null, rightCorner = null;
        List<Corner> corners = null;
        availablePlays = 0;
        int h;

        for (int i = topLeftBound.y(); i <= botRightBound.y(); i++) {
            PlacedCard card;
            for (int j = 0; j < 2; j++) {
                for (int k = topLeftBound.x(); k <= botRightBound.x(); k++) {
                    cell = new Coordinates(k, i);
                    if (field.getPlacedCard(cell) == null) {
                        if (validCoords.contains(cell)) {
                            if(j == 0) {
                                availablePlays++;
                                validPlays.put(availablePlays, cell);
                                System.out.print(PlayableCell + "  " + availablePlays + "   " + RESET);
                            } else {
                                System.out.print(PlayableCell + "      " + RESET);
                            }
                        } else {
                            System.out.print("      ");
                        }
                    } else {
                        card = field.getPlacedCard(cell);
                        if (card.card().getColor() == null || (k == 41 && i == 41)) { cardColor = StarterCard; }
                        else if("FUNGI".equals(card.card().getColor().name())) { cardColor = FungiCard; }
                        else if ("ANIMAL".equals(card.card().getColor().name())) { cardColor = AnimalCard; }
                        else if ("INSECT".equals(card.card().getColor().name())) { cardColor = InsectCard; }
                        else if ("PLANT".equals(card.card().getColor().name())) { cardColor = PlantCard; }

                        if (j == 0) {
                            corners = new ArrayList<>();
                            for (int c = 0; c < 4; c++) {
                                if (!card.flipped()) {
                                    corners.add(card.card().getCorners().get(c));
                                } else {
                                    corners.add(card.card().getCorners().get(c + 4));
                                }
                            }
                        }

                        if(j == 0) { h = j; }
                        else { h = j + 1; }
                        if(corners.get(h).type().name().equalsIgnoreCase("visible")) {
                            leftCorner = SymbolCorner + getSymbolName(corners.get(h));
                        } else {
                            leftCorner = getSymbolName(corners.get(h));
                        }
                        h++;
                        if(corners.get(h).type().name().equalsIgnoreCase("visible")) {
                            rightCorner = getSymbolName(corners.get(h)) + SymbolCorner;
                        } else {
                            rightCorner = getSymbolName(corners.get(h));
                        }

                        System.out.print(cardColor + leftCorner + "  " + rightCorner + RESET);
                    }
                }
                System.out.print("\n");
            }
        }

        /*System.out.print("Player's resources: "
                + "\n-Animals: " + field.getSymbolCount(Resource.ANIMAL)
                + "\n-Insects: " + field.getSymbolCount(Resource.INSECT)
                + "\n-Plant: " + field.getSymbolCount(Resource.PLANT)
                + "\n-Fungi: " + field.getSymbolCount(Resource.FUNGI)
                + "\n-Inkwell: " + field.getSymbolCount(Item.INKWELL)
                + "\n-Quill: " + field.getSymbolCount(Item.QUILL)
                + "\n-Manuscript: " + field.getSymbolCount(Item.MANUSCRIPT) + "\n");*/

    }

    private String getSymbolName(Corner corner) {
        if(corner.type().equals(CornerType.VISIBLE) && corner.symbol() == null ){
            return NeutralCorner;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("fungi")) {
            return FUNGI;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("animal")) {
            return ANIMAL;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("insect")) {
            return INSECT;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("plant")) {
            return PLANT;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("manuscript")) {
            return MANUSCRIPT;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("inkwell")) {
            return INKWELL;
        } else if (corner.type().equals(CornerType.VISIBLE) && corner.symbol().toString().equalsIgnoreCase("quill")) {
            return QUILL;
        }
        return "  ";
    }

    public void printHand(){
        System.out.print("\n");
        PlayableCard card;
        for(int j = 0; j < 5; j++){

            String cardColor = null, leftCorner, rightCorner;
            int k=0;
            if(j != 2) {
                for (PlayableCard playableCard : cardPool) {
                    if(j == 0) { k = j; }
                    else { k = j + 1; }
                    card = playableCard;
                    if ("FUNGI".equals(card.getColor().name())) {
                        cardColor = FungiCard;
                    } else if ("ANIMAL".equals(card.getColor().name())) {
                        cardColor = AnimalCard;
                    } else if ("INSECT".equals(card.getColor().name())) {
                        cardColor = InsectCard;
                    } else if ("PLANT".equals(card.getColor().name())) {
                        cardColor = PlantCard;
                    }
                    List<Corner> corners = new ArrayList<>(card.getCorners());
                    if(corners.get(k).type().name().equalsIgnoreCase("visible")) {
                        leftCorner = SymbolCorner + getSymbolName(corners.get(k));
                    } else {
                        leftCorner = getSymbolName(corners.get(k));
                    }
                    k++;
                    if(corners.get(k).type().name().equalsIgnoreCase("visible")) {
                        rightCorner = getSymbolName(corners.get(k)) + SymbolCorner;
                    } else {
                        rightCorner = getSymbolName(corners.get(k));
                    }

                    System.out.print(cardColor + leftCorner + "  " + rightCorner + RESET + "   ");
                }

            } //else { System.out.print("\n"); }
            System.out.print("\n");
        }
    }

    public int makeChoice(int min, int max){
        int i;
        while(true) {
            System.out.println("Choose a number between " + min + " and " + max + ":");
            i = scanner.nextInt();
            if(i >= min && i <= max){
                return i;
            } else {
                System.out.println(Printer.RED + "Choose a valid option!" + RESET);
            }
        }
    }

    public boolean chooseFace(){
        boolean flipped;
        while(true) {
            System.out.println("false or true?");
            try {
                flipped = scanner.nextBoolean();
                return flipped;
            } catch (InputMismatchException e){
                System.out.println(Printer.RED + "Type a valid input!" + RESET);
            }
        }
    }

    public static void main(String[] args) {
        FieldPrinter fieldPrinter = new FieldPrinter();
        Coordinates cell;
        PlayableCard card;
        boolean flipped;
        fieldPrinter.printField();
        if(!cardPool.isEmpty()) fieldPrinter.printHand();
        do{
            if(!validPlays.isEmpty()){
                System.out.println("Choose a card");
                card =  cardPool.get(fieldPrinter.makeChoice(1, cardPool.size()) - 1);
                System.out.println("Flipped or not?");
                flipped = fieldPrinter.chooseFace();
                System.out.println("Choose a cell");
                cell = validPlays.get(fieldPrinter.makeChoice(1, fieldPrinter.availablePlays));
                try {
                    fieldPrinter.field.addCard(card, flipped, cell);
                    cardPool.remove(card);
                } catch (CoordinatesAreNotValidException e) {
                    throw new RuntimeException(e);
                }
            }
            fieldPrinter.printField();
            if(!cardPool.isEmpty()) fieldPrinter.printHand();
        }while(!cardPool.isEmpty());

    }
}
