<?php
include 'header.php';

if (!isset($_POST['action']))
	die("no data available");

$action = $_POST['action'];

switch($action) {
	case "profile":
		if (isset($_POST['email'])) {
			$query = "
				SELECT id, name, email, phone, password
				FROM persons
				WHERE email = '".$_POST['email']."'
					AND password = '".$_POST['password']."'";
		} else if (isset($_POST['id'])) {
			$query = "
				SELECT id, name, email, phone, password
				FROM persons
				WHERE id = '".$_POST['id']."'";
		}
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
    $id = $_POST['id'];
		$query = "
			SELECT id, name, email, phone, password FROM persons INNER JOIN friends ON id=friend_id WHERE pers_id = '$id'";
      break;
      
	case "getnotaddedpeople":
		$id = $_POST['id'];
		$query = "
			SELECT password,name,email,id,phone FROM persons where NOT id = '$id' EXCEPT
      SELECT password,name,email,id,phone FROM friends INNER JOIN persons ON friend_id = id WHERE pers_id = '$id' ";
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
