<?php
class DoController extends AppController {
	var $name = 'Do';
	var $uses = array('Account', 'WeiboUser');
	var $components = array('Auth');
	var $helpers = array(
		'Form', 'Html', 'Javascript',
		'Paginator'
	);
	
	var $__limit = 50;
	
	function beforeFilter() {
		//Configure::write('debug', 2);
		$this->pageTitle = 'Manager';
		$this->Auth->authenticate = ClassRegistry::init('Account'); 
		$this->Auth->userModel = 'Account';
		$this->Auth->loginAction = array('controller' => 'do', 'action' => 'login');
		$this->Auth->loginRedirect = array('controller' => 'do', 'action' => 'index');
		$this->Auth->logoutRedirect = array('controller' => 'do', 'action' => 'login');
		$this->Auth->userScope = array('Account.username' => 'admin');
		
		mysql_query("set names 'utf8';");
		
		parent::beforeFilter();
	}
	
	function __deleteAll($directory, $empty = false) { 
	    if(substr($directory,-1) == "/") { 
	        $directory = substr($directory,0,-1); 
	    } 
	
	    if(!file_exists($directory) || !is_dir($directory)) { 
	        return false; 
	    } elseif(!is_readable($directory)) { 
	        return false; 
	    } else { 
	        $directoryHandle = opendir($directory); 
	        
	        while ($contents = readdir($directoryHandle)) { 
	            if($contents != '.' && $contents != '..') { 
	                $path = $directory . "/" . $contents; 
	                
	                if(is_dir($path)) { 
	                    deleteAll($path); 
	                } else { 
	                    unlink($path); 
	                } 
	            } 
	        } 
	        
	        closedir($directoryHandle); 
	
	        if($empty == false) { 
	            if(!rmdir($directory)) { 
	                return false; 
	            } 
			} 
        
        	return true; 
    	} 
	}
	
	function login() {
		if (!empty($this->data)) {//if there are any POST data
			if ($this->Auth->user()) {//means username/password/status are all correct, login succeeded
				$this->Auth->login();
				$this->redirect($this->Auth->redirect());
			} else {// means login failed
				$this->Session->setFlash('Login failed, please try again.');
			}
		}
	}
	
	function logout() {
		/*logout part*/
		$this->Session->destroy();
		$this->Auth->logout();
		$this->redirect($this->Auth->redirect());
	}
	
	function index() {
		if (!$this->Auth->user()) $this->redirect(array('controller' => 'images', 'action' => 'login'));
		
		if (!empty($this->data)) {
			
		} else {
			$this->paginate = array(
				'WeiboUser' => array(
					'conditions' => array('1' => '1'),
					'order' => 'created_at desc',
					'limit' => $this->__limit
				)
			);
			$this->set('rs',
				$this->paginate('WeiboUser')
			);
		}
	}
	
	function delete() {
		if (!$this->Auth->user()) $this->redirect(array('controller' => 'images', 'action' => 'login'));
		
		$from = array();
		$id = 0;
		if (array_key_exists('id', $this->passedArgs)) {
			$id = intval($this->passedArgs['id']);
		}
		if (array_key_exists('fromk', $this->passedArgs)
			&& array_key_exists('fromv', $this->passedArgs)
		) {
			$fromk = explode(',', $this->passedArgs['fromk']);
			$fromv = explode(',', $this->passedArgs['fromv']);
			$from = array_combine($fromk, $fromv);
		}
		//need to be back to "key->value" kind of array
		/*
		 * 1.delete the record in db.
		 * 2.delete the whole folder that hold all the kinds of the picture
		 */
		if ($this->WeiboUser->delete($id)) {
			$this->Session->setFlash("Successfully deleted.");
			$this->redirect(array('controller' => 'do', 'action' => 'index') + $from);
		} else {
			$this->Session->setFlash("Failed to delete it in DB.");
			$this->render("error");
		}
	}
}