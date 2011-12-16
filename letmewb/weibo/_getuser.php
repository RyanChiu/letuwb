<?php
/*
 * use the API "users/show" to get user's information from SINA and put
 * them into local DB. if there are any repeatedly informations with
 * the same uid, just overwrite it.   
 */
include_once('config.php');
include_once('weibooauth.php');
include_once(dirname(__FILE__) . '/../zmysqlConn.class.php');
include_once("expkits.inc.php");

$o = new WeiboOAuth(WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$zconn = new zmysqlConn;

if ($argc != 2) {
	exit("it should take 1 parameter as 'uid'.\n");
}

$uid = $argv[1];

$user =
	$o->get(
		"http://api.t.sina.com.cn/users/show/$uid.json",
		array('source' => WB_AKEY)
	);
//var_dump($user);
if (array_key_exists("error", $user)) {
	echo $user['error'] . "#$uid#\n";
} else {
	$sql = __get_user_insert_update_sql($user);
	mysql_query("set names 'utf8';");
	mysql_query($sql, $zconn->dblink)
		or die ("~failed~~db_err:" . mysql_error() . "~~failed~\n");
	echo mysql_affected_rows() . " row affected.\n";
}
?>