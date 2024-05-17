package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.*;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import it.polimi.ingsw.model.exceptions.CoordinatesAreNotValidException;
import it.polimi.ingsw.utilities.Printer;

import java.io.IOException;
import java.util.*;

public class GamePrinter {
    public static void main(String[] args) {
        GamePrinter gamePrinter = new GamePrinter();
        Coordinates cell;
        PlayableCard card;
        boolean flipped;
        gamePrinter.printBoard();
        gamePrinter.printField();
        if(!cardPool.isEmpty()) gamePrinter.printHand();
        for(int i = 0; i < 3; i++) {
            switch (objectives.get(i)) {
                case SymbolsObjectiveCard symbolsObjectiveCard ->
                        gamePrinter.printSymbolsObjective(symbolsObjectiveCard);
                case DiagonalPatternObjectiveCard diagonalPatternObjectiveCard ->
                        gamePrinter.printDiagonalObjective(diagonalPatternObjectiveCard);
                case VerticalPatternObjectiveCard verticalPatternObjectiveCard ->
                        gamePrinter.printVerticalObjective(verticalPatternObjectiveCard);
                case null, default -> System.out.println("Not an Objective");
            }
            System.out.print("\n");
        }
        do{
            if(!validPlays.isEmpty()){
                System.out.println("Choose a card");
                card = cardPool.get(gamePrinter.makeChoice(cardPool.size()) - 1);
                System.out.println("Flipped or not?");
                flipped = gamePrinter.chooseFace();
                if(card instanceof GoldCard goldCard && !goldCard.hasResourcesNeeded(gamePrinter.field) && !flipped) {
                    System.out.println("Not enough resources to play the card");
                    continue;
                }
                System.out.println("Choose a cell");
                cell = validPlays.get(gamePrinter.makeChoice(gamePrinter.availablePlays));
                try {
                    gamePrinter.field.addCard(card, flipped, cell);
                    cardPool.remove(card);
                } catch (CoordinatesAreNotValidException e) {
                    throw new RuntimeException(e);
                }
            }
            gamePrinter.printField();
            if(!cardPool.isEmpty()) gamePrinter.printHand();
        }while(!cardPool.isEmpty());
    }

    public static final String RESET = Printer.RESET;
    public static final String FungiCard = Printer.RED_BACKGROUND_BRIGHT;
    public static final String AnimalCard = Printer.BLUE_BACKGROUND_BRIGHT;
    public static final String InsectCard = Printer.PURPLE_BACKGROUND_BRIGHT;
    public static final String PlantCard = Printer.GREEN_BACKGROUND_BRIGHT;

    public static final String FUNGI = Printer.RED+"█";
    public static final String ANIMAL = Printer.BLUE+"█";
    public static final String INSECT = Printer.PURPLE+"█";
    public static final String PLANT = Printer.GREEN+"█";
    public static final String INKWELL = Printer.BLACK+"I";
    public static final String QUILL = Printer.BLACK+"Q";
    public static final String MANUSCRIPT = Printer.BLACK+"M";
    public static final String SymbolCorner = Printer.YELLOW+"█";
    public static final String NeutralCorner = Printer.YELLOW+"█";
    public static final String CoveredCorner = "  ";
    public static final String PlayableCell = Printer.CYAN_BACKGROUND;
    public static final String StarterCard = Printer.WHITE_BACKGROUND;

