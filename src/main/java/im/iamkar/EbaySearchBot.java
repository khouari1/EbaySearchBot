package im.iamkar;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

public class EbaySearchBot {

    private final String EBAY_PROD = "http://svcs.ebay.com";
    private final String EBAY_SANDBOX = "http://svcs.sandbox.ebay.com";
    private final String OPERATION_NAME = "findItemsByKeywords";
    private final String SERVICE_NAME = "FindingService";
    private final String SERVICE_VERSION = "1.0.0";
    private final String GLOBAL_ID = "EBAY-GB";
    private final String RESPONSE_DATA_FORMAT = "JSON";
    private final String EBAY_FINDING_API_ENDPOINT = "{env}/services/search/FindingService/v1?OPERATION-NAME={operationName}&" +
            "SERVICE-NAME={serviceName}&SERVICE_VERSION={serviceVersion}&GLOBAL-ID={globalId}&SECURITY-APPNAME={securityAppName}&" +
            "RESPONSE-DATA-FORMAT={responseDataFormat}&keywords={keywords}";
    private final String SANDBOX_APP_ID;
    private final String PROD_APP_ID;

    public EbaySearchBot() {
        Properties props = loadProperties();
        SANDBOX_APP_ID = props.getProperty("devAppId");
        PROD_APP_ID = props.getProperty("prodAppId");
    }

    public String createProdAddress(String minPrice, String maxPrice, String keywords) {
        String address = createAddress(minPrice, maxPrice, keywords);
        address = address.replace("{env}", EBAY_PROD);
        address = address.replace("{securityAppName}", PROD_APP_ID);
        return address;
    }

    public String createDevAddress(String minPrice, String maxPrice, String keywords) {
        String address = createAddress(minPrice, maxPrice, keywords);
        address = address.replace("{env}", EBAY_SANDBOX);
        address = address.replace("{securityAppName}", SANDBOX_APP_ID);
        return address;
    }

    public URLConnection connect(String address) {
        URLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public String getStream(URLConnection urlConnection) {
        String line;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public SearchResult getSearchResult(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        EbayResponseWrap r;
        List<SearchResult> searchResults = null;
        try {
            r = objectMapper.readValue(data, EbayResponseWrap.class);
            searchResults = r.getResponse().get(0).getSearchResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchResult searchResult = null;
        if (searchResults != null) {
            searchResult = searchResults.get(0);
        }
        return searchResult;
    }

    public void printItems(List<EbayItem> items) {
        if (items != null) {
            for (EbayItem item : items) {
                System.out.println(item.getTitle());
            }
        }
    }

    private Properties loadProperties() {
        Properties prop = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream in = classLoader.getResourceAsStream("config.properties")) {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    private String createAddress(String minPrice, String maxPrice, String keywords) {
        String address = EBAY_FINDING_API_ENDPOINT;
        address = address.replace("{operationName}", OPERATION_NAME);
        address = address.replace("{serviceName}", SERVICE_NAME);
        address = address.replace("{serviceVersion}", SERVICE_VERSION);
        address = address.replace("{globalId}", GLOBAL_ID);
        address = address.replace("{responseDataFormat}", RESPONSE_DATA_FORMAT);
        if (minPrice != null) {
            address += "&itemFilter(0).name=MinPrice&itemFilter(0).value=" + minPrice + "&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=GBP";
        }
        if (maxPrice != null) {
            address += "&itemFilter(1).name=MaxPrice&itemFilter(1).value=" + maxPrice + "&itemFilter(1).paramName=Currency&itemFilter(1).paramValue=GBP";
        }
        try {
            address = address.replace("{keywords}", URLEncoder.encode(keywords, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return address;
    }

}
