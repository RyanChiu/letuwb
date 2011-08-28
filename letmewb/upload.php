<?php
/*
 * this script try to get the file POSTED FROM AN ANDROID TERMINAL
*/
require_once 'zmysqlConn.class.php';
require_once 'extrakits.inc.php';
$zconn = new zmysqlConn;

//list($t1, $t2) = actualresizedrect(259, 194, $fitwidth, $fitheight);//for debug
//exit("$t1, $t2\n");//for debug

/*
 * try to prevent illegal uploading
*/
//var_dump($_POST);
if($_POST["action"] != "Upload Image" 
	|| !array_key_exists("accountid", $_POST)
	|| !array_key_exists("intro", $_POST)) {
	echo "~failed~~Illegal uploading.~~failed~";
	exit();
}
$accountid = intval($_POST['accountid']);
if($accountid != 1) {
 	echo "sorry, this feature is temporarily suspended";
        exit();
}

/*
 * see if there is any file uploads
*/
if (!isset($HTTP_POST_FILES) && !isset($_FILES)) {
	echo "~failed~~No file uploads.~~failed~";
	exit();
}

if(!isset($_FILES) && isset($HTTP_POST_FILES)) {
	$_FILES = $HTTP_POST_FILES;
}

$files = array_values($_FILES);

loadsharingcopy($files[0], $relpath, $accountid, $_POST['intro']);

//echo print_r($_POST, true);//for debug

//echo print_r($files, true);//for debug

?>
