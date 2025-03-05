import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

class Main {
    private static void help() {
        System.out.println("Usage: java -jar bcmd.jar commands main");
        System.out.println("Where commands is a set of commands put together (i.e. br) and main is the fully qualified name of the main class to run.");
        System.out.println("Commands:");
        System.out.println("b: Build project");
        System.out.println("r: Run project");
        System.out.println("c: Clean project");
    }
    private static String resolve(String p, String p2) {
        return Paths.get(p).resolve(p2).normalize().toAbsolutePath().toString();
    }
    public static void main(String[] args) {
        PropertiesX p = new PropertiesX();

        p.setProperty("jdk", "C:\\Program Files (x86)\\BlueJ\\jdk\\bin");
        p.setProperty("cp", "lib/*;.");
        p.setProperty("include", "*.java");
        p.setProperty("main", "HelloWorld");

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
                
            }
        }
        
        try {
            String[] commands = args[0].split("");
            for (String c : commands) {
                ProcessBuilder pr = null;
                if (c.equals("b")) {
                    pr = new ProcessBuilder(resolve(p.g("jdk"), "javac"), "-cp", p.g("cp"), p.g("include"));
                } else if (c.equals("r")) {
                    pr = new ProcessBuilder(resolve(p.g("jdk"), "java"), "-cp", p.g("cp"), p.g("main"));
                } else if (c.equals("c")) {
                    for (File f : new File(".").listFiles()) {
                        if (f.getName().endsWith(".class")) {
                            f.delete();
                        }
                    }
                }
                if (pr != null) {
                    try {
                        pr.inheritIO();
                        pr.start().waitFor();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            help();
        }
    }
}