<?php
/*
 * use the API "users/hot" to get users informations from SINA and put
 * them into local DB. if there are any repeatedly informations with
 * the same id, which is in local DB and from "users/hot", then refresh
 * the ones in local DB.   
 */

include_once('config.php');
include_once('weibooauth.php');
include_once('../zmysqlConn.class.php');

$o = new WeiboOAuth(WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$zconn = new zmysqlConn;

$hots = 
	$o->get(
		"http://api.t.sina.com.cn/users/hot.json",
		array('source' => WB_AKEY)
	);
$i = 0;
foreach ($hots as $hot) {
	if (in_array('' . $hot['gender'], array('f', 'm')) && intval($hot['id']) >= 0) {
		$sql = sprintf(
			'insert into weibo_users (`uid`, `screen_name`, `name`, `province`, `city`, `location`, '
			. '`description`, `url`, `profile_image_url`, `domain`, `gender`, `followers_count`, '
			. '`friends_count`, `statuses_count`, `favourites_count`, `created_at`, `following`, '
			. '`allow_all_act_msg`, `geo_enabled`, `verified`, `status_id`)'
			. ' values (%d, "%s", "%s", %d, %d, "%s", '
			. '"%s", "%s", "%s", "%s", "%s", %d, '
			. '%d, %d, %d, "%s", null, '
			. '%d, %d, %d, null)'
			. ' on duplicate key'
			. ' update `screen_name` = "%s", `name` = "%s", `province` = %d, `city` = %d, '
			. ' `location` = "%s", `description` = "%s", `url` = "%s", `profile_image_url` = "%s", '
			. ' `domain` = "%s", `gender` = "%s", `followers_count` = %d, `friends_count` = %d, '
			. ' `statuses_count` = %d, `favourites_count` = %d, `created_at` = "%s", '
			. ' `allow_all_act_msg` = %d, `geo_enabled` = %d, `verified` = %d;',
			$hot['id'],
			/*
			 * the same 2 pieces below
			 */
			/*
			 * piece NO.1
			 */
			mysql_real_escape_string($hot['screen_name']),
			mysql_real_escape_string($hot['name']), $hot['province'], $hot['city'],
			mysql_real_escape_string($hot['location']),
			mysql_real_escape_string($hot['description']),
			mysql_real_escape_string($hot['url']),
			mysql_real_escape_string($hot['profile_image_url']),
			mysql_real_escape_string($hot['domain']),
			mysql_real_escape_string($hot['gender']), $hot['followers_count'], $hot['friends_count'],
			$hot['statuses_count'],	$hot['favourites_count'],
			mysql_real_escape_string($hot['created_at']),
			$hot['allow_all_act_msg'], $hot['geo_enabled'], $hot['verified'],
			/*
			 * piece NO.2
			 */
			mysql_real_escape_string($hot['screen_name']),
			mysql_real_escape_string($hot['name']), $hot['province'], $hot['city'],
			mysql_real_escape_string($hot['location']),
			mysql_real_escape_string($hot['description']),
			mysql_real_escape_string($hot['url']),
			mysql_real_escape_string($hot['profile_image_url']),
			mysql_real_escape_string($hot['domain']),
			mysql_real_escape_string($hot['gender']), $hot['followers_count'], $hot['friends_count'],
			$hot['statuses_count'],	$hot['favourites_count'],
			mysql_real_escape_string($hot['created_at']),
			$hot['allow_all_act_msg'], $hot['geo_enabled'], $hot['verified']
		);
		mysql_query("set names 'utf8';");
		mysql_query($sql, $zconn->dblink)
			or die ("~failed~~db_err:" . mysql_error() . "~~failed~");
		$i += mysql_affected_rows();
	}
}
echo "Total: " . count($hots) . " / Affected: " . $i . "\n";
?>