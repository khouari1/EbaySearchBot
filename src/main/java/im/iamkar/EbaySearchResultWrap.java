package im.iamkar;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EbaySearchResultWrap {

    private List<EbayItem> item;

    private List<SearchResult> searchResult;

    @JsonIgnore
    private List<String> paginationOutput;

    public List<String> getPaginationOutput() {
        return paginationOutput;
    }

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public List<EbayItem> getItem() {
        return item;
    }

}
