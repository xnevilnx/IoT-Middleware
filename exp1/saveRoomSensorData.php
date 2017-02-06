<?php

$deviceId = $_GET['deviceID'];
$temperature = $_GET['temperature'];
$humidity = $_GET['humidity'];

$gasData = $_GET['gasData'];
$brightness = $_GET['brightness'];
//$uvValue = $_GET['uvvalue'];

	require_once("DB_config.php");
    require_once("DB_class.php");
	
    $db = new DB();
    $db->connect_db($_DB['host'], $_DB['username'], $_DB['password'], $_DB['dbname']);
    $db->query("UPDATE tempandhumisensor SET temperature = '$temperature', humidity = '$humidity' WHERE deviceId = '$deviceId'");
	$db->query("UPDATE brightnesssensor SET brightness = '$brightness' WHERE deviceId = '$deviceId'");
	$db->query("UPDATE gassensor SET gasData = '$gasData' WHERE deviceId = '$deviceId'");

	echo"asdasdadsas";
?>