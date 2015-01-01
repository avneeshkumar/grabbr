package com.grabbr.grabbrapp.utils;

public class MyConstants {
	public String url="http://ec2-54-169-110-170.ap-southeast-1.compute.amazonaws.com/";
	public String chat_url="ec2-54-169-110-170.ap-southeast-1.compute.amazonaws.com";
	public String register_url = url+"user/account/register/";
	public String profile_url = url+"user/profile/image/";
	public String register_url_fb = url+"user/account/register/fb/";
	public String verify_url = url+"user/account/verify/";
	public String sendpost_url = url+"user/post/create/";
	public String send_contacts = url+"user/contacts/save/";
	public String login_url = url+"login/";
	public String sendlocation_url = url+"user/locate/";
	public String profile_update_url = url+"user/profile/settings/";
	public String getnamefromid_url = url+"user/name/";
	public String username = "username";
	public String password = "password";
	public String id = "id";
	public String isfb = "isfb";
	public String fb_token = "fb_token";
	public String fb_id = "fb_id";
	public String isverified = "isverified";
	public String name = "name";
	public String chatpassword = "chat_password";
	public int noOfpostOnetime = 3;
	
	public String postview_url = url+"user/post/view/"+noOfpostOnetime+"/";
	public String mypostview_url = url+"user/mypost/view/"+noOfpostOnetime+"/";
	public String favpost_url = url+"user/favourite/view/"+noOfpostOnetime+"/";
	public String morepostview_url = url+"user/post/scroll/"+noOfpostOnetime+"/";
	public String moremypostview_url = url+"user/mypost/scroll/"+noOfpostOnetime+"/";
	public String morefavpost_url = url+"user/favourite/scroll/"+noOfpostOnetime+"/";
	
	public String fetchOnePost_url = url+"user/post/info/";
	
    public String postimage_url = url+"user/post/view/image/";
    public String like_url = url+"user/post/like/";
    public String share_url = url+"user/post/share/";
    public String sold_url = url+"user/post/sold/";
    public String discover_url = url+"user/post/search/8/";
    public String discover_scroll_url = url+"user/post/search/scroll/8/";
    public String DBNAME = "CHATS.db";
    
    public String myfollowers_url = url+"user/myfollowers/";
    public String myfollowings_url = url+"user/myfollowings/";
    
    public String publicfollowers_url = url+"user/public/followers/";
    public String publicfollowings_url = url+"user/public/followings/";
    
	public final static String MyPREFERENCES = "user_passwd" ;

    public String suggestfollower_url=url+"user/followers/suggest/";
    public String follow_url= url+"user/follow/";
    public String profileview_url = url+"user/profile/view/";
    public String search_friend_url=url+"user/friends/search/";
    
    public String publicprofile_info_url = url+"user/publicprofile/view/";
    
    public String publicpost_url = url+"user/publicpost/view/"+noOfpostOnetime+"/";
	public String publicfavpost_url = url+"user/favourite/public/view/"+noOfpostOnetime+"/";
	public String morepublicpost_url = url+"user/publicpost/scroll/"+noOfpostOnetime+"/";
	public String publicmorefavpost_url = url+"user/favourite/public/scroll/"+noOfpostOnetime+"/";
    
    public String notification_url = url+"user/notifications/";
    
}
