module idv.mibudin.cwbApp
{
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens idv.mibudin.cwbApp to javafx.fxml;
    exports idv.mibudin.cwbApp;
}
