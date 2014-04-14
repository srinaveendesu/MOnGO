package project.finalyear.init;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    final static String SETTINGS_KEY = "settings";

    final static String FOOTWEAR_PREFERENCE = "footwear";
    final static String GENERAL_PREFERENCE = "general";
    final static String WATCHES_PREFERENCE = "watches";
    final static String CLOTHING_PREFERENCE = "clothing";
    final static String ELECTRONICS_PREFERENCE = "electronics";
    final static String ACCESSORIES_PREFERENCE = "accessories";

    private SharedPreferences preferences;

    public Settings(Context context) {
        this.preferences = context.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public void setBoolean(final String key, final boolean value) {
        save(new SaveOperation() {
            @Override
            public void saveValue(SharedPreferences.Editor editor) {
                editor.putBoolean(key, value);
            }
        });
    }

    private interface SaveOperation {
        public void saveValue(SharedPreferences.Editor editor);
    }

    private void save(SaveOperation operation) {
        SharedPreferences.Editor editor = preferences.edit();
        operation.saveValue(editor);
        editor.commit();
    }
}
