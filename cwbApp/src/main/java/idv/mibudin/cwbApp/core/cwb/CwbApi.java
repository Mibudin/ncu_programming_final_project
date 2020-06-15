package idv.mibudin.cwbApp.core.cwb;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import idv.mibudin.cwbApp.core.net.HttpsConnector;
import idv.mibudin.cwbApp.core.net.HttpsResponse;


public class CwbApi
{
    private static final String DATASET_URL   = "https://opendata.cwb.gov.tw/api/v1/rest/dataset/";
    private static final String DATASTORE_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/";

    /**
     * （HTTP Basic Authentication)
     */
    private String authorizationKey;

    private boolean shouldReturnJson;


    public CwbApi(String authorizationKey)
    {
        setAuthorizationKey(authorizationKey);
        setShouldReturnJson(true);
    }

    public HttpsResponse requestDatastore(String dataId, Map<String, String> parameters)
    {
        try
        {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("Content-Type", shouldReturnJson ? "application/json" : "application/xml");
            properties.put("Authorization", authorizationKey);

            parameters.put("format", shouldReturnJson ? "JSON" : "XML");

            return HttpsConnector.httpsRequest(DATASTORE_URL + dataId, "GET", null, properties, parameters);
        }
        catch(IOException e)
        {
            e.printStackTrace();

            return null;
        }
    }

    public HttpsResponse requestDatastore(String dataId, String... rawParameters)
    {
        Map<String, String> parameters = new HashMap<String, String>();
        for(int i = 0; i < rawParameters.length / 2; i++)
        {
            parameters.put(rawParameters[i * 2], rawParameters[i * 2 + 1]);
        }

        return requestDatastore(dataId, parameters);
    }

    public String getAutorizationaKey()
    {
        return authorizationKey;
    }

    public boolean isShouldReturnJson()
    {
        return shouldReturnJson;
    }

    public void setAuthorizationKey(String authorizationKey)
    {
        this.authorizationKey = authorizationKey;
    }

    public void setShouldReturnJson(boolean shouldReturnJson)
    {
        this.shouldReturnJson = shouldReturnJson;
    }
}
