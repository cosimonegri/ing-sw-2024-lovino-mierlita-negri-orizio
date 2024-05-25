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
    private static final String StarterCard = Printer.WHITE_BACKGROUND;
    private static final String FungiCard = Printer.RED_BACKGROUND;
    private static final String AnimalCard = Printer.BLUE_BACKGROUND;
    private static final String InsectCard = Printer.PURPLE_BACKGROUND;
    private static final String PlantCard = Printer.GREEN_BACKGROUND;

    private static final String FUNGI_CORNER = Printer.RED+"█";
    private static final String ANIMAL_CORNER = Printer.BLUE+"█";
    private static final String INSECT_CORNER = Printer.PURPLE+"█";
    private static final String PLANT_CORNER = Printer.GREEN+"█";
    private static final String INKWELL_CORNER = Printer.BLACK+"I";
    private static final String QUILL_CORNER = Printer.BLACK+"Q";
    private static final String MANUSCRIPT_CORNER = Printer.BLACK+"M";

    private static final String NeutralCorner = Printer.BEIGE +"█";
    private static final String CoveredCorner = "  ";
    private static final String PlayableCell = Printer.GRAY_BACKGROUND;

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
                                Coordinates neighborCoords = new Coordinates(cell.x() + dx, cell.y() + dy);
                                if (field.areCoordsOutOfBound(neighborCoords) || field.getPlacedCard(neighborCoords) == null) {
                                    neighborPlacementIndexes.add(0);
                                } else {
                                    neighborPlacementIndexes.add(field.getPlacedCard(neighborCoords).placementIndex());
                                }
                            }
                        }

                        printRow(card.card(), cornerIndex, rowPrinterIndex, card.flipped(), true, card.placementIndex(), neighborPlacementIndexes);
                    }
                }
                System.out.print("\n");
            }
        }
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
    }

    private static void printPlayerResources(FieldView field) {
        System.out.println("Resources available:");
        for (Symbol symbol : Symbol.values()) {
            System.out.println(symbol.toString() + ": " + field.getSymbolCount(symbol));
        }
    }

    private static String getCornerRect(Symbol symbol) {
        if (symbol == null) {
            return NeutralCorner;
        }
        switch (symbol) {
            case Resource.FUNGI -> {
                return FUNGI_CORNER;
            }
            case Resource.ANIMAL -> {
                return ANIMAL_CORNER;
            }
            case Resource.PLANT -> {
                return PLANT_CORNER;
            }
            case Resource.INSECT -> {
                return INSECT_CORNER;
            }
            case Item.INKWELL -> {
                return INKWELL_CORNER;
            }
            case Item.QUILL -> {
                return QUILL_CORNER;
            }
            case Item.MANUSCRIPT -> {
                return MANUSCRIPT_CORNER;
            }
            default -> {
                return NeutralCorner;
            }
        }
    }

    private static String assignCorner(String cardColor, Corner corner, int cornerIndex, boolean onField, boolean isLeft, int placementIndex, List<Integer> neighborPlacementIndexes) {
        if (corner.type() == CornerType.VISIBLE) {
            if (onField) {
                if (neighborPlacementIndexes != null && neighborPlacementIndexes.get(cornerIndex) > placementIndex) {
                    return cardColor + CoveredCorner;
                } else if (corner.symbol() instanceof Item) {
                    if (isLeft) {
                        return getCornerRect(corner.symbol()) + NeutralCorner;
                    } else {
                        return NeutralCorner + getCornerRect(corner.symbol());
                    }
                } else {
                    return getCornerRect(corner.symbol()) + getCornerRect(corner.symbol());
                }
            } else {
                if (corner.symbol() instanceof Item) {
                    return NeutralCorner + getCornerRect(corner.symbol()) + NeutralCorner;
                }
                return getCornerRect(corner.symbol()) + getCornerRect(corner.symbol()) + getCornerRect(corner.symbol());
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
            if(!onField) { printCardInHand(card, true, cardColor, leftCorner, rightCorner, rowPrinterIndex); }
            if(rowPrinterIndex == 0) {
                if(card.getBackResources().size() <= 2) {
                    System.out.print(cardColor + leftCorner + " " + getCornerRect(card.getBackResources().getFirst()) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + getCornerRect(card.getBackResources().getFirst()));
                    System.out.print(cardColor + getCornerRect(card.getBackResources().get(1)));
                    System.out.print(cardColor + getCornerRect(card.getBackResources().get(2)));
                }
                System.out.print(rightCorner + RESET);
            } else if(rowPrinterIndex == 1) {
                if(card.getBackResources().size() == 4) {
                    System.out.print(cardColor + leftCorner + " " + getCornerRect(card.getBackResources().get(3)) + " ");
                } else if(card.getBackResources().size() == 2) {
                    System.out.print(cardColor + leftCorner + " " + getCornerRect(card.getBackResources().get(1)) + " ");
                } else {
                    System.out.print(cardColor + leftCorner + "   ");
                }
                System.out.print(cardColor + rightCorner + RESET);
            }
        } else {
            if(onField) {
                if(flipped) {
                    String backResource = getCornerRect(card.getBackResources().getFirst());
                    System.out.print(cardColor + leftCorner + " " + (rowPrinterIndex == 0 ? backResource : " ")
                            + " " + rightCorner + RESET);
                } else { System.out.print(cardColor + leftCorner + "   " + rightCorner + RESET); }
            } else {
                printCardInHand(card, flipped, cardColor, leftCorner, rightCorner, rowPrinterIndex);
            }
        }
    }

    private static void printCardInHand(PlayableCard card, boolean flipped, String cardColor, String leftCorner, String rightCorner, int rowPrinterIndex) {
        if(flipped) {
            if(rowPrinterIndex != 1 && rowPrinterIndex != 5) {
                if(card instanceof GoldCard && (rowPrinterIndex == 3 || rowPrinterIndex == 6)) {
                    System.out.print(cardColor + leftCorner + "  " + Printer.YELLOW_BACKGROUND + Printer.BLACK + "$$" + cardColor + "  " + rightCorner + RESET);
                } else { System.out.print(cardColor + leftCorner + "      " + rightCorner + RESET); }
            } else {
                if(!card.isStarter()) { System.out.print(cardColor + "            " + RESET);
                } else {
                    System.out.print(cardColor + "     " + getCornerRect(card.getBackResources().getFirst()));
                    if(card.getBackResources().size() > 1) {
                        System.out.print(getCornerRect(card.getBackResources().get(1)));
                    } else { System.out.print(" "); }
                    if(card.getBackResources().size() > 2) {
                        System.out.print(getCornerRect(card.getBackResources().get(2)));
                    } else { System.out.print(" "); }
                    System.out.print("    " + RESET);
                }
            }
        } else {
            if(rowPrinterIndex != 1 && rowPrinterIndex != 5){
                switch (card) {
                    case SimpleGoldCard simpleGoldCard -> {
                        if(rowPrinterIndex == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + "  " + Printer.BLACK +
                                simpleGoldCard.getPoints() + "   " + rightCorner + RESET);
                        }
                        else{
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(simpleGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case CornerGoldCard cornerGoldCard -> {
                        if(rowPrinterIndex == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + "  "
                                + Printer.BLACK + cornerGoldCard.getPoints() + "╔  " + rightCorner + RESET);
                        }
                        else {
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(cornerGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    case ItemGoldCard itemGoldCard -> {
                        if(rowPrinterIndex == 0) { System.out.print(cardColor + leftCorner + Printer.BLACK + " "
                                + Printer.BLACK + itemGoldCard.getPoints() + "x"
                                + itemGoldCard.getItem().name().charAt(0) + "  " + rightCorner + RESET);
                        }
                        else {
                            System.out.print(cardColor + leftCorner + " " + getGoldCost(itemGoldCard) + cardColor + " " + rightCorner + RESET);
                        }
                    }

                    default -> {
                        if (card.getPoints() != 0 && rowPrinterIndex == 0) {
                            System.out.print(cardColor + leftCorner + "  " + Printer.BLACK +
                                    card.getPoints() + "   " + rightCorner + RESET);
                        } else{
                            System.out.print(cardColor + leftCorner + "      " + rightCorner + RESET);
                        }
                    }
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
        return RESET + Printer.YELLOW_BACKGROUND + Printer.BLACK + "$" + placeHolder + costList + Printer.YELLOW_BACKGROUND + Printer.BLACK + "$";
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
}