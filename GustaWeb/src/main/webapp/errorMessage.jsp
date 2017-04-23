<%--
  Created by IntelliJ IDEA.
  User: zuoduzuodu
  Date: 21/04/2017
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=ISO-8859-1" language="java" session="false" %>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
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
<div class="site-wrapper">
    <div class="site-wrapper-inner">
        <!-- LOGO -->
        <div class="cover-container" style="height: 30%;padding-top: 2%">
            <div class="inner cover">
                <img src="resource/logo.png" alt="GustatIF">
            </div>
        </div>
        <!-- MILIEU : CONTENU  -->
        <div class="cover-container">
            <div class="col-md-8 col-md-offset-2">
                <div class="col-md-12">
                    <img src="/resource/errorPanda.jpg " alt="ErrorPanda">
                </div>
                <div class="col-md-8 col-md-offset-2">
                    <h4>Une erreur est survenue :</h4>
                    <p>${errorMessage}</p>
                </div>
                <div class="col-md-8 col-md-offset-2">
                    <a href="/" class="btn btn-primary">Retour Accueil</a>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
        <div class="cover-container">
            <div class="mastfoot">
                <div class="inner">
                    <p>Projet GustatIF : Binôme B3142, INSA de Lyon.</p>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
