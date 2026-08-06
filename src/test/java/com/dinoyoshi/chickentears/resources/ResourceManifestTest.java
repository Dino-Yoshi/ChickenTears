package com.dinoyoshi.chickentears.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.junit.Test;

public class ResourceManifestTest {
    private static final Path ROOT = Paths.get("");
    private static final Path ASSET_ROOT = ROOT.resolve("src/main/resources/assets/chickentears");

    @Test
    public void langFileContainsRequiredRecordKeys() throws IOException {
        Properties lang = new Properties();
        try (Reader reader = Files.newBufferedReader(ASSET_ROOT.resolve("lang/en_us.lang"), StandardCharsets.UTF_8)) {
            lang.load(reader);
        }

        assertEquals("Music Disc", lang.getProperty("item.record.name"));
        assertEquals("Amos Roddy - Tears", lang.getProperty("item.record.tears.desc"));
        assertEquals("Hyper Potions - Lava Chicken", lang.getProperty("item.record.lava_chicken.desc"));
    }

    @Test
    public void exactlyTwoModelsReferenceExistingTextures() throws IOException {
        Path modelDir = ASSET_ROOT.resolve("models/item");
        Set<String> modelNames = new HashSet<String>();
        modelNames.add("music_disc_tears.json");
        modelNames.add("music_disc_lava_chicken.json");

        int modelCount = 0;
        for (Path modelPath : Files.newDirectoryStream(modelDir, "*.json")) {
            modelCount++;
            assertTrue("unexpected model " + modelPath.getFileName(), modelNames.contains(modelPath.getFileName().toString()));

            JsonObject model = readJson(modelPath);
            assertEquals("item/generated", model.get("parent").getAsString());
            String texture = model.getAsJsonObject("textures").get("layer0").getAsString();
            assertTrue(texture.equals("chickentears:items/music_disc_tears")
                || texture.equals("chickentears:items/music_disc_lava_chicken"));

            String textureName = texture.substring("chickentears:items/".length()) + ".png";
            assertTrue(Files.isRegularFile(ASSET_ROOT.resolve("textures/items").resolve(textureName)));
        }

        assertEquals(2, modelCount);
    }

    @Test
    public void soundsJsonContainsOnlyStreamedRecordSounds() throws IOException {
        JsonObject sounds = readJson(ASSET_ROOT.resolve("sounds.json"));
        assertEquals(2, sounds.entrySet().size());
        assertRecordSound(sounds, "record.tears", "chickentears:records/tears");
        assertRecordSound(sounds, "record.lava_chicken", "chickentears:records/lava_chicken");
    }

    @Test
    public void binaryAssetsMatchManifestHashes() throws Exception {
        JsonObject manifest = readJson(ROOT.resolve("src/test/resources/com/dinoyoshi/chickentears/asset_manifest.json"));
        JsonArray assets = manifest.getAsJsonArray("assets");
        assertEquals(4, assets.size());

        for (JsonElement element : assets) {
            JsonObject asset = element.getAsJsonObject();
            Path destination = ROOT.resolve(asset.get("destinationPath").getAsString());
            assertTrue("missing asset " + destination, Files.isRegularFile(destination));
            assertEquals(asset.get("sha256").getAsString(), sha256(destination));
        }
    }

    @Test
    public void oggAssetsHaveVorbisHeaders() throws IOException {
        assertOggVorbis(ASSET_ROOT.resolve("sounds/records/tears.ogg"));
        assertOggVorbis(ASSET_ROOT.resolve("sounds/records/lava_chicken.ogg"));
    }

    private static void assertRecordSound(JsonObject sounds, String key, String expectedSoundName) {
        JsonObject sound = sounds.getAsJsonObject(key);
        assertNotNull(sound);
        assertEquals("record", sound.get("category").getAsString());

        JsonArray soundEntries = sound.getAsJsonArray("sounds");
        assertEquals(1, soundEntries.size());
        JsonObject soundEntry = soundEntries.get(0).getAsJsonObject();
        assertEquals(expectedSoundName, soundEntry.get("name").getAsString());
        assertTrue(soundEntry.get("stream").getAsBoolean());
    }

    private static void assertOggVorbis(Path path) throws IOException {
        byte[] header = new byte[64];
        int read;
        try (InputStream stream = Files.newInputStream(path)) {
            read = stream.read(header);
        }

        assertTrue("short ogg header " + path, read > 36);
        assertEquals('O', header[0]);
        assertEquals('g', header[1]);
        assertEquals('g', header[2]);
        assertEquals('S', header[3]);
        assertTrue("missing Vorbis marker " + path, contains(header, read, new byte[] {'v', 'o', 'r', 'b', 'i', 's'}));
    }

    private static JsonObject readJson(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    private static String sha256(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = Files.readAllBytes(path);
        byte[] hash = digest.digest(bytes);
        StringBuilder hex = new StringBuilder();
        for (byte value : hash) {
            hex.append(String.format("%02x", value & 0xff));
        }
        return hex.toString();
    }

    private static boolean contains(byte[] haystack, int length, byte[] needle) {
        for (int i = 0; i <= length - needle.length; i++) {
            boolean matches = true;
            for (int j = 0; j < needle.length; j++) {
                if (haystack[i + j] != needle[j]) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                return true;
            }
        }
        return false;
    }
}
