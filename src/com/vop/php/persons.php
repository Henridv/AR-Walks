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
		
	case "delfriend":
    $idfriend = $_POST['idfriend'];
    $id = $_POST['id'];
		$query = "
			DELETE FROM friends where pers_id='$id ' and friend_id='$idfriend'";
		break;
		
		case "deluser":
		$query = "
			DELETE FROM persons
			WHERE id = ".$_POST['id'];
		break;
		
	case "friends":
    $id = $_POST['id'];
		$query = "
			SELECT * FROM (SELECT pers_id FROM friends WHERE friend_id='$id' INTERSECT SELECT friend_id FROM friends WHERE pers_id='$id') AS foo  INNER JOIN persons ON foo.pers_id=persons.id";
      break;
      
	case "getpeoplewhoaddedyou":
		$id = $_POST['id'];
		$query = "
			select password,name,email,id,phone from ((select pers_id from friends where friend_id='$id') EXCEPT (select friend_id from friends where pers_id='$id')) as foo INNER JOIN persons ON foo.pers_id=persons.id";
		break;
		
		case "getnotaddedpeople":
		$id = $_POST['id'];
		$query = "
			select password,name,email,foo.id,phone from (select id from persons where NOT(id='$id') 
      EXCEPT (( select pers_id from friends where friend_id='$id')UNION (select friend_id from friends where pers_id='$id'))) as foo INNER JOIN persons ON foo.id=persons.id";
		break;
		
		case "addfriend":
		$id_1= $_POST['id_1'];
		$id_2= $_POST['id_2'];
		$query = "
			INSERT INTO friends (pers_id, friend_id,accept) VALUES ('$id_1', '$id_2', true)";
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
