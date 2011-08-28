<?php
class Account extends AppModel {
	var $name = "Account";
	
	function hashPasswords($data) {
		if (isset($data['Account']['password'])) {
			$data['Account']['password'] = md5($data['Account']['password']);
			return $data; 
		}
		return $data;
	}
}
?>