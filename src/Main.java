
import java.nio.file.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.jar.*;

class Main {
    private static void help() {
        System.out.println("Incorrect syntax! See README.txt for help.");
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
    private static PropertiesX p;
    private static final String ver = "1.3";
    public static void main(String[] args) {
        System.out.println("BCMD " + ver);
        p = new PropertiesX();

        p.setProperty("jdk", "C:\\Program Files (x86)\\BlueJ\\jdk\\bin");
        p.setProperty("cp", "lib/*;.");
        p.setProperty("include", "*.java");
        p.setProperty("main", "Main");
        p.setProperty("jarinclude", "*.class *.jar");

        try {
            FileInputStream in = new FileInputStream(".bcmd");
            p.load(in);
            in.close();
        } catch (Exception e) {
            try {
                FileOutputStream out = new FileOutputStream(".bcmd");
                p.store(out, "BCMD Configuration");
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

                boolean jdkempty = (p.g("jdk").trim().equals(""));

                if (c.equals("b")) {
                    System.out.println("Building...");
                    pr = new ProcessBuilder(
                        (jdkempty ? "javac" : resolve(p.g("jdk"), "javac")),
                         "-cp", p.g("cp"));
                    
                    for (String arg : p.g("include").split(" ")) {
                        pr.command().add(arg);
                    }
                } else if (c.equals("r")) {
                    System.out.println("Running...");
                    pr = new ProcessBuilder(
                        (jdkempty ? "java" : resolve(p.g("jdk"), "java")),
                         "-cp", p.g("cp"), p.g("main"));
                } else if (c.equals("c")) {
                    System.out.println("Cleaning...");
                    for (File f : new File(".").listFiles()) {
                        if (f.getName().endsWith(".class")) {
                            f.delete();
                        }
                    }
                } else if (c.equals("j")) {
                    System.out.println("Jarring...");
                    
                    try {
                        
                        pr = new ProcessBuilder(
                            (jdkempty ? "jar" : resolve(p.g("jdk"), "jar")),
                            "cvfe", new File(norm(".")).getName() + ".jar", p.g("main")
                        );
                        for (String arg : p.g("jarinclude").split(" ")) {
                            pr.command().add(arg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
            System.out.println("Done!");
        } catch (Exception e) {
            help();
        }
    }
}