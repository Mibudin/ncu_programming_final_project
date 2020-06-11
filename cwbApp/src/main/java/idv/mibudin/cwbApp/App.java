package idv.mibudin.cwbApp;


import idv.mibudin.cwbApp.test.Tester;


public class App
{
    public static void main(String[] args)
    {
        Tester.test(args);
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
