<?php
class Weibouser extends PBMessage
{
  var $wired_type = PBMessage::WIRED_LENGTH_DELIMITED;
  public function __construct($reader=null)
  {
    parent::__construct($reader);
    $this->fields["1"] = "PBInt";
    $this->values["1"] = "";
    $this->fields["2"] = "PBInt";
    $this->values["2"] = "";
    $this->fields["3"] = "PBString";
    $this->values["3"] = "";
    $this->fields["4"] = "PBString";
    $this->values["4"] = "";
    $this->fields["5"] = "PBInt";
    $this->values["5"] = "";
    $this->fields["6"] = "PBInt";
    $this->values["6"] = "";
    $this->fields["7"] = "PBString";
    $this->values["7"] = "";
    $this->fields["8"] = "PBString";
    $this->values["8"] = "";
    $this->fields["9"] = "PBString";
    $this->values["9"] = "";
    $this->fields["10"] = "PBString";
    $this->values["10"] = "";
    $this->fields["11"] = "PBString";
    $this->values["11"] = "";
    $this->fields["12"] = "PBString";
    $this->values["12"] = "";
    $this->fields["13"] = "PBInt";
    $this->values["13"] = "";
    $this->fields["14"] = "PBInt";
    $this->values["14"] = "";
    $this->fields["15"] = "PBInt";
    $this->values["15"] = "";
    $this->fields["16"] = "PBInt";
    $this->values["16"] = "";
    $this->fields["17"] = "PBString";
    $this->values["17"] = "";
    $this->fields["18"] = "PBInt";
    $this->values["18"] = "";
    $this->fields["19"] = "PBInt";
    $this->values["19"] = "";
    $this->fields["20"] = "PBInt";
    $this->values["20"] = "";
    $this->fields["21"] = "PBInt";
    $this->values["21"] = "";
    $this->fields["22"] = "PBInt";
    $this->values["22"] = "";
    $this->fields["23"] = "PBInt";
    $this->values["23"] = "";
    $this->fields["24"] = "PBInt";
    $this->values["24"] = "";
    $this->fields["25"] = "PBInt";
    $this->values["25"] = "";
    $this->fields["26"] = "PBString";
    $this->values["26"] = "";
    $this->fields["27"] = "PBInt";
    $this->values["27"] = "";
    $this->fields["28"] = "PBInt";
    $this->values["28"] = "";
    $this->fields["29"] = "PBInt";
    $this->values["29"] = "";
  }
  function id()
  {
    return $this->_get_value("1");
  }
  function set_id($value)
  {
    return $this->_set_value("1", $value);
  }
  function uid()
  {
    return $this->_get_value("2");
  }
  function set_uid($value)
  {
    return $this->_set_value("2", $value);
  }
  function screen_name()
  {
    return $this->_get_value("3");
  }
  function set_screen_name($value)
  {
    return $this->_set_value("3", $value);
  }
  function name()
  {
    return $this->_get_value("4");
  }
  function set_name($value)
  {
    return $this->_set_value("4", $value);
  }
  function province()
  {
    return $this->_get_value("5");
  }
  function set_province($value)
  {
    return $this->_set_value("5", $value);
  }
  function city()
  {
    return $this->_get_value("6");
  }
  function set_city($value)
  {
    return $this->_set_value("6", $value);
  }
  function location()
  {
    return $this->_get_value("7");
  }
  function set_location($value)
  {
    return $this->_set_value("7", $value);
  }
  function description()
  {
    return $this->_get_value("8");
  }
  function set_description($value)
  {
    return $this->_set_value("8", $value);
  }
  function url()
  {
    return $this->_get_value("9");
  }
  function set_url($value)
  {
    return $this->_set_value("9", $value);
  }
  function profile_image_url()
  {
    return $this->_get_value("10");
  }
  function set_profile_image_url($value)
  {
    return $this->_set_value("10", $value);
  }
  function domain()
  {
    return $this->_get_value("11");
  }
  function set_domain($value)
  {
    return $this->_set_value("11", $value);
  }
  function gender()
  {
    return $this->_get_value("12");
  }
  function set_gender($value)
  {
    return $this->_set_value("12", $value);
  }
  function followers_count()
  {
    return $this->_get_value("13");
  }
  function set_followers_count($value)
  {
    return $this->_set_value("13", $value);
  }
  function friends_count()
  {
    return $this->_get_value("14");
  }
  function set_friends_count($value)
  {
    return $this->_set_value("14", $value);
  }
  function statuses_count()
  {
    return $this->_get_value("15");
  }
  function set_statuses_count($value)
  {
    return $this->_set_value("15", $value);
  }
  function favourites_count()
  {
    return $this->_get_value("16");
  }
  function set_favourites_count($value)
  {
    return $this->_set_value("16", $value);
  }
  function created_at()
  {
    return $this->_get_value("17");
  }
  function set_created_at($value)
  {
    return $this->_set_value("17", $value);
  }
  function following()
  {
    return $this->_get_value("18");
  }
  function set_following($value)
  {
    return $this->_set_value("18", $value);
  }
  function allow_all_act_msg()
  {
    return $this->_get_value("19");
  }
  function set_allow_all_act_msg($value)
  {
    return $this->_set_value("19", $value);
  }
  function geo_enabled()
  {
    return $this->_get_value("20");
  }
  function set_geo_enabled($value)
  {
    return $this->_set_value("20", $value);
  }
  function verified()
  {
    return $this->_get_value("21");
  }
  function set_verified($value)
  {
    return $this->_set_value("21", $value);
  }
  function status_id()
  {
    return $this->_get_value("22");
  }
  function set_status_id($value)
  {
    return $this->_set_value("22", $value);
  }
  function clicks()
  {
    return $this->_get_value("23");
  }
  function set_clicks($value)
  {
    return $this->_set_value("23", $value);
  }
  function likes()
  {
    return $this->_get_value("24");
  }
  function set_likes($value)
  {
    return $this->_set_value("24", $value);
  }
  function dislikes()
  {
    return $this->_get_value("25");
  }
  function set_dislikes($value)
  {
    return $this->_set_value("25", $value);
  }
  function lastvotetime()
  {
    return $this->_get_value("26");
  }
  function set_lastvotetime($value)
  {
    return $this->_set_value("26", $value);
  }
  function weeklyclicks()
  {
    return $this->_get_value("27");
  }
  function set_weeklyclicks($value)
  {
    return $this->_set_value("27", $value);
  }
  function weeklylikes()
  {
    return $this->_get_value("28");
  }
  function set_weeklylikes($value)
  {
    return $this->_set_value("28", $value);
  }
  function weeklydislikes()
  {
    return $this->_get_value("29");
  }
  function set_weeklydislikes($value)
  {
    return $this->_set_value("29", $value);
  }
}
class Weibousers extends PBMessage
{
  var $wired_type = PBMessage::WIRED_LENGTH_DELIMITED;
  public function __construct($reader=null)
  {
    parent::__construct($reader);
    $this->fields["1"] = "Weibouser";
    $this->values["1"] = array();
  }
  function usr($offset)
  {
    return $this->_get_arr_value("1", $offset);
  }
  function add_usr()
  {
    return $this->_add_arr_value("1");
  }
  function set_usr($index, $value)
  {
    $this->_set_arr_value("1", $index, $value);
  }
  function remove_last_usr()
  {
    $this->_remove_last_arr_value("1");
  }
  function usr_size()
  {
    return $this->_get_arr_size("1");
  }
}
?>