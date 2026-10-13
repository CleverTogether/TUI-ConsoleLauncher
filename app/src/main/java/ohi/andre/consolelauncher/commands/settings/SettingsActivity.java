package ohi.andre.consolelauncher.commands.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import ohi.andre.consolelauncher.LauncherActivity;

/**
 * GUI settings screen: lets the user browse and edit every XMLPrefsSave-backed
 * configuration value (previously only reachable via the "config" terminal command or
 * by hand-editing the XML files under Tuils.getFolder()), each with the description
 * already provided by XMLPrefsSave#info().
 *
 * Changes are written to disk immediately (see SettingsCategoryFragment#writeValue), but
 * the running launcher only reloads once, when this activity is closed (see
 * finishWithResult()), to avoid the launcher visibly restarting after every single edit.
 */
public class SettingsActivity extends AppCompatActivity {

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

        // Registering an explicit OnBackPressedCallback here -- rather than overriding the
        // legacy Activity#onBackPressed() -- fixes back presses closing this whole screen (and
        // the launcher activity underneath it) in one press instead of stepping back one level
        // at a time. The legacy override relies on Fragment's own back-stack callback correctly
        // intercepting first; this checks the back-stack state directly instead, so it does the
        // right thing regardless of how that interaction behaves.
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finishWithResult();
                }
            }
        });
    }

    void openCategory(@Nullable String categoryName) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction()
                .replace(containerId, SettingsCategoryFragment.newInstance(categoryName));
        if (categoryName != null) t.addToBackStack(null);
        t.commit();
    }

    void finishWithResult() {
        // Explicitly re-targeting LauncherActivity (singleTask, so this resumes its existing
        // instance via onNewIntent() rather than creating a new one) instead of relying on
        // setResult()+finish() returning focus to it implicitly -- that path wasn't reliably
        // resuming the caller, closing the whole app instead.
        Intent intent = new Intent(this, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        if (anyChanged) intent.putExtra(LauncherActivity.EXTRA_RELOAD_AFTER_SETTINGS, true);
        startActivity(intent);
        finish();
    }
}
