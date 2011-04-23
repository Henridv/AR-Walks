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
		
	case "trajects2":
		$query = "
			SELECT id, name, pers_id as person
			FROM trajects
			ORDER BY id";
		break;
		
	case "get_walk":
		$id = $_POST['id'];
		$query = "
			SELECT id, name, pers_id as person
			FROM trajects
			WHERE id=$id";
		break;
		
	case "addtraject":
		$name = $_POST['name'];
		$person = $_POST['person'];
		
		$walk = json_decode($_POST['walk']);
		$geom = "GeomFromText('MULTIPOINT(";
		
		$i = 0;
		foreach ($walk as $point) {
			if ($i++) $geom .= ", ";
			$point = str_replace(Array("{", "}", ","), "", $point);
			$geom .= $point;
		}
		$geom .= ")', 4326)";
		
		if (isset($_POST['id'])) {
			$id = $_POST['id'];
			$query = "
				UPDATE trajects
				SET
					name='$name',
					pers_id='$person',
					walk=$geom
				WHERE id='$id'";
		} else {
			$query = "
				INSERT INTO trajects
				(name, pers_id, walk)
				VALUES
				('$name', '$person', $geom)";
		}
		break;
		
	case "deltraject":
		$query = "
			DELETE FROM trajects
			WHERE id = ".$_POST['id'];
		break;
	default:
		die("no data available");
}

$result = pg_query($conn, $query);
if ($action == "trajects" || $action == "get_walk") {
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
} else {
	echo "OK";
}
?>
