package backend.entity;

public class LoginRequest {

    private String username;
    private String password;
    private String userId;
    
    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserId() {
    	return userId;
    }
    
    public void setUserId(String userId) {
    	this.userId = userId;
    }
}

