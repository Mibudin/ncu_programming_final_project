package idv.mibudin.cwbApp.core.gui.controller;


import java.net.URL;
import java.util.ResourceBundle;

import idv.mibudin.cwbApp.App;
import idv.mibudin.cwbApp.core.data.Vector2D;
import idv.mibudin.cwbApp.core.gui.JavafxApp;
import idv.mibudin.cwbApp.core.gui.SceneManager;
import idv.mibudin.cwbApp.core.tool.ResourceLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


public class MainSceneController implements Initializable
{
    @FXML private ButtonBar titleBar;

    @FXML private Button closeScreenButton;
    @FXML private Button iconifiyScreenButton;

    @FXML private Tab homePageTab;
    @FXML private Tab observeDataTab;
    @FXML private Tab radarDataTab;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setTitleBarDragEvent();

        homePageTab.setContent(SceneManager._homePage);
        // try {
        //     observeDataTab.setContent(App.getJavafxApp().test());
        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        observeDataTab.setContent(SceneManager._observeData);
    }

    private void setTitleBarDragEvent()
    {
        Vector2D dragDelta = new Vector2D(0, 0);

        titleBar.setOnMousePressed(
            (MouseEvent mouseEvent) ->
            {
                dragDelta.setX(App.getJavafxApp().getMainStage().getX() - mouseEvent.getScreenX());
                dragDelta.setY(App.getJavafxApp().getMainStage().getY() - mouseEvent.getScreenY());
            }
        );
        titleBar.setOnMouseDragged(
            (MouseEvent mouseEvent) ->
            {
                App.getJavafxApp().getMainStage().setX(mouseEvent.getScreenX() + dragDelta.getX());
                App.getJavafxApp().getMainStage().setY(mouseEvent.getScreenY() + dragDelta.getY());
            }
        );
    }
    
    @FXML
    private void onMouseClickCloseScreenButton(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            App.getJavafxApp().getMainStage().close();
        }
    }

    @FXML
    private void onMouseClickIconifiyScreenButton(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton() == MouseButton.PRIMARY)
        {
            App.getJavafxApp().getMainStage().setIconified(true);
        }
    }
}
