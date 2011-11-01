<?php
/*
 * the script will all _getuser.php repeatedly with the file
 * which will be given by the first parameter. And the file will
 * hold the user ids one line after another.
 */
if (($argc) != 2) {
	exit("It'll take one parameter point to a file that holds the user ids.\n");
}
$path_parts = pathinfo($argv[0]);
$fn = $argv[1];
$ids = array();
if (file_exists($fn)) {
	$handle = fopen($fn, 'r');
	if ($handle) {
		while (!feof($handle)) {
			$buf = fgets($handle);
			$buf = trim($buf);
			if (!empty($buf)) {
				array_push($ids, $buf);
			}
		}
		foreach ($ids as $id) {
			$output = array();
			exec("php " . $path_parts["dirname"] . "/_getuser.php " . $id, $output);
			echo implode("\n", $output) . "\n";
			echo "User " . $id . " imported.==--(" . date('Y-m-d H:i:s') . ")\n";
		}
	} else {
		exit(sprintf("Failed to open file \"%s\".\n", $fn));
	}
} else {
	exit(sprintf("File \"%s\" does not exist.\n", $fn));
}
?>