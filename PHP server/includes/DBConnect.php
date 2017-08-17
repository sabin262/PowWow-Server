<?php
	class DBConnect{
		private $con;
		
		function __construct(){

		}

		function connect(){
			include_once dirname(__FILE__).'/Constants.php';
			$this->con = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

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

			if(mysqli_connect_errno()){
				echo "Failed to connect with dadtabase".mysqli_connect_err();
			}
				return $this->con;
			
		}

	}