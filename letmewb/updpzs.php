<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * the script takes 3 parameters:clientkey, channelid, uid.
 * and it'll update or insert one record into table possessions according to them.
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
	"insert into possessions (clientid, uid, channelid) values (%s, %s, %s)"
	. " ON DUPLICATE KEY UPDATE clientid = %s, uid = %s, channelid = %s",
	$clientid, $uid, $channelid,
	$clientid, $uid, $channelid
);
mysql_query($sql, $zconn->dblink);
if (mysql_insert_id() === false) {
	$mappings->set_flag(-1);//-1 means there is some db related errors
	echo $mappings->SerializeToString();
	exit();
}
if (mysql_insert_id() == 0) {
	$mappings->set_flag(1);//1 means the 3 exist in DB already
	echo $mappings->SerializeToString();
	exit();
} else {
	$mappings->set_flag(2);//2 means the 3 are inserted
	echo $mappings->SerializeToString();
	exit();
}
?>