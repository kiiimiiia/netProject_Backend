package net.net.response;

import net.net.entity.Paraf;

import java.util.List;

public class ParafsResponse extends BaseResponse {
    private List<Paraf> parafs;

    public ParafsResponse(List<Paraf> parafs) {
        this.parafs = parafs;
    }

    public List<Paraf> getParafs() {
        return parafs;
    }

    public void setParafs(List<Paraf> parafs) {
        this.parafs = parafs;
    }
}
