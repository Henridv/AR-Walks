<?php
include 'header.php';

if (isset($_POST['id'])) {
	$query = "
		SELECT id, name, description, pers_id, date, X(position) as lat, Y(position) as lng, Z(position) as alt
		FROM locations
		WHERE pers_id = '".$_POST['id']."'";

	$result = pg_query($conn, $query);
	while ($location = pg_fetch_assoc($result)) {
		$output[] = $location;
	}

	print(json_encode($output));
} else {
	echo "No data available!";
}
?>
