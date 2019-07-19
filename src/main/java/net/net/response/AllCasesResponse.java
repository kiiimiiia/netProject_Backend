package net.net.response;

import net.net.entity.Cases;

import java.util.List;

public class AllCasesResponse extends BaseResponse {
    private List<Cases> cases;

    public AllCasesResponse(List<Cases> cases) {
        this.cases = cases;
    }

    public List<Cases> getCases() {
        return cases;
    }

    public void setCases(List<Cases> cases) {
        this.cases = cases;
    }
}
