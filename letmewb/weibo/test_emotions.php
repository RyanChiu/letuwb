<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="zh-cn" dir="ltr">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
<body>

<?php
include_once( 'config.php' );
include_once( 'weibooauth.php' );

$o = new WeiboOAuth( WB_AKEY , WB_SKEY , LAST_OAUTH_TOKEN, LAST_OAUTH_TOKEN_SECRET);
$content = $o->get(sprintf("http://api.t.sina.com.cn/emotions.json"), array('source' => '3150341378'));
//var_dump($content);
echo "<b>The following is the emotions:</b>" . "<br/>";
foreach ($content as $c) {
	//var_dump($c);
	echo $c['phrase']. ", " . $c['type']
		. ", " . ($c['is_common'] ? 'common' : '!');
	echo '<img src="' . $c['url'] . '" border=0/>';
	echo "<br/>";
/*
	echo $c['name'];
	$img_url = $c['profile_image_url'];
	echo '<img src="' . $img_url . '" border=0/>';
	$img_url = str_replace("/50/", "/180/", $img_url);
	echo '<img src="' . $img_url . '" border=0/>';
	echo "<br/>";
*/
}
?>

</body>
</html>
