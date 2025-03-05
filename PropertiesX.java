import java.util.Properties;
class PropertiesX extends Properties {
    public String g(String k) {
        return super.getProperty(k);
    }
    public void s(String k, String o) {
        super.setProperty(k, o);
    }
}