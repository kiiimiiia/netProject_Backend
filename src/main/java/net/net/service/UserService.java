package net.net.service;

import net.net.entity.Role;
import net.net.entity.User;
import net.net.repository.RoleRepository;
import net.net.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public User SignUp(User user) {
        User signedup = userRepository.save(user);
        return signedup;
    }

    @Transactional
    public User SignIn(String userName, String passWord) {
        return userRepository.FindByUserNameAndPass(userName, passWord);
    }

    public User getUser(long id) {
        User user = userRepository.get(id);
        return user;
    }

    @Transactional
    public void ChangePassWord(long id, String pass) {
        User user = userRepository.get(id);
        user.setPassWord(pass);
        userRepository.saveOrUpdate(user);
    }

    @Transactional
    public void Update(User user) {
        userRepository.saveOrUpdate(user);
    }

    @Transactional
    public User SignOut(long id) {
        User user = userRepository.get(id);
        if (user == null) {
            return new User();
        }
        user.setStatus(0);
        user = userRepository.saveOrUpdate(user);
        return user;
    }

    public long getRole(String roleName) {
        Role role = roleRepository.getRole(roleName);
        return role.getId();
    }

    public List<User> getAllUser() {
        List<User> users = userRepository.getAll();
        List<User> result = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (!users.get(i).getRole().equals("student"))
                result.add(users.get(i));
        }
        return result;
    }

    public List<User> getUnConfirmedUser() {
        List<User> users = userRepository.getUnConfirmedUser();
        return users;
    }

    @Transactional
    public void changeStatus(User user, long status) {
        user.setConfirmed(1);
        user.setStatus(status);
        userRepository.saveOrUpdate(user);
    }

    @Transactional
    public void unableUser(long id) {
        User user = userRepository.get(id);
        user.setStatus(0);
        userRepository.saveOrUpdate(user);
    }

    @Transactional
    public User getUserByToken(String token) {
        return userRepository.getUserByToken(token);
    }


    public String createRandomToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public boolean checkAvailableStudentId(String studentId) {
        if (studentId.length() != 8)
            return false;
        if (studentId.charAt(0) != '9')
            return false;
        try
        {
            Long.parseLong(studentId);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }


    public boolean checkAvailableAccountInformation(JSONObject jsonObject) {
        try {
            String phone = jsonObject.getString("phone");
            String Email = jsonObject.getString("Email");
            if (!userRepository.checkAvailableAccountInformation(phone,Email))
                return false;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameById(long userId) {
        return userRepository.get(userId).getName();

    }

    public long getNumberOfUser() {
        return userRepository.getAll().size();
    }
}
