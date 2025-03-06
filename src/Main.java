import java.nio.file.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.jar.*;

class Main {
    private static void help() {
        System.out.println("Incorrect syntax! See manual.md for help.");
    }
    private static String norm(String p) {
        return Paths.get(p).normalize().toAbsolutePath().toString();
    }
    private static String resolve(String p, String p2) {
        return Paths.get(p).resolve(p2).normalize().toAbsolutePath().toString();
    }
    private static String relativize(String p, String p2) {
        return Paths.get(p).relativize(Paths.get(p2)).toString();
    }
    private static void addToJarManual(File f, byte[] bytes, JarOutputStream out) {
        try {
            String n = relativize(".", f.getPath()).replace("\\", "/");
            if (f.isDirectory() && !n.endsWith("/")) {
                n += "/";
            }
            
            System.out.println("Adding entry " + n + ", " + bytes.length + "b");
            
            JarEntry entry = new JarEntry(n);
            out.putNextEntry(entry);
            // Empty bytes array can be used to create a directory entry
            if (bytes.length != 0) {
                out.write(bytes);
            }
            out.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating jar entry!");
        }
    }
    private static void addToJar(File f, JarOutputStream out) {
        if (f.isFile()) {
            String[] jarinclude = p.g("jarinclude").split(",");
            for (String j : jarinclude) {
                if (f.getName().endsWith(j.trim()) && !f.getName().equals("jar.jar") && !f.getName().equals("bcmd.jar")) {
                    try {
                        addToJarManual(f, Files.readAllBytes(Paths.get(f.getPath())), out);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error reading jar entry file!");
                    }
                    break;
                }
            }
        } else {
            addToJarManual(f, new byte[0], out);
            for (File fl : f.listFiles()) {
                addToJar(fl, out);
            }
        }
    }
    private static PropertiesX p;
    private static final String ver = "1.0";
    public static void main(String[] args) {
        System.out.println("BCMD " + ver);
        p = new PropertiesX();

        p.setProperty("jdk", "C:\\Program Files (x86)\\BlueJ\\jdk\\bin");
        p.setProperty("cp", "lib/*;.");
        p.setProperty("include", "*.java");
        p.setProperty("main", "Main");
        p.setProperty("jarinclude", "class,java,jar");

        try {
            FileInputStream in = new FileInputStream(".bcmd");
            p.load(in);
            in.close();
        } catch (Exception e) {
            try {
                FileOutputStream out = new FileOutputStream(".bcmd");
                p.store(out, "bcmd configuration");
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Error creating .bcmd file!");
            }
        }
        
        try {
            String[] commands = args[0].split("");
            for (String c : commands) {
                ProcessBuilder pr = null;

                if (c.equals("b")) {
                    pr = new ProcessBuilder(
                        (p.g("jdk").trim().equals("") ? "javac" : resolve(p.g("jdk"), "javac")),
                         "-cp", p.g("cp"), p.g("include"));
                } else if (c.equals("r")) {
                    pr = new ProcessBuilder(
                        (p.g("jdk").trim().equals("") ? "java" : resolve(p.g("jdk"), "java")),
                         "-cp", p.g("cp"), p.g("main"));
                } else if (c.equals("c")) {
                    for (File f : new File(".").listFiles()) {
                        if (f.getName().endsWith(".class")) {
                            f.delete();
                        }
                    }
                } else if (c.equals("j")) {
                    FileOutputStream fout = new FileOutputStream("jar.jar");
                    
                    Manifest manifest = new Manifest();
                    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
                    manifest.getMainAttributes().put(new Attributes.Name("Created-By"), "BCMD " + ver);
                    manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, p.g("main"));
                    
                    JarOutputStream out = new JarOutputStream(fout, manifest);
                    //addToJarManual("META-INF/MANIFEST.MF", ("Manifest-Version: 1.0\nCreated-By: BCMD\nMain-Class: " + p.g("main")).getBytes(), out);
                    for (File f : new File(".").listFiles()) {
                        addToJar(f, out);
                    }
                    System.out.println("Done");
                    out.close();
                    fout.close();
                }
                if (pr != null) {
                    try {
                        pr.inheritIO();
                        pr.start().waitFor();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Failed to start compiler!");
                    }
                }
            }
        } catch (Exception e) {
            help();
        }
    }
}