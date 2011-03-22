<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch ($action) {
	case "getloc":
		$query = "
			SELECT id, name, description, pers_id, date, X(position) as lat, Y(position) as lng, Z(position) as alt
			FROM locations
			WHERE pers_id = '".$_POST['id']."'
			ORDER BY id";
		break;
		
	case "addloc":
		$name = $_POST['name'];
		$descr = $_POST['description'];
		$lng = $_POST['lng'];
		$lat = $_POST['lat'];
		$alt = $_POST['alt'];
		$date = $_POST['date'];
		$pers_id = $_POST['pers_id'];
		
		if (isset($_POST['id'])) {
			$id = $_POST['id'];
			$query = "
				UPDATE locations
				SET
					name='$name',
					description='$descr',
					date='$date',
					pers_id='$pers_id',
					position=GeomFromText('POINT($lat $lng $alt)', 4326)
				WHERE id='$id'";
		} else {
			$query = "
				INSERT INTO locations
				(name, description, pers_id, date, position)
				VALUES
				('$name','$description','$pers_id','$date',GeomFromText('POINT($lat $lng $alt)', 4326))";
		}
		break;
		
	case "delloc":
		$query = "
			DELETE FROM locations
			WHERE id = ".$_POST['id'];
		echo $query;
		break;
}

$result = pg_query($conn, $query);
while ($location = pg_fetch_assoc($result)) {
	$output[] = $location;
}

print(json_encode($output));
?>
