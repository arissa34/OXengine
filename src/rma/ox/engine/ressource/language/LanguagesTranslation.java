package rma.ox.engine.ressource.language;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.Locale;
import rma.ox.engine.settings.SettingsHelper;

public class LanguagesTranslation {

    private String path;
    private I18NBundle bundle;
    private Locale locale;
    private boolean isInternal;

    public LanguagesTranslation(String path, boolean isInternal){
        this.path = path;
        this.isInternal = isInternal;
        init();
    }

    private void init() {
        I18NBundle.setExceptionOnMissingKey(false);
        I18NBundle.setSimpleFormatter(true);
        String language = SettingsHelper.get().getLanguage();
        if (language != null && language.contains("default")) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(language);
        }
        createBundle();
    }

    /**
     * @param language example "en" or "fr"
     */
    public void changeLanguage(String language){
        if(language == null || language.isEmpty()) return;

        locale = new Locale(language);
        SettingsHelper.get().setLanguage(locale);
        createBundle();
    }

    private void createBundle(){
        if(isInternal){
            bundle = I18NBundle.createBundle(Gdx.files.internal(path), locale);
        }else {
            bundle = I18NBundle.createBundle(Gdx.files.local(path), locale);
        }
    }

    /**
     * Libgdx file syntaxe (use {0} for args, start at 0):
     * myKey=Best - My key is {0}
     * <p>
     * in java call: LanguagesTranslation.get("myKey", "Coucou");
     * result: "My key is Coucou"
     * <p>
     * assets/strings/
     * -strings.properties (default file)
     * -strings_fr.properties OR strings_fr_FR.properties
     */
    public String get(String key, Object... args) {
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
