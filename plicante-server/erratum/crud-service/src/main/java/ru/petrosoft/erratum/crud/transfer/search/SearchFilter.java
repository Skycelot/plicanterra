package ru.petrosoft.erratum.crud.transfer.search;

import java.util.List;

/**
 *
 */
public class SearchFilter {

    public Long templateId;
    public List<SearchElement> searchElements;
    public Integer resultSetStartIndex;
    public Integer resultSetNumber;
}
