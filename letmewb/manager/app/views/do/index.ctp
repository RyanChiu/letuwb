<center>
<b><font color="red"><?php $session->flash('auth'); ?></font></b>
</center>

<table width="100%" style="border-width:1px;">
<thead>
<tr>
	<th><b><?php echo $paginator->sort('ID', 'Picture.id'); ?></b></th>
	<th><b><?php echo $paginator->sort('File Serial', 'Picture.fileserial'); ?></b></th>
	<th><b><?php echo $paginator->sort('File Name', 'Picture.filename'); ?></b></th>
	<th><b><?php echo $paginator->sort('Account', 'Picture.accountid'); ?></b></th>
	<th><b><?php echo $paginator->sort('Upload Time', 'Picture.time'); ?></b></th>
	<th><b><?php echo $paginator->sort('Last Vote Time', 'Picture.lastvotetime'); ?></b></th>
	<th><b><?php echo $paginator->sort('Likes', 'Picture.likes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Dislikes', 'Picture.dislikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Clicks', 'Picture.clicks'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Likes', 'Picture.weeklylikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Dislikes', 'Picture.weeklydislikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Clicks', 'Picture.weeklyclicks'); ?></b></th>
	<th><b>Image</b></th>
	<th style="text-align:center;"><b>Operation</b></th>
</tr>
</thead>
<?php
$i = 0;
foreach ($rs as $r) {
?>
<tr <?php echo $i % 2 == 0? '' : 'style="background-color:#00ffff;"'; ?>>
	<td><?php echo $r['Picture']['id']; ?></td>
	<td><?php echo $r['Picture']['fileserial']; ?></td>
	<td><?php echo $r['Picture']['filename']; ?></td>
	<td><?php echo $r['Picture']['accountid']; ?></td>
	<td><?php echo $r['Picture']['time']; ?></td>
	<td><?php echo $r['Picture']['lastvotetime']; ?></td>
	<td><?php echo $r['Picture']['likes']; ?></td>
	<td><?php echo $r['Picture']['dislikes']; ?></td>
	<td><?php echo $r['Picture']['clicks']; ?></td>
	<td><?php echo $r['Picture']['weeklylikes']; ?></td>
	<td><?php echo $r['Picture']['weeklydislikes']; ?></td>
	<td><?php echo $r['Picture']['weeklyclicks']; ?></td>
	<td>
	<?php
	$picurl = "/../" . $r['Picture']['relpath'] . substr($r['Picture']['fileserial'], 0, 3) . "/" . substr($r['Picture']['fileserial'], 3, 3) . "t.jpg";
	echo $html->image($picurl, array('style' => 'width:136px;border:0px;'))
	?>
	</td>
	<td style="text-align:center;font-weight:bold;">
	<?php
	$fromk = array();
	$fromv = array();
	foreach ($this->params['named'] as $k => $v) {
		array_push($fromk, $k);
		array_push($fromv, $v);
	}
	echo $html->link(
		'Delete',
		array(
			'controller' => 'do', 'action' => 'delete',
			'picid' => $r['Picture']['id'],
			'picpath' => $r['Picture']['relpath'],
			'picdir' => substr($r['Picture']['fileserial'], 0, 3),
			'fromk' => implode(',', $fromk),
			'fromv' => implode(',', $fromv)
		),
		array('title' => 'Click to delete.'),
		'Are you sure to delete it?',
		false
	);
	?>
	</td>
</tr>
<?php
$i++;
}
?>
</table>

<!-- paginator start -->
<?php
if ($paginator->hasPage(null, 2)) {
?>
<table>
<tr>
<td>
<!-- Shows the page numbers -->
<?php 
	echo $paginator->numbers(
		array(
			'first' => '|<<', 'last' => '>>|',
			'before' => ' | ', 'after' => ' | ',
			'modulus' => 16
		)
	);
?>
</td>
<td>
<!-- Shows the next and previous links -->
<?php
	echo $paginator->prev(
		$html->image('prev.gif', array('style' => 'border:0px;margin-top:2px;')),
		array('escape' => false),
		$html->image('prev_dis.gif', array('style' => 'border:0px;margin-top:2px;')),
		array('escape' => false, 'class' => 'disabled')
	);
?>
</td>
<td>
<?php
	echo $paginator->next(
		$html->image('next.gif', array('style' => 'border:0px;margin-top:2px;')),
		array('escape' => false),
		$html->image('next_dis.gif', array('style' => 'border:0px;margin-top:2px;')),
		array('escape' => false, 'class' => 'disabled')
	);
?>
</td>
<td>
<!-- prints X of Y, where X is current page and Y is number of pages -->
<?php echo $paginator->counter(); ?>
</td>
</tr>
</table>
<?php
}
?>
<!-- paginator end -->

<br/>
<?php
echo $html->link(
	'Logout',
	array('controller' => 'do', 'action' => 'logout'),
	null, false, false
);
?>
