<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="zh-cn" dir="ltr">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
<body>

<?php

session_start();
include_once( 'config.php' );
include_once( 'weibooauth.php' );



$o = new WeiboOAuth( WB_AKEY , WB_SKEY , $_SESSION['keys']['oauth_token'] , $_SESSION['keys']['oauth_token_secret']  );

$last_key = $o->getAccessToken(  $_REQUEST['oauth_verifier'] ) ;

echo print_r($last_key, true);


?>

</body>
</html>
