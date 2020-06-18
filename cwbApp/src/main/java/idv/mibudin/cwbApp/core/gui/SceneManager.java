package idv.mibudin.cwbApp.core.gui;


import idv.mibudin.cwbApp.core.tool.ResourceLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class SceneManager
{
    public static Scene mainScene;

    public static Parent _forecastData;
    public static Parent _observeData;


    static
    {
        loadAllNeeded();
    }

    private static void loadAllNeeded()
    {
        mainScene = new Scene(ResourceLoader.loadFXML("MainScene"));

        _forecastData = ResourceLoader.loadFXML("_ForecastData");
        _observeData = ResourceLoader.loadFXML("_ObserveData");
    }
}
