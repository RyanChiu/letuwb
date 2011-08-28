<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * The script accept 3 parameters:
 * the "weibouserid" means the id of weibouser which is going to be voted,
 * the "clientkey" means the client key gained from server before,
 * the "vote" means that we should make it up or down:
 * 1 means like, -1 means dislike, and 2 means click.
 * If successful, will print clicks, likes and dislikes, such as
 * "16,12,3" inside a successful symbol pair.
 */
$weibouserid = array_key_exists('weibouserid', $_GET) ? $_GET['weibouserid'] : 0;
$clientkey = array_key_exists('clientkey', $_GET) ? $_GET['clientkey'] : '';
$vote = array_key_exists('vote', $_GET) ? $_GET['vote'] : 0;
if ($weibouserid == 0 || empty($clientkey) || $vote == 0 || !in_array($vote, array(-1, 1, 2))) {
	echo "~failed~Illegal vote.~failed~";
	exit();
}
$sql = "select id from clients where `key` = '$clientkey'";
$rs = mysql_query($sql, $zconn->dblink)
	or die ("~failed~DB err: " . mysql_error() . "~failed~");
if (mysql_num_rows($rs) <= 0) {
	echo "~failed~Illegal vote.~failed~";
	exit();
}
$r = mysql_fetch_row($rs);
$clientid = $r[0];

$time = date("Y-m-d H:i:s");

$sqls = array();
if ($vote != 2) {
	$sql = "select * from votes where weibouserid = $weibouserid and clientid = $clientid";
	$rs = mysql_query($sql, $zconn->dblink)
		or die ("~failed~DB err: " . mysql_error() . "~failed~");
	if (mysql_num_rows($rs) > 0) {
		$r = mysql_fetch_assoc($rs);
		$periodlimit = 86400;/*in seconds*/
		if (strtotime($time) - strtotime($r['time']) <= $periodlimit) {
			echo "~failed~You could vote only once within " . $periodlimit / 3600 . " hour(s).~failed~";
			exit();
		}
	}
	
	if ($vote == 1) {
		$sql = "insert into votes (weibouserid, clientid, likes, `type`, `time`) values ($weibouserid, $clientid, 1, $vote, '$time')"
		. " ON DUPLICATE KEY UPDATE likes = likes + 1, `type` = $vote, `time` = '$time'";
		array_push($sqls, $sql);
		$sql = "update weibo_users set likes = likes + 1, weeklylikes = weeklylikes + 1, lastvotetime = '$time' where id = $weibouserid;";
		array_push($sqls, $sql);
	} else if ($vote == -1) {
		$sql = "insert into votes (weibouserid, clientid, dislikes, `type`, `time`) values ($weibouserid, $clientid, 1, $vote, '$time')"
		. " ON DUPLICATE KEY UPDATE dislikes = dislikes + 1, `type` = $vote, `time` = '$time'";
		array_push($sqls, $sql);
		$sql = "update weibo_users set dislikes = dislikes + 1, weeklydislikes = weeklydislikes + 1, lastvotetime = '$time' where id = $weibouserid;";
		array_push($sqls, $sql); 
	}
} else {
	$sql = sprintf("update weibo_users set clicks = clicks + 1, weeklyclicks = weeklyclicks + 1 where id = %d", $weibouserid);
	array_push($sqls, $sql);
}
//echo print_r($sqls, true); exit();//for debug
for ($i = 0; $i < count($sqls); $i++) {
	$sql = $sqls[$i];
	mysql_query($sql, $zconn->dblink)
		or die ("~failed~DB err: " . mysql_error() . "~failed~");	
}

$r = _getlastvote($zconn, $weibouserid, $clientkey);
if (count($r) > 0) {
	echo "~successful~"
		. $r['lastvote'] . ","
		. $r['lastvotetime'] . ","
		. $r['clicks'] . ","
		. $r['likes'] . ","
		. $r['dislikes'] . ","
		. $r['weeklyclicks'] . ","
		. $r['weeklylikes'] . ","
		. $r['weeklydislikes']
		. "~successful~";
} else {
	echo "~successful~~successful~" . print_r($r, true);
}
?>