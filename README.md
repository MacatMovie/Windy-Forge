# Windy [Forge Port] - NeoForge 1.21.1 fork source

This is an unofficial NeoForge 1.21.1 fork/remake source tree of **Windy** by BonfireMC.

- Original project: https://modrinth.com/mod/windy
- Original source: https://github.com/BonfireMC/Windy
- Original license: LGPL-3.0-only

## Notes

- This fork keeps the original wind particle behavior and client mixin logic.
- The original YACL/Mod Menu config GUI was replaced with a lightweight Forge config screen and JSON config file at `config/windy-config.json` for easier standalone NeoForge 1.21.1 compilation.
- Intended for client-side use.

## Build

Use Java 17, then run:

```powershell
./gradlew build
```

The built jar should appear in `build/libs`.
