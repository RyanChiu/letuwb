<?php
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * the script should be executed by crontab every weekend at 23:45 on sunday
 */
$sql = "update weibo_users set weeklyclicks = 0, weeklylikes = 0, weeklydislikes = 0";
mysql_query($sql, $zconn->dblink)
	or die ("~failed~DB err: " . mysql_error() . "~failed~");
echo mysql_affected_rows() . " row(s) updated (" . date("Y-m-d H:i:s") . ").\n";
?>