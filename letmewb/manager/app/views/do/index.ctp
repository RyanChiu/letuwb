<center>
<b><font color="red"><?php $session->flash('auth'); ?></font></b>
</center>

<table width="100%" style="border-width:1px;">
<thead>
<tr>
	<th><b><?php echo $paginator->sort('ID', 'WeiboUser.id'); ?></b></th>
	<th><b><?php echo $paginator->sort('User ID', 'WeiboUser.uid'); ?></b></th>
	<th><b><?php echo $paginator->sort('Screen Name', 'WeiboUser.screen_name'); ?></b></th>
	<th><b><?php echo $paginator->sort('Gender', 'WeiboUser.gender'); ?></b></th>
	<th><b><?php echo $paginator->sort('Location', 'WeiboUser.location'); ?></b></th>
	<th><b><?php echo $paginator->sort('Last Vote Time', 'WeiboUser.lastvotetime'); ?></b></th>
	<th><b><?php echo $paginator->sort('Likes', 'WeiboUser.likes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Dislikes', 'WeiboUser.dislikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Clicks', 'WeiboUser.clicks'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Likes', 'WeiboUser.weeklylikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Dislikes', 'WeiboUser.weeklydislikes'); ?></b></th>
	<th><b><?php echo $paginator->sort('Wkly Clicks', 'WeiboUser.weeklyclicks'); ?></b></th>
	<th><b>Image</b></th>
	<th style="text-align:center;"><b>Operation</b></th>
</tr>
</thead>
<?php
$i = 0;
foreach ($rs as $r) {
?>
<tr <?php echo $i % 2 == 0? '' : 'style="background-color:#00ffff;"'; ?>>
	<td><?php echo $r['WeiboUser']['id']; ?></td>
	<td><?php echo $r['WeiboUser']['uid']; ?></td>
	<td><?php echo $r['WeiboUser']['screen_name']; ?></td>
	<td><?php echo $r['WeiboUser']['gender']; ?></td>
	<td><?php echo $r['WeiboUser']['location']; ?></td>
	<td><?php echo $r['WeiboUser']['lastvotetime']; ?></td>
	<td><?php echo $r['WeiboUser']['likes']; ?></td>
	<td><?php echo $r['WeiboUser']['dislikes']; ?></td>
	<td><?php echo $r['WeiboUser']['clicks']; ?></td>
	<td><?php echo $r['WeiboUser']['weeklylikes']; ?></td>
	<td><?php echo $r['WeiboUser']['weeklydislikes']; ?></td>
	<td><?php echo $r['WeiboUser']['weeklyclicks']; ?></td>
	<td>
	<?php
	$picurl = $r['WeiboUser']['profile_image_url'];
	$picurl = str_replace("/50/", "/180/", $picurl);
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
			'uid' => $r['WeiboUser']['uid'],
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
