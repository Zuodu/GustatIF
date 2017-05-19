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
<!-- titre -->
<div class="container" style="padding-top: 50px">
    <div class="page-header">
        <h1>Visualisation des commandes</h1>
        <div class="lead">Vous avez ${requestScope.livreursEnCours.size()} livraison(s) en cours.</div>
    </div>
    <c:if test="${requestScope.livreursEnCours.size()>0}">
    <!-- fonctionnalites -->
    <div class="panel panel-default">
        <div class="panel-heading text-left">
            <form action="/dashboard" method="post" class="form-inline" id="formCommande">
                <span>Choix de la livraison : </span>
                <div class="form-group">
                    <input type="hidden" name="action" value="cloturerCommande">
                </div>
                <div class="form-group">
                    <label for="commandeControl" class="sr-only">select</label>
                    <select form="formCommande" class="form-control" id="commandeControl" name="livreurID" onchange="switchCommande();">
                        <option value="" class="toDisable"></option>
                        <c:forEach items="${requestScope.livreursEnCours}" var="livreur" varStatus="loop">
                            <option value="${livreur}">Commande ${loop.count}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="commandeSubmit" class="sr-only">submit</label>
                    <input type="submit" id="commandeSubmit" class="btn btn-primary toDisable" value="Clôturer" disabled>
                </div>
            </form>
        </div>
        <div class="panel-body text-left">
            <h4>détails de la commande :</h4>
            <dl class="dl-horizontal">
                <dt>Date</dt>
                <dd class="detail"></dd>
                <dt>Livreur</dt>
                <dd class="detail"></dd>
                <dt>Restaurant</dt>
                <dd class="detail"></dd>
                <dt>Client</dt>
                <dd class="detail"></dd>
                <dt id="listeProduitsCommandes">Commande</dt>
                <dt>Total</dt>
                <dd class="detail"></dd>
            </dl>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">Itinéraire</div>
        <div class="panel-body" id="googleMaps" style="height: 600px;"></div>
    </div>
    </c:if>
</div>
<script>
    // coords de nos acteurs
    var clientLat;
    var clientLng;
    var restoLat;
    var restoLng;
    var livreurLat;
    var livreurLng;
    //partie d'init de la map google
    var map;
    var marker1;
    var marker2;
    var marker3;
    var flightPath;
//-----------------------------------------------------------------------------------
    function initMap() {
        map = new google.maps.Map(document.getElementById("googleMaps"),{
            center : {lat : 45.768477, lng : 4.873731},
            zoom : 14
        });
        marker1 = new google.maps.Marker({
            label: 'A'
        });
        marker2 = new google.maps.Marker({
            label: 'B'
        });
        marker3 = new google.maps.Marker({
            label: 'C'
        });
        flightPath = new google.maps.Polyline({
            geodesic: true,
            strokeColor: '#3399ff',
            strokeOpacity: 0.6,
            strokeWeight: 8
        });
    }
//-----------------------------------------------------------------------------------
    function switchCommande(){
        $(".toDisable").attr("disabled","disabled");
        $("#commandeSubmit").removeAttr("disabled");
        var options = document.getElementById("commandeControl").options;
        var livreurID = options[options['selectedIndex']].value;
        $.ajax({
            url: '/dashboard',
            type: 'POST',
            data: {
                action: 'recupererCommande',
                livreurID: livreurID
            },
            dataType : 'json'
            })
            .done(function(data){
                console.log(data);
                var lp = "";
                for(var i in data.produitsCommandes){
                    lp+='<dd>'+data.produitsCommandes[i]+'</dd>';
                }
                var detailClass = document.getElementsByClassName("detail");
                detailClass[0].innerHTML = data.date;
                detailClass[1].innerHTML = data.livreur;
                detailClass[2].innerHTML = data.restaurant;
                detailClass[3].innerHTML = data.client+', au: '+data.clientAdr;
                detailClass[4].innerHTML = data.total+' €';
                $("#listeProduitsCommandes").after(lp);
                clientLat = parseFloat(data.clientLat);
                clientLng = parseFloat(data.clientLng);
                restoLat = parseFloat(data.restaurantLat);
                restoLng = parseFloat(data.restaurantLng);
                livreurLat = parseFloat(data.livreurLat);
                livreurLng = parseFloat(data.livreurLng);
                calculateAndDisplayFlightRoute(map);
            })
            .fail(function(){
                $(".detailsCommande").html("erreur");
                alert("erreur de chargement, veuillez recharger la page.");
            })
    }
    //-----------------------------------------------------------------------------------
    function calculateAndDisplayFlightRoute(map) {
        marker1.setPosition({lat:livreurLat,lng:livreurLng});
        marker2.setPosition({lat:restoLat,lng:restoLng});
        marker3.setPosition({lat:clientLat,lng:clientLng});
        marker1.setMap(map);
        marker2.setMap(map);
        marker3.setMap(map);
        var flightPlanCoordinates = [
            {lat: livreurLat, lng: livreurLng},
            {lat: restoLat, lng: restoLng},
            {lat: clientLat, lng: clientLng}];
        flightPath.setPath(flightPlanCoordinates);
        flightPath.setMap(map);
    }
    // key : AIzaSyCJfG6DckT0nIh8qRsNe7wobF7XpGuJYy0
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCJfG6DckT0nIh8qRsNe7wobF7XpGuJYy0&callback=initMap" async defer></script>
</body>
</html>
