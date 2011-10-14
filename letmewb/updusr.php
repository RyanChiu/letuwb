<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * the script takes 3 parameters:uid, channelid, clientkey.
 * and it'll update or insert one record into table users according them:
 * if the clientkey from table users according to "uid, channelid" is 
 * different with the one passed from "GET", do nothing to table users but 
 * just return the clientkey from table users. and at the same time set
 * the flag of the one passed from "GET" in table clients into -1. 
 */
require_once './protocolbuf/message/pb_message.php';
require_once './protocolbuf/parser/pb_proto_weibousers_protos.php';
$mappings = new UCMappings();
if (!array_key_exists('uid', $_GET)
	|| !array_key_exists('channelid', $_GET)
	|| !array_key_exists('clientkey', $_GET)) {
	$mappings->set_flag(0);//0 means parameters are kind of wrong or something
	echo $mappings->SerializeToString();
	exit();
}
$uid = $_GET['uid'];
$channelid = $_GET['channelid'];
$clientkey = $_GET['clientkey'];
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
	"select users.*, clients.key as clientkey from users, clients where users.uid = %s and users.channelid = %s and users.clientid = clients.id", 
	$uid, $channelid
);
$rs = mysql_query($sql, $zconn->dblink);
if ($rs === false) {
	$mappings->set_flag(-1);//-1 means there is some db related errors
	echo $mappings->SerializeToString();
	exit();
}

if (mysql_num_rows($rs) == 0) {
	$sql = sprintf(
		"insert into users (uid, channelid, clientid) values (%s, %s, %s)",
		$uid, $channelid, $clientid
	);
	mysql_query($sql, $zconn->dblink);
	if (mysql_insert_id() == 0 || mysql_insert_id() === false) {
		$mappings->set_flag(-1);//-1 means there is some db related errors
		echo $mappings->SerializeToString();
		exit();
	} else {
		$mappings->set_flag(1);//means successfully insert a record
		$mapping = $mappings->add_mapping();
		$mapping->set_id(mysql_insert_id());
		$mapping->set_uid($uid . '');
		$mapping->set_channelid($channelid . '');
		$mapping->set_clientid($clientid . '');
		$mapping->set_clientkey($clientkey);
		echo $mappings->SerializeToString();
		exit();
	}
} else {
	$r = mysql_fetch_assoc($rs);
	if ($r['clientkey'] == $clientkey) {
		$mappings->set_flag(2);//means all 3 do already exist in table users
		echo $mappings->SerializeToString();
		exit();
	} else {
		$mappings->set_flag(3);//means the client should replace his key with the returned one
		$mapping = $mappings->add_mapping();
		$mapping->set_id($r['id'] . '');
		$mapping->set_uid($r['uid'] . '');
		$mapping->set_channelid($r['channelid'] . '');
		$mapping->set_clientid($r['clientid'] . '');
		$mapping->set_clientkey($r['clientkey']);
		echo $mappings->SerializeToString();
		exit();
	}	
}
?>