# Futuristic Keyboard

A native Android Input Method Editor (IME) built with Kotlin and platform Android APIs. It is designed as a privacy-first, offline-first keyboard with QWERTY, numbers, symbols, function keys, emoji, clipboard, navigation/editing tools, themes, configurable animation, sound/haptics, and GitHub Actions APK builds.

## Features
- Real `InputMethodService` using `InputConnection`.
- Letters, numbers, symbols, FN, emoji, clipboard, navigation/editing, and custom macro modes.
- Shift/caps, backspace repeat, enter/IME actions, space, cursor movement, selection, copy/cut/paste, undo/redo.
- Editor-aware enter behavior and password-field-safe operation: no typed text logging or network transmission.
- Dark, Light, AMOLED, Cyber, Glass, Minimal, and Custom theme controls.
- Minimal/Smooth/Cinematic animation levels; effects can be disabled.
- Optional haptics and key sounds.
- Local clipboard history with pin/delete/clear.
- Responsive custom rendering for portrait/landscape and different densities.
- Accessibility labels and reduced-effect behavior where practical.

## Build with GitHub only
Push this repository to GitHub. The workflow in `.github/workflows/build.yml` builds the debug APK and uploads it as an artifact. No Android Studio is required.

Local command (if Gradle/JDK 17 are installed): `./gradlew :app:assembleDebug`.

## Install and enable
1. Download the APK artifact from GitHub Actions.
2. Install the APK on Android.
3. Open **Futuristic Keyboard**.
4. Tap **Enable keyboard** and enable it in Android's system keyboard settings.
5. Return to the app and use **Switch keyboard** when needed, or select it from the keyboard chooser.
6. Configure appearance, animation, layout, typing, and privacy options.

Android may show the standard warning that an input method can process text. This project intentionally has no analytics, no text-upload endpoint, and no network permission. Typed text is processed by the IME locally so it can be committed to the active editor.

## Architecture
`ime/` contains the IME service. `keyboard/` contains layout models, rendering, actions, themes, clipboard and emoji data. `settings/` contains the native settings UI. Preferences are stored with Android `SharedPreferences`.

## Testing
The build workflow validates APK production. Manual testing is still required on physical/emulated Android devices for OEM-specific IME behavior, hardware keyboards, TalkBack, orientation changes, password fields, and app-specific editor implementations.

## Known Android limitations
- An IME cannot force every application to implement unsupported editing operations. For unsupported actions, the keyboard uses the closest `InputConnection`/key-event behavior.
- Function keys, Ctrl/Alt combinations, and selection commands depend on the receiving application.
- Android clipboard privacy rules vary by OS version and foreground state; the keyboard only reads clipboard content while its service is active and stores history locally.
- Emoji glyph appearance comes from the receiving Android font/emoji implementation.
- The emoji panel uses a curated Unicode character set rather than downloading emoji packages.

## License
Apache License 2.0. See `LICENSE`.
