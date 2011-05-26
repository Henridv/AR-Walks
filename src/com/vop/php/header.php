<?php
// Start overall output buffering
ob_start();

// Files to include
if (file_exists('config.php'))
	include 'config.php';
else
	die ('You need a config.php file. See the README for more information.');

$conn = pg_connect($conn_str);
if (!$conn) {
	echo "Failed to connect to database.";
	echo "<p>".pg_error()."</p>";
	exit;
}

/**
 * Strip magic quotes
 */
if (get_magic_quotes_gpc()) {
	$in = array(&$_GET, &$_POST, &$_COOKIE);
	while (list($k,$v) = each($in)) {
		foreach ($v as $key => $val) {
			if (!is_array($val)) {
				$in[$k][$key] = stripslashes($val);
				continue;
			}
			$in[] =& $in[$k][$key];
		}
	}
	unset($in);
}

// Set timezone and locale
date_default_timezone_set('Europe/Brussels');
setlocale(LC_ALL, 'en_US.UTF-8');

// Error reporting on for debugging
error_reporting(E_ALL | E_STRICT);

// Extra css/js init
$extracss = '';
$extrajs = '';

pg_close();

?>
