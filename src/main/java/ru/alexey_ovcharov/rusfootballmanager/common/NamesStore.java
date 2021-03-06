package ru.alexey_ovcharov.rusfootballmanager.common;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Alexey
 */
public class NamesStore {

    private static final NamesStore INSTANCE = new NamesStore();
    private final List<String> firstNames;
    private final List<String> lastNames;

    private NamesStore() {
        try (InputStream fNamesStream = getClass().getResourceAsStream("/fnames.txt")) {
            firstNames = fillCollection(fNamesStream);
        } catch (IOException e) {
            throw new FatalError(e);
        }
        try (InputStream lNamesStream = getClass().getResourceAsStream("/lnames.txt")) {
            lastNames = fillCollection(lNamesStream);
        } catch (IOException e) {
            throw new FatalError(e);
        }
    }

    @Nonnull
    private List<String> fillCollection(InputStream is) throws IOException {
        try (BufferedReader firstNamesReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<String> collection = new ArrayList<>();
            while (firstNamesReader.ready()) {
                String line = firstNamesReader.readLine();
                collection.add(line);
            }
            return collection;
        }
    }

    @Nonnull
    public static NamesStore getInstance() {
        return INSTANCE;
    }

    @Nonnull
    public String getRandomFirstName() {
        if (firstNames.isEmpty()) {
            throw new NoSuchElementException();
        }
        return firstNames.get(Randomization.nextInt(firstNames.size()));
    }

    @Nonnull
    public String getRandomLastName() {
        if (lastNames.isEmpty()) {
            throw new NoSuchElementException();
        }
        return lastNames.get(Randomization.nextInt(lastNames.size()));
    }
}
