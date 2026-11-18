package ohi.andre.consolelauncher.commands.main.raw;

import android.app.Activity;
import android.content.Intent;

import ohi.andre.consolelauncher.LauncherActivity;
import ohi.andre.consolelauncher.R;
import ohi.andre.consolelauncher.commands.CommandAbstraction;
import ohi.andre.consolelauncher.commands.ExecutePack;
import ohi.andre.consolelauncher.commands.main.MainPack;
import ohi.andre.consolelauncher.commands.main.specific.ParamCommand;
import ohi.andre.consolelauncher.tuils.BackupManager;
import ohi.andre.consolelauncher.tuils.Tuils;

public class backup extends ParamCommand {

    private enum Param implements ohi.andre.consolelauncher.commands.main.Param {

        export {
            @Override
            public String exec(ExecutePack pack) {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/zip");
                intent.putExtra(Intent.EXTRA_TITLE, BackupManager.suggestedName());
                ((Activity) pack.context).startActivityForResult(intent, LauncherActivity.BACKUP_EXPORT_REQUEST);
                return null;
            }

            @Override
            public int[] args() {
                return new int[0];
            }
        },
        import_ {
            @Override
            public String exec(ExecutePack pack) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                ((Activity) pack.context).startActivityForResult(intent, LauncherActivity.BACKUP_IMPORT_REQUEST);
                return null;
            }

            @Override
            public int[] args() {
                return new int[0];
            }

            @Override
            public String label() {
                return Tuils.MINUS + "import";
            }
        };

        static Param get(String p) {
            p = p.toLowerCase();
            Param[] ps = values();
            for (Param p1 : ps)
                if (p.endsWith(p1.label()))
                    return p1;
            return null;
        }

        static String[] labels() {
            Param[] ps = values();
            String[] ss = new String[ps.length];

            for (int count = 0; count < ps.length; count++) {
                ss[count] = ps[count].label();
            }

            return ss;
        }

        @Override
        public String label() {
            return Tuils.MINUS + name();
        }

        @Override
        public String onNotArgEnough(ExecutePack pack, int index) {
            return pack.context.getString(R.string.help_backup);
        }

        @Override
        public String onArgNotFound(ExecutePack pack, int index) {
            return null;
        }
    }

    @Override
    public String[] params() {
        return Param.labels();
    }

    @Override
    protected ohi.andre.consolelauncher.commands.main.Param paramForString(MainPack pack, String param) {
        return Param.get(param);
    }

    @Override
    protected String doThings(ExecutePack pack) {
        return null;
    }

    @Override
    public int helpRes() {
        return R.string.help_backup;
    }

    @Override
    public int priority() {
        return 2;
    }
}
