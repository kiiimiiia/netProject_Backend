package net.net.service;

import net.net.response.BaseResponse;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public static BaseResponse unauthorizedResponse() {
        BaseResponse response = new BaseResponse();
        response.setStatus(401);
        response.setMessage("unauthorized");
        return response;
    }

    public String getShamsiDate() {
        return "";
    }
}
