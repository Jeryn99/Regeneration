package me.suff.regeneration.testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.suff.regeneration.RegenerationMod;
import net.minecraftforge.fml.common.Mod;

/**
 * A class containing tests relating to the validity & consistency of <code>gradle.properties</code>, <code>mcmod.info</code>, <code>update.json</code> and the information listed in {@link RegenerationMod}
 * 
 * @author HoldYourWaffle
 */
public class TestMetadataConsistency {
	
	/**
	 * Tests if the version listed in <code>gradle.properties</code> is the same as the internal version inside {@link RegenerationMod}
	 * 
	 * @throws IOException If there's an error reading <code>gradle.properties</code>
	 */
	@Test
	public void testVersion() throws IOException {
		Properties gradleProperties = new Properties();
		gradleProperties.load(new FileInputStream("gradle.properties"));
		
		assertEquals(RegenerationMod.VERSION, gradleProperties.getProperty("mod_version"));
	}
	
	/**
	 * Tests if the forge dependency listed inside {@link RegenerationMod} is the same as the forge version we're listing in <code>gradle.properties</code>
	 * 
	 * @throws IOException If there's an error reading <code>gradle.properties</code>
	 */
	@Test
	public void testForgeDependency() throws IOException {
		Mod[] annotations = RegenerationMod.class.getAnnotationsByType(Mod.class);
		assertEquals("Multiple @Mod annotations", 1, annotations.length);
		
		Properties gradleProperties = new Properties();
		gradleProperties.load(new FileInputStream("gradle.properties"));
		
		Mod mod = annotations[0];
		String forgeBuildDep = gradleProperties.getProperty("forge_version").replace(".", "\\.");
		
		assertTrue(mod.dependencies().matches(".*required:forge@\\[" + forgeBuildDep + ",\\);.*"));
	}
	
	/**
	 * Tests the current <code>update.json</code> file for validity & correctness
	 * 
	 * @throws IOException If <code>gradle.properties</code> or <code>update.json</code> could not be read
	 */
	@Test
	public void updateJsonValid() throws IOException {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new FileReader("update.json"));
		
		Properties gradleProperties = new Properties();
		gradleProperties.load(new FileInputStream("gradle.properties"));
		String mcVersion = gradleProperties.getProperty("minecraft_version");
		
		JsonObject rootObj = element.getAsJsonObject();
		
		assertTrue("Invalid update.json", rootObj.has("homepage") && rootObj.has("promos"));
		assertTrue("Missing versions for current mc version " + mcVersion, rootObj.has(mcVersion));
		
		JsonObject versionObj = rootObj.getAsJsonObject(mcVersion);
		assertTrue("Missing version entry for current version " + mcVersion + "-" + RegenerationMod.VERSION, versionObj.has(RegenerationMod.VERSION));
	}
	
	/**
	 * Tests <code>mcmod.info</code> for consistency & correctness
	 * 
	 * @throws IOException If <code>update.json</code> or <code>mcmod.info</code> could not be read
	 */
	@Test
	public void consistentModinfo() throws IOException {
		JsonParser parser = new JsonParser();
		JsonElement mcmmodRoot = parser.parse(new FileReader("src/main/resources/mcmod.info")),
				updateRoot = parser.parse(new FileReader("update.json"));
		
		JsonArray mcmodRootArr = mcmmodRoot.getAsJsonArray();
		JsonObject mcmodRootObj = mcmodRootArr.get(0).getAsJsonObject();
		
		assertEquals("Inconsistent modid", mcmodRootObj.get("modid").getAsString(), RegenerationMod.MODID);
		assertEquals("Inconsistent modname", mcmodRootObj.get("name").getAsString(), RegenerationMod.NAME);
		assertTrue("Logo file doesn't exist", new File("src/main/resources", mcmodRootObj.get("logoFile").getAsString()).exists());
		
		JsonObject updateRootObj = updateRoot.getAsJsonObject();
		assertEquals("Inconsistent homepage", updateRootObj.get("homepage").getAsString(), mcmodRootObj.get("url").getAsString());
	}
	
}
