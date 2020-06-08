package idv.mibudin.cwbApp.core.cwb;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import idv.mibudin.cwbApp.core.net.HttpsConnector;
import idv.mibudin.cwbApp.core.net.HttpsResponse;


public class CwbApi
{
    private static final String DATASTORE_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/";
    private static final String DATASET_URL   = "https://opendata.cwb.gov.tw/api/v1/rest/dataset/";

    /**
     * （HTTP Basic Authentication)
     */
    private String authorizationKey;

    private boolean shouldReturnJson;


    public CwbApi(String authorizationKey)
    {
        setAuthorizationKey(authorizationKey);
        setShouldReturnJson(false);
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
