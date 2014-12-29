<?php

// JSONP
$callback = $_GET["callback"];

/////////////////////////////////////////////////////////////////////////////////////

$rssURL = "http://radioverda.com/programoj/rss.xml";
$rss = new SimpleXMLElement($rssURL, null, true);

//toDebug($rss);



$rssTitle = $rss->channel->title;
$count = count($rss->channel->item);

$json = array("success"=>($count > 1), "rssTitle"=>(string)$rssTitle, "count"=>$count);


// Parse out the title, pub date, mp3 and image URLs from the RSS XML
$podcasts = array();

foreach($rss->xpath('channel/item') as $item) {

    $title =  (string)$item->title;
    $pubDate = (string)$item->pubDate;

    $description = $item->description;
    $image = null;
    $mp3 = null;
    $topic = null;

    // Use '@' as RegEx pattern delimiter instead of '/' for easier reading

    // MP3 source URL
    //if(preg_match("@http://radioverda\.com/.*?\.mp3@", $description, $matches))
    if(preg_match("@http://radioverda\.com/(.*?)/(RV.*?)\.mp3@", $description, $matches) )
        $mp3 = "http://informagen.org/mobile-eo/RadioVerda/".$matches[2].".mp3";

    // Podcast image, 200x200 png
    if(preg_match("@http://radioverda\.com/.*?\.png@", $description, $matches))
        $image = $matches[0];

    // Podcast Topics
    if(preg_match("@<ul>.*</ul>@s", $description, $matches))
        $topic = $matches[0];

    $podcast = array("title"=>$title, 
                    "pubDate"=>$pubDate, 
                    "image"=>$image, 
                    "mp3"=>$mp3, 
                    "topic"=>$topic);
                    
    array_push($podcasts, $podcast);
}

// Add the PodCasts array to the JSON object
$json["podcasts"] = $podcasts;


// Return a JSONP response
header('Content-type: text/json; charset=utf-8');
echo "$callback && $callback(".json_encode($json).")";



//=============================================================================================

/*
function toDebug($rss) {

    header('Content-type: text/json; charset=utf-8');

    echo $rss->channel->title.PHP_EOL.PHP_EOL;
    
    foreach($rss->xpath('channel/item') as $item) {
    
        echo $item->title.PHP_EOL;
        echo $item->pubDate.PHP_EOL;
    
    
        $description = $item->description;
        
       //echo $description;
    
        // Use '@' as RegEx pattern delimiter instead of '/' for easier reading
    
        // MP3 sound source
        if(preg_match("@http://radioverda\.com/(.*?)/(RV.*?)\.mp3@", $description, $matches) )
            echo $matches[2].PHP_EOL;
    
        // Podcast image, 200x200 png
         if(preg_match("@http://radioverda\.com/.*?\.png@", $description, $matches) )
             echo $matches[0].PHP_EOL;
    
        // Podcast Topics
         if(preg_match("@<ul>.*</ul>@s", $description, $matches) )
             echo $matches[0].PHP_EOL;
    
        echo PHP_EOL.PHP_EOL;
    
    }

}

*/


/*

Test URLs
=========
-- Esperanto Dictionary
http://informagen.org/eo-vortaro-beta/jsonp-server/fetch-radio-verda.php?callback=callback
*/

?>