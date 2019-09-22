package com.hrznstudio.titanium.api.filter;

public class FilterSlot<T extends Object> {

    private int x;
    private int y;
    private T filter;
    private int filterID;

    public FilterSlot(int x, int y, int filterID, T defaultFilter) {
        this.x = x;
        this.y = y;
        this.filterID = filterID;
        this.filter = defaultFilter;
    }

    public T getFilter() {
        return filter;
    }

    public void setFilter(T filter) {
        this.filter = filter;
    }

    public int getFilterID() {
        return filterID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
