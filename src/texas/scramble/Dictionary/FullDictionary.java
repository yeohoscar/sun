package texas.scramble.Dictionary;

public class FullDictionary extends DictionaryTrie {
    private static final String pathToFullDictionary = "resources/full_dict.txt";

    private static FullDictionary cache = null;
    private FullDictionary() {
        super(pathToFullDictionary);
    }

    public static synchronized FullDictionary getFullDictionary() {
        if (cache == null) {
            cache = new FullDictionary();
        }
        return cache;
    }
}
