<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/WebProject_MHJ/css/signup.css"> <!-- 별도 CSS 파일 -->
    <style>
        /* 회원가입 페이지 스타일 */
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Raleway', sans-serif;
            background: url('/WebProject_MHJ/images/pics01.jpg') no-repeat center center fixed;
            background-size: cover;
        }
        .signup-wrapper {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-color: rgba(0, 0, 0, 0.6); /* 다크 오버레이 */
        }
        .signup-container {
            width: 100%;
            max-width: 400px;
            background: #ffffff;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
            color: #333;
        }
        .signup-container h2 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #555;
        }
        .signup-container input[type="text"],
        .signup-container input[type="password"],
        .signup-container input[type="email"] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .signup-container input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }
        .signup-container input[type="submit"]:hover {
            background-color: #45a049;
        }
        .signup-container .login-text {
            margin-top: 1em;
            font-size: 0.9em;
            color: #333;
        }
        .signup-container .login-text a {
            color: #FF3002;
            text-decoration: none;
        }
        .signup-container .login-text a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="signup-wrapper">
        <div class="signup-container">
            <h2>Sign Up</h2>
            <form action="Signup_Process.jsp" method="post" name="signupFrm" onsubmit="return validateForm(this);">
                <input type="text" name="user_id" id="user_id" placeholder="아이디" required 
                       oninvalid="this.setCustomValidity('아이디를 입력해 주세요.')" 
                       oninput="this.setCustomValidity('')">
                <input type="password" name="password" id="password" placeholder="비밀번호" required 
                       oninvalid="this.setCustomValidity('비밀번호를 입력해 주세요.')" 
                       oninput="this.setCustomValidity('')">
                <input type="password" name="confirm_password" id="confirm_password" placeholder="비밀번호 확인" required 
                       oninvalid="this.setCustomValidity('비밀번호 확인을 입력해 주세요.')" 
                       oninput="this.setCustomValidity('')">
                <input type="text" name="name" id="name" placeholder="이름" required 
                       oninvalid="this.setCustomValidity('이름을 입력해 주세요.')" 
                       oninput="this.setCustomValidity('')">
                <input type="email" name="email" id="email" placeholder="이메일" required 
                       oninvalid="this.setCustomValidity('올바른 이메일 주소를 입력해 주세요.')" 
                       oninput="this.setCustomValidity('')">
                <input type="submit" value="회원가입">
            </form>
            <div class="login-text">
                이미 계정이 있으신가요? <a href="login.jsp">로그인</a>
            </div>
        </div>
    </div>

    <script>
        function validateForm(form) {
            // 비밀번호 확인
            if (form.password.value !== form.confirm_password.value) {
                alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
                form.confirm_password.focus();
                return false;
            }
            return true;
        }
    </script>
</body>
</html>
