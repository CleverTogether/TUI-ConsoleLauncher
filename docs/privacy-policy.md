# Privacy Policy for T-UI Console Launcher

**Effective date:** 2026-10-30
**Developer:** CleverTogether
**Contact:** vladimir@clevertogether.net
**Source code:** https://github.com/CleverTogether/TUI-ConsoleLauncher

T-UI Console Launcher ("the app") is an open-source, terminal-style Android launcher. This policy explains what
data the app handles and where it goes.

## Summary

- The developer **does not operate any server** for this app and **does not collect, receive, or store** your data.
- The app has **no accounts, no advertising, and no analytics or crash-reporting services**.
- Almost everything the app does happens **on your device**. A few optional features contact third-party
  services directly from your device; these are listed below.

## Data the app accesses on your device

The app only accesses the following when you use the related feature or grant the related permission. This data is
used to show results in the app and is **not sent to the developer or to any third party by the app**, except
where stated under "Network requests".

| Data | Permission | Used for |
|---|---|---|
| Installed apps list | Query all packages | Showing and launching your apps (core launcher function) |
| Contacts | Read/write contacts | The contacts and call commands |
| Phone number dialing | Call phone | Placing a call only when you run the call command |
| Phone/network state | Read phone state | Showing the current network type in status output |
| Location | Fine/coarse location | Location and weather commands |
| Notifications | Notification access (you enable it in system settings) | Showing and replying to notifications inside the app |
| Files and media | Media audio | The built-in music player. File commands and the text editor work inside the app's own folder |
| Camera | Camera | Controlling the flashlight |
| Bluetooth, Wi-Fi, network state | Bluetooth, Wi-Fi, network | Status and toggle commands |
| System settings | Modify system settings | Brightness command |

Your configuration (themes, settings, notes, command history) is stored in the app's own folder on your device.
A local crash log may be written there (`crash.txt`) and is never uploaded.

## Network requests

The app makes network requests only for the features below, always over HTTPS, and sends them directly from your
device to the service named. The developer does not see these requests.

- **Weather (OpenWeatherMap, api.openweathermap.org):** if you configure weather, the app sends either your city
  name or, if you use location-based weather, your device's latitude and longitude, together with the API key you
  entered. See OpenWeatherMap's privacy policy.
- **Online themes (tui.tarunshankerpandey.com):** if you download a theme, the app sends the theme name to this
  third-party server and receives the theme file.
- **Changelog (pastebin.com):** the app can fetch a changelog text from Pastebin.
- **Connectivity check (clients3.google.com):** the app can make a request to Google to check whether the
  internet is reachable.
- **RSS feeds and HTML extraction:** if you add feeds or web addresses, the app fetches those addresses.
- **Web searches and links:** commands such as search open your browser; what happens next is governed by the
  site you open.

Like any network request, these services can see your device's IP address. We do not control their data
practices; please review their policies.

## Sharing, selling, and retention

The developer does not receive your data, so nothing is shared, sold, or retained by the developer. Third-party
services above handle the requests they receive under their own policies.

## Permissions you control

You can revoke any permission or notification access at any time in Android settings. Features that depend on a
revoked permission will stop working; the rest of the app continues to work.

## Children

The app is not directed at children under 13 and does not knowingly collect any personal information.

## Deleting your data

Because the developer holds no data about you, there is nothing for the developer to delete. Uninstalling the app
removes its local data from your device.

## Changes to this policy

If this policy changes, the updated version will be published at this address with a new effective date.

## Contact

Questions: vladimir@clevertogether.net
