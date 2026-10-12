package ohi.andre.consolelauncher.commands.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

/**
 * GUI settings screen: lets the user browse and edit every XMLPrefsSave-backed
 * configuration value (previously only reachable via the "config" terminal command or
 * by hand-editing the XML files under Tuils.getFolder()), each with the description
 * already provided by XMLPrefsSave#info().
 *
 * Changes are written to disk immediately (see SettingsCategoryFragment#writeValue), but
 * the running launcher only reloads once, when this activity is closed (see
 * LauncherActivity#onActivityResult's handling of SETTINGS_REQUEST), to avoid the
 * launcher visibly restarting after every single edit.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final int RESULT_SETTINGS_CHANGED = 1;

    static volatile boolean anyChanged = false;

    private int containerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anyChanged = false;

        FrameLayout container = new FrameLayout(this);
        containerId = View.generateViewId();
        container.setId(containerId);
        setContentView(container);

        // Edge-to-edge is mandatory from API 35+ (unavoidable on API 36+), so this screen's
        // content needs the same manual systemBars() inset handling as the main launcher UI
        // (see UIManager), otherwise its top entries draw underneath the status bar.
        ViewCompat.setOnApplyWindowInsetsListener(container, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            openCategory(null);
        }
    }

    void openCategory(@Nullable String categoryName) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction()
                .replace(containerId, SettingsCategoryFragment.newInstance(categoryName));
        if (categoryName != null) t.addToBackStack(null);
        t.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            setResult(anyChanged ? RESULT_SETTINGS_CHANGED : RESULT_CANCELED);
            finish();
        }
    }
}
