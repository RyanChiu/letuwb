<?php
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
$limit = 6;
$page = 1;
/*
 * The script gets several parameters from $_GET:
 * 1, $uid from "file".
 * 2, $direction from "direction".
 * (Only if the above 2 parameters are both passed by, we try to get current, next or previous picture's data from DB.
 * Otherwise, we try to get pictures' data from DB through the following parameters or combinations of them instead.)
 * 3, from "top", 0 means most last uploaded, 2 means most liked, 3 means most disliked,
 * 		4 means most liked the week, 5 means most disliked the week 
 * 4, from "limit", default is 6
 * 5, from "page", which means page number here, from 1 to n
*/
$uid = 0;
$direction = 0; //1 means previous one, 2 means next one, the other values means no moves
$time = "2011-01-01 00:00:00";
$clicks = $likes = $dislikes = 0;

$sql = sprintf("select * from weibo_users where 0 = 1");
if (array_key_exists('file', $_GET) && array_key_exists('direction', $_GET)) {
	$uid = $_GET['file'];
	$direction = $_GET['direction'];
	$sql = sprintf("select * from weibo_users where uid = %d", $uid);
	if ($direction == 1) {
		$sql = sprintf("select * from weibo_users where id < %d order by id desc limit 1", $uid);
	}
	if ($direction == 2) {
		$sql = sprintf("select * from weibo_users where id > %d limit 1", $uid);
	}
}
if (array_key_exists('top', $_GET)) {
	if ($_GET['top'] == 0) {// means last registered at SINA
		$sql = sprintf("select * from weibo_users order by id desc");
	}
	if ($_GET['top'] == 1) {// means most visited
		$sql = sprintf("select * from weibo_users order by clicks desc");
	}
	if ($_GET['top'] == 2) {// means most liked
		$sql = sprintf("select * from weibo_users order by likes-dislikes desc, clicks desc, lastvotetime desc");
	}
	if ($_GET['top'] == 3) {// means most disliked
		$sql = sprintf("select * from weibo_users order by dislikes-likes desc,clicks desc, lastvotetime desc");
	}
	if ($_GET['top'] == 4) {// means most liked the week
		$sql = "select * from weibo_users order by weeklylikes-weeklydislikes desc, weeklyclicks asc, lastvotetime desc";
	}
	if ($_GET['top'] == 5) {// means most disliked the week
		$sql = "select * from weibo_users order by weeklydislikes-weeklylikes desc, weeklyclicks desc, lastvotetime desc";
	}
	if ($_GET['top'] == 6) {// means randomly
		$sql = "select * from weibo_users order by rand()";
	}
}
if (array_key_exists('clientkey', $_GET)) {
	$sql = sprintf(
		"select * from weibo_users where uid in"
		. " (select a.uid from possessions a, clients b where a.clientid = b.id and b.`key` = '%s')",
		$_GET['clientkey']
	);
}

/*
 * append limit to $sql
 */
if (array_key_exists('limit', $_GET)) {
	$i = $_GET['limit'];
	if ($i > 0) $limit = $i;
}
if (array_key_exists('page', $_GET)) {
	$i = $_GET['page'];
	if ($i > 0) $page = $i;
}
$sql = $sql . " limit " . (($page - 1) * $limit) . ", " . $limit;
//echo $sql; echo print_r($_GET, true);//for debug
mysql_query("set names 'utf8';");
$rs = mysql_query($sql, $zconn->dblink)
	or die ("Something wrong with: " . mysql_error());

/*
 * once we get the picture's data,
 * try to put them into a protobuf
 * structured one.
*/
require_once './protocolbuf/message/pb_message.php';
require_once './protocolbuf/parser/pb_proto_weibousers_protos.php';
$usrs = new Weibousers();
while ($row = mysql_fetch_assoc($rs)) {
	$usr = $usrs->add_usr();
	$usr->set_id($row['id']);
	$usr->set_uid($row['uid']);
	$usr->set_screen_name($row['screen_name']);
	$usr->set_name($row['name']);
	$usr->set_province($row['province']);
	$usr->set_city($row['city']);
	$usr->set_location($row['location']);
	$usr->set_description($row['description']);
	$usr->set_url($row['url']);
	$usr->set_profile_image_url($row['profile_image_url']);
	$usr->set_domain($row['domain']);
	$usr->set_gender($row['gender']);
	$usr->set_followers_count($row['followers_count']);
	$usr->set_friends_count($row['friends_count']);
	$usr->set_statuses_count($row['statuses_count']);
	$usr->set_favourites_count($row['favourites_count']);
	$usr->set_created_at($row['created_at']);
	$usr->set_following($row['following']);
	$usr->set_allow_all_act_msg($row['allow_all_act_msg']);
	$usr->set_geo_enabled($row['geo_enabled']);
	$usr->set_verified($row['verified']);
	$usr->set_status_id($row['status_id']);
	$usr->set_clicks($row['clicks']);
	$usr->set_likes($row['likes']);
	$usr->set_dislikes($row['dislikes']);
	$usr->set_lastvotetime($row['lastvotetime']);
	$usr->set_weeklyclicks($row['weeklyclicks']);
	$usr->set_weeklylikes($row['weeklylikes']);
	$usr->set_weeklydislikes($row['weeklydislikes']);
}
echo $usrs->SerializeToString();
?>
