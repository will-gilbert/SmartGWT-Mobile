<?php

// Parameters
$searchTerm   = $_GET["searchTerm"];
$maxReturn =  $_GET["maxReturn"];

// JSONP callback
$callback = $_GET["callback"];


/////////////////////////////////////////////////////////////////////////////////////

// In case the user can type in UTF-8
$searchTerm = mb_strtolower($searchTerm, "UTF-8");

// Transliterte model to EO using the 'X' system, after lower case conversion
$searchTerm = str_replace("cx", "ĉ", $searchTerm);
$searchTerm = str_replace("gx", "ĝ", $searchTerm);
$searchTerm = str_replace("jx", "ĵ", $searchTerm);
$searchTerm = str_replace("ux", "ŭ", $searchTerm);
$searchTerm = str_replace("hx", "ĥ", $searchTerm);


$found = 0;
$entries = array();

// Connect to the datbase
$db = mysql_pconnect("localhost", "willgilb_proxy", "tLVFvrsxHQJ3");

if (!$db) { 
    $json = array("success"=>false, "found"=>$found, "searchTerm"=>$searchTerm, "entries"=>$entries);
    // Return a json response as failed
    header('Content-type: text/json; charset=utf-8');
    echo "$callback && $callback(".json_encode($json).")";
    exit;
}

mysql_select_db("willgilb_espdic", $db);
mysql_set_charset('utf8', $db);

// Create the string
$rs = mysql_query("SELECT esperanto, english FROM glossary WHERE esperanto LIKE '".$searchTerm."%'");

$found = mysql_num_rows($rs);

if($found < $maxReturn) {
    while ($record = mysql_fetch_row($rs)) {
        array_push($entries, array("eo"=>$record[0], "en"=>$record[1]));
    }
} else {

    $rs = mysql_query("SELECT esperanto, english FROM glossary WHERE esperanto = '".$searchTerm."'");
    $found = mysql_num_rows($rs);

    if($found < $maxReturn) {
        while ($record = mysql_fetch_row($rs)) {
            array_push($entries, array("eo"=>$record[0], "en"=>$record[1]));
        }
    }
}

// Create return json; Status and entires
$json = array("success"=>true, "found"=>$found, "searchTerm"=>$searchTerm, "entries"=>$entries);

// Return a json response
header('Content-type: text/json; charset=utf-8');
echo "$callback && $callback(".json_encode($json).")";
exit;

/*
Test URLs
=========
-- Simple test; teni - to hang onto, hold, keep, retain 
http://informagen.org/mobile-eo/jsonp-server/fetch-espdic.php?searchTerm=teni&callback=callback

-- UTF-8 encoding:
http://informagen.org/mobile-eo/jsonp-server/fetch-espdic.php?searchTerm=ĉevalo&callback=callback

-- 'X' system encoding
http://informagen.org/mobile-eo/jsonp-server/fetch-espdic.php?searchTerm=cxevalo&callback=callback
http://informagen.org/mobile-eo/jsonp-server/fetch-espdic.php?searchTerm=CXEVALO&callback=callback

-- Model not found
http://informagen.org/mobile-eo/jsonp-server/fetch-espdic.php?searchTerm=xxxx&callback=callback

*/

?>