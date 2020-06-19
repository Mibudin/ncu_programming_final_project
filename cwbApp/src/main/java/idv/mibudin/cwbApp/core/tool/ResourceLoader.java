package idv.mibudin.cwbApp.core.tool;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import idv.mibudin.cwbApp.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;


public class ResourceLoader
{
    private static final Class<App> rootClass = App.class;

    private static final String FXML_PATH = "fxml/";
    private static final String CSS_PATH  = "css/";

    private static final String FXML_EXPANSION = ".fxml";
    private static final String CSS_EXPANSION  = ".css";


    public static Parent loadFXML(String fileName)
    {
        try
        {
            return new FXMLLoader(rootClass.getResource(FXML_PATH + fileName + FXML_EXPANSION)).load();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String loadCSS(String fileName)
    {
        return rootClass.getResource(CSS_PATH + fileName + CSS_EXPANSION).toExternalForm();
    }

    public static String LoadStream(String filePath)
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp;
            while((temp = br.readLine()) != null)
            {
                sb.append(temp);
            }
            br.close();

            return sb.toString();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    public static Image loadImage(String filePath)
    {
        return new Image(rootClass.getResourceAsStream(filePath));
    }
}
