package com.grabbr.grabbrapp.daos;

public class NewsfeedData {
	
	String Catgeory;
	String name;
    String Description;
    String Privacy;
    Float Price;
    String Heading;
    String TimeStamp;
    String PostId;
    String UserId;
    String picurl1;
    String picurl2;
    String picurl3;
    String like;
    String liked;
    String city;
    int currImg = 1;
    String isSold;
    
    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsSold() {
		return isSold;
	}

	public void setIsSold(String string) {
		this.isSold = string;
	}

	public int getCurrImg() {
		return currImg;
	}
    
   
	public void setCurrImg(int currImg) {
		this.currImg = currImg;
	}

	public String getLiked() {
		return liked;
	}

	public void setLiked(String liked) {
		this.liked = liked;
	}

	public String getLike() {
		return like;
	}

	public void setLike(String like) {
		this.like = like;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicurl1() {
        return picurl1;
    }

    public void setPicurl1(String picurl1) {
        this.picurl1 = picurl1;
    }

    public String getPicurl2() {
        return picurl2;
    }

    public void setPicurl2(String picurl2) {
        this.picurl2 = picurl2;
    }

    public String getPicurl3() {
        return picurl3;
    }

    public void setPicurl3(String picurl3) {
        this.picurl3 = picurl3;
    }

    public String getCatgeory() {
        return Catgeory;
    }

    public void setCatgeory(String catgeory) {
        Catgeory = catgeory;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrivacy() {
        return Privacy;
    }

    public void setPrivacy(String privacy) {
        Privacy = privacy;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getPostId() {
        return PostId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }


}
