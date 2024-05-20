package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.deck.card.objectivecard.DiagonalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.SymbolsObjectiveCard;
import it.polimi.ingsw.model.deck.card.objectivecard.VerticalPatternObjectiveCard;
import it.polimi.ingsw.model.deck.card.playablecard.*;
import it.polimi.ingsw.model.deck.card.playablecard.corner.*;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.model.player.PlacedCard;
import it.polimi.ingsw.modelView.BoardView;
import it.polimi.ingsw.modelView.FieldView;
import it.polimi.ingsw.utilities.Printer;

import java.util.*;

public class GamePrinter {
    private static final String RESET = Printer.RESET;
    private static final String FungiCard = Printer.RED_BACKGROUND_BRIGHT;
    private static final String AnimalCard = Printer.BLUE_BACKGROUND_BRIGHT;
    private static final String InsectCard = Printer.PURPLE_BACKGROUND_BRIGHT;
    private static final String PlantCard = Printer.GREEN_BACKGROUND_BRIGHT;

    private static final String FUNGI = Printer.RED+"█";
    private static final String ANIMAL = Printer.BLUE+"█";
    private static final String INSECT = Printer.PURPLE+"█";
    private static final String PLANT = Printer.GREEN+"█";
    private static final String INKWELL = Printer.BLACK+"I";
    private static final String QUILL = Printer.BLACK+"Q";
    private static final String MANUSCRIPT = Printer.BLACK+"M";
    private static final String SymbolCorner = Printer.YELLOW+"█";
    private static final String NeutralCorner = Printer.YELLOW+"█";
    private static final String CoveredCorner = "  ";
    private static final String PlayableCell = Printer.CYAN_BACKGROUND;
    private static final String StarterCard = Printer.WHITE_BACKGROUND;

