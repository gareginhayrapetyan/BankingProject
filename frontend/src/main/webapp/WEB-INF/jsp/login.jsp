<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Login page</title>

    <link href="../static/login.css"
          th:href="@{login.css}" rel="stylesheet" media="screen">

</head>
<body>
<form>
    <div class="group">
        <input id="username" type="text"><span class="highlight"></span><span class="bar"></span>
        <label>Username</label>
    </div>
    <div class="group">
        <input id="password" type="password"><span class="highlight"></span><span class="bar"></span>
        <label>Password</label>
    </div>
    <button type="button" class="button buttonBlue" formmethod="post" formaction>Login
        <div class="ripples buttonRipples"><span class="ripplesCircle"></span></div>
    </button>
    <a class="register" href="/register">Register</a>
</form>
</body>
</html>