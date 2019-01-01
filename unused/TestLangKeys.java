package me.fril.regeneration.testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import net.minecraftforge.common.config.Config;
import scala.actors.threadpool.Arrays;

@RunWith(Parameterized.class)
public class TestLangKeys {
	
	//TODO test language keys used in code
	//TODO check for unused language keys
	
	private static final Map<Field, String> configMap;
	private static final List<String> langKeyReferences;
	
	static {
		try {
			configMap = initConfigMap();
			langKeyReferences = initLangKeyRefs();
		} catch (Exception e) {
			throw new Error("Could not initialise language key tests", e);
		}
	}
	
	private static Map<Field, String> initConfigMap() {
		Class<RegenConfig> configClass = RegenConfig.class;
		List<Field> configEntries = new ArrayList<>();
		
		for (Class<?> category : configClass.getDeclaredClasses())
			configEntries.addAll(Arrays.asList(category.getFields()));
		configEntries.addAll(Arrays.asList(configClass.getFields()));
		configEntries.removeIf(f->!f.isAnnotationPresent(Config.LangKey.class));
		
		return configEntries.stream().collect(Collectors.toMap(f->f, f->f.getAnnotation(Config.LangKey.class).value()));
	}
	
	private static List<String> initLangKeyRefs() throws IOException {
		List<String> refs = new ArrayList<>();
		
		/*List<String> paths = Arrays.asList(System.getProperty("java.class.path").split(System.getProperty("path.separator")));
		List<TypeSolver> solvers = paths.stream().filter(s->s.endsWith(".jar")).map(s->{
			try {
				return new JarTypeSolver(s);
			} catch (IOException e) {
				throw new Error("Could not initialise classpath JarTypeSolver's", e);
			}
		}).collect(Collectors.toList());
		solvers.add(new JavaParserTypeSolver("src/main/java/"));
		solvers.add(new ReflectionTypeSolver(false));
		
		TypeSolver typeSolver = new CombinedTypeSolver(solvers.toArray(new TypeSolver[0]));*/
		TypeSolver typeSolver = new ReflectionTypeSolver(false);
		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
		
		Files.find(Paths.get("src", "main", "java"), Integer.MAX_VALUE,
				(path, attr)->attr.isRegularFile() && path.toString().endsWith(".java"))
				.map(TestLangKeys::assertParse)
				.forEach(source-> {
			//ObjectCreationExpr
			//new TextComponentTranslation(translationKey, args)
			//I18n.format(translateKey, parameters)
			
			//NOW move to a findType -> if no annotation -> findMethods
			
			for (MethodCallExpr mce : source.findAll(MethodCallExpr.class)) {
				
				ResolvedMethodDeclaration rmd;
				try {
					rmd = mce.resolve();
					//System.out.println("\t"+rmd.getClassName() + "." + rmd.getName());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(mce.getNameAsString());
					System.out.println(source.getType(0).getNameAsString());
					System.out.println(mce.getBegin().get());
					throw new Error("Error solving", e);
				}
				
				//mce.getArguments().forEach(e->System.out.println("\t"+e.toString()));
				//System.out.println();
			}
		});
		
		return refs;
	}
	
	
	@Parameters
	public static List<File[]> languageFiles() {
		List<File[]> list = new ArrayList<>();
		
		for (File f : new File("src/main/resources/assets/"+RegenerationMod.MODID+"/lang").listFiles()) {
			assertTrue(f.isFile()); //TODO give message
			assertEquals(f.getName().toLowerCase(), f.getName());
			assertTrue(f.getName().endsWith(".lang"));
			list.add(new File[] { f });
		}
		
		return list;
	}
	
	@Parameter
	public File langFile;
	
	
	
	@Test
	public void testConfig() throws IOException {
		Properties langMap = new Properties();
		langMap.load(new FileInputStream(langFile));
		
		for (Entry<Field, String> entries : configMap.entrySet()) {
			String fieldName = entries.getKey().getName(), langKey = entries.getValue();
			
			assertTrue("Inconsistent config language key, doesn't start with 'config.regeneration.' prefix: "+langKey, langKey.startsWith("config.regeneration."));
			//assertEquals("Inconsistent config language key, contains uppercase characters: "+langKey, langKey.toLowerCase(), langKey); TODO fix
			assertTrue("Missing language key for config option "+fieldName+" ("+langKey+")", langMap.containsKey(langKey));
		}
	}
	
	@Test
	public void testTextComponent() {
		
	}
	
	@Test
	public void testI18n() {
		
	}
	
	
	private static CompilationUnit assertParse(Path p) {
		try {
			return JavaParser.parse(p);
		} catch (IOException e) {
			throw new Error("Could not resolve "+p.toString(), e);
		}
	}
	
}
