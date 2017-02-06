<?php
	require_once("DB_config.php");
    require_once("DB_class.php");

    $db = new DB();
    $db->connect_db($_DB['host'], $_DB['username'], $_DB['password'], $_DB['dbname']);
    $db->query("SELECT * from roomdata");
	
	$dataarray = array();
	
	//新增datetime、userid
	//透过userid捞出适当地使用者资料产生json
	
    while($result = $db->fetch_array())
    {
        $dataarray['room'][] = $result;
    }
	echo json_encode($dataarray);
?>