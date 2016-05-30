package im.iamkar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EbayResponseWrap {

    @JsonProperty("findItemsByKeywordsResponse")
    private List<EbaySearchResultWrap> response;

    public List<EbaySearchResultWrap> getResponse() {
        return response;
    }
}
