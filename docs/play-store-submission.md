# Google Play submission pack — T-UI Console Launcher

Package: `com.clevertogether.tui` (permanent after first upload) · Upload: `dist/tui-console-launcher-release.aab`
Privacy policy source: `docs/privacy-policy.md` (needs a public URL, see "Hosting the policy").

## 1. Store listing

**App name (<=30):** T-UI Console Launcher
**Short description (<=80):** Linux-style terminal launcher: type commands to open apps, call, and more.

**Full description (<=4000):**

T-UI Console Launcher replaces your home screen with a command line. Instead of icons, you type: open apps,
call contacts, check the weather, take notes, play music, and control your device with short commands.

FEATURES
- Type to launch any installed app, with fast autocomplete suggestions
- Call and contact commands
- Notes with optional automatic numbering
- Weather (bring your own free OpenWeatherMap API key)
- Music player, flashlight, Bluetooth and Wi-Fi shortcuts, brightness control
- RSS feed reader and web-page data extraction
- Notification viewer and quick reply from the console
- Built-in file browser and text editor commands
- Fully themeable: colors, fonts, header, notes, toolbar
- A graphical settings screen for every option: type "tui -settings"
- Open source, no ads, no tracking, no account needed

PRIVACY
The app has no ads and no analytics. Your data stays on your device. Optional features such as weather contact
their service directly. See the privacy policy for details.

Open source: https://github.com/CleverTogether/TUI-ConsoleLauncher

**Category:** Personalization · **Tags:** Launcher · **Contact email:** vladimir.mihaylov@clevertogether.com
**Graphics needed (you provide):** app icon 512x512 PNG, feature graphic 1024x500, >=2 phone screenshots
(16:9 or 9:16, min 320px, max 3840px). Take screenshots from the release build.

## 2. App content declarations

- **Privacy policy URL:** see "Hosting the policy".
- **Ads:** No ads.
- **App access:** All functionality available without login or special access.
- **Target audience:** 13+ (not designed for children; do not pick the under-13 groups).
- **Content rating (IARC):** answer the questionnaire truthfully. Expected answers: no violence, no sexual
  content, no gambling, no user-generated content shared between users, does not share location with other
  users. RSS/HTML features display third-party web content; answer the "unrestricted internet access/web content"
  question honestly.
- **News app / Government / Financial / Health / COVID:** No to all.
- **Data Safety:** see section 3.

## 3. Data Safety form (recommended answers)

Rule used: data only processed on-device is **not** "collected". Data sent off-device to a third party is.

- **Does the app collect or share user data?** Yes (location only, see below).
- **Location (approximate and precise):** Collected = Yes; Shared = Yes (OpenWeatherMap, only if the user turns on
  location-based weather); Optional = Yes (user can use a city name instead); Purpose = App functionality.
  Conservative choice: declaring it is safer than under-declaring.
- **Contacts, installed apps, notifications, files/media, phone/network state, camera:** not collected (processed
  on-device only, never transmitted).
- **Personal info, financial info, health, messages, photos/videos, audio, calendar, web history, app activity,
  diagnostics, device IDs:** not collected.
- **Data encrypted in transit:** Yes (cleartext traffic is disabled in the manifest).
- **Users can request data deletion:** developer holds no data; uninstall removes local data.
- **Independent security review / committed to Play Families policy:** No.
- Re-check this form if you add any analytics, ads, or server.

## 4. Permission and policy declarations

**QUERY_ALL_PACKAGES — Core launcher functionality.** Choose "Device search / launcher" use case. Justification:
"The app is a home-screen launcher. It must enumerate all launchable apps to display the app list, match typed
commands to apps, and launch them. This is core launcher functionality."

**MANAGE_EXTERNAL_STORAGE — All files access (HIGHEST REVIEW RISK).** Justification draft: "The app's terminal
lets users navigate and edit any file in shared storage with commands such as cd/ls/cat and a built-in text
editor, similar to a terminal emulator or file manager. Scoped storage cannot provide arbitrary-path shell
navigation." Play's accepted all-files use cases center on file managers, backup, antivirus, document managers
and on-device search; a terminal-style launcher may be questioned. **Fallback if rejected:** remove the
permission from the Play build and limit file commands to the app folder (plus Storage Access Framework for
other locations). Decide before submission whether to ship the fallback by default.

**Foreground service — specialUse** (declared with a property in the manifest): "Keeps the launcher console
running and provides persistent notification access to T-UI commands." Optional feature (the user enables
"show notification that T-UI is running"). Console may ask for a short screen recording of that setting.
**Foreground service — mediaPlayback:** the built-in music player. Console may ask for a recording of playback.

**Notification access (NotificationListenerService):** user-enabled in system settings; shows notifications
inside the console and allows replies. Not transmitted. Disclosed in the privacy policy.

**Other sensitive permissions** (runtime, requested when used): READ/WRITE_CONTACTS (contacts command),
CALL_PHONE (call command, user-initiated), READ_PHONE_STATE (network type in status output),
ACCESS_FINE/COARSE_LOCATION (location/weather), CAMERA (flashlight), WRITE_SETTINGS (brightness),
REQUEST_DELETE_PACKAGES (uninstall command). Optional trim: CAMERA is only needed for the flashlight on
Android 5.0/5.1 (the modern path needs no permission); removing it would simplify review.

## 5. Release and testing

1. Create the app in Play Console (default language, name, free app, declarations).
2. Enroll in Play App Signing (default for new apps). Your `release-upload.jks` is the **upload key**.
3. Upload the AAB to **Internal testing** first and install from the Play link to smoke-test.
4. **Account type matters:** personal developer accounts created after Nov 2023 must run a closed test with
   a minimum number of testers for a minimum number of days before production access (was 12 testers / 14 days;
   verify the current rule in Console). Organization accounts are exempt. Start this early if applicable.
5. Complete every "App content" item until the dashboard shows no warnings, then promote to Production.

## 6. Hosting the policy

Simplest: enable GitHub Pages on this repo (Settings → Pages → Deploy from branch → `master` → `/docs`). The
policy will be at `https://clevertogether.github.io/TUI-ConsoleLauncher/privacy-policy`. The repo must be public
for free Pages. Any public URL works; it must not require login.

## 7. Before every upload

- Raise `versionCode` in `app/build.gradle` (Play rejects a repeat).
- Keep `release-upload.jks` + password backed up; never commit them.
- Keep `mapping.txt` from the build for deobfuscating crash reports (Gradle attaches it to the AAB).
