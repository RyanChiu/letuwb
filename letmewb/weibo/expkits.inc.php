<?php
function __get_user_insert_update_sql($user) {
	$sql = sprintf(
		'insert into weibo_users (`uid`, `screen_name`, `name`, `province`, `city`, `location`, '
		. '`description`, `url`, `profile_image_url`, `domain`, `gender`, `followers_count`, '
		. '`friends_count`, `statuses_count`, `favourites_count`, `created_at`, `following`, '
		. '`allow_all_act_msg`, `geo_enabled`, `verified`, `status_id`)'
		. ' values (%s, "%s", "%s", %d, %d, "%s", '
		. '"%s", "%s", "%s", "%s", "%s", %d, '
		. '%d, %d, %d, "%s", null, '
		. '%d, %d, %d, null)'
		. ' on duplicate key'
		. ' update `screen_name` = "%s", `name` = "%s", `province` = %d, `city` = %d, '
		. ' `location` = "%s", `description` = "%s", `url` = "%s", `profile_image_url` = "%s", '
		. ' `domain` = "%s", `gender` = "%s", `followers_count` = %d, `friends_count` = %d, '
		. ' `statuses_count` = %d, `favourites_count` = %d, `created_at` = "%s", '
		. ' `allow_all_act_msg` = %d, `geo_enabled` = %d, `verified` = %d;',
		$user['id'],
		/*
		 * the same 2 pieces below
		 */
		/*
		 * piece NO.1
		 */
		mysql_real_escape_string($user['screen_name']),
		mysql_real_escape_string($user['name']), $user['province'], $user['city'],
		mysql_real_escape_string($user['location']),
		mysql_real_escape_string($user['description']),
		mysql_real_escape_string($user['url']),
		mysql_real_escape_string($user['profile_image_url']),
		mysql_real_escape_string($user['domain']),
		mysql_real_escape_string($user['gender']), $user['followers_count'], $user['friends_count'],
		$user['statuses_count'],	$user['favourites_count'],
		mysql_real_escape_string($user['created_at']),
		$user['allow_all_act_msg'], $user['geo_enabled'], $user['verified'],
		/*
		 * piece NO.2
		 */
		mysql_real_escape_string($user['screen_name']),
		mysql_real_escape_string($user['name']), $user['province'], $user['city'],
		mysql_real_escape_string($user['location']),
		mysql_real_escape_string($user['description']),
		mysql_real_escape_string($user['url']),
		mysql_real_escape_string($user['profile_image_url']),
		mysql_real_escape_string($user['domain']),
		mysql_real_escape_string($user['gender']), $user['followers_count'], $user['friends_count'],
		$user['statuses_count'],	$user['favourites_count'],
		mysql_real_escape_string($user['created_at']),
		$user['allow_all_act_msg'], $user['geo_enabled'], $user['verified']
	);
	return $sql;
}
?>