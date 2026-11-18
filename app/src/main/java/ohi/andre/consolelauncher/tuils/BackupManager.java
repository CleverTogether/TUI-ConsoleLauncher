package ohi.andre.consolelauncher.tuils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Exports T-UI's config folder (settings, aliases, notes, themes, ...) to a single zip file
 * and restores it, so everything can be moved to another phone.
 */
public class BackupManager {

    private static final String MARKER = "tui-backup.txt";

    public static String suggestedName() {
        return "tui-backup-" + new SimpleDateFormat("yyyyMMdd-HHmm", Locale.US).format(new Date()) + ".zip";
    }

    private static boolean skip(String relativePath) {
        return relativePath.equals("crash.txt") || relativePath.equals("old") || relativePath.startsWith("old/");
    }

    public static int export(Context context, Uri destination) throws IOException {
        File folder = Tuils.getFolder();
        if(folder == null) throw new IOException("config folder not available");

        OutputStream os = context.getContentResolver().openOutputStream(destination, "wt");
        if(os == null) throw new IOException("cannot write to the chosen location");

        int count = 0;
        try (ZipOutputStream zip = new ZipOutputStream(os)) {
            zip.putNextEntry(new ZipEntry(MARKER));
            zip.write(("T-UI backup, package " + context.getPackageName() + "\n").getBytes("UTF-8"));
            zip.closeEntry();

            count = addDir(zip, folder, "");
        }
        return count;
    }

    private static int addDir(ZipOutputStream zip, File dir, String prefix) throws IOException {
        File[] files = dir.listFiles();
        if(files == null) return 0;

        int count = 0;
        byte[] buffer = new byte[8192];
        for(File f : files) {
            String rel = prefix + f.getName();
            if(skip(rel)) continue;

            if(f.isDirectory()) {
                count += addDir(zip, f, rel + "/");
            } else {
                zip.putNextEntry(new ZipEntry(rel));
                try (FileInputStream in = new FileInputStream(f)) {
                    int n;
                    while((n = in.read(buffer)) > 0) zip.write(buffer, 0, n);
                }
                zip.closeEntry();
                count++;
            }
        }
        return count;
    }

    public static int restore(Context context, Uri source) throws IOException {
        File folder = Tuils.getFolder();
        if(folder == null) throw new IOException("config folder not available");

        InputStream is = context.getContentResolver().openInputStream(source);
        if(is == null) throw new IOException("cannot read the chosen file");

//        extract aside first, so a wrong or damaged file can't leave a half-restored config
        File tmp = new File(context.getCacheDir(), "tui-import");
        deleteRecursive(tmp);
        if(!tmp.mkdirs()) throw new IOException("cannot create a temporary folder");

        try {
            boolean marker = false;
            byte[] buffer = new byte[8192];
            String tmpPath = tmp.getCanonicalPath() + File.separator;

            try (ZipInputStream zip = new ZipInputStream(is)) {
                ZipEntry entry;
                while((entry = zip.getNextEntry()) != null) {
                    if(entry.getName().equals(MARKER)) marker = true;
                    if(entry.isDirectory() || entry.getName().equals(MARKER)) continue;

                    File out = new File(tmp, entry.getName());
                    if(!out.getCanonicalPath().startsWith(tmpPath)) throw new IOException("invalid file in the backup");
                    if(skip(entry.getName())) continue;

                    File parent = out.getParentFile();
                    if(parent != null && !parent.exists() && !parent.mkdirs()) throw new IOException("cannot create folder");
                    try (FileOutputStream fos = new FileOutputStream(out)) {
                        int n;
                        while((n = zip.read(buffer)) > 0) fos.write(buffer, 0, n);
                    }
                }
            }

            if(!marker) throw new IOException("this is not a T-UI backup");

            return copyDir(tmp, folder);
        } finally {
            deleteRecursive(tmp);
        }
    }

    private static int copyDir(File from, File to) throws IOException {
        File[] files = from.listFiles();
        if(files == null) return 0;
        if(!to.exists() && !to.mkdirs()) throw new IOException("cannot create " + to.getName());

        int count = 0;
        byte[] buffer = new byte[8192];
        for(File f : files) {
            File dest = new File(to, f.getName());
            if(f.isDirectory()) {
                count += copyDir(f, dest);
            } else {
                try (FileInputStream in = new FileInputStream(f); FileOutputStream out = new FileOutputStream(dest)) {
                    int n;
                    while((n = in.read(buffer)) > 0) out.write(buffer, 0, n);
                }
                count++;
            }
        }
        return count;
    }

    private static void deleteRecursive(File f) {
        if(f == null || !f.exists()) return;
        File[] children = f.listFiles();
        if(children != null) for(File c : children) deleteRecursive(c);
        f.delete();
    }
}
