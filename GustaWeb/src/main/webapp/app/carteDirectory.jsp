<%--
  Created by IntelliJ IDEA.
  User: zuoduzuodu
  Date: 22/04/2017
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>GustatIF</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" charset="ISO-8859-15">
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
        <button class="btn btn-primary navbar-btn navbar-right" style="margin-right: 0">Déconnexion</button>

    </div>
</nav>
<!-- CONTENU -->
<div class="container" style="padding-top: 50px">
    <div class="page-header">
        <h1>Composez votre menu !</h1>
        <div class="lead">Voici la carte du restaurant "${requestScope.resto.denomination}". Bon appétit !</div>
    </div>
    <!-- groupage -->
    <div class="panel panel-default">
        <div class="panel panel-body">
            <form action="#" class="form-horizontal">
                <c:forEach items="${requestScope.carte}" var="plat">
                    <div class="form-group form-group-lg">
                        <label for="q${plat.id}" class="col-md-8 control-label">${plat.denomination} (${plat.description})
                            <span class="label label-info">${plat.prix} €</span>
                        </label>
                        <div class="col-md-4">
                            <input type="number" class="form-control" id="q${plat.id}" name="q${plat.id}" placeholder="quantité" style="width: 20%">
                        </div>
                    </div>
                </c:forEach>
                <div class="form-group">
                    <div class="col-md-offset-7 col-md-5">
                        <div class="progress">
                            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100"
                                 aria-valuemin="0" aria-valuemax="100" style="width:40%"></div>
                        </div>
                        <button type="submit" class="btn btn-primary">Valider la commande</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
