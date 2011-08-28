<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * The scripts allow client to request a key from server,
 * and when get back the answer message from the request-client,
 * it save the time that mentioned when the server dispatch
 * a key out, otherwise, it leave field time null.
 * It must recieve a serial string, otherwise it will be seen
 * as a illegal request.
 */
if (!array_key_exists("serial", $_GET)) {
	echo "~failed~Illegal request!~failed~";
	exit();
}
$serial = $_GET["serial"];
if ($serial != md5($appserial)) {
	echo "~failed~Illegal request!~failed~";
	exit();
}
$time = date("Y-m-d H:i:s");
if (!array_key_exists("key", $_GET)) {
	$jump = 0;
	do {
		$key = generate_rand(20);
		$sql = sprintf("insert into clients (`id`, `key`, `cretime`, `time`, `times`) values (null, '$key', '$time', null, 1)");
		mysql_query($sql, $zconn->dblink);
		if (mysql_affected_rows() > 0) {
			echo "~successful~$key~successful~";
			exit();
		}
		$jump++;
		if ($jump > 3) {
			break;
		}
	} while (true);
	echo "~failed~Failed to generate the key.~failed~";
	exit();
} else {
	$key = $_GET["key"];
	$sql = "update clients set `time` = '$time', `times` = `times` + 1 where `key` = '$key'";
	$rs = mysql_query($sql, $zconn->dblink);
	if (mysql_affected_rows() > 0) {
		echo "~successful~~successful~";
		exit();
	} else {
		echo "~failed~Illegal key!~failed~";
		exit();
	}
}

?>