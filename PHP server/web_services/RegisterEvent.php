<?php

require_once '../includes/DBOperations.php'; 
$response = array();

if($_SERVER['REQUEST_METHOD']=="POST"){
	if(
		isset($_POST['title']) and 
			isset($_POST['description']) and
				isset($_POST['tag']))
		{
		$db = new DBOperations();
		if($db->createEvent(
			$_POST['title'],
			$_POST['description'],
			$_POST['tag']
			)){
			$response['error'] = false;
			$response['message'] = "Event registered successfully";
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
		}
		else{
			$response['error'] = true;
			$response['message'] = "Event not registered ";
		}
	}
	else{
		$response['error'] = true;
		$response['message'] = "All fields required";
	}
}
else{
	$response['error'] = true;
	$response['message'] = "Invalid request";
}



echo json_encode($response);