<?php
include 'header.php';

if (isset($_POST['action'])) {
	if ($_POST['action'] == "profile") {
		$query = "
			SELECT id, name, email, phone, password
			FROM persons
			WHERE email = '".$_POST['email']."'";
	} else {
		$query = "
			SELECT id, name, email, phone, password
			FROM persons";
	}
}

$result = pg_query($conn, $query);
while ($person = pg_fetch_assoc($result))
$output[] = $person;

print(json_encode($output));
?>
