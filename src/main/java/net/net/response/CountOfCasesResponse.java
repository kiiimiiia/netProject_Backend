package net.net.response;

public class CountOfCasesResponse extends BaseResponse {
    private long count;

    public CountOfCasesResponse(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
