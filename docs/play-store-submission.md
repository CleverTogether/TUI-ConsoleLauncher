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

**Category:** Personalization · **Tags:** Launcher · **Contact email:** vladimir@clevertogether.net
**Graphics** (in `docs/store-assets/`):
- Feature graphic 1024x500: `feature-graphic-1024x500.png` (ready).
- App icon 512x512: `icon-512.png` (crisp re-render, near-identical font) or `icon-512-upscaled.png` (your exact
  original, softer because the source is only 192px). Both are the green `>>t-ui` on a dark background, because the
  in-app icon is transparent and would be nearly invisible on Play's white page.
- Phone screenshots (you take them on the device, release build): 2 to 8 images, 16:9 or 9:16, PNG/JPEG.
  Suggested set, in order:
  1. Home screen with the prompt and suggestions visible (the first impression).
  2. Launching an app by typing its name.
  3. Notes with automatic numbering (`notes -add`, then the list).
  4. The graphical settings screen (`tui -settings`).
  5. Weather or status output.
  6. A customised theme (different colors/font) to show it is themeable.
  Rules: use real screens of the real app; no fake device frames or claims; hide personal info (contacts,
  notifications, real names); keep status-bar clutter minimal.

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

**MANAGE_EXTERNAL_STORAGE — NOT requested in the Play build.** Decision (2026-10-30): the permission lives
only in the `fdroid` flavor manifest and the Play build never prompts for it (`BuildConfig.ALL_FILES_ACCESS`
is false). In the Play build the file commands and the text editor work inside the app's own folder
(`Android/data/<package>/files`, the default home path); other locations show "Permission denied" from the
system shell. Do NOT fill in the All files access declaration form in Play Console. If Console still shows the
permission, you uploaded the wrong build.

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
4. **Closed-testing requirement (personal accounts only):** personal developer accounts created after Nov 2023
   must run a closed test before applying for production access: real people with Google accounts, invited by
   email or a Google Group, who opt in and install your app from Play, and stay opted in for a continuous period
   (was 12 testers for 14 days; verify the current numbers in Console). Organization accounts are exempt.

5. Complete every "App content" item until the dashboard shows no warnings, then promote to Production.

## 6. Hosting the policy

Play requires a public URL that opens without login. This repo is public, so GitHub Pages works: Settings → Pages →
Deploy from a branch → `master` → `/docs`. The page `docs/privacy-policy.html` is then served at
`https://clevertogether.github.io/TUI-ConsoleLauncher/privacy-policy.html`. Open the URL in a private window
before pasting it into Play Console.

## 7. Before every upload

- Raise `versionCode` in `app/build.gradle` (Play rejects a repeat).
- Keep `release-upload.jks` + password backed up; never commit them.
- Keep `mapping.txt` from the build for deobfuscating crash reports (Gradle attaches it to the AAB).
