package net.net.controller;


import com.typesafe.config.ConfigFactory;
import net.net.base.TrippleDes;
import net.net.entity.Cases;
import net.net.entity.Paraf;
import net.net.entity.User;
import net.net.entity.report;
import net.net.repository.CaseRepository;
import net.net.repository.ParafRepository;
import net.net.repository.RoleRepository;
import net.net.repository.UserRepository;
import net.net.response.*;
import net.net.service.CaseService;
import net.net.service.UserService;
import net.net.service.UtilService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/case")
public class CaseController {
    CaseRepository caseRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    ParafRepository parafRepository;

    @Autowired
    public CaseController(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Autowired
    CaseService caseService = new CaseService(caseRepository, parafRepository, userRepository);

    @Autowired
    UserService userService = new UserService(userRepository,roleRepository);

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public BaseResponse register(@RequestBody String json, @RequestHeader String token) {
        try {
            User user = userService.getUserByToken(token);
            Cases newCases = new Cases();
            JSONObject request = new JSONObject(json);
            newCases.setSubject(request.getString("subject"));
            newCases.setSenderId(user.getId());
            newCases.setText(request.getString("text"));
            newCases.setFileName(request.getString("fileName"));
            newCases.setType(request.getString("type"));
            newCases.setReceiverId(request.getLong("receiver"));
            long time = System.currentTimeMillis();
            Date date = new Date(time);
            newCases.setTime(date);
            newCases.setStatus(0);
            Cases fetchCases = caseService.RegisterCase(newCases);
            BaseResponse response = new BaseResponse();
            if (fetchCases == null) {
                response.setStatus(500);
                response.setMessage("Internal Server Error");
            }
            response.setStatus(200);
            response.setMessage("successful");
            return response;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseResponse response = new BaseResponse();
        response.setStatus(500);
        response.setMessage("Internal Server Error");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/countOfCases", method = RequestMethod.GET)
    public BaseResponse getCountOfCases(@RequestHeader String token) throws Exception {
        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
        long CountOfCases = caseService.getCountOfCases(Long.parseLong(decrypt.split(":")[1]));
        CountOfCasesResponse response = new CountOfCasesResponse(CountOfCases);
        response.setStatus(200);
        response.setMessage("successful");
        return  response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getAllCasesForUser", method = RequestMethod.GET)
    public BaseResponse getAllCasesForUser(@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        List<Cases> cases = caseService.getAllCasesForUser(user.getId());
        AllCasesResponse response = new AllCasesResponse(cases);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getAllCases", method = RequestMethod.GET)
    public BaseResponse getAllCases(@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        if (!user.getRole().equals("manager")&& !user.getRole().equals("admin"))
        {
           return UtilService.unauthorizedResponse();
        }
        List<Cases> cases = caseRepository.getAll();
        for (Cases c : cases){
            c.setReciverName(userService.getUsernameById(c.getReceiverId()));
            c.setSenderName(userService.getUsernameById(c.getSenderId()));
        }
        AllCasesResponse response = new AllCasesResponse(cases);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getAllReferCase", method = RequestMethod.GET)
    public BaseResponse getAllReferCase (@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        long id = user.getId();
        List<Cases> cases = caseService.getReferCase(id);
        for (Cases c : cases){
            c.setReciverName(userService.getUsernameById(c.getReceiverId()));
            c.setSenderName(userService.getUsernameById(c.getSenderId()));
        }
        AllCasesResponse response = new AllCasesResponse(cases);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getCase", method = RequestMethod.GET)
    public BaseResponse getCase (@RequestParam long id)
    {
        Cases cases = caseService.getCase(id);
        if (cases.getId() == 0)
        {
            BaseResponse response = new BaseResponse();
            response.setStatus(404);
            response.setMessage("not found");
            return response;
        }
        CaseResponse response = new CaseResponse(cases);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/referToOthers", method = RequestMethod.POST)
    public BaseResponse ReferToOthers (@RequestBody String json,@RequestHeader String token) throws Exception {
        BaseResponse response = new BaseResponse();
        User user = userService.getUserByToken(token);
        long id = user.getId();
        JSONObject request = new JSONObject(json);
        long Caseid = request.getLong("id");
        String paraf = request.getString("paraf");

        long userId = request.getLong("userId");
        Cases cases = caseService.getCase(id);
        if (cases.getId() == 0)
        {
            response.setStatus(404);
            response.setMessage("not found");
            return response;
        }
        caseService.ChangeCase(userId,id);
        Paraf newParaf = new Paraf(paraf,user.getId(),Caseid,System.currentTimeMillis());
        caseService.SaveParaf(newParaf);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/changeCase", method = RequestMethod.POST)
    public BaseResponse ChangeCase (@RequestBody String json,@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        JSONObject request = new JSONObject(json);
        long Caseid = request.getLong("id");
        String paraf = request.getString("paraf");
        long status = request.getLong("status");
        BaseResponse response = new BaseResponse();
        Cases cases = caseService.getCase(user.getId());
        if (cases.getId() == 0)
        {
            response.setStatus(404);
            response.setMessage("not found");
            return response;
        }
        caseService.ChangeCaseStatus(Caseid,status);
        Paraf newParaf = new Paraf(paraf,user.getId(),Caseid,System.currentTimeMillis());
        caseService.SaveParaf(newParaf);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getParafsForCase", method = RequestMethod.POST)
    public BaseResponse getParafsForCase (@RequestBody String request) throws JSONException {
        JSONObject request1 = new JSONObject(request);
        long caseId = request1.getLong("id");
        List<Paraf> parafs = caseService.getAllParaf(caseId);
        for (Paraf p : parafs){
            p.setSenderName(userService.getUsernameById(p.getSenderId()));
        }
        ParafsResponse response = new ParafsResponse(parafs);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/setSatisfactionForCase" , method = RequestMethod.POST)
    public BaseResponse setSatisfactionForCase (@RequestBody String request) throws JSONException {
        BaseResponse baseResponse = new BaseResponse();
        JSONObject request1 = new JSONObject(request);
        long caseId = request1.getLong("id");
        int  sat    = request1.getInt("Rate");
        caseService.setSatisfactionForaCase(caseId , sat);
        baseResponse.setMessage("Successful");
        baseResponse.setStatus(200);
        return baseResponse;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/report" , method = RequestMethod.GET)
    public BaseResponse report (@RequestHeader String token)
    {
        User user = userService.getUserByToken(token);
        if (!user.getRole().equals("manager"))
        {
            BaseResponse response = new BaseResponse();
            response.setStatus(401);
            response.setMessage("unauthorized");
            return response;
        }
        report report =  caseService.getReport();
        Reportrespone reportrespone = new Reportrespone(report);
        reportrespone.setMessage("successful");
        reportrespone.setStatus(200);
        return  reportrespone;
    }
}
