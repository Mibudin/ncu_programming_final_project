package idv.mibudin.cwbApp.core.net;

import java.util.List;
import java.util.Map;

/**
 * The {@code HttpsResponse} class is a simple structure containing the response content
 * and some associated information about the response.
 */
public class HttpsResponse
{
    /**
     * The response header of the response.
     */
    private Map<String, List<String>> responseHeader;
    /**
     * The response code of the response.
     */
    private int responseCode;
    /**
     * The response massage of the response.
     */
    private String responseMessage;
    /**
     * The response content of the response.
     */
    private String responseContent;


    /**
     * Construct a new {@code httpsResponse}.
     * @param responseHeader The response header of the response.
     * @param responseCode The response code of the response.
     * @param responseMessage The response massage of the response.
     * @param responseContent The response content of the response.
     */
    public HttpsResponse(Map<String, List<String>> responseHeader, int responseCode, String responseMessage, String responseContent)
    {
        this.responseHeader = responseHeader;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseContent = responseContent;
    }

    /**
     * Get the response header of the response.
     * @return The response header of the response.
     */
    public Map<String, List<String>> getResponseHeader()
    {
        return responseHeader;
    }

    /**
     * Get the response code of the response.
     * @return The response code of the response.
     */
    public int getResponseCode()
    {
        return responseCode;
    }

    /**
     * Get the response massage of the response.
     * @return The response massage of the response.
     */
    public String getResponseMessage()
    {
        return responseMessage;
    }

    /**
     * Get the response content of the response.
     * @return The response content of the response.
     */
    public String getResponseContent()
    {
        return responseContent;
    }
}
