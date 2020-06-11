package idv.mibudin.cwbApp.core.gui;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


/**
 * @deprecated <p>
 * A canvas with the information map.
 */
public class InformationMapCanvas extends Canvas
{
    public InformationMapCanvas(double width, double height)
    {
        super(width, height);
    }

    public void draw()
    {
        GraphicsContext gc = getGraphicsContext2D();

        gc.save();
        gc.strokePolygon(new double[]{50, 150, 150, 50}, new double[]{50, 50, 150, 150}, 4);
        gc.restore();
    }
}
