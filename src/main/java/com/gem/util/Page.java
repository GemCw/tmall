package com.gem.util;

public class Page {
    /**开始页数**/
    private int start;
    /**每页显示个数**/
    private int count;
    /**总个数**/
    private int total;
    /**参数**/
    private String param;
    /**默认每页显示5条**/
    private static final int defaultCount = 5;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public Page(){
        count = defaultCount;
    }
    public Page(int start,int count){
        this();
        this.start = start;
        this.count = count;
    }

    public boolean isHasPreviouse(){
        if(start==0)
            return false;
        return true;
    }
    public boolean isHasNext(){
        if(start==getLast())
            return false;
        return true;
    }

    public int getTotalPage(){
        int totalPage;
        // 假设总数是50，是能够被5整除的，那么就有10页
        if (0 == total % count)
            totalPage = total /count;
            // 假设总数是51，不能够被5整除的，那么就有11页
        else
            totalPage = total / count + 1;
        if(0==totalPage)
            totalPage = 1;
        return totalPage;
    }

    public int getLast(){
        int last;
        if(0 == total%count)
            last = total-count;
        else
            last = total-total%count;
        last = last-total%count;
        return last;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHasPreviouse() + ", isHasNext()="
                + isHasNext() + ", getTotalPage()=" + getTotalPage() + ", getLast()=" + getLast() + "]";
    }
}
