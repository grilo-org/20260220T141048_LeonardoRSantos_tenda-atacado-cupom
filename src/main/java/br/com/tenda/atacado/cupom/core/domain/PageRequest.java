package br.com.tenda.atacado.cupom.core.domain;

import java.util.List;

public class PageRequest {

    private int pageNumber;
    private int pageSize;
    private List<Sort> sort;

    public PageRequest(int pageNumber, int pageSize, List<Sort> sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<Sort> getSort() {
        return sort;
    }
}
