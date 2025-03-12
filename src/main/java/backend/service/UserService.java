package backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import backend.entity.User;
import backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 查詢用戶電話號碼，返回 Optional<User>
    public Optional<User> findByUserPhone(String userPhone) {
        return userRepository.findByUserPhone(userPhone);
    }    
    
    // 驗證用戶是否有效，檢查電話號碼和密碼
    public boolean isValidUser(String userPhone, String password) {
        Optional<User> userOpt = findByUserPhone(userPhone);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    // 註冊用戶
    public boolean register(User user) {
        Optional<User> existingUserOpt = findByUserPhone(user.getUserPhone());
        if (existingUserOpt.isPresent()) {
            return false; // 用戶已存在
        }
        userRepository.save(user); // 保存新用戶
        return true; // 註冊成功
    }
}
