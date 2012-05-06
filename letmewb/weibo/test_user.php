<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="zh-cn" dir="ltr">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
<body>

<?php
include_once( 'config.php' );
include_once( 'weibooauth.php' );

$c = new WeiboClient(WB_AKEY, WB_SKEY, LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$uinfo = $c->show_user('2253016840');
echo print_r($uinfo, true);

echo "\n<br/><br/>\n";

$o = new WeiboOAuth( WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$content = $o->get(sprintf("http://api.t.sina.com.cn/users/hot.json"), array('source' => WB_AKEY));
//var_dump($content);
echo "<b>The following is the hottest ones:</b>" . "<br/>";
foreach ($content as $c) {
	echo $c['name'];
	$img_url = $c['profile_image_url'];
	echo '<img src="' . $img_url . '" border=0/>';
	$img_url = str_replace("/50/", "/180/", $img_url);
	echo '<img src="' . $img_url . '" border=0/>';
	echo "<br/>";
}
?>

</body>
</html>
