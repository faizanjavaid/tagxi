package com.tyt.driver.retro.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoryMeta implements Serializable {

    @SerializedName("pagination")
    @Expose
    public  Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public static class Pagination{

        @SerializedName("total")
        @Expose
        public int total;

        @SerializedName("count")
        @Expose
        public int count;

        @SerializedName("per_page")
        @Expose
        public int perpage;

        @SerializedName("current_page")
        @Expose
        public int current_page;

        @SerializedName("total_pages")
        @Expose
        public int total_pages;

        @SerializedName("links")
        @Expose
        public Links links;

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPerpage() {
            return perpage;
        }

        public void setPerpage(int perpage) {
            this.perpage = perpage;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getTotal_pages() {
            return total_pages;
        }

        public void setTotal_pages(int total_pages) {
            this.total_pages = total_pages;
        }
    }

    public static class Links{
        @SerializedName("next")
        @Expose
        public String nextpagelink;

        @SerializedName("previous")
        @Expose
        public String prevpagelink;

        public String getPrevpagelink() {
            return prevpagelink;
        }

        public void setPrevpagelink(String prevpagelink) {
            this.prevpagelink = prevpagelink;
        }

        public String getNextpagelink() {
            return nextpagelink;
        }

        public void setNextpagelink(String nextpagelink) {
            this.nextpagelink = nextpagelink;
        }
    }


}

