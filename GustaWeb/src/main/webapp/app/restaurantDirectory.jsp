<%--
    Document   : essai
    Created on : 14 avr. 2017, 21:33:00
    Author     : zifanYao
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>GustatIF</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <!-- bootstrapt minCSS-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="resource/cover.css">
    <link rel="shortcut icon" href="resource/favicon.ico" />
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <img src="resource/logo.png" alt="" class="navbar-brand" style="padding:0 0">
        </div>
        <p class="navbar-text" style="font-size: 13pt">Bonjour ${sessionScope.client.nom} ! Bienvenue chez GustatIF !</p>
        <a href="/dashboard?action=deconnexion" class="btn btn-primary navbar-btn navbar-right" style="margin-right: 0">Déconnexion</a>

    </div>
</nav>
<!-- CONTENU -->
<div class="container" style="padding-top: 50px">
    <div class="page-header">
        <h1>Choisissez votre restaurant !</h1>
        <div class="lead">Découvrez une description de nos établissements et choisissez-en un !</div>
    </div>
    <!-- GROUPE DE PANNEAUX -->
    <div class="panel-group">
        <c:forEach items="${requestScope.listeResto}" var="resto">
            <div class="panel panel-default">
                <div class="panel-heading">
                        ${resto.denomination}
                    <a data-toggle="collapse" href="#${resto.id}" class="pull-right" style="padding: 0 10px 0 10px;">Voir</a>
                </div>
                <div class="panel-collapse collapse" id="${resto.id}">
                    <div class="panel-body">
                        <p><span class="text-primary">Description : </span>${resto.description}</p>
                        <p><span class="text-primary">Adresse : </span>${resto.adresse}</p>
                        <form action="/dashboard" method="post" class="pull-right">
                            <input type="hidden" name="action" value="afficherCarte">
                            <input type="hidden" name="restoId" value="${resto.id}">
                            <input type="hidden" name="restoDenomination" value="${resto.denomination}">
                            <input type="submit" class="btn btn-success" value="Choisir">
                        </form>
                        <a data-toggle="collapse" href="#${resto.id}" class="btn btn-default pull-right" style="margin-right: 10px">Voir moins</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
