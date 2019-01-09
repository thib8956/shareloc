<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="api/signup" method="POST">
        <label for="login">Identifiant&nbsp:</label>
        <input type="text" id="login" name="login" />
        <label for="password">Mot de passe&nbsp: </label>
        <input type="password" id="password" name="password" />
        <label for="firstname">Prénom&nbsp:</label>
        <input type="text" id="firstname" name="firstname" />
        <label for="lastname">Nom&nbsp:</label>
        <input type="text" id="lastname" name="lastname" />
        <input type="submit">
    </form>
</body>
</html>
