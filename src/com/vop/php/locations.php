<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch ($action) {
	// get all locations from one person
	case "getlocs":
		$query = "
			SELECT id, name, pers_id, date, X(position) as lat, Y(position) as lng, Z(position) as alt, image
			FROM locations
			WHERE pers_id = '".$_POST['id']."'
			ORDER BY id";
		break;
		
	// get one location
	case "getloc":
		$query = "
			SELECT id, name, pers_id, date, X(position) as lat, Y(position) as lng, Z(position) as alt, image
			FROM locations
			WHERE id = '".$_POST['id']."'";
		break;
		
	// get location of friends
	case "getlocfriends":
		$pers_id = $_POST['pers_id'];
		$query = "
			SELECT foo1.id,foo1.name,foo1.description,foo1.pers_id,foo1.date,lat,lng,alt from (select id,name,description,pers_id,date,x(position) as lat,y(position) as lng, Z(position) as alt
			FROM locations) as foo1 INNER JOIN 
				(SELECT * FROM (SELECT pers_id FROM friends WHERE friend_id='$pers_id ' INTERSECT 
				SELECT friend_id FROM friends WHERE pers_id='$pers_id ') AS foo  INNER JOIN persons ON foo.pers_id=persons.id) AS foo2 ON foo1.pers_id=foo2.pers_id";
		break;
		
	case "addloc":
		$name = $_POST['name'];
		$lng = $_POST['lng'];
		$lat = $_POST['lat'];
		$alt = $_POST['alt'];
		$pers_id = $_POST['pers_id'];
		
		if (isset($_POST['id'])) {
			$id = $_POST['id'];
			$date = $_POST['date'];
			$query = "
				UPDATE locations
				SET
					name='$name',
					date='$date',
					pers_id='$pers_id',
					position=GeomFromText('POINT($lat $lng $alt)', 4326)
				WHERE id='$id'";
		} else {
			// upload image
			if (isset($_FILES['image'])) {
			    // Upload dir
			    $imagedir = "images/";
			
			    // Get extension
			    $ext = pathinfo($_FILES['image']['name'], PATHINFO_EXTENSION);
			
				// Give uniqid
			    $image = $imagedir.uniqid("vop_", true).'.'.$ext;
			
			    // Move image to imagedir
			    move_uploaded_file($_FILES['image']['tmp_name'], $image)
					or die ("Could not save file: ".$image);
			
			    // Set mode bytes for linux
			    chmod($image, 0777);
			}
		    
		    if (isset($image)) {
				$query = "
					INSERT INTO locations
					(name, pers_id, date, position, image)
					VALUES
					('$name','$pers_id',NOW(), GeomFromText('POINT($lat $lng $alt)', 4326), '$image')";
		    } else {
				$query = "
					INSERT INTO locations
					(name, pers_id, date, position)
					VALUES
					('$name','$pers_id',NOW(),GeomFromText('POINT($lat $lng $alt)', 4326))";
		    }
		}
		break;
		
	case "delloc":
		$result = pg_query($conn, "SELECT image FROM locations WHERE id=".$_POST['id']);
		$location = pg_fetch_assoc($result);
		if (file_exists($location['image']))
			unlink($location['image']);
		
		$query = "
			DELETE FROM locations
			WHERE id = ".$_POST['id'];
		break;

	case "getlochuidigfriends":
		$pers_id = $_POST['userid'];
		$name = $_POST['name'];
		$query = "
			SELECT id, name, pers_id, date, X(position) as lat, Y(position) as lng, Z(position) as alt
			FROM locations
			WHERE NOT(pers_id = '$pers_id') AND name = '$name' ";
		break;
}

$result = pg_query($conn, $query);
while ($location = pg_fetch_assoc($result)) {
	$output[] = $location;
}

if (isset($output) && !empty($output))
	print(json_encode($output));
else
	echo "[]";
?>
