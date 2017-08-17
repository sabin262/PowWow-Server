<?php
    //open connection to mysql db
    $connection = mysqli_connect("localhost","root","","event_info") or die("Error " . mysqli_error($connection));

    //fetch table rows from mysql db
    $sql = "select * from events";
    $result = mysqli_query($connection, $sql) or die("Error in Selecting " . mysqli_error($connection));

    //create an array
    $eventarray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $eventarray[] = $row;
    }
    echo json_encode($eventarray);

    //write to json file
    $fp = fopen('events.json', 'w');
    fwrite($fp,"{\n\t"."\"events\"".":\n\t".json_encode($eventarray)."\n}");
    fclose($fp);

    //close the db connection
    mysqli_close($connection);
?>