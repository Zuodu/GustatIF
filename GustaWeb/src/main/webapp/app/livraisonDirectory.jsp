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
        <p class="navbar-text" style="font-size: 13pt">Bonjour ${sessionScope.user} ! Bienvenue chez GustatIF !</p>
        <a href="/dashboard?action=deconnexion" class="btn btn-primary navbar-btn navbar-right" style="margin-right: 0">Déconnexion</a>

    </div>
</nav>
<!-- CONTENU -->
<div class="container" style="padding-top: 50px">
    <div class="page-header">
        <h1>Visualisation des commandes</h1>
        <div class="lead">Vous avez ${requestScope.commandesEnCours.size()} livraisons en cours.</div>
    </div>
    <!-- groupage -->
    <div class="panel panel-default">
        <div class="panel panel-body">
            hahaha
        </div>
    </div>
</div>
</body>
<script>
</script>
</html>
