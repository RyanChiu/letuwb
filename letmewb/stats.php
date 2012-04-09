<?php
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
if (array_key_exists("total", $_GET) && $_GET['total'] == "pages"
	&& array_key_exists("limit", $_GET) && intval($_GET['limit']) > 0) {
	if (array_key_exists("clientkey", $_GET)) {
		$sql = sprintf(
			"select count(id) from weibo_users where uid in"
			. " (select a.uid from possessions a, clients b where a.clientid = b.id and b.`key` = '%s')",
			$_GET['clientkey']
		);
	} else {
		$sql = "select count(id) from weibo_users";
	}
	$rs = mysql_query($sql, $zconn->dblink)
		or die ("db_err: " . mysql_error());
	$total = 0;
	if (mysql_num_rows($rs) > 0) {
		$r = mysql_fetch_row($rs);
		$total = $r[0];
	}
	if ($total != 0) {
		$limit = intval($_GET['limit']);
		if (($total % $limit) == 0) {
			$total = $total / $limit;
		} else {
			$total = floor((float)$total / (float)$limit) + 1;
		}
	}
	echo "~successful~$total~successful~";
	exit();
}

if (array_key_exists("total", $_GET) && $_GET['total'] == "usrs") {
	if (array_key_exists("clientkey", $_GET)) {
		$sql = sprintf(
				"select count(id) from weibo_users where uid in"
		. " (select a.uid from possessions a, clients b where a.clientid = b.id and b.`key` = '%s')",
		$_GET['clientkey']
		);
	} else {
		$sql = sprintf("select count(id) from weibo_users");
	}
	$rs = mysql_query($sql, $zconn->dblink)
		or die ("db_err: " . mysql_error());
	$total = 0;
	if (mysql_num_rows($rs) > 0) {
		$r = mysql_fetch_row($rs);
		$total = $r[0];
	}
	echo "~successful~$total~successful~";
	exit();
}

echo "~failed~Illegal submit~failed~";
?>