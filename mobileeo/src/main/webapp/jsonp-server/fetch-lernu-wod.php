<?php

// JSONP
$callback = $_GET["callback"];

/////////////////////////////////////////////////////////////////////////////////////

$rssURL = "http://en.lernu.net/rss/tagovortoj.php";
$rss = new SimpleXMLElement($rssURL, null, true);

$count = count($rss->channel->item);

// Retrieve the last entry (most recent) on the list
foreach($rss->xpath('channel/item') as $item) {

    $title       = (string)$item->title;
    $pubDate     = (string)$item->pubDate;
    $link        = (string)$item->link;
    $description = (string)$item->description;
        
    break;
}

$json = array("success"=>($count > 0),
              "word"=>$title, 
              "pubDate"=>$pubDate, 
              "link"=>$link, 
              "description"=>$description
        );


// Return a JSONP response
header('Content-type: text/json; charset=utf-8');
echo "$callback && $callback(".json_encode($json).")";

/*
Test URLs
=========
-- Esperanto Dictionary
http://informagen.org/mobile-eo/jsonp-server/fetch-lernu-wod.php?callback=callback
*/

?>