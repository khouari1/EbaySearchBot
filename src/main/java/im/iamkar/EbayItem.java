package im.iamkar;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EbayItem {

    @JsonProperty("itemId")
    private List<String> itemId;

    @JsonProperty("title")
    private List<String> title;

    public List<String> getItemId() {
        return itemId;
    }

    public List<String> getTitle() {
        return title;
    }
}
