<?php

	$deviceID = $_GET["deviceID"];
	
	
	require_once("DB_config.php");
    require_once("DB_class.php");

    $db = new DB();
    $db->connect_db($_DB['host'], $_DB['username'], $_DB['password'], $_DB['dbname']);
    $db->query("SELECT temperature, humidity from tempandhumisensor WHERE deviceId = '$deviceID'");
	
	$dataarray = array();
	
	//新增datetime、userid
	//透过userid捞出适当地使用者资料产生json
	//溫濕度
    while($result = $db->fetch_array())
    {
        $dataarray['room'][] = $result;
    }
	
	//亮度
	$db->query("SELECT brightness from brightnesssensor WHERE deviceId = '$deviceID'");
	while($result2 = $db->fetch_array())
    {
        $dataarray['room'][] = $result2;
    }
	
	//毒氣
	$db->query("SELECT gasData from gassensor WHERE deviceId = '$deviceID'");
	while($result3 = $db->fetch_array())
    {
        $dataarray['room'][] = $result3;
    }
	
	echo json_encode($dataarray);
?>