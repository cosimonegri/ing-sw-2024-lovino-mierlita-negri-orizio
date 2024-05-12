package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.playablecard.*;
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
    public static final String CoveredCorner = Printer.BLACK+"▒▒";
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
        for(int i=0; i < 3; i++){
            cardPool.add(board.getResourceDeck().draw());
        }
        for(int i=3; i < 6; i++){
            cardPool.add(board.getGoldDeck().draw());
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
                                if(availablePlays <= 9 ) { System.out.print(PlayableCell + availablePlays + "     " + RESET); }
                                else { System.out.print(PlayableCell + availablePlays + "    " + RESET); }
                            } else {
                                System.out.print(PlayableCell + "      " + RESET);
                            }
                        } else {
                            System.out.print("      ");
                        }
                    } else {
                        card = field.getPlacedCard(cell);
                        if(j == 0) { h = j; }
                        else { h = j + 1; }
                        boolean isStarter = k == 41 && i == 41;
                        List<Integer> neighborPlacementIndexes = new ArrayList<>();
                        for(int y = -1; y < 2; y+=2) {
                            for(int x = -1; x < 2; x+=2) {
                                if(field.getPlacedCard(new Coordinates(cell.x() + x, cell.y() + y)) != null) {
                                    neighborPlacementIndexes.add(field.getPlacedCard(new Coordinates(cell.x() + x, cell.y() + y)).placementIndex());
                                } else { neighborPlacementIndexes.add(0); }
                            }
                        }

                        printCard(card.card(), h, j, isStarter, card.flipped(), true, card.placementIndex(), neighborPlacementIndexes);
                    }
                }
                System.out.print("\n");
            }
        }

        printPlayerResources();
    }

    private void printPlayerResources() {
        System.out.print("Player's resources: "
                + "\n-Fungi: " + field.getSymbolCount(Resource.FUNGI)
                + "\n-Animals: " + field.getSymbolCount(Resource.ANIMAL)
                + "\n-Plant: " + field.getSymbolCount(Resource.PLANT)
                + "\n-Insects: " + field.getSymbolCount(Resource.INSECT)
                + "\n-Quill: " + field.getSymbolCount(Item.QUILL)
                + "\n-Inkwell: " + field.getSymbolCount(Item.INKWELL)
                + "\n-Manuscript: " + field.getSymbolCount(Item.MANUSCRIPT) + "\n");
    }

    private String getSymbolName(Symbol symbol) {
        if( symbol == null ){
            return NeutralCorner;
        } else if ( symbol.toString().equalsIgnoreCase("fungi")) {
            return FUNGI;
        } else if ( symbol.toString().equalsIgnoreCase("animal")) {
            return ANIMAL;
        } else if ( symbol.toString().equalsIgnoreCase("insect")) {
            return INSECT;
        } else if ( symbol.toString().equalsIgnoreCase("plant")) {
            return PLANT;
        } else if ( symbol.toString().equalsIgnoreCase("manuscript")) {
            return MANUSCRIPT;
        } else if ( symbol.toString().equalsIgnoreCase("inkwell")) {
            return INKWELL;
        } else if ( symbol.toString().equalsIgnoreCase("quill")) {
            return QUILL;
        }
        return "  ";
    }

    public void printHand(){
        System.out.print("\n");
        PlayableCard card;
        for(int j = 0; j < 7; j++){
            int k;
            if(j != 3) {
                for (PlayableCard playableCard : cardPool) {
                    if(j == 0) { k = 0; }
                    else if(j == 2) { k = 2; }
                    else if(j == 4) { k = 4; }
                    else if(j == 6) { k = 6; }
                    else { k = -10; }
                    card = playableCard;
                    printCard(card, k, j, false, j > 3, false, 0,null);
                    System.out.print("   ");
                }
            }
            System.out.print("\n");
        }

        System.out.print("\n");
    }

    private String assignCorner(Corner corner, int k, boolean onField, boolean isLeft, int placementIndex, List<Integer> neighborPlacementIndexes) {
        if (corner.type().name().equalsIgnoreCase("visible")) {
            if (onField) {
                if(isLeft) {
                    if(neighborPlacementIndexes != null && neighborPlacementIndexes.get(k) > placementIndex) {
                        return CoveredCorner;
                    } else {
                        return getSymbolName(corner.symbol()) + SymbolCorner;
                    }
                } else {
                    if(neighborPlacementIndexes != null && neighborPlacementIndexes.get(k) > placementIndex) {
                        return CoveredCorner;
                    } else {
                        return SymbolCorner + getSymbolName(corner.symbol());
                    }
                }
            } else {
                return SymbolCorner + getSymbolName(corner.symbol()) + SymbolCorner;
            }
        } else {
            if (onField) {
                return "  ";
            } else {
                return "   ";
            }
        }
    }

    private void printCard(PlayableCard card, int k, int j, boolean starter, boolean flipped, boolean onField, int placementIndex, List<Integer> neighborPlacementIndexes) {
        String leftCorner = null, rightCorner = null, cardColor;
        List<Corner> corners;

        if(!onField) {
            corners = new ArrayList<>(card.getCorners());
        } else {
            corners = new ArrayList<>();
            for (int c = 0; c < 4; c++) {
                if (!flipped) {
                    corners.add(card.getCorners().get(c));
                } else {
                    corners.add(card.getCorners().get(c + 4));
                }
            }
        }

        if(starter) { cardColor = StarterCard; }
        else { cardColor = getCardColor(card.getColor()); }

        if((j != 1 && j != 5) || onField) {
            leftCorner = assignCorner(corners.get(k), k, onField, true, placementIndex, neighborPlacementIndexes);
            k++;
            rightCorner = assignCorner(corners.get(k), k, onField, false, placementIndex, neighborPlacementIndexes);
        }

        if(flipped && starter) {
            if(j == 0) {
                System.out.print(cardColor + leftCorner + getSymbolName(card.getBackResources().getFirst()));
                if(card.getBackResources().size()>1) {
                    System.out.print(cardColor + getSymbolName(card.getBackResources().get(1)));
                } else {
                    System.out.print(cardColor + " ");
                }
                System.out.print(rightCorner + RESET);
            } else if(j == 1) {
                if(card.getBackResources().size()>2) {
                    System.out.print(cardColor + leftCorner + getSymbolName(card.getBackResources().get(2)) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + "  ");
                }
                System.out.print(cardColor + rightCorner + RESET);
            }
        } else {
            if(onField) {
                System.out.print(cardColor + leftCorner + "  " + rightCorner + RESET);
            } else {
                printCardInHand(card, flipped, cardColor, leftCorner, rightCorner, j);
            }
        }
    }

    private void printCardInHand(PlayableCard card, boolean flipped, String cardColor, String leftCorner, String rightCorner, int j) {
        if(flipped) {
            if(j != 1 && j != 5) {
                System.out.print(cardColor + leftCorner + "      " + rightCorner + RESET);
            } else {
                System.out.print(cardColor + "            " + RESET);
            }
        } else {
            if(j != 1 && j != 5){
                switch (card) {
                    case SimpleGoldCard simpleGoldCard -> {
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + " S " + Printer.BLACK +
                                simpleGoldCard.getPoints() + "  " + rightCorner + RESET);
                        }
                        else{
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(simpleGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case CornerGoldCard cornerGoldCard -> {
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + " C "
                                + Printer.BLACK + cornerGoldCard.getPoints() + "x " + rightCorner + RESET);
                        }
                        else {
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(cornerGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case ItemGoldCard itemGoldCard -> {
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + " I "
                                + Printer.BLACK + itemGoldCard.getPoints() + "x"
                                + itemGoldCard.getItem().name().charAt(0) + rightCorner + RESET);
                        }
                        else {
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(itemGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    default -> System.out.print(cardColor + leftCorner + "      " + rightCorner + RESET);
                }
            } else {
                System.out.print(cardColor + "            " + RESET);
            }
        }
    }

    private String getCardColor(Resource resource) {
        if ("FUNGI".equalsIgnoreCase(resource.name())) {
            return FungiCard;
        } else if ("ANIMAL".equalsIgnoreCase(resource.name())) {
            return AnimalCard;
        } else if ("INSECT".equalsIgnoreCase(resource.name())) {
            return InsectCard;
        } else if ("PLANT".equalsIgnoreCase(resource.name())) {
            return PlantCard;
        }
        return StarterCard;
    }

    private String getGoldCost(GoldCard goldCard) {
        Map<Resource, Integer> cost = new HashMap<>(goldCard.getResourcesNeeded());
        StringBuilder costList = new StringBuilder(Printer.BLACK);
        for(Resource resource : Resource.values()){
            costList.append(getCardColor(resource)).append(cost.get(resource));
        }
        return costList.toString();
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
                if(card instanceof GoldCard goldCard && !goldCard.hasResourcesNeeded(fieldPrinter.field) && !flipped) {
                    System.out.println("Not enough resources to play the card");
                    continue;
                }
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
