<?php
include "extrakits.inc.php";
include 'zmysqlConn.class.php';
/**
 * _xsendfile(..) will send an image and then end the page immediately.
 * 
 * this page will only give right image when the md5 of "skey + rkey + 
 * ckey + prefixurl" equals to $_GET['check']. where skey = $appserial,
 * rkey = _getrandomkey(), ckey = $_GET['ckey'] and means a clientkey,
 * prefixurl the current url without parameter "check".
 * if parameter rkey is missing, it will give a clean "text content"
 * randomkey from the server.
 */

if (!array_key_exists("f", $_GET)
	|| !array_key_exists("ckey", $_GET)
	|| !array_key_exists("check", $_GET)) {
	/**
	 * if the neccesary parameters are missing, it's a illegal request absolutely.
	 */
	exit("Illegal request(0).");
}

if (!array_key_exists("rkey", $_GET) || $_GET['rkey'] != _getrandomkey()) {
	echo "~successful~" . _getrandomkey() . "~successful~";
	exit();
}

$url = get_cururl();
$rst = parse_url($url);
$pfurl = $rst['scheme'] . "://" . $rst['host'] . $rst['path'] . "?";
$i = 0;
foreach ($_GET as $k => $v) {
	if ($k != "check") {
		if ($i != 0) $pfurl = $pfurl . "&";
		$pfurl .= ($k . "=" . $v);
		$i++;
	}
}
//echo "<br/>" . $pfurl;//for debug
//echo "<br/>" . $appserial . _getrandomkey() . $_GET['ckey'] . $pfurl;//for debug
//echo "<br/>" . md5($appserial . _getrandomkey() . $_GET['ckey'] . $pfurl);//for debug
//echo "<br/>" . $_GET['check'];//for debug
//exit();//for debug
if (md5($appserial . _getrandomkey() . $_GET['ckey'] . $pfurl) == $_GET['check']) {
	$file = $_GET['f'];
	$fi = explode(".", $file);
	if (count($fi) != 2) {// if $file is not like abc/def.jpg
		//exit("1");//for debug
		exit("Wrong path format.");
	} else {
		$fs = explode("/", $fi[0]);
		if (count($fs) != 2) {// if $fi[0] is not like abc/def
			_xsendfile($picpath, $_GET['f']);
		} else {
			if (strlen($fs[0]) != 3) {
				//exit("2");//for debug
				exit("Wrong directory name format.");
			} else {
				if (strlen($fs[1]) < 3) {
					//exit("3");//for debug
					exit("Wrong file name format.");
				} else {
					$zconn = new zmysqlConn;
					$fileserial = $fs[0] . substr($fs[1], 0, 3);
					$clientkey = $_GET['ckey'];
					$r = _getlastvote($zconn, $fileserial, $clientkey);
					$sql = "update pictures set clicks = clicks + 1, weeklyclicks = weeklyclicks + 1 where fileserial = '$fileserial'";
					mysql_query($sql, $zconn->dblink);
					if (count($r) >= 2) {
						//exit("0, $file");//for debug
						_xsendfile($picpath, $file, $r);
					} else {
						//exit("4");//for debug
						_xsendfile($picpath, $file);
					}
				}
			}
		}
	}
} else {
	/**
	 * if checksum is not passed, it's a illegal request, either.
	 */
	//exit("5");//for debug
	exit("Illegal request(1).");
}
?>
