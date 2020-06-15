package idv.mibudin.cwbApp.core.data;


import java.util.Vector;

import org.json.JSONObject;

import idv.mibudin.cwbApp.core.data.Information.InformationType;
import idv.mibudin.cwbApp.core.tool.JsonTools;


public class InformationElement
{
    private String name;

    /**
     * TODO: Lon, lat.
     */
    private Vector2D location;

    private InformationGroup informationGroup;

    
    public InformationElement(String name, Vector2D location, InformationGroup informationGroup)
    {
        this.name = name;
        this.location = location;
        this.informationGroup = informationGroup;
    }

    public Information getInformation(InformationType informationType)
    {
        return informationGroup.getInformation(informationType);
    }

    public double getDoubleValue(InformationType informationType)
    {
        return informationGroup.getInformation(informationType).getDoubleValue();
    }

    public String getStringValue(InformationType informationType)
    {
        return informationGroup.getInformation(informationType).getStringValue();
    }

    public String getName()
    {
        return name;
    }

    public Vector2D getLocation()
    {
        return location;
    }

    public InformationGroup getInformationGroup()
    {
        return informationGroup;
    }

    public static InformationElement createInformationElementWithLocation(JSONObject locationJson, String doubleValuePaths, String stringValuePaths)
    {
        String name = locationJson.getString("locationName");
        Vector2D location = new Vector2D(locationJson.getDouble("lon"), locationJson.getDouble("lat"));
        InformationGroup informationGroup = new InformationGroup();
        /**
         * TODO:
         */


        return new InformationElement(name, location, informationGroup);
    }
}
