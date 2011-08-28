<?php
require_once("./pb_parser.php");
$parser = new PBParser();
$parser->parse("../copiesonly/weibousers_protos.proto");
echo "done\n";
?>
