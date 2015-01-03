
var Configuration = {};

Configuration.windowTitle   = "EO Retaĵaroj";


Configuration.analyticsKey  = "";
//Configuration.analyticsKey  = "UA-13220092-1";

// Server connection configuration
Configuration.serviceType   = "jsonp";


// JSONP configuration ------------------------------------------------------------------
Configuration.jsonp = {};

// JSONP Server
jsonServer = "informagen.org/mobile-eo/jsonp-server/"

// Network services as PHP
Configuration.jsonp.lookupWord        = jsonServer + "eo-fetch.php";
Configuration.jsonp.radioVerdaRSS     = jsonServer + "fetch-radio-verda.php";
Configuration.jsonp.wordOfTheDay      = jsonServer + "fetch-lernu-wod.php";
Configuration.jsonp.esperantaRetradio = jsonServer + "fetch-eo-retradio.php";
Configuration.jsonp.fetchESPDIC       = jsonServer + "fetch-espdic.php";

