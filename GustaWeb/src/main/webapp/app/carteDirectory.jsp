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
        <a href="/dashboard?action=deconnexion" class="btn btn-primary navbar-btn navbar-right" style="margin-right: 0">Déconnexion</a>

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
            <form action="/dashboard" method="post" class="form-horizontal"><!-- TODO: ne pas authoriser la validation si 0 articles commandés -->
                <input type="hidden" name="action" value="attribuerLivreur">
                <input type="hidden" name="restoID" value="${requestScope.resto.id}">
                <input type="hidden" name="clientID" value="${sessionScope.client.id}">
                <c:forEach items="${requestScope.carte}" var="plat" varStatus="loop">
                    <div class="form-group form-group-lg menuItem">
                        <label for="lab${plat.id}" class="col-md-8 control-label">${plat.denomination} (${plat.description})
                            <span class="label label-info">${plat.prix} €</span>
                            <span class="hidden">${plat.poids}</span>
                        </label>
                        <div class="col-md-4">
                            <input type="number" class="form-control" id="lab${plat.id}" name="${plat.id}" onclick="refreshInfo();"
                                   style="width: 20%" placeholder="0" value="0">
                        </div>
                    </div>
                </c:forEach>
                <div class="form-group">
                    <div class="col-md-offset-7 col-md-5">
                        <div class="well well-sm">Prix total : 0 €</div>
                        <div class="progress">
                            <div class="progress-bar progress-bar-striped active" style="width:0%">Poids max</div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="validate" disabled="disabled">Valider la commande</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script>
    function refreshInfo(){
        var total = 0 ;
        var poids = 0;
        var itemList = document.getElementsByClassName("menuItem");
        var quantite;
        var lock = false;
        for(var i=0;i<itemList.length; i++) {
            quantite = parseInt(itemList[i].getElementsByTagName("input")[0].value);
            total += parseInt(itemList[i].getElementsByTagName("span")[0].innerHTML) * quantite;
            console.log(total);
            poids += parseInt(itemList[i].getElementsByTagName("span")[1].innerHTML) * quantite;
        }
        document.getElementsByClassName("well-sm")[0].innerHTML="Prix total : "+total+" €";
        var pourcent = (poids/${requestScope.chargeMaxLimit})*100;
        if(pourcent >= 100){
            lock = true;
            document.getElementsByClassName("progress-bar-striped")[0].classList.add("progress-bar-danger");
            $('#validate').attr("disabled","disabled");
        }else{
            lock = false;
            document.getElementsByClassName("progress-bar-striped")[0].classList.remove("progress-bar-danger");
            $('#validate').removeAttr("disabled");
        }
        document.getElementsByClassName("progress-bar-striped")[0].style.width = pourcent+"%";
        if(total <= 0){
            $('#validate').attr("disabled","disabled");
        }else if(lock === false){
            $('#validate').removeAttr("disabled");
        }
    }
</script>
</html>
