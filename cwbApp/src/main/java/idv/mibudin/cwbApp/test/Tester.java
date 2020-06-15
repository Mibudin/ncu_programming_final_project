package idv.mibudin.cwbApp.test;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import idv.mibudin.cwbApp.core.cwb.CwbApi;
import idv.mibudin.cwbApp.core.gui.JavafxApplication;
import idv.mibudin.cwbApp.core.net.HttpsConnector;
import idv.mibudin.cwbApp.core.net.HttpsResponse;


/**
 * The {@code Tester} class is used for testing CwbApp.
 */
public class Tester
{
    public static void test(String[] args)
    {
        javafxAppTest(args);
        // cwbApiTest();
    }

    private static void javafxAppTest(String[] args)
    {
        System.out.println(System.getProperty("java.runtime.version"));
        for(String value : args)
        {
            System.out.println(value);
        }

        JavafxApplication.launchJavafxApplication(args);
    }

    private static void cwbApiTest()
    {
        CwbApi ca = new CwbApi("CWB-D3AA9928-023B-4902-BBAB-55FB9A448508");
        ca.setShouldReturnJson(true);

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("limit", "3");

        System.out.println(ca.requestDatastore("O-A0003-001", parameters).getResponseContent());
    }

    private static void httpsTest()
    {
        // For simulating the Man-in-the-middle attack (MITM).
        // System.setProperty("http.proxyHost" , "127.0.0.1");
        // System.setProperty("http.proxyPort" , "8888");
        // System.setProperty("https.proxyHost", "127.0.0.1");
        // System.setProperty("https.proxyPort", "8888");

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("Content-Type", "application/json");
        properties.put("Authorization", "CWB-D3AA9928-023B-4902-BBAB-55FB9A448508");

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("limit", "3");

        try
        {
            HttpsResponse res = HttpsConnector.httpsRequest("https://opendata.cwb.gov.tw/api/v1/rest/datastore/O-A0002-001", "GET", null, properties, parameters);
            System.out.println(res.getResponseContent());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
