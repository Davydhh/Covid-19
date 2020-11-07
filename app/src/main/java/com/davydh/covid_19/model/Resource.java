package com.davydh.covid_19.model;

/**
 * Generic class that can be used to contain the data and the status of the request.
 *
 * @param <T> The type of data associated with the request.
 */
public class Resource<T> {

    private T data;
    private int totalResults;
    private int statusCode;
    private String statusMessage;
    private boolean isLoading;

    public Resource() {}

    public Resource(T data, int totalResults, int statusCode, String statusMessage, boolean isLoading) {
        this.data = data;
        this.totalResults = totalResults;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.isLoading = isLoading;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "data=" + data +
                ", totalResults=" + totalResults +
                ", statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", isLoading=" + isLoading +
                '}';
    }
}
