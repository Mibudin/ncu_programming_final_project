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
module idv.mibudin.cwbApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens idv.mibudin.cwbApp to javafx.fxml;
    exports idv.mibudin.cwbApp;
}
 */