    /**
     * Print the cards in a field and an incremental integer in the cells where the player could place a card
     *
     * @param field field to print
     * @return an object that maps an integer to the coordinates where a card can be played
     */
    public static Map<Integer, Coordinates> printField(FieldView field) {
        Coordinates topLeftCoords = field.getTopLeftBound();
        Coordinates bottomRightCoords = field.getBottomRightBound();
        Coordinates cell;
        PlacedCard card;
        Map<Integer, Coordinates> validPlays = new HashMap<>();
        int availablePlays = 0;
        int cornerIndex;

        // iterate over rows
        for (int y = topLeftCoords.y(); y >= bottomRightCoords.y(); y--) {
            // each row is going to be printed in the lines
            for (int rowPrinterIndex = 0; rowPrinterIndex < 2; rowPrinterIndex++) {
                // iterate over cards in the current row
                for (int x = topLeftCoords.x(); x <= bottomRightCoords.x(); x++) {
                    cell = new Coordinates(x, y);
                    if (field.getPlacedCard(cell) == null) {
                        if (field.getAllValidCoords().contains(cell)) {
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
                        if(rowPrinterIndex == 0) { cornerIndex = rowPrinterIndex; }
                        else { cornerIndex = rowPrinterIndex + 1; }
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

                        printRow(card.card(), cornerIndex, rowPrinterIndex, card.flipped(), true, card.placementIndex(), neighborPlacementIndexes);
                    }
                }
                System.out.print("\n");
            }
        }
        printPlayerResources(field);
        return validPlays;
    }

    /**
     * @param card card to print
     * @param flipped false to show the front of the card, true to show its back
     */
    public static void printCard(PlayableCard card, boolean flipped) {
        printHand(Collections.singletonList(card), flipped);
    }

    /**
     * Prints a list of cards, a player hand
     * @param cards list of cards
     * @param flipped false to show the owning player's hand, true to show an oppenent's hand, thus only the back of the cards
     */
    public static void printHand(List<PlayableCard> cards, boolean flipped) {
        int startingIndex, endingIndex;
        if(flipped) {
            startingIndex = 4;
            endingIndex = 7;
        } else {
            startingIndex = 0;
            endingIndex = 3;
        }
        for (int rawPrinterIndex = startingIndex; rawPrinterIndex < endingIndex; rawPrinterIndex++) {
            int cornerIndex = 0;
            for (PlayableCard card : cards) {
                if (rawPrinterIndex % 2 == 0 && rawPrinterIndex != 0) {
                    cornerIndex = rawPrinterIndex;
                }
                printRow(card, cornerIndex, rawPrinterIndex, flipped, false, 0, null);
                System.out.print("   ");
            }
            System.out.print("\n");
        }
    }

    public static String getObjectiveDescription(ObjectiveCard objective) {
        switch (objective) {
            case SymbolsObjectiveCard symbolsObjectiveCard -> {
                return getSymbolsObjectiveDescr(symbolsObjectiveCard);
            }
            case DiagonalPatternObjectiveCard diagonalPatternObjectiveCard -> {
                return getDiagonalObjectiveDescr(diagonalPatternObjectiveCard);
            }
            case VerticalPatternObjectiveCard verticalPatternObjectiveCard -> {
                return getVerticalObjectiveDescr(verticalPatternObjectiveCard);
            }
            default -> {
                return "";
            }
        }
    }

    public static void printBoard(BoardView board) {
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
                    card = board.getGoldTopCard();
                } else {
                    card = board.getResourceTopCard();
                }
                printRow(card, cornerIndex + 4, boardRowPrinterIndex + 4, true, false, 0, null);
                System.out.print("   ");

                for (PlayableCard visibleCard : Arrays.asList(board.getVisibleCards()).subList(min, max)) {
                    printRow(visibleCard, cornerIndex, boardRowPrinterIndex, false, false, 0, null);
                    System.out.print("   ");
                }
            }
            System.out.print("\n");
        }

        System.out.print("\n");
    }

    private static void printPlayerResources(FieldView field) {
        System.out.print("Player's resources: "
                + "\n-Fungi: " + field.getSymbolCount(Resource.FUNGI)
                + "\n-Animals: " + field.getSymbolCount(Resource.ANIMAL)
                + "\n-Plant: " + field.getSymbolCount(Resource.PLANT)
                + "\n-Insects: " + field.getSymbolCount(Resource.INSECT)
                + "\n-Quill: " + field.getSymbolCount(Item.QUILL)
                + "\n-Inkwell: " + field.getSymbolCount(Item.INKWELL)
                + "\n-Manuscript: " + field.getSymbolCount(Item.MANUSCRIPT) + "\n");
    }

    private static String getSymbolName(Symbol symbol) {
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

    private static String assignCorner(String cardColor, Corner corner, int cornerIndex, boolean onField, boolean isLeft, int placementIndex, List<Integer> neighborPlacementIndexes) {
        if (corner.type() == CornerType.VISIBLE) {
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

    private static void printRow(PlayableCard card, int cornerIndex, int rowPrinterIndex, boolean flipped, boolean onField, int placementIndex, List<Integer> neighborPlacementIndexes) {
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

        cardColor = getCardColor(card.getColor());

        if((rowPrinterIndex != 1 && rowPrinterIndex != 5) || onField) {
            leftCorner = assignCorner(cardColor, corners.get(cornerIndex), cornerIndex, onField, true, placementIndex, neighborPlacementIndexes);
            cornerIndex++;
            rightCorner = assignCorner(cardColor, corners.get(cornerIndex), cornerIndex, onField, false, placementIndex, neighborPlacementIndexes);
        }

        if(flipped && card.isStarter()) {
            if(rowPrinterIndex == 0) {
                if(card.getBackResources().size() <= 2) {
                    System.out.print(cardColor + leftCorner + " " + getSymbolName(card.getBackResources().getFirst()) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + getSymbolName(card.getBackResources().getFirst()));
                    System.out.print(cardColor + getSymbolName(card.getBackResources().get(1)));
                    System.out.print(cardColor + getSymbolName(card.getBackResources().get(2)));
                }
                System.out.print(rightCorner + RESET);
            } else if(rowPrinterIndex == 1) {
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
                    System.out.print(cardColor + leftCorner + " " + (rowPrinterIndex == 0 ? StarterCard + backResource : " ")
                            + cardColor + " " + cardColor + rightCorner + RESET);
                } else { System.out.print(cardColor + leftCorner + "   " + rightCorner + RESET); }
            } else {
                printCardInHand(card, flipped, cardColor, leftCorner, rightCorner, rowPrinterIndex);
            }
        }
    }

    private static void printCardInHand(PlayableCard card, boolean flipped, String cardColor, String leftCorner, String rightCorner, int j) {
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

    private static String getCardColor(Resource resource) {
        if (resource == null) {
            return StarterCard;
        }
        switch (resource) {
            case Resource.FUNGI -> {
                return FungiCard;
            }
            case Resource.ANIMAL -> {
                return AnimalCard;
            }
            case Resource.INSECT -> {
                return InsectCard;
            }
            case Resource.PLANT -> {
                return PlantCard;
            }
            default -> {
                return StarterCard;
            }
        }
    }

    private static String getGoldCost(GoldCard goldCard) {
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

    private static String getSymbolsObjectiveDescr(SymbolsObjectiveCard card) {
        StringBuilder stb = new StringBuilder();
        stb.append(card.getPoints()).append(" points for each set of ");
        int last = card.getSymbols().size() - 1;
        int i = 0;
        for (Symbol symbol : card.getSymbols().keySet()) {
            stb.append(card.getSymbols().get(symbol)).append(" ").append(symbol.toString());
            if (i != last) {
                stb.append(", ");
            }
            i++;
        }
        return stb.toString();
    }

    private static String getDiagonalObjectiveDescr(DiagonalPatternObjectiveCard card) {
        return card.getPoints() + " points for each pattern of 3 " + card.getColor().toString() + " cards " +
                (card.getMainDiagonal() ? "from bottom-left to top-right" : "from top-left to bottom-right");
    }

    private static String getVerticalObjectiveDescr(VerticalPatternObjectiveCard card) {
        return card.getPoints() + " points for each pattern of 2 vertical "
                + card.getMainColor().toString() + " cards and 1 "
                + card.getThirdCardColor().toString() + " card on the "
                + card.getThirdCardPos().toString() + " corner of the column";
    }

//    private int makeChoice(int max){
//        int i;
//        while(true) {
//            System.out.println("Choose a number between " + 1 + " and " + max + ":");
//            i = scanner.nextInt();
//            if(i >= 1 && i <= max){
//                return i;
//            } else {
//                System.out.println(Printer.RED + "Choose a valid option!" + RESET);
//            }
//        }
//    }
//
//    private boolean chooseFace(){
//        boolean flipped;
//        while(true) {
//            System.out.println("false or true?");
//            try {
//                flipped = scanner.nextBoolean();
//                return flipped;
//            } catch (InputMismatchException e){
//                System.out.println(Printer.RED + "Type a valid input!" + RESET);
//            }
//        }
//    }
}