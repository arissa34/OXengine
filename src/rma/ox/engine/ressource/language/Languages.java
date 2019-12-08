package rma.ox.engine.ressource.language;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.Locale;
import rma.ox.engine.settings.SettingsHelper;

public class Languages {

    private static String PATH_STRINGS = "data/lang/strings";
    private static I18NBundle bundle;
    private static Locale locale;

    public static void init() {
        init(PATH_STRINGS);
    }

    public static void init(String path) {
        PATH_STRINGS = path;
        I18NBundle.setExceptionOnMissingKey(false);
        I18NBundle.setSimpleFormatter(true);
        String language = SettingsHelper.get().getLanguage();
        if (language != null && language.contains("default")) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(language);
        }
        bundle = I18NBundle.createBundle(Gdx.files.internal(PATH_STRINGS), locale);
    }

    /**
     * @param language example "en" or "fr"
     */
    public static void changeLanguage(String language){
        if(language == null || language.isEmpty()) return;

        locale = new Locale(language);
        SettingsHelper.get().setLanguage(locale);
        bundle = I18NBundle.createBundle(Gdx.files.internal(PATH_STRINGS), locale);
    }

    /**
     * Libgdx file syntaxe (use {0} for args, start at 0):
     * myKey=Best - My key is {0}
     * <p>
     * in java call: Languages.get("myKey", "Coucou");
     * result: "My key is Coucou"
     * <p>
     * assets/strings/
     * -strings.properties (default file)
     * -strings_fr.properties OR strings_fr_FR.properties
     */
    public static String get(String key, Object... args) {
        if (bundle == null) {
            return "BUNDLE NULL";
        }
        if (key == null || key.isEmpty()) {
            return "KEY NULL OR EMPTY";
        }
        if (args.length > 0) {
            return bundle.format(key, args);
        } else {
            return bundle.get(key);
        }
    }

}
