<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch($action) {
	case "profile":
		$query = "
			SELECT id, name, email, phone, password
			FROM persons
			WHERE email = '".$_POST['email']."'";
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
			$name = $_POST['name'];
			$email = $_POST['email'];
			$phone = $_POST['phone'];
			$password = $_POST['password'];
			
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
		
	default:
		die("no data available");
}

$result = pg_query($conn, $query);
while ($person = pg_fetch_assoc($result))
	$output[] = $person;

print(json_encode($output));
?>
