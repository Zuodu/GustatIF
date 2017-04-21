<%--
    Document   : essai
    Created on : 14 avr. 2017, 21:33:00
    Author     : zifanYao
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Dashboard</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <p>Utilisateur ${sessionScope.user}</p>
        <p>nom : ${sessionScope.client.nom}</p>
        <p>resto : ${requestScope.listeResto[0].denomination}</p>
    </body>
</html>
