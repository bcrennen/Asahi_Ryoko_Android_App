package ca.bcit.asahiryoko;
/***
 * This Activity makes a model list of a signlelistview format.
 * Author: Saksham Bhardwaj
 * StudentNo: A01185352
 * Data April 09, 2021
 * Version: 1.0
 */
public class model
{
  String name,purl;
    model()
    {

    }
	/***
	References - https://www.gkmit.co/blog/mobile-development/how-to-use-model-class-in-android
			 https://developer.android.com/training/search/setup
			 https://www.youtube.com/watch?v=PmqYd-AdmC0
	*/
    public model(String name, String purl) {
        this.name = name;
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
