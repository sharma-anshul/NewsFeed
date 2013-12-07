<?php
				
	class NewsFeedAPI {
			
		public $mysqlConn;

		public $mysql_host = "mysql3.000webhost.com";
		public $mysql_database = "a4144322_summ";
		public $mysql_user = "a4144322_ansh";
		public $mysql_password = "sahil91";
			
		/** 
		 * Connects to mysql
		 *
		 * @return boolean true if connection to mysql was successful, false
		 *  otherwise
		 *
		 * @access private
		 */
		private function _mysqlConnect() {
			$this->mysqlConn = new mysqli($this->mysql_host, $this->mysql_user, $this->mysql_password, $this->mysql_database);

			if ($this->mysqlConn->connect_errno) {
			        return false;
			}

			return true;
		}


		/**
		 * Determines if mysql is connected by testing whether or not the 
		 * mysql instance variable has been set
		 *
		 * @return boolean true if $this->mysqlConn is set, false otherwise
		 *
		 * @access private
		 */
		private function _isMysqlConnected() {
			if (isset($this->mysqlConn)) {
			        return $this->mysqlConn->ping();
			} else {
			        return false;
			}
		}


		/**
		 * Exectues a sql statement and logs an error if the 
		 * query fails. 
		 *
		 * @return none
		 *
		 * @access private
		 */
		private function _executeSQLStatement(&$statement) {
			$success = $statement->execute();
			return $success;
		}

		/**
		 * Check for sql connection before performing a query.
		 * If there is no connection, try to make a connection.
		 * 
		 * @return false if connection cannot be established, true otherwise
		 *
		 * @access private
		 */
        	private function _checkForConnection() {
		        if (!$this->_isMysqlConnected()) {
		            // connect to mysql
		        	if (!$this->_mysqlConnect()) {
		               	return false;
		           	}
                }

               	return true;
        	}

		/**
		 * Return everything in the database.
		 *
		 * @return everything
		 *
		 * @access public
		 */
		public function getEverything() {
			if(!$this->_checkForConnection()) {
				return false;
			}

			$res = Array();
			$query = 'SELECT url '.
				 'FROM urls';

			if($statement = $this->mysqlConn->prepare($query)) {
				$this->_executeSQLStatement($statement); 
				$statement->bind_result($url);   

				while($statement->fetch()) {
					$res[] = array($url);
				}

				$statement->close();
			}

			return json_encode($res);			
		}

		/**
		 * Store new row in table
		 *
		 * @return True if success, False otherwise
		 *
		 * @access public
		 */
		public function addSummary($url, $topWords, $summary) {
			if(!$this->_checkForConnection()) {
		    	return false;
			}
			
			$success = false;
			$query  =
					'INSERT INTO urls ' .
					'(url, top_words, summary) ' .
					'VALUES (?, ?, ?)';
			
			if($statement = $this->mysqlConn->prepare($query)) {
				$statement->bind_param('sss', $url, $topWords, $summary);
				$success = $this->_executeSQLStatement($statement);
				$statement->close();
			}
			return $success;
		}
			
	}
	
	$nfapi = new NewsFeedAPI();

	$url = $_SERVER['REQUEST_URI'];
	$parsed_url = parse_url($url);
	$path = $parsed_url["path"];
	
	echo $path;
	echo "true";
	$root = '';

	if($path === ($root.'/api.php/addSummary')) {
		echo 'inside function';
		echo $nfapi->addSummary($_GET['url'], $_GET['topWords'], $_GET['summary']);
	}
	//echo $nfapi->getEverything();
?>
