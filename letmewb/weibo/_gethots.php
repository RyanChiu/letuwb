<?php
/*
 * use the API "users/hot" to get users' informations from SINA and put
 * them into local DB. if there are any repeatedly informations with
 * the same uid, which is in local DB and from "users/hot", then refresh
 * the ones in local DB.   
 */

include_once('config.php');
include_once('weibooauth.php');
include_once('../zmysqlConn.class.php');
include_once("expkits.inc.php");

$o = new WeiboOAuth(WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$zconn = new zmysqlConn;

$hots = 
	$o->get(
		"http://api.t.sina.com.cn/users/hot.json",
		array('source' => WB_AKEY)
	);

$sex = array('f', 'm');
if ($argc == 2) {
	if (in_array($argv[1] . '', $sex)) {
		$sex = array($argv[1]);
	}
}

$i = 0;
foreach ($hots as $hot) {
	if (in_array('' . $hot['gender'], $sex)
		&& intval($hot['id']) >= 0
	) {
		$sql = __get_user_insert_update_sql($hot);
		mysql_query("set names 'utf8';");
		mysql_query($sql, $zconn->dblink)
			or die ("~failed~~db_err:" . mysql_error() . "~~failed~");
		$i += mysql_affected_rows();
	}
}
echo "Total: " . count($hots) . " / Affected: " . $i . ". (" . date("Y-m-d H:i:s") . ")\n";
?>