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
$zconn = new zmysqlConn;

$uid = -1;

if ($argc >= 2) {
	$uid = $argv[1];
} else {
	$sql = "select * from weibo_users order by weeklylikes-weeklydislikes desc limit 1";
	$rs = mysql_query($sql, $zconn->dblink);
	$r = mysql_fetch_assoc($rs);
	$uid = $r['uid'];
}

$friends = $c->friends(false, 100, $uid);

//var_dump($friends);//for debug
//exit("\n");//for debug

/*
 * get the totals before insert / update.
 */
$sql = "select count(id) from weibo_users";
$rs = mysql_query($sql, $zconn->dblink);
$r = mysql_fetch_row($rs);
$foretotals = $r[0];

$i = $j = $f = 0;
if ($friends != null && !array_key_exists("error", $friends)) {
	foreach ($friends as $friend) {
		/*
		 * we only gather the female ones and their followers 
		 * must not be over 100000.
		 */
		if ($friend['gender'] == 'f' && $friend['followers_count'] < 100000) {
			//echo ++$i //for debug
			//	. " | " . $friend["id"] //for debug
			//	. " | " . $friend["screen_name"] //for debug
			//	. " | " . $friend["gender"] //for debug
			//	. " | " . $friend["followers_count"]; //for debug
			//echo "\n"; //for debug
			++$i;
			$sql = __get_user_insert_update_sql($friend);
			mysql_query("set names 'utf8';");
			mysql_query($sql, $zconn->dblink);
			$f += (mysql_affected_rows() == -1 ? 1 : 0);
			$j += (mysql_affected_rows() == -1 ? 0 : mysql_affected_rows());
		}
	}
}

/*
 * get the totals after insert / update.
 */
$sql = "select count(id) from weibo_users";
$rs = mysql_query($sql, $zconn->dblink);
$r = mysql_fetch_row($rs);
$aftertotals = $r[0];

$adds = $aftertotals - $foretotals;

echo "Get $i friends of '$uid' totally and put $adds into DB. ($j affected, $f failed. totals: $foretotals before, $aftertotals after.) (" 
	. date("Y-m-d H:i:s") 
	. ")\n";
?>