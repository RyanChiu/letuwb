<?php
/*
 * use the API "friends" to get user's female and unfamouse friends.
 * if the parameter uid is given, we get the friends of the uid.
 * if the uid is not given, we get the most weekly liked one's friends.
 */
include_once('config.php');
include_once('weibooauth.php');
include_once(dirname(__FILE__) . '/../zmysqlConn.class.php');
include_once("expkits.inc.php");

$c = new WeiboClient(WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);

$ip = "0.0.0.0";

if ($argc >= 2) {
	$ip = $argv[1];
}

$geos = $c->oauth->get(
	"https://api.weibo.com/2/location/geo/ip_to_geo.json",
	array('ip' => $ip)
);

var_dump($geos);//for debug
exit("\n");//for debug
?>