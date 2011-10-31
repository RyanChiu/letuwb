<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * the script takes 3 parameters:clientkey, channelid, uid.
 * and it'll delete the record that matches the 3 values passed from above parameters.
 */
require_once './protocolbuf/message/pb_message.php';
require_once './protocolbuf/parser/pb_proto_weibousers_protos.php';
$mappings = new UCMappings();
if (!array_key_exists('clientkey', $_GET)
	|| !array_key_exists('channelid', $_GET)
	|| !array_key_exists('uid', $_GET)) {
	$mappings->set_flag(0);//0 means parameters are kind of wrong or something
	echo $mappings->SerializeToString();
	exit();
}
$clientkey = $_GET['clientkey'];
$uid = $_GET['uid'];
$channelid = $_GET['channelid'];
$sql = sprintf("select * from clients where `key` = '%s'", $clientkey);
$rs = mysql_query($sql, $zconn->dblink);
if ($rs === false) {
	$mappings->set_flag(-1);//-1 means there is some db related errors
	echo $mappings->SerializeToString();
	exit();
}
$clientid = 0;
if (mysql_num_rows($rs) == 0) {
	$mappings->set_flag(-2);//-2 means illegal clientkey
	echo $mappings->SerializeToString();
	exit();
} else {
	$r = mysql_fetch_assoc($rs);
	$clientid = $r['id'];
}
$sql = sprintf(
	"delete from possessions where clientid = %s and uid = %s and channelid = %s",
	$clientid, $uid, $channelid
);
//exit($sql);//for debug
if (mysql_query($sql, $zconn->dblink)) {
	$mappings->set_flag(1);//1 means successful deleted
	echo $mappings->SerializeToString();
	exit();
} else {
	$mappings->set_flag(-1);//-1 means there is some db related errors
	echo $mappings->SerializeToString();
	exit();
}
?>