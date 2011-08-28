<center>
<b><font color="red"><?php $session->flash('auth'); ?></font></b>
</center>

<?php
echo $form->create(null, array('controller' => 'do', 'action' => 'login'));
?>

<?php
echo $form->input('Account.username', array('label' => 'Username:', 'style' => 'width:112px;'));
?>

<?php
echo $form->input('Account.password', array('label' => 'Password:', 'style' => 'width:112px;', 'type' => 'password'));
?>

<?php
echo $form->submit('Go', array('style' => 'border:0px;width:95px;height:28px;'));
?>

<?php
echo $form->end();
?>