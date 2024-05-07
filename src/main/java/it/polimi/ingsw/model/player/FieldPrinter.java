package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Corner;
import it.polimi.ingsw.model.deck.card.playablecard.corner.CornerType;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Position;
import it.polimi.ingsw.model.deck.card.playablecard.corner.Symbol;
import it.polimi.ingsw.model.exceptions.CoordinatesAreNotValidException;
import it.polimi.ingsw.utilities.Printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FieldPrinter {
    public static final String RESET = Printer.RESET;
    public static final String FungiCard = Printer.RED_BACKGROUND;
    public static final String AnimalCard = Printer.BLUE_BACKGROUND;
    public static final String InsectCard = Printer.PURPLE_BACKGROUND;
    public static final String PlantCard = Printer.GREEN_BACKGROUND;

    public static final String FUNGI = Printer.RED+"██";
    public static final String ANIMAL = Printer.BLUE+"██";
    public static final String INSECT = Printer.PURPLE+"██";
    public static final String PLANT = Printer.GREEN+"██";
    public static final String ItemCorner = Printer.YELLOW+"██";
    public static final String NeutralCorner = Printer.WHITE+"██";

    public static final String PlayableCell = Printer.CYAN_BACKGROUND;
    public static final String StarterCard = Printer.WHITE_BACKGROUND;
    Board board;
    Field field;
    int maxHeight, maxLength;
    Coordinates topLeftBound, botRightBound;
    List<PlayableCard> cardPool;
    public FieldPrinter(){
        maxHeight = 11;
        maxLength = 11;
        field = new Field();
        cardPool = new ArrayList<>();
        Coordinates coordinates = new Coordinates(41,41);
        topLeftBound = new Coordinates(36,36);
        botRightBound = new Coordinates(46,46);
        //crea field
        try {
            board = new Board();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //pesca carte
        cardPool.add(board.getStarterDeck().draw());
        /*for(int i=0; i < 5; i++){
            cardPool.add(board.getResourceDeck().draw());
        }*/
        //posiziona carte
        try {
            field.addCard(cardPool.getFirst(), true, coordinates);
        } catch (CoordinatesAreNotValidException e) {
            throw new RuntimeException(e);
        }
    }

    public void runPrint(){
        Coordinates cell;
        List<Coordinates> validPlays = new ArrayList<>(field.getAllValidCoords());
        String cardColor = null, leftCorner = null, rightCorner = null;
        List<Corner> corners = null;


        for (int i = topLeftBound.y(); i <= botRightBound.y(); i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = topLeftBound.x(); k <= botRightBound.x(); k++) {
                    cell = new Coordinates(k, i);
                    if (field.getPlacedCard(cell) == null) {
                        if (validPlays.contains(cell)) {
                            System.out.print(PlayableCell + "      " + RESET);
                        } else {
                            System.out.print("      ");
                        }
                    } else {
                        PlacedCard card = field.getPlacedCard(cell);
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
                            if (corners.getFirst().type() == CornerType.VISIBLE) {
                                leftCorner = getSymbolName(corners.getFirst().symbol());
                            }
                            if (corners.get(1).type() == CornerType.VISIBLE) {
                                rightCorner = getSymbolName(corners.get(1).symbol());
                            }
                        }
                        else {
                            if (corners.get(2).type() == CornerType.VISIBLE) {
                                leftCorner = getSymbolName(corners.get(2).symbol());
                            }
                            if (corners.get(3).type() == CornerType.VISIBLE) {
                                rightCorner = getSymbolName(corners.get(3).symbol());
                            }
                        }
                        System.out.print(cardColor + leftCorner + "  " + rightCorner + RESET);

                    }
                }
                System.out.print("\n");
            }
        }
    }

    private String getSymbolName(Symbol symbol) {
        if(symbol == null){
          return NeutralCorner;
        } else if (symbol.toString().equalsIgnoreCase("fungi")) {
            return FUNGI;
        } else if (symbol.toString().equalsIgnoreCase("animal")) {
            return ANIMAL;
        } else if (symbol.toString().equalsIgnoreCase("insect")){
            return INSECT;
        } else if (symbol.toString().equalsIgnoreCase("plant")) {
            return PLANT;
        }
        return null;
    }

    public static void main(String[] args) {
        FieldPrinter fieldPrinter = new FieldPrinter();
        fieldPrinter.runPrint();
    }
}