    private final Field field;
    private final Board board;
    private final Coordinates botLeftBound;
    private final Coordinates topRightBound;
    private static List<PlayableCard> cardPool;
    private static List<ObjectiveCard> objectives;
    private static List<PlayableCard> visibleCards;
    private static Map<Integer, Coordinates> validPlays;
    private final Scanner scanner;
    private int availablePlays;
    public GamePrinter(){
        this.scanner = new Scanner(System.in);
        field = new Field();
        cardPool = new ArrayList<>();
        objectives = new ArrayList<>();
        Coordinates coordinates = new Coordinates(40,40);
        botLeftBound = new Coordinates(35,35);
        topRightBound = new Coordinates(45,45);
        //create field
        try {
            board = new Board();
            visibleCards = new ArrayList<>(List.of(board.getVisibleCards()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //draw cards
        cardPool.add(board.getStarterDeck().draw());
        for(int i = 0; i < 3; i++){ cardPool.add(board.getResourceDeck().draw()); }
        for(int i = 3; i < 6; i++){ cardPool.add(board.getGoldDeck().draw()); }
        for(int i = 0; i < 3; i++){ objectives.add(board.getObjectiveDeck().draw()); }
        //position cards
        try {
            field.addCard(cardPool.getFirst(), true, coordinates);
            cardPool.removeFirst();
        } catch (CoordinatesAreNotValidException e) {
            throw new RuntimeException(e);
        }
    }

    //printField(FieldView field)
    public void printField(){
        Coordinates cell;
        List<Coordinates> validCoords = new ArrayList<>(field.getAllValidCoords());
        validPlays = new HashMap<>();
        availablePlays = 0;
        int h;

        //columns
        for (int y = topRightBound.y(); y >= botLeftBound.y(); y--) {
            PlacedCard card;
            for (int rowPrinterIndex = 0; rowPrinterIndex < 2; rowPrinterIndex++) {
                //rows
                for (int x = botLeftBound.x(); x <= topRightBound.x(); x++) {
                    cell = new Coordinates(x, y);
                    if (field.getPlacedCard(cell) == null) {
                        if (validCoords.contains(cell)) {
                            if(rowPrinterIndex == 0) {
                                availablePlays++;
                                validPlays.put(availablePlays, cell);
                                if(availablePlays <= 9 ) { System.out.print(PlayableCell + availablePlays + "      " + RESET); }
                                else { System.out.print(PlayableCell + availablePlays + "     " + RESET); }
                            } else {
                                System.out.print(PlayableCell + "       " + RESET);
                            }
                        } else {
                            System.out.print("       ");
                        }
                    } else {
                        card = field.getPlacedCard(cell);
                        if(rowPrinterIndex == 0) { h = rowPrinterIndex; }
                        else { h = rowPrinterIndex + 1; }
                        boolean isStarter = x == 40 && y == 40;
                        List<Integer> neighborPlacementIndexes = new ArrayList<>();
                        for(int dy = 1; dy >= -2; dy-=2) {
                            for(int dx = -1; dx < 2; dx+=2) {
                                if(field.getPlacedCard(new Coordinates(cell.x() + dx, cell.y() + dy)) != null) {
                                    neighborPlacementIndexes.add(
                                            field.getPlacedCard(
                                                    new Coordinates(cell.x() + dx, cell.y() + dy)).placementIndex());
                                } else { neighborPlacementIndexes.add(0); }
                            }
                        }

                        printRow(card.card(), h, rowPrinterIndex, isStarter, card.flipped(), true, card.placementIndex(), neighborPlacementIndexes);
                    }
                }
                System.out.print("\n");
            }
        }
        printPlayerResources();
    }

    public void printHand() {
        System.out.print("\n");
        PlayableCard card;
        for (int rawPrinterIndex = 0; rawPrinterIndex < 7; rawPrinterIndex++) {
            int cornerIndex = 0;
            if (rawPrinterIndex != 3) {
                for (PlayableCard playableCard : cardPool) {
                    if (rawPrinterIndex % 2 == 0 && rawPrinterIndex != 0) {
                        cornerIndex = rawPrinterIndex;
                    }
                    card = playableCard;
                    printRow(card, cornerIndex, rawPrinterIndex, false, rawPrinterIndex > 3, false, 0, null);
                    System.out.print("   ");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public void printBoard(){
        System.out.print("\n");
        PlayableCard card;

        for(int rowPrinterIndex = 0; rowPrinterIndex < 7; rowPrinterIndex++) {
            int min = -1, max = -1;
            if(rowPrinterIndex < 3) {
                min = 0;
                max = 2;
            } else if(rowPrinterIndex > 3){
                min = 2;
                max = 4;
            }

            int cornerIndex = 0;
            int boardRowPrinterIndex;
            if(rowPrinterIndex != 3){
                if(rowPrinterIndex < 3) { boardRowPrinterIndex = rowPrinterIndex; }
                else { boardRowPrinterIndex = rowPrinterIndex - 4; }
                if (rowPrinterIndex % 2 == 0 && rowPrinterIndex != 0) {
                    if(rowPrinterIndex < 3) { cornerIndex = rowPrinterIndex; }
                    else { cornerIndex = boardRowPrinterIndex; }
                }
                if (rowPrinterIndex < 3) {
                    card = board.getGoldDeck().getCards().getFirst();
                } else {
                    card = board.getResourceDeck().getCards().getFirst();
                }
                printRow(card, cornerIndex + 4, boardRowPrinterIndex + 4, false, true, false, 0, null);
                System.out.print("   ");

                for (PlayableCard playableCard : visibleCards.subList(min, max)) {
                    card = playableCard;
                    printRow(card, cornerIndex, boardRowPrinterIndex, false, false, false, 0, null);
                    System.out.print("   ");
                }
            }
            System.out.print("\n");
        }

        System.out.print("\n");
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

    private String assignCorner(String cardColor, Corner corner, int cornerIndex, boolean onField, boolean isLeft, int placementIndex, List<Integer> neighborPlacementIndexes) {
        if (corner.type().name().equalsIgnoreCase("visible")) {
            if (onField) {
                if(isLeft) {
                    if(neighborPlacementIndexes != null && neighborPlacementIndexes.get(cornerIndex) > placementIndex) {
                        return cardColor + CoveredCorner;
                    } else {
                        return getSymbolName(corner.symbol()) + SymbolCorner;
                    }
                } else {
                    if(neighborPlacementIndexes != null && neighborPlacementIndexes.get(cornerIndex) > placementIndex) {
                        return cardColor + CoveredCorner;
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

    private void printRow(PlayableCard card, int cornerIndex, int j, boolean starter, boolean flipped, boolean onField, int placementIndex, List<Integer> neighborPlacementIndexes) {
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
            leftCorner = assignCorner(cardColor, corners.get(cornerIndex), cornerIndex, onField, true, placementIndex, neighborPlacementIndexes);
            cornerIndex++;
            rightCorner = assignCorner(cardColor, corners.get(cornerIndex), cornerIndex, onField, false, placementIndex, neighborPlacementIndexes);
        }

        if(flipped && starter) {
            if(j == 0) {
                if(card.getBackResources().size() <= 2) {
                    System.out.print(cardColor + leftCorner + " " + getSymbolName(card.getBackResources().getFirst()) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + getSymbolName(card.getBackResources().getFirst()));
                    System.out.print(cardColor + getSymbolName(card.getBackResources().get(1)));
                    System.out.print(cardColor + getSymbolName(card.getBackResources().get(2)));
                }
                System.out.print(rightCorner + RESET);
            } else if(j == 1) {
                if(card.getBackResources().size() == 4) {
                    System.out.print(cardColor + leftCorner + " " + getSymbolName(card.getBackResources().get(3)) + " ");
                } else if(card.getBackResources().size() == 2) {
                    System.out.print(cardColor + leftCorner + " " + getSymbolName(card.getBackResources().get(1)) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + "   ");
                }
                System.out.print(cardColor + rightCorner + RESET);
            }
        } else {
            if(onField) {
                if(flipped) {
                    String backResource = getSymbolName(card.getBackResources().getFirst());
                    System.out.print(cardColor + leftCorner + " " + (j == 0 ? StarterCard + backResource : " ")
                            + cardColor + " " + cardColor + rightCorner + RESET);
                } else { System.out.print(cardColor + leftCorner + "   " + rightCorner + RESET); }
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
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + "  " + Printer.BLACK +
                                simpleGoldCard.getPoints() + "   " + rightCorner + RESET);
                        }
                        else{
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(simpleGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case CornerGoldCard cornerGoldCard -> {
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + "  "
                                + Printer.BLACK + cornerGoldCard.getPoints() + "╔  " + rightCorner + RESET);
                        }
                        else {
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(cornerGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case ItemGoldCard itemGoldCard -> {
                        if(j == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + " "
                                + Printer.BLACK + itemGoldCard.getPoints() + "x"
                                + itemGoldCard.getItem().name().charAt(0) + "  " + rightCorner + RESET);
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
        int resourceTypes = 0;
        String placeHolder = "";
        for(Resource resource : Resource.values()){
            if(cost.get(resource) != 0) {
                costList.append(getCardColor(resource)).append(Printer.BLACK).append(cost.get(resource));
                resourceTypes++;
            }
        }
        if(resourceTypes == 1) { placeHolder = " "; }
        return RESET + Printer.YELLOW_BACKGROUND_BRIGHT + Printer.BLACK + "$" + placeHolder + costList + Printer.YELLOW_BACKGROUND_BRIGHT + Printer.BLACK + "$";
    }

    private void printSymbolsObjective(SymbolsObjectiveCard objectiveCard) {
        System.out.print("Objective: " + objectiveCard.getPoints() + " points for each set of: ");

        for(Resource resource : Resource.values()) {
            if(objectiveCard.getSymbols().containsKey(resource)) {
                System.out.print(objectiveCard.getSymbols().get(resource) + " " + resource.name() + " ");
            }
        }

        for(Item item : Item.values()) {
            if(objectiveCard.getSymbols().containsKey(item)) {
                System.out.print(objectiveCard.getSymbols().get(item) + " " + item.name() + " ");
            }
        }

        System.out.print("\n");
    }

    private void printDiagonalObjective(DiagonalPatternObjectiveCard objectiveCard) {
        System.out.print("Objective: " + objectiveCard.getPoints() + " points for each pattern of: 3 ");

        if(!objectiveCard.getMainDiagonal()) {
            System.out.print(objectiveCard.getColor().name() + " from top left to bottom right ");
        } else {
            System.out.print(objectiveCard.getColor().name() + " from bottom left to top right ");
        }

        System.out.print("\n");
    }

    private void printVerticalObjective(VerticalPatternObjectiveCard objectiveCard) {
        System.out.print("Objective: " + objectiveCard.getPoints() + " points for each pattern of: 2 vertical "
                        + objectiveCard.getMainColor().name()
                        + " and 1 " + objectiveCard.getThirdCardColor().name() + " on the "
                        + objectiveCard.getThirdCardPos().name() + " corner of the column\n");
    }

    private int makeChoice(int max){
        int i;
        while(true) {
            System.out.println("Choose a number between " + 1 + " and " + max + ":");
            i = scanner.nextInt();
            if(i >= 1 && i <= max){
                return i;
            } else {
                System.out.println(Printer.RED + "Choose a valid option!" + RESET);
            }
        }
    }

    private boolean chooseFace(){
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
}