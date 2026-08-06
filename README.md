# Chicken Tears

A small standalone Minecraft Forge 1.12.2 mod that backports exactly two modern music discs: **Tears** and **Lava Chicken**.

- **Tears** is obtained by killing a ghast with that ghast's own fireball after the fireball has been deflected by a player.
- **Lava Chicken** is obtained by killing a chicken jockey whose rider is a baby zombie or baby zombie villager.

Both discs use the exact item sprites and Ogg audio from modern Minecraft (Java 1.21.7), and play through the normal 1.12.2 music-disc/jukebox system.

## Building

Requires JDK 8 on `PATH`.

```
./gradlew build
```

## Running in dev

```
./gradlew runClient
```

Or point at a Prism Launcher instance:

```
./gradlew copyJarToPrismMods -PprismModsDir=/absolute/path/to/instance/minecraft/mods
```
