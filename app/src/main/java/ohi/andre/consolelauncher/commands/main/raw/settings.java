package ohi.andre.consolelauncher.commands.main.raw;

import android.app.Activity;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import ohi.andre.consolelauncher.LauncherActivity;
import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.settings.SettingsActivity;

public class settings implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        Intent intent = new Intent(pack.context, SettingsActivity.class);
        ActivityCompat.startActivityForResult((Activity) pack.context, intent, LauncherActivity.SETTINGS_REQUEST, null);
        return null;
    }

    @Override
    public int helpRes() {
        return R.string.help_settings;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

}
