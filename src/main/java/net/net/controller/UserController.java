package net.net.controller;


import net.net.base.TrippleDes;
import net.net.entity.Role;
import net.net.entity.User;
import net.net.repository.RoleRepository;
import net.net.repository.UserRepository;
import net.net.response.*;
import net.net.service.UserService;
import net.net.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Autowired
    UserService userService = new UserService(userRepository, roleRepository);

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public BaseResponse SignUp(@RequestBody String json) {
        BaseResponse response = new BaseResponse();
        try {
            User signInUser = new User();
            JSONObject request = new JSONObject(json);
            try {
                String studentId = request.getString("studentId");
                signInUser.setRole("student");
                signInUser.setIdNumber(studentId);
                signInUser.setStatus(1);
                if (!userService.checkAvailableStudentId(studentId)) {
                    response.setStatus(500);
                    response.setMessage("Invalid studentId");
                    return response;
                }
            } catch (Exception e) {
                signInUser.setRole("operator");
                // PENDING ACTIVATION
                signInUser.setIdNumber("");
                signInUser.setStatus(3);
            }
            boolean availabilityError = userService.checkAvailableAccountInformation(request);
            if (availabilityError) {
                signInUser.setName(request.getString("name"));
                signInUser.setMailAddress(request.getString("Email"));
                signInUser.setPassWord(request.getString("pass"));
                signInUser.setPhoneNumber(request.getString("phone"));
                signInUser.setUserName(request.getString("Uname"));

                User fetchUser = userService.SignUp(signInUser);
                if (fetchUser == null) {
                    response.setStatus(500);
                    response.setMessage("Internal Server Error");
                }
                response.setStatus(200);
                response.setMessage("Successful");
                return response;
            } else {
                //
                response.setStatus(400);
                response.setMessage("Phone or Email is duplicate");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setStatus(500);
        response.setMessage("Internal Server Error");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public BaseResponse SignIn(@RequestBody String json) throws Exception {

        SignInResponse response = new SignInResponse("");
        StringBuilder token = new StringBuilder();
        try {
            JSONObject request = new JSONObject(json);
            String UserName = request.getString("Uname");
            String PassWord = request.getString("Pass");
            User signInUser = userService.SignIn(UserName, PassWord);
            if (signInUser.getName() == null) {
                response.setStatus(500);
                response.setMessage("نام کاربری یا رمز عبور صحیح وارد نشده است.");
                return response;
            }
            if (signInUser.getStatus() == 3) {
                response.setStatus(400);
                response.setMessage("حساب کاربری شما در انتظار تایید است.");
                return response;
            } else if (signInUser.getStatus() == 2) {
                response.setStatus(400);
                response.setMessage("حساب کاربری شما غیر فعال شده است. لطفا با مدیریت سامانه ارتباط برقرار تمایید.");
                return response;
            }
            signInUser.setToken(userService.createRandomToken());
            userService.Update(signInUser);
            response.setToken(signInUser.getToken());
            response.setStatus(200);
            response.setMessage("Successful");
            return response;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setStatus(500);
        response.setMessage("Internal Server Error");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getProfile", method = RequestMethod.GET)
    public BaseResponse getProfile(@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        UserResponse response = new UserResponse(user);
        if (user == null) {
            response.setStatus(404);
            response.setMessage("not found");
        }
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/changePass", method = RequestMethod.POST)
    public BaseResponse changePassWord(@RequestHeader String token, @RequestBody String json) throws Exception {
        BaseResponse response = new BaseResponse();
        User user = userService.getUserByToken(token);
        try {
            JSONObject request = new JSONObject(json);
            String oldPass = request.getString("oldPass");
            String newPass = request.getString("newPass");
            if (user == null) {
                response.setStatus(404);
                response.setMessage("not found");
                return response;
            }
            if (!user.getPassWord().equals(oldPass)) {
                response.setStatus(500);
                response.setMessage("old pass not matched");
                return response;
            }
            userService.ChangePassWord(user.getId(), newPass);
            response.setStatus(200);
            response.setMessage("successful");
            return response;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setStatus(500);
        response.setMessage("Internal Server Error");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public BaseResponse SignOut(@RequestHeader String token) throws Exception {
        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
        long id = Long.parseLong(decrypt.split(":")[1]);
        BaseResponse response = new BaseResponse();
        User user = userService.SignOut(id);
        if (user.getId() == 0) {
            response.setStatus(404);
            response.setMessage("not found");
            return response;
        }
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public BaseResponse Config() {
        List<User> users = userService.getAllUser();
        List<Role> roles = roleRepository.getAll();
        ConfigResponse response = new ConfigResponse(users, roles);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/unConfirmedUser", method = RequestMethod.GET)
    public BaseResponse UnConfirmedUser(@RequestHeader String token) throws Exception {
        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
        long id = Long.parseLong(decrypt.split(":")[1]);
        User user = userService.getUser(id);
        if (!user.getRole().equals("manager")) {
            BaseResponse response = new BaseResponse();
            response.setStatus(401);
            response.setMessage("unauthorized");
            return response;
        }
        List<User> users = userService.getUnConfirmedUser();
        UsersResponse response = new UsersResponse(users);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResponse changeStatus(@RequestHeader String token, @RequestBody String json) throws Exception {
        User user = userService.getUserByToken(token);
        BaseResponse response = new BaseResponse();
        if (!user.getRole().equals("manager") && !user.getRole().equals("admin")) {
            return UtilService.unauthorizedResponse();
        }
        JSONObject jsonObject = new JSONObject(json);
        User existUser = userService.getUser(jsonObject.getLong("userId"));
        if (existUser.getId() == 0) {
            response.setStatus(404);
            response.setMessage("Not found");
            return response;
        }
        userService.changeStatus(existUser, jsonObject.getLong("status"));
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public BaseResponse getAllUser(@RequestHeader String token) throws Exception {
        User user = userService.getUserByToken(token);
        if (!user.getRole().equals("manager") && !user.getRole().equals("admin")) {
            BaseResponse response = new BaseResponse();
            response.setStatus(401);
            response.setMessage("unauthorized");
            return response;
        }
        List<User> users = userRepository.getAll();
        List<User> result = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
//            if (!users.get(i).getRole().equals("manager"))
            result.add(users.get(i));
        }
        UsersResponse response = new UsersResponse(result);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }

/*    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/searchUser", method = RequestMethod.POST)
    public BaseResponse searchUser(@RequestHeader String token, @RequestBody String json) throws Exception {
        User user = userService.getUserByToken(token);
        if (!user.getRole().equals("manager")){
            return UtilService.unauthorizedResponse();
        }
        JSONObject jsonObject = new JSONObject(json);
        User filter = new User();
        filter.setfName(jsonObject.getString("query"));
        List<User> users = userService.searchUsers();
        UsersResponse response = new UsersResponse(users);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }*/


    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/unableUser", method = RequestMethod.GET)
    public BaseResponse unableUser(@RequestHeader String token, long id) throws Exception {
        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
        long UserId = Long.parseLong(decrypt.split(":")[1]);
        User user = userService.getUser(UserId);
        BaseResponse response = new BaseResponse();
        if (!user.getRole().equals("manager")) {
            response.setStatus(401);
            response.setMessage("unauthorized");
            return response;
        }
        userService.unableUser(id);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }


}


//    @CrossOrigin(origins = "http://localhost:3000")
//    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
//    public BaseResponse deleteUser (@RequestHeader String token ,long id) throws Exception {
//        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
//        long UserId = Long.parseLong(decrypt.split(":")[1]);
//        User user = userService.getUser(UserId);
//        BaseResponse response = new BaseResponse();
//        if (!user.getRole().equals("manager"))
//        {
//            response.setStatus(401);
//            response.setMessage("unauthorized");
//            return response;
//        }
//        userService.deleteUser(id);
//        response.setStatus(200);
//        response.setMessage("successful");
//        return response;
//    }


/*
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/confirmUser", method = RequestMethod.POST)
    public BaseResponse ConfirmUser(@RequestHeader String token, @RequestParam long id) throws Exception {
        String decrypt = new TrippleDes(ConfigFactory.load().getString("cipher.key")).decrypt(token);
        long UserId = Long.parseLong(decrypt.split(":")[1]);
        User user = userService.getUser(UserId);
        BaseResponse response = new BaseResponse();
        if (!user.getRole().equals("manager")) {
            response.setStatus(401);
            response.setMessage("unauthorized");
            return response;
        }
        User existUser = userService.getUser(id);
        if (existUser.getId() == 0) {
            response.setStatus(404);
            response.setMessage("Not found");
            return response;
        }
        userService.ConfirmUser(existUser);
        response.setStatus(200);
        response.setMessage("successful");
        return response;
    }
*/
