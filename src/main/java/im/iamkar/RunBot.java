package im.iamkar;

import org.apache.commons.cli.*;

import java.net.URLConnection;

public class RunBot {

    public final static String DEV_OPTION = "dev";
    public final static String KEYWORDS_OPTION = "keywords";
    public final static String SEARCH_INTERVAL_OPTION = "searchInterval";
    public final static String MIN_PRICE_OPTION = "minPrice";
    public final static String MAX_PRICE_OPTION = "maxPrice";

    public static void main(String[] args) {
        // to build: mvn clean compile assembly:single
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption(DEV_OPTION, false, "Use dev url connection.");
        options.addOption(KEYWORDS_OPTION, true, "Keywords of item to search for.");
        options.addOption(MIN_PRICE_OPTION, true, "Min price of item.");
        options.addOption(MAX_PRICE_OPTION, true, "Max price of item.");
        options.addOption(SEARCH_INTERVAL_OPTION, true, "Delay between searching.");
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Problem with given options.");
            System.exit(0);
        }
        EbaySearchBot bot = new EbaySearchBot();
        String address;
        boolean hasKeywords = cmd.hasOption(KEYWORDS_OPTION);
        if (!hasKeywords) {
            System.out.println("Please provide keywords using the -keywords option.");
            System.exit(0);
        }
        String keywords = cmd.getOptionValue(KEYWORDS_OPTION);
        String minPrice = cmd.hasOption(MIN_PRICE_OPTION) ? cmd.getOptionValue(MIN_PRICE_OPTION) : null;
        String maxPrice = cmd.hasOption(MAX_PRICE_OPTION) ? cmd.getOptionValue(MAX_PRICE_OPTION) : null;
        System.out.print("Bot search options: keywords='" + keywords + "'");
        if (minPrice != null) {
            System.out.print(", min price='" + minPrice + "'");
        }
        if (maxPrice != null) {
            System.out.print(", max price='" + maxPrice + "'");
        }
        System.out.println();
        if (cmd.hasOption(DEV_OPTION)) {
            System.out.println("Using dev url connection.");
            address = bot.createDevAddress(minPrice, maxPrice, keywords);
        } else {
            address = bot.createProdAddress(minPrice, maxPrice, keywords);
        }
        boolean found = false;
        int searchCount = 0;
        int searchInterval = cmd.hasOption(SEARCH_INTERVAL_OPTION) ? Integer.parseInt(cmd.getOptionValue(SEARCH_INTERVAL_OPTION)) : 60000;
        System.out.println("Bot search interval set to: " + searchInterval + "ms");
        while (!found) {
            URLConnection connection = bot.connect(address);
            String response = bot.getStream(connection);
            SearchResult searchResult = bot.getSearchResult(response);
            found = searchResult != null && Integer.parseInt(searchResult.getCount()) > 0;
            if (!found) {
                System.out.println( searchCount + ": No items found. Bot waiting.");
                try {
                    Thread.sleep(searchInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Items found!");
                bot.printItems(searchResult.getItem());
            }
        }
        System.out.println("Bot execution finished.");
    }

}
