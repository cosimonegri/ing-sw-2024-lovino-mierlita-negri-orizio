package it.polimi.ingsw.model.deck;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 *
 * @param <T> the type of card to be used
 */
public class Deck<T> { // TODO: Deck<T extends Card>
    private final List<T> cards;

    /**
     * Create a list of cards from a JSON file and initialize it
     * @param file the JSON file to be read
     * @throws IOException if the read fails
     */
    public Deck(String file) throws IOException {
        // Parse the JSON file
        Gson gson = new Gson();
        Reader reader;
        reader = Files.newBufferedReader(Paths.get(file));
        List<T> cards = new Gson().fromJson(reader, new TypeToken<List<T>>() {}.getType());
        // close reader
        reader.close();

        this.cards = cards;
        Collections.shuffle(cards);
    }
    public T draw() {
        if (cards.isEmpty()) throw new IllegalStateException("Deck is empty");
        return cards.removeFirst();
    }

    public List<T> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
