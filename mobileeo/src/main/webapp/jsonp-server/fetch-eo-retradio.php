<?php

// JSONP
$callback = $_GET["callback"];

/////////////////////////////////////////////////////////////////////////////////////

$rssURL = "http://esperantaretradio.blogspot.com/rss.xml";
$rss = new SimpleXMLElement($rssURL, null, true);

//toDebug($rss);

$rssTitle = $rss->channel->title;
$count = count($rss->channel->item);

// $json = array("success"=>($count > 1), "rssTitle"=>(string)$rssTitle, "count"=>$count);

foreach($rss->xpath('channel/item') as $item) {

    $title =  (string)$item->title;
    $pubDate = (string)$item->pubDate;
    $description = $item->description;
    $url = (string)$item->enclosure["url"];
    $text = "";

    // Use '@' as RegEx pattern delimiter instead of '/' for easier reading

    // Extract everything starting with the first '</div>' tag then 
    //   discard this '</div>' tag
    
    // if(preg_match_all("@<p>.*</p>@", $description, $matches) ) {
    if(preg_match_all("@</div>.*@", $description, $matches) ) {
        $lines = count($matches[0]);
        for($i=0; $i<$lines; $i++)            
           $text = substr((string)$text.$matches[0][$i], 6);
    }

    // Return the first 'item' as the current item
    break;
}


$json = array("success"=>($count > 0),
              "title"=>$title, 
              "pubDate"=>$pubDate, 
              "mp3"=>$url, 
              "text"=>$text
        );


// Return a JSONP response
header('Content-type: text/json; charset=utf-8');
echo "$callback && $callback(".json_encode($json).")";




//=============================================================================================

/*
function toDebug($rss) {

    header('Content-type: text/xml; charset=utf-8');

    echo $rss->channel->title.PHP_EOL.PHP_EOL;
    
    foreach($rss->xpath('channel/item') as $item) {
    
        echo $item->title.PHP_EOL;
        echo $item->pubDate.PHP_EOL;
    
    
        $description = $item->description;
        $url = $item->enclosure["url"];
        echo $url;
        echo PHP_EOL.PHP_EOL;
            
        // Use '@' as RegEx pattern delimiter instead of '/' for easier reading
    
        // Podcast Topics
        if(preg_match_all("@<p>.*</p>@", $description, $matches) ) {
            $count = count($matches[0]);
            for($i=0; $i<$count-2; $i++)            
                echo $matches[0][$i].PHP_EOL;
        }
    
        echo PHP_EOL.PHP_EOL;
        break;
    
    }

}
*/



/*

Test URLs
=========
-- RetRadio Podcast
http://informagen.org/mobile-eo/jsonp-server/fetch-eo-retradio.php?callback=callback
*/

?>