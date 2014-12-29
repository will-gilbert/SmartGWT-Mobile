<?php

// Parameters
$modelo   = $_GET["modelo"];
$delingvo = $_GET["delingvo"];
$allingvo = $_GET["allingvo"];

// JSONP
$callback = $_GET["callback"];


/////////////////////////////////////////////////////////////////////////////////////

// Build the URL to the Lernu.net vortarto site
$url = "http://en.lernu.net/cgi-bin/serchi.pl";
$getURL = $url."?modelo=".urlencode($modelo)."&delingvo=".$delingvo."&allingvo=".$allingvo."&prioritato=0&starto=0&bobeloid=undefined&dishaki=true";


// Build URL header and options
$header[] = "Cache-Control: no-cache";
$header[] = "Accept: text/plain";
$header[] = "Accept-Encoding: *";
	
$curl = curl_init();
		
curl_setopt($curl, CURLOPT_URL,  $getURL);
curl_setopt($curl, CURLOPT_FOLLOWLOCATION, 1);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($curl, CURLOPT_HTTPHEADER, $header);
curl_setopt($curl, CURLOPT_HEADER, 0);

// Get the response and close the connection
$response = curl_exec($curl);
curl_close($curl);


// NB: May return more than 1 set of triplet lines or a single line 
//     when the word was not found
$lines = preg_split("/\\n/", $response);

// Word was not explictly found; A breakdown by syllable was proposed was returned
if(substr($lines[0], 0,1) == " ") 
    $propono = trim(array_shift($lines));

$count = count($lines);

// Parse out the word as interpreted by 'Vortaro' found in the last line 
//   i.e. Vortaro has converted 'x' characters to EO unicode

$tokens = preg_split("/[\\[\\[.*\\]\\]]/", $lines[$count-1], null, PREG_SPLIT_NO_EMPTY);
$vorto = $tokens[0];


// Create return json
$json = array("success"=>($count > 1), "vorto"=>$vorto, "propono"=>$propono);

// Parse out each return meaning for the set of triplet lines; Line 3 is blank
if($count > 1) {

    $set = array();

    for($i=0; $i<($count-1)/3; $i++) {
    
        $line1 = preg_split("/\\t/", $lines[$i*3]);
        $line2 = preg_split("/\\t/", $lines[$i*3+1]);

        $definition = array("fondata"=>$line1[1], "partoj"=>$line1[2], "radiko"=>$line1[3], "lingvo"=>$line2[2], "signifo"=>$line2[3]);
        array_push($set, $definition);
    }

    $json["aroj"] = $set;
}

// Return a json response
header('Content-type: text/json; charset=utf-8');
echo "$callback && $callback(".json_encode($json).")";


/*
Test URLs
=========
-- Unicode and works with multiple parts
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=almenaux&delingvo=eo&allingvo=en&callback=callback
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=estonteco&delingvo=eo&allingvo=en&callback=callback

-- No defintions found:
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=sxveri&delingvo=eo&allingvo=en&callback=callback
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=parts&delingvo=en&allingvo=eo&callback=callback

-- 10 definitions based on the model
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=part&delingvo=eo&allingvo=en&callback=callback

-- Word not found; proposed breakdown by syllable
http://informagen.org/mobile-eo/jsonp-server/eo-fetch.php?modelo=estoneco&delingvo=eo&allingvo=en&callback=callback

*/

?>