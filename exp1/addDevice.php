<?php
$userID = $_GET["UserID"];
$roomName = $_GET["RoomName"];
$deviceID = $_GET["DeviceID"];
$selectedSensor = $_GET["SelectedSensor"];


	require_once("DB_config.php");
    require_once("DB_class.php");

    $db = new DB();
    $db->connect_db($_DB['host'], $_DB['username'], $_DB['password'], $_DB['dbname']);
    $db->query("INSERT INTO userdevice SET userId = '$userID', deviceId = '$deviceID', roomName = '$roomName', selectedSensor = '$selectedSensor'");

	echo "ok";
?>