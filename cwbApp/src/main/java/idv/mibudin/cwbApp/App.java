package idv.mibudin.cwbApp;


import idv.mibudin.cwbApp.core.gui.JavafxApp;
import idv.mibudin.cwbApp.test.Tester;


public class App
{
    private static JavafxApp javafxApp;


    public static void main(String[] args)
    {
        Tester.test(args);
    }

    public static JavafxApp getJavafxApp()
    {
        return javafxApp;
    }

    public static void setJavafxApp(JavafxApp javafxApp)
    {
        App.javafxApp = javafxApp;
    }
}

/**
 * module-info.java
module idv.mibudin.cwbApp
{
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.json;

    opens idv.mibudin.cwbApp to javafx.fxml;
    exports idv.mibudin.cwbApp.core.gui to javafx.graphics;
    exports idv.mibudin.cwbApp;
}
 */
