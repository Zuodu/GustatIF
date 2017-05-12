function chargerInfos(){
    $.ajax({
        url: '/dashboard',
        type: 'POST',
        data: {
            action: 'chargerInfos'
        },
        dataType : 'json'
    })
        .done(function(data){
            var list = $('#infos').find('td');
            list[0].innerHTML = data.currentUsers;
            list[1].innerHTML = data.nbrClients;
            list[2].innerHTML = data.nbrLivreurs;
            list[3].innerHTML = data.nbrRestos;
            list[4].innerHTML = data.nbrCommandes;
        })
        .fail(function(){
            $("#error").html("erreur de chargement");
        })
}

function chargerRestos(){
    $.ajax({
        url: '/dashboard',
        type: 'POST',
        data: {
            action: 'chargerRestos'
        },
        dataType : 'json'
    })
        .done(function(data){
            var node = document.getElementById('restos').firstElementChild;
                for(var i in data.restaurants){
                    node.insertAdjacentHTML('beforeend',"<tr><td>" +data.restaurants[i].id+
                        "</td><td>" + data.restaurants[i].denomination +
                        "</td><td>" + data.restaurants[i].description +
                        "</td><td>" + data.restaurants[i].adresse +
                        "</td></tr>");
                }
        })
        .fail(function(){
            $("#error").html("erreur de chargement");
        })
}

function chargerLivreurs(){
    $.ajax({
        url: '/dashboard',
        type: 'POST',
        data: {
            action: 'chargerLivreurs'
        },
        dataType : 'json'
    })
        .done(function(data){
            var node = document.getElementById('livreurs').firstElementChild;
            for(var i in data.livreurs){
                node.insertAdjacentHTML('beforeend',"<tr><td>" +data.livreurs[i].id+
                    "</td><td>" + data.livreurs[i].mail +
                    "</td><td>" + data.livreurs[i].adresse +
                    "</td><td>" + data.livreurs[i].statut +
                    "</td></tr>");
            }
        })
        .fail(function(){
            $("#error").html("erreur de chargement");
        })
}

function chargerLivraisons(){
    $.ajax({
        url: '/dashboard',
        type: 'POST',
        data: {
            action: 'chargerLivraisons'
        },
        dataType : 'json'
    })
        .done(function(data){
            var node = document.getElementById('livraisons').firstElementChild;
            for(var i in data.commandes){
                node.insertAdjacentHTML('beforeend',"<tr><td>" +data.commandes[i].id+
                    "</td><td>" + data.commandes[i].mail +
                    "</td><td>" + data.commandes[i].livreur +
                    "</td><td>" + data.commandes[i].prix +
                    "</td><td>" + data.commandes[i].date +
                    "</td></tr>");
            }
        })
        .fail(function(){
            $("#error").html("erreur de chargement");
        })
}