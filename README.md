# ImmediatelyFast Backport for Minecraft 1.16.5

This is a Fabric client backport of ImmediatelyFast for Minecraft 1.16.5.

The project keeps the mod id `immediatelyfast` and uses the latest upstream release version as its base version: `1.15.2+mc1.16.5-backport.2`.

Backported from latest upstream:

- Universal immediate mode batching for 1.16.5 `VertexConsumerProvider.immediate`.
- Map atlas generation, font atlas resizing, fast text lookup, text buffer lookup caching, texture cleanup, redundant framebuffer and viewport suppression, and text translucency sorting skip.
- Legacy HUD batching exists as an experimental opt-in only because it caused a low-load FPS regression and is not part of the latest 1.15.2 default mixin set.
- The official latest jar is much larger mostly because it embeds `Reflect-1.6.2.jar`. This Fabric 1.16.5 backport uses direct platform code and does not need that helper jar.

Build:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat build
```

Output jar:

```text
build/libs/immediatelyfast-1.16.5-1.15.2+mc1.16.5-backport.2.jar
```
