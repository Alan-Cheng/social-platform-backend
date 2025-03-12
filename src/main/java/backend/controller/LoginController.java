package backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.service.AuthService;
import backend.service.UserService;
import java.util.Optional;

import backend.PasswordUtil;
import backend.entity.LoginRequest;
import backend.entity.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequest request) throws NoSuchAlgorithmException {
        // 查詢用戶資料
        Optional<User> userOpt = userService.findByUserPhone(request.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get(); // 這裡使用 get() 來獲取 User
            // 驗證密碼是否正確
            boolean isPasswordValid = PasswordUtil.validatePassword(request.getPassword(), user.getPassword());
            if (isPasswordValid) {
                // 認證成功後生成 JWT token
                String token = authService.authenticateAndGenerateJwt(request.getUsername(), request.getPassword());
                // 構建回應內容，將 token 放入 "token" 鍵中
                Map<String, String> responseData = new HashMap<>();
                responseData.put("userName", user.getUserName());
                responseData.put("userId", user.getUserId().toString());
                responseData.put("token", token);

                // 返回包含 token 的回應，並回傳 200 OK
                return ResponseEntity.ok(responseData);

            }
        }
        // 當用戶名或密碼不正確時返回 401 Unauthorized
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", "Invalid username or password"));
    }

}