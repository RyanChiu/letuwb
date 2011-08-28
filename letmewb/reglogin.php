<?php
include 'zmysqlConn.class.php';
$zconn = new zmysqlConn;
/*
 * the script deal with 2 moves:
 * 1.login
 * check username/password from POST data with $_POST['usr'] and $_POST['pwd'] 
 * 2.register
 * register a new user with $_POST['usr'] and $_POST['pwd']
 */
$ckey = array_key_exists("ckey", $_POST) ? $_POST['ckey'] : "";
if (array_key_exists("usr", $_POST) && array_key_exists("pwd", $_POST) && !array_key_exists("rpt", $_POST)) {//log in
	$sql = sprintf("select * from accounts where username = '%s'", $_POST['usr']);
	$rs = mysql_query($sql, $zconn->dblink)
		or die ("~failed~db_err:" . mysql_error() . ".~failed~");
	if (mysql_num_rows($rs) > 0) {
		$r = mysql_fetch_assoc($rs);
		$id = $r['id'];
		if ($r['password'] === md5($_POST['pwd'])) {
			$sql = sprintf(
				"update accounts set clientid = (%s) where id = %d",
				"select id from clients where `key` = '$ckey'",
				$id
			);
			mysql_query($sql, $zconn->dblink);
			echo "~successful~Logged-in.$id~successful~";
			exit();
		} else {
			echo "~failed~Wrong password.~failed~";
			exit();
		}
	} else {
		echo "~failed~No such a username.~failed~";
		exit();
	}
} else {
	if (array_key_exists("usr", $_POST) && array_key_exists("pwd", $_POST) && array_key_exists("rpt", $_POST)) {//register
		$sql = sprintf("select * from accounts where username = '%s'", $_POST['usr']);
		$rs = mysql_query($sql, $zconn->dblink)
			or die ("~failed~db_err:" . mysql_error() . ".~failed~");
		if (mysql_num_rows($rs) > 0) {
			echo "~failed~Username alread exists.~failed~";
			exit();
		} else {
			$sql = sprintf("insert accounts (username, password, regtime, clientid) values ('%s', '%s', '%s', (%s))",
				$_POST['usr'], md5($_POST['pwd']), date('Y-m-d h:i:s'),
				"select id from clients where `key` = '$ckey'"
			);
			mysql_query($sql, $zconn->dblink)
				or die ("~failed~db_err:" . mysql_error() . ".~failed~");
			$id = mysql_insert_id();
			if (mysql_affected_rows() > 0) {
				echo "~successful~Registered.$id~successful~";
				exit();
			} else {
				echo "~failed~Not registered.~failed~";
				exit();
			}
		}
	} else {
		echo "~failed~Illegal submit.~failed~";
	}
}
?>