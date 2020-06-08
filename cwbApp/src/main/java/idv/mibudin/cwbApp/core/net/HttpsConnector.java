package idv.mibudin.cwbApp.core.net;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;


/**
 * The {@code HttpsConnector} class providing some useful and simple HTTPS request methods.
 */
public class HttpsConnector
{
    /**
     * The default charset used in HTTPS requests.
     */
    private static final String DEFAULT_CHARSET = "utf-8";


    /**
     * The useful and simple HTTPS request method.
     * 
     * This is in safe by using the default TLS(Transport Layer Security) v1.2
     * in JDK with version above 1.8, instead of using a {@code TrustManager}
     * with older SSL(Secure Sockets Layer) system or other ways.
     * 
     * @param requestUrl The request URL of this HTTPS request.
     *                   Do not forget to add {@code https://} in the front.
     *                   Should not be {@code null}.
     * @param requestMethod The HTTP method of this HTTPS request.
     *                      Should not be {@code null}.
     * @param outputString The output string sending to the server in this HTTPS request.
     * @param properties The request properties of this HTTPS request.
     * @param parameters The parameters append in the end of the request URL
     *                   (like queries or others) of this HTTPS request.
     * @return The HttpsResponse containing the response centent and other
     *         associated informations.
     * @throws IOException If {@code requestUrl} or {@code parameters} is invalid,
     *                     or the operation of opening HTTPS connection is failed,
     *                     still or other errors occur in some setting methods or
     *                     other operations.
     */
    public static HttpsResponse httpsRequest(String requestUrl, String requestMethod, String outputString, Map<String, String> properties, Map<String, String> parameters) throws IOException
    {
        // Add the parameters after the end of the request URL.
        StringBuilder requestUrlStringBuilder = new StringBuilder(requestUrl);
        if(parameters != null)
        {
            requestUrlStringBuilder.append("?");

            Iterator<Entry<String, String>> parametersIterator = parameters.entrySet().iterator();
            while(parametersIterator.hasNext())
            {
                Entry<String, String> parametersPair = parametersIterator.next();
                requestUrlStringBuilder.append(parametersPair.getKey()).append("=").append(parametersPair.getValue());
                if(parametersIterator.hasNext())
                {
                    requestUrlStringBuilder.append("&");
                }
            }
        }

        // Create and open the HTTPS URL connection.
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)(new URL(requestUrlStringBuilder.toString())).openConnection();

        // Enable the input and output.
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setDoOutput(true);

        // Disable the cache uses.
        httpsURLConnection.setUseCaches(false);

        // Set the request method.
        httpsURLConnection.setRequestMethod(requestMethod);

        // Set the charset to the default.
        httpsURLConnection.setRequestProperty("charset", DEFAULT_CHARSET);

        // Set the properties.
        if(properties != null)
        {
            Iterator<Entry<String, String>> propertiesIterator = properties.entrySet().iterator();
            while(propertiesIterator.hasNext())
            {
                Entry<String, String> propertyPair = propertiesIterator.next();
                httpsURLConnection.setRequestProperty(propertyPair.getKey(), propertyPair.getValue());
            }
        }

        // Connect the HTTPS URL connection. 
        httpsURLConnection.connect();

        // Do output.
        if(outputString != null)
        {
            // httpsURLConnection.setRequestProperty("Content-Length", Integer.toString(outputString.length()));
            OutputStream os = httpsURLConnection.getOutputStream();
            os.write(outputString.getBytes(DEFAULT_CHARSET));
            os.close();
        }

        // Do input.
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), DEFAULT_CHARSET));
        String inputLine;
        while((inputLine = bufferedReader.readLine()) != null)
        {
            inputStringBuilder.append(inputLine);
        }

        // Disconnect the HTTPS URL connection. 
        httpsURLConnection.disconnect();

        // Return the HTTPS response.
        return new HttpsResponse(httpsURLConnection.getResponseCode(), httpsURLConnection.getResponseMessage(), inputStringBuilder.toString());
    }
}
