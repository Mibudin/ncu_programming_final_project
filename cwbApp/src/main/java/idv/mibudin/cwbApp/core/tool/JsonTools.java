package idv.mibudin.cwbApp.core.tool;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonTools
{
    private static final String TARGET_PATHS_SEPERATOR_REGEX = "(?<=.)(?=[\\.-])";

    // TODO: `has` function?


    private static Object getObjectOneStep(Object json, String targetPath)
    {
        switch(targetPath.toCharArray()[0])
        {
            case '.':
                return ((JSONObject)json).get(targetPath.substring(1));
            case '-':
                return ((JSONArray)json).get(Integer.parseInt(targetPath.substring(1)));
            default:
                return null;
        }
    }

    private static Object getObject(Object json, String... targetPaths)
    {
        Object currentVisitObject = json;
        for(int i = 0; i < targetPaths.length; i++)
        {
            currentVisitObject = getObjectOneStep(currentVisitObject, targetPaths[i]);
        }

        return currentVisitObject;
    }

    private static Object getObject(Object json, String unsplitTargetPaths)
    {
        return getObject(json, unsplitTargetPaths.split(TARGET_PATHS_SEPERATOR_REGEX));
    }

    public static JSONObject getJsonObject(Object json, String... targetPaths)
    {
        return (JSONObject)getObject(json, targetPaths);
    }

    public static JSONObject getJsonObject(Object json, String unsplitTargetPaths)
    {
        return getJsonObject(json, unsplitTargetPaths.split(TARGET_PATHS_SEPERATOR_REGEX));
    }

    public static JSONArray getJsonArray(Object json, String... targetPaths)
    {
        return (JSONArray)getObject(json, targetPaths);
    }

    public static JSONArray getJsonArray(Object json, String unsplitTargetPaths)
    {
        return getJsonArray(json, unsplitTargetPaths.split(TARGET_PATHS_SEPERATOR_REGEX));
    }

    public static String getString(Object json, String... targetPaths)
    {
        return (String)getObject(json, targetPaths);
    }

    public static String getString(Object json, String unsplitTargetPaths)
    {
        return getString(json, unsplitTargetPaths.split(TARGET_PATHS_SEPERATOR_REGEX));
    }
}
