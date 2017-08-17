<?php
	class DBOPerations{
		private $con;

		function __construct(){
			require_once dirname(__FILE__).'/DBConnect.php';

			$db = new DBConnect();

			$this->con = $db->connect();
		}
		/*Creating event*/	
		function createEvent($title, $description, $tag){
			$stmt = $this->con->prepare("INSERT INTO `events` (`id`, `title`, `description`, `tag`) VALUES (NULL, ?, ?, ?);");
			$stmt->bind_param("sss", $title, $description, $tag);

			/*if($stmt->execute()){
				return true;
			}
			else{
				return false;
			}*/
			return $stmt->execute();
		}
	}
