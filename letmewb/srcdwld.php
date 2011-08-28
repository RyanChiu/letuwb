<?php
include 'zmysqlConn.class.php';
include 'extrakits.inc.php';

if (array_key_exists("src", $_GET)) {
	$url = $_GET['src'];
	if (strpos($url, "letuwb.apk") !== false) {
		$zconn = new zmysqlConn;
		$srcid = 0;
		$ip = __getclientip();
		$dltime = date("Y-m-d H:i:s");
		$sql = "insert into downloads (id, srcid, ip, dltime) values (null, $srcid, '$ip', '$dltime');";
		mysql_query($sql, $zconn->dblink);
		header("Location:$url");
	}
}
?>