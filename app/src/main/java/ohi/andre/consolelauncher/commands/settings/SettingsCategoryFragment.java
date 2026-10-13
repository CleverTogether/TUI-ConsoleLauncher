package ohi.andre.consolelauncher.commands.settings;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import ohi.andre.consolelauncher.managers.xml.XMLPrefsManager;
import ohi.andre.consolelauncher.managers.xml.classes.XMLPrefsSave;
import ohi.andre.consolelauncher.managers.xml.options.Apps;
import ohi.andre.consolelauncher.managers.xml.options.Behavior;
import ohi.andre.consolelauncher.managers.xml.options.Cmd;
import ohi.andre.consolelauncher.managers.xml.options.Notifications;
import ohi.andre.consolelauncher.managers.xml.options.Reply;
import ohi.andre.consolelauncher.managers.xml.options.Rss;
import ohi.andre.consolelauncher.managers.xml.options.Suggestions;
import ohi.andre.consolelauncher.managers.xml.options.Theme;
import ohi.andre.consolelauncher.managers.xml.options.Toolbar;
import ohi.andre.consolelauncher.managers.xml.options.Ui;

/**
 * One screen of the settings GUI: either the root list of categories (categoryName ==
 * null) or the list of individual settings for one category, built generically from
 * whatever XMLPrefsSave[] that category's enum returns -- nothing here is hand-written
 * per setting.
 */
public class SettingsCategoryFragment extends PreferenceFragmentCompat {

    private static final String ARG_CATEGORY = "category";

    private static final String[] CATEGORY_NAMES = {
            "Behavior", "UI", "Theme", "Suggestions", "Toolbar", "Apps", "Notifications", "Rss", "Reply", "Cmd"
    };

    private Preference restartPreference;

    static SettingsCategoryFragment newInstance(@Nullable String categoryName) {
        SettingsCategoryFragment f = new SettingsCategoryFragment();
        Bundle b = new Bundle();
        b.putString(ARG_CATEGORY, categoryName);
        f.setArguments(b);
        return f;
    }

    private static XMLPrefsSave[] valuesFor(String categoryName) {
        switch (categoryName) {
            case "Behavior": return Behavior.values();
            case "UI": return Ui.values();
            case "Theme": return Theme.values();
            case "Suggestions": return Suggestions.values();
            case "Toolbar": return Toolbar.values();
            case "Apps": return Apps.values();
            case "Notifications": return Notifications.values();
            case "Rss": return Rss.values();
            case "Reply": return Reply.values();
            case "Cmd": return Cmd.values();
            default: return new XMLPrefsSave[0];
        }
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        String categoryName = getArguments() != null ? getArguments().getString(ARG_CATEGORY) : null;

        PreferenceManager manager = getPreferenceManager();
        PreferenceScreen screen = manager.createPreferenceScreen(requireContext());

        if (categoryName == null) {
            requireActivity().setTitle("Settings");

            restartPreference = new Preference(requireContext());
            restartPreference.setKey("restart_tui");
            restartPreference.setTitle("Restart TUI to apply changes");
            restartPreference.setSummary("You've changed one or more settings -- tap to restart and apply them");
            restartPreference.setOnPreferenceClickListener(pref -> {
                ((SettingsActivity) requireActivity()).finishWithResult();
                return true;
            });
            screen.addPreference(restartPreference);
            updateRestartVisibility();

            for (String name : CATEGORY_NAMES) {
                Preference p = new Preference(requireContext());
                p.setKey("category_" + name);
                p.setTitle(name);
                p.setSummary(valuesFor(name).length + " settings");
                p.setOnPreferenceClickListener(pref -> {
                    ((SettingsActivity) requireActivity()).openCategory(name);
                    return true;
                });
                screen.addPreference(p);
            }
        } else {
            requireActivity().setTitle(categoryName);

            for (XMLPrefsSave save : valuesFor(categoryName)) {
                screen.addPreference(buildPreference(save));
            }
        }

        setPreferenceScreen(screen);
    }

    private Preference buildPreference(XMLPrefsSave save) {
        String type = save.type();

        if (XMLPrefsSave.BOOLEAN.equals(type)) {
            SwitchPreferenceCompat p = new SwitchPreferenceCompat(requireContext());
            p.setKey(save.label());
            p.setTitle(save.label());
            p.setSummary(save.info());
            p.setPersistent(false);
            p.setChecked(XMLPrefsManager.getBoolean(save));
            p.setOnPreferenceChangeListener((pref, newValue) -> {
                writeValue(save, String.valueOf(newValue));
                return true;
            });
            return p;
        }

        EditTextPreference p = new EditTextPreference(requireContext());
        p.setKey(save.label());
        p.setTitle(save.label());
        p.setSummary(save.info());
        p.setDialogTitle(save.label());
        p.setPersistent(false);
        p.setText(XMLPrefsManager.get(save));

        if (XMLPrefsSave.INTEGER.equals(type)) {
            p.setOnBindEditTextListener(et -> et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
        } else if (XMLPrefsSave.COLOR.equals(type)) {
            applyColorSwatch(p, XMLPrefsManager.get(save));
        }

        p.setOnPreferenceChangeListener((pref, newValue) -> {
            String value = String.valueOf(newValue);

            String[] invalid = save.invalidValues();
            if (invalid != null) {
                for (String iv : invalid) {
                    if (iv.equals(value)) {
                        Toast.makeText(requireContext(), save.label() + ": that value isn't allowed", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

            if (XMLPrefsSave.INTEGER.equals(type)) {
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), save.label() + " must be a whole number", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (XMLPrefsSave.COLOR.equals(type)) {
                try {
                    Color.parseColor(value);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(requireContext(), save.label() + " must be a valid color (e.g. #RRGGBB)", Toast.LENGTH_SHORT).show();
                    return false;
                }
                applyColorSwatch(p, value);
            }

            writeValue(save, value);
            return true;
        });

        return p;
    }

    private void applyColorSwatch(Preference p, String hex) {
        try {
            int color = Color.parseColor(hex);
            GradientDrawable swatch = new GradientDrawable();
            swatch.setShape(GradientDrawable.OVAL);
            swatch.setColor(color);
            swatch.setSize(48, 48);
            p.setIcon(swatch);
        } catch (Exception e) {
            // leave the default icon if the stored value isn't parseable as a color
        }
    }

    private void writeValue(XMLPrefsSave save, String value) {
        save.parent().write(save, value);
        SettingsActivity.anyChanged = true;
        updateRestartVisibility();
    }

    private void updateRestartVisibility() {
        if (restartPreference != null) {
            restartPreference.setVisible(SettingsActivity.anyChanged);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Catches the case where a change was made in a different category screen and the
        // user has since popped back to this (root) screen -- onCreatePreferences only runs
        // once per fragment instance, so this is what keeps the button's visibility current.
        updateRestartVisibility();
    }
}
