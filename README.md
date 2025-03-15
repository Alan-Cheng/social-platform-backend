# 社交平台

![社交平台demo](https://github.com/Alan-Cheng/social-platform-backend/blob/main/demo/home.png?raw=true "投資設定頁面")

### 功能

* 類似FaceBook的社交平台

* 註冊並登入平台，可新增、編輯或刪除自己的發文與留言

* 即時聊天室功能：
    1. 可透過右下角的即時聊天室可與在線使用者通訊
    2. 以 JWT(JSON Web Tokens) 中儲存的權限來決定可否使用即時聊天室
    
    *備註：註冊帳號時並無設定權限因此只能從資料庫設定，未來有空再新增該功能
    
* API ：
    1. RESTful 風格，以 HTTP 方法中的 POST、GET、PUT、DELETE 對應 CRUD(Create、Read、Update、Delete)操作
    2. 以 Spring Security 做各API的權限管理
    3. 以 JWT(JSON Web Tokens) 取代 Session 機制

* 前端： https://github.com/Alan-Cheng/social-platform-frontend

## DEMO
>
>
>
>### 1. 發文與留言
>
> * 新增、編輯或刪除自己的「貼文」，新增或刪除自己的「留言」：
>
>![社交平台貼文與留言](https://github.com/Alan-Cheng/social-platform-backend/blob/main/demo/post_and_comment.png?raw=true "社交平台貼文與留言")
>
>
>### 2. 即時聊天
>
> * 即時聊天室功能：
>
>![社交平台聊天室](https://github.com/Alan-Cheng/social-platform-backend/blob/main/demo/chatroom.png?raw=true "社交平台聊天室")
>
> * 若無使用權限則無法連接上聊天室：
>
>![社交平台聊天室阻擋](https://github.com/Alan-Cheng/social-platform-backend/blob/main/demo/chatroom_denial.png?raw=true "社交平台聊天室阻擋")

>
>




## 技術工具



>| 功能  | 技術 |
>| ------------- |:-------------:|
>| 後端       | Spring Boot      |
>| 驗證與權限       | Spring Security, JWT      |
>| 即時聊天      | WebSocket      |
>| 資料庫       | MySQL, Redis      |
>| API 風格       | RESTful      |
>| 前端       | Vue      |



---

