package net.net.response;

import net.net.entity.Cases;

public class CaseResponse extends BaseResponse {
    private Cases aCases;

    public CaseResponse(Cases aCases) {
        this.aCases = aCases;
    }

    public Cases getaCases() {
        return aCases;
    }

    public void setaCases(Cases aCases) {
        this.aCases = aCases;
    }
}
