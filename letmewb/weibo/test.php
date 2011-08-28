<?php
/*
 * suggestion steps:
 * 1,curl this page and get the aurl;
 * 2,use curl to post the auth message "user/pwd" from aurl and try to get the content of page "callback.php"
 */

session_start();
//if( isset($_SESSION['last_key']) ) header("Location: weibolist.php");
include_once( 'config.php' );
include_once( 'weibooauth.php' );



$o = new WeiboOAuth( WB_AKEY , WB_SKEY  );

$keys = $o->getRequestToken();

$_SESSION['keys'] = $keys;

$callback_prefix = 'http://' . $_SERVER['HTTP_HOST'];
$callback_self = $_SERVER['PHP_SELF'];
$callback = $callback_prefix . str_replace("test.php", "test_callback.php", $callback_self);

$aurl = $o->getAuthorizeURL( $keys['oauth_token'] ,false , $callback);
?>

<a href="<?=$aurl?>">Use Oauth to login</a>
