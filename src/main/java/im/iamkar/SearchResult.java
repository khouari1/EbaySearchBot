package im.iamkar;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {

    private String count;

    private List<EbayItem> item;

    @JsonProperty("@count")
    public String getCount() {
        return count;
    }

    public List<EbayItem> getItem() {
        return item;
    }

}
