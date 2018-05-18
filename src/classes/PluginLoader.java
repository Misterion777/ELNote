package classes;

import crypto.Algorithm;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    public static Algorithm load(String pathToJar){
        try {
            JarFile jarFile = new JarFile(pathToJar);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".java")) {
                    continue;
                }
                // -5 because of .java
                String className = je.getName().substring(0, je.getName().length() - 5);
                className = className.replace('/', '.');
                Class c = cl.loadClass("crypto." + className);
                return (Algorithm) c.newInstance();
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
