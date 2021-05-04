import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

public class Main {

    public static void main(String[] args) throws GeneralSecurityException, IOException, URISyntaxException {
        System.out.println(System.getProperty("file.encoding"));

        System.out.println(getApplicationPath(Main.class));

        Frame frame = new Frame("PassManage", new Dimension(500, 500));
        frame.setVisible(true);

    }

    public static String getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path.getParent().toString();
    }

}
