<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch($action) {
	case "trajects":
		$query = "
			SELECT id, name, pers_id as person
			FROM trajects
			ORDER BY id";
		break;
		
	case "adduser":
		$name = $_POST['name'];
		$email = $_POST['email'];
		$phone = $_POST['phone'];
		$password = $_POST['password'];
		if (isset($_POST['id'])) {
			$id = $_POST['id'];
			$query = "
				UPDATE persons
				SET
					name='$name',
					email='$email',
					phone='$phone',
					password='$password'
				WHERE id='$id'";
		} else {
			$query = "
				INSERT INTO persons
				(name, email, phone, password)
				VALUES
				('$name', '$email', '$phone', '$password')";
		}
		break;
		
	case "deluser":
		$query = "
			DELETE FROM persons
			WHERE id = ".$_POST['id'];
		echo $query;
		break;
	
	case "friends":
		$query = "
			SELECT id, name, email, phone, password
			FROM persons
				INNER JOIN friends ON id=friendId
			WHERE personId=".$_POST['id'];
	default:
		//die("no data available");
}

$result = pg_query($conn, $query);
while ($traject = pg_fetch_assoc($result)) {
	$id = $traject["id"];
	
	$query = "
		SELECT X(geom(dump(walk))) as lat, Y(geom(dump(walk))) as lng, Z(geom(dump(walk))) as alt
		FROM trajects
		WHERE id=$id";
	$point_res = pg_query($conn, $query);
	while ($point = pg_fetch_assoc($point_res)) {
		$walk[] = $point;
	}
	$traject["walk"] = $walk;
	$output[] = $traject;
}

print(json_encode($output));
?>
