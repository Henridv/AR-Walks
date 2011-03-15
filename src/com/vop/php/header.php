<?php
// Start overall output buffering
ob_start();

// Files to include
include 'config.php';

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
