<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch($action) {		
		case "huidigelocatie":
		$pers_id = $_POST['pers_id'];
		$lng = $_POST['lng'];
		$lat = $_POST['lat'];
		$alt = $_POST['alt'];
		$query = "
			UPDATE recentelocatie SET pers_id = '$pers_id' ,position = GeomFromText('POINT($lat $lng $alt)', 4326) where pers_id='$pers_id'";
		break;
	
	default:
		die("no data available");	
}
$result = pg_query($conn, $query);
while ($person = pg_fetch_assoc($result)){
	$output[] = $person;
}
print(json_encode($output));
?>
