<?php
	/*
	 * default global value zone
	 */
	$appserial = "gbhytfvnjurdcmkiesx,lowaz.;p201108282317";//must be as the same as the one in EntranceActivity 
	$accountid = 0;
	$relpath = "pictures/";
	$picpath='/var/www/html/letmesee/pictures/';
	
	/*
	 *@l - length of random string
	 */
	function generate_rand($l){
		$c= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		srand((double)microtime()*1000000);
		$rand = '';
		for($i=0; $i<$l; $i++) {
			$rand.= $c[rand()%strlen($c)];
		}
		return $rand;
	}
	
	/*
	 * get the actually size that should shrink or enlarge to, according
	 * the original size and the suggested size.
	 */
	function actualresizedrect($orgwidth, $orgheight, $dstwidth, $dstheight) {
		/*
		 * see if the original image is in portait or in landscape,
		 * and switch the width & height of the target image size.
		 */
		if ($orgwidth > $orgheight) {
			if ($dstwidth < $dstheight) {
				$tmp = $dstwidth;
				$dstwidth = $dstheight;
				$dstheight = $tmp;
			}
		} else {
			if ($dstwidth > $dstheight) {
				$tmp = $dstheight;
				$dstheight = $dstwidth;
				$dstwidth = $tmp;
			}
		}
		/*
		 * if the original width > height, resize it to the width of the target image size and let
		 * height resized with the same scale.
		 * if the original width <= height, resize it to the height of the target image size and
		 * let width resized with the same scale.
		 */
		if ($orgwidth > $orgheight) {
			$percent = $dstwidth / $orgwidth;
			$width = $dstwidth;
			$height = floor($orgheight * $percent + 0.5);
			return array($width, $height);
		} else {
			$percent = $dstheight / $orgheight;
			$width = floor($orgwidth * $percent + 0.5);
			$height = $dstheight;
			return array($width, $height);
		}
	}
	
	/*
	 * randomly get 6 charaters length string with "0-9, a-z".
	 */
	function get6lenstr() {
		$chars = array(
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		$dir = get3lenstr($chars);
		$chars = array(
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
			"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
			"u", "v", "w", "x", "y", "z"
		);
		$file = get3lenstr($chars);
		return $dir . $file;
	}
	function get3lenstr(&$chars) {
		$str = "";
		for ($i = 0; $i < 3; $i++) {
			$key = array_rand($chars);
			$str .= $chars[$key];
			unset($chars[$key]);
		}
		return $str;
	}
	
	/*
	 * as the function name says
	 */
	function __getclientip() {
    	$onlineip = false;
		if(getenv('HTTP_CLIENT_IP')) { 
			$onlineip = getenv('HTTP_CLIENT_IP');
		} elseif(getenv('HTTP_X_FORWARDED_FOR')) { 
			$onlineip = getenv('HTTP_X_FORWARDED_FOR');
		} elseif(getenv('REMOTE_ADDR')) { 
			$onlineip = getenv('REMOTE_ADDR');
		} else {
			if (!isset($HTTP_SERVER_VARS)) return false;
			$onlineip = $HTTP_SERVER_VARS['REMOTE_ADDR'];
		}
		return $onlineip;
    }
	
    /*
	 * local file namespace rulse:
	 * 1.combined with fixed 6-character string
	 * 2.the first 3 are directory (relative) name, and the second 3 are file name.
	 *   (for example:ert123.jpg will be stored in ert/123.jpg)
	 * 3.fit one will append a "f" after the second 3-character string,
	 *   thumb one will append a "t", and hight quality one will append a "h" and so on.
	 *   (for example:thumb of ert123.jpg will be stored in ert/123t.jpg)
	 */
    function loadsharingcopy($_file, $dstpath, $accountid, $intro = " ") {
    	$fitwidth = 400;
		$fitheight = 640;
		$hdwidth = 640;
		$hdheight = 1024;
		$thumbwidth = 180;
		$thumbheight = 240;
    	/*
		 * try to get $dir and $file following namespace rules mentioned above
		 */
    	$dir = $file = "";
    	$zconn = new zmysqlConn;
		do {
			/*
			 * get filename by namespace rules
			 */
			$fn = get6lenstr();
			$dir = substr($fn, 0, 3);
			$file = substr($fn, 3, 3);
			/*
			 * check if $dir + $file exists,
			 * if it does, reget them
			 */
			$sql = "select * from pictures where fileserial = '$fn'";
			$rs = mysql_query($sql, $zconn->dblink)
				or die ("~failed~~db_err:" . mysql_error() . "~~failed~");	
		} while (mysql_num_rows($rs) > 0);
		/*
		 * see if $dir exists, if not, create it
		 */
	    if (!is_dir($dstpath . '/' . $dir)) {
			if (!mkdir($dstpath . '/' . $dir)) {
				exit("~failed~~failed to create a directory.~~failed~");
			}
		}
		$filenameparts = explode(".", $_file['name']);
		$fileext = $filenameparts[count($filenameparts) - 1];
		if (count($filenameparts) == 1 || !in_array($fileext, array("jpg", "jpeg", "png", "bmp"))) {
			echo "~failed~~" . $_file['name'] . "is not a well-known (jpg, jpeg, png, bmp) picture format.~~failed~";
			exit();
		}
		$filename = $dstpath . '/' . $dir . '/' . $file. '.' . $fileext;
		if (!copy($_file['tmp_name'], $filename)) {
			echo "~failed~~Failed to copy file '" . $filename . "'.~~failed~";
			exit();
		} else {
			/*
			 * could put some picture quality control here.
			 */
			list($imgwidth, $imgheight) = getimagesize($filename);
			$source = null; 
			switch ($fileext) {
				case "jpg":
				case "jpeg":
					$source = imagecreatefromjpeg($filename);
					break;
				case "png":
					$source = imagecreatefrompng($filename);
					break;
				case "bmp":
					$source = imagecreatefrombmp($filename);
					break;
			}
			if ($source === false) {
				echo "~failed~~Failed to resize the picture(0).~~failed~";
				exit();
			}
			
			/*
			 * save into a thumb one
			 */
			list($twidth, $theight) = actualresizedrect($imgwidth, $imgheight, $thumbwidth, $thumbheight);
			$thumb = imagecreatetruecolor($twidth, $theight);
			if ($thumb === false) {
				echo "~failed~~Failed to resize into a thumb(1).~~failed~";
				exit();
			}
			if (!imagecopyresized($thumb, $source, 0, 0, 0, 0, $twidth, $theight, $imgwidth, $imgheight)) {
				echo "~failed~~Failed to resize into a thumb(2).~~failed~";
				exit();
			}
			$jpgfilename = $dstpath . '/' . $dir . '/' . $file . 't.jpg';
			if (!imagejpeg($thumb, $jpgfilename)) {
				echo "~failed~~Failed to resize into a thumb(3).~~failed~";
				exit();
			}
			
			/*
			 * save into a fit one
			 */
			list($fwidth, $fheight) = actualresizedrect($imgwidth, $imgheight, $fitwidth, $fitheight);
			$fit = imagecreatetruecolor($fwidth, $fheight);
			if ($fit === false) {
				echo "~failed~~Failed to resize into a fit one(1).~~failed~";
				exit();
			}
			if (!imagecopyresized($fit, $source, 0, 0, 0, 0, $fwidth, $fheight, $imgwidth, $imgheight)) {
				echo "~failed~~Failed to resize into a fit one(2).~~failed~";
				exit();
			}
			$jpgfilename = $dstpath . '/' . $dir . '/' . $file . 'f.jpg';
			if (!imagejpeg($fit, $jpgfilename)) {
				echo "~failed~~Failed to resize into a fit one(3).~~failed~";
				exit();
			}
		}
		
	    if ($_file['error'] == 0 && $_file['size'] > 0) {
			/*
			 * if file uploaded and then try to put the information about it into DB
			 */
			$ip = __getclientip();
			$sql = sprintf(
				"insert pictures (filename, relpath, time, ip, accountid, fileserial, clicks, likes, dislikes, intro)"
				. " values ('%s', '%s', '%s', '%s', %d, '%s', %d, %d, %d, '%s')",
				$_file['name'], $dstpath, date('Y-m-d H:i:s'),
				(($ip === false) ? '0.0.0.0' : $ip),
				$accountid, ($dir . $file), 0, 0, 0, $intro
			);
			mysql_query($sql, $zconn->dblink)
				or die ("~failed~~db_err:" . mysql_error() . "~~failed~");
			
			echo "~successful~File \"" . $_file['name'] . "\" uploaded.~successful~";
		} else {
			echo "~failed~~Something wrong with upload.~~failed~";
		}
    }
	
	/*
	 * copy from php.net
	 */	
	function imagecreatefrombmp($p_sFile) 
    { 
        //    Load the image into a string 
        $file    =    fopen($p_sFile,"rb"); 
        $read    =    fread($file,10); 
        while(!feof($file)&&($read<>"")) 
            $read    .=    fread($file,1024); 
        
        $temp    =    unpack("H*",$read); 
        $hex    =    $temp[1]; 
        $header    =    substr($hex,0,108); 
        
        //    Process the header 
        //    Structure: http://www.fastgraph.com/help/bmp_header_format.html 
        if (substr($header,0,4)=="424d") 
        { 
            //    Cut it in parts of 2 bytes 
            $header_parts    =    str_split($header,2); 
            
            //    Get the width        4 bytes 
            $width            =    hexdec($header_parts[19].$header_parts[18]); 
            
            //    Get the height        4 bytes 
            $height            =    hexdec($header_parts[23].$header_parts[22]); 
            
            //    Unset the header params 
            unset($header_parts); 
        } 
        
        //    Define starting X and Y 
        $x                =    0; 
        $y                =    1; 
        
        //    Create newimage 
        $image            =    imagecreatetruecolor($width,$height); 
        
        //    Grab the body from the image 
        $body            =    substr($hex,108); 

        //    Calculate if padding at the end-line is needed 
        //    Divided by two to keep overview. 
        //    1 byte = 2 HEX-chars 
        $body_size        =    (strlen($body)/2); 
        $header_size    =    ($width*$height); 

        //    Use end-line padding? Only when needed 
        $usePadding        =    ($body_size>($header_size*3)+4); 
        
        //    Using a for-loop with index-calculation instaid of str_split to avoid large memory consumption 
        //    Calculate the next DWORD-position in the body 
        for ($i=0;$i<$body_size;$i+=3) 
        { 
            //    Calculate line-ending and padding 
            if ($x>=$width) 
            { 
                //    If padding needed, ignore image-padding 
                //    Shift i to the ending of the current 32-bit-block 
                if ($usePadding) 
                    $i    +=    $width%4; 
                
                //    Reset horizontal position 
                $x    =    0; 
                
                //    Raise the height-position (bottom-up) 
                $y++; 
                
                //    Reached the image-height? Break the for-loop 
                if ($y>$height) 
                    break; 
            } 
            
            //    Calculation of the RGB-pixel (defined as BGR in image-data) 
            //    Define $i_pos as absolute position in the body 
            $i_pos    =    $i*2; 
            $r        =    hexdec($body[$i_pos+4].$body[$i_pos+5]); 
            $g        =    hexdec($body[$i_pos+2].$body[$i_pos+3]); 
            $b        =    hexdec($body[$i_pos].$body[$i_pos+1]); 
            
            //    Calculate and draw the pixel 
            $color    =    imagecolorallocate($image,$r,$g,$b); 
            imagesetpixel($image,$x,$height-$y,$color); 
            
            //    Raise the horizontal position 
            $x++; 
        } 
        
        //    Unset the body / free the memory 
        unset($body); 
        
        //    Return image-object 
        return $image; 
    }
    
    function get_cururl() {
    	return
    		(!empty($_SERVER['HTTPS'])) ?
    			"https://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI']
    			: "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
    }
    
    function _xsendfile($path, $f, $lastvoteinfos = null) {
    	header('X-SendFile: ' . $path . $f);
		header('Content-Type: image/jpg');
		if ($lastvoteinfos != null) {
			header('lastvote:' . $lastvoteinfos['lastvote']);
			header('lastvotetime:' . $lastvoteinfos['lastvotetime']);
			header('clicks:' . $lastvoteinfos['clicks']);
			header('likes:' . $lastvoteinfos['likes']);
			header('dislikes:' . $lastvoteinfos['dislikes']);
			header('weeklyclicks:' . $lastvoteinfos['weeklyclicks']);
			header('weeklylikes:' . $lastvoteinfos['weeklylikes']);
			header('weeklydislikes:' . $lastvoteinfos['weeklydislikes']);
		}
		die();
    }
    
    function _getrandomkey() {
		$randomkey = date("YmdH");
    	return $randomkey;
    }
    
    function _getlastvote($dbconn, $weibouserid, $clientkey) {
    	$sql = "select a.id as pictureid, a.clicks, a.likes, a.dislikes, a.weeklyclicks, a.weeklylikes, a.weeklydislikes,"
    		. " b.clientid, date_format(b.`time`, '%Y-%m-%d %H:%i:%s') as lastvotetime, b.`type` as lastvote,"
    		. " b.likes as peronallikes, b.dislikes as personaldislikes"
			. " from weibo_users a, votes b, clients c"
			. " where a.id = '$weibouserid' and a.id = b.weibouserid"
			. " and c.key = '$clientkey' and b.clientid = c.id";
		$rs = mysql_query($sql, $dbconn->dblink);
		$r = array();
		if (mysql_num_rows($rs) > 0) {
			$r = mysql_fetch_assoc($rs);
		}
		return $r;
    }
?>
