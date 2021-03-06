
var lang = navigator.language || navigator.userLanguage;
if (lang === undefined) {
    lang = "nb";
}
if (lang.indexOf("en") === 0 ) {
    lang = "en";
}
if (lang.indexOf("nb") === 0) {
    lang = "nb";
}


var themeList = "";
var queryParameterPublisher= "";
var queryParameterQuery = "";
var queryParameterFrom = 0;
var queryParameterSize = 10;
var queryParameterSortfield = "";
var queryParameterSortdirection = "";

console.log("browser language: ", lang);

var params= window.location.search.substr(1);

if (params.indexOf("\&") !== -1 ) {
    params.split("&").forEach(function (param){
        var pair = param.split("=");

        if (pair[0] === "lang") {
            lang = pair[1];
        } else if (pair[0] === "theme") {
            themeList =pair[1];
        } else if (pair[0] === "publisher") {
            queryParameterPublisher = decodeURIComponent(pair[1]);
        } else if (pair[0] === "q") {
            queryParameterQuery = pair[1];
        } else if (pair[0] === "from") {
            queryParameterFrom = pair[1];
        } else if (pair[0] === "size") {
            queryParameterSize = pair[1];
        } else if (pair[0] === "sortfield") {
            queryParameterSortfield = pair[1];
        } else if (pair[0] === "sortdirection") {
            queryParameterSortdirection = pair[1];
        }
    });
} else {
    var pair = params.split("=");
    if (pair[0] === "lang") {
        lang = pair[1];
    }
}

console.log("application lang: " , lang);

// set correct chosen language
var langElement = document.getElementById("lang." + lang);
$("#chosenLanguage").html($(langElement).html() + ' <span class="caret"></span>');

// handle language toggle
/*$(".dropdown-menu li a").click(function(event){
 $(this).parents(".dropdown").find('.btn').html($(this).html() + ' <span class="caret"></span>');
 });*/

// Set up searchbar controller
function searchController() {
    var search = document.getElementById("search");
    var dosearch = document.getElementById("dosearch");

    if (dosearch)
        dosearch.onclick = function (event) {
            var query = "?q=" + encodeURIComponent(search.value) ;
            console.log("traverse ", query);
            window.location = "/datasets" + query + "&lang=" + lang;

        };

    if (search)
        search.onkeypress = function (event) {
            if (event.keyCode === 13) {
                var query = "?q=" + encodeURIComponent(search.value)  ;
                console.log("traverse ", query);
                window.location = "/datasets" + query + "&lang=" + lang;

            }
        };
}