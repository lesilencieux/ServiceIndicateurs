
<%@page import="com.bootcamp.rest.application.MyServer" %>
<%@page import="com.bootcamp.rest.controllers.MainApp" %>
<html>
<body>
<h2>Bienvenue dans le service de gestion des indicateurs de performance de vos projets et programmes!</h2>
<% 
MyServer myServer = new MyServer();
myServer.startServer();
%> 

<a href="http://localhost:8080/docs/"> Consulter la documentation pour l'utlisation  </a>

</body>
</html>
