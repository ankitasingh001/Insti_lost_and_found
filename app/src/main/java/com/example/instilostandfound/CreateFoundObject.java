package com.example.instilostandfound;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates a database object for each item found or lost
 */
public class CreateFoundObject implements Serializable {

    private String mImageUrl;
    private String mKey;
    private String mTitle;
    private String mDate;
    private String mLDAP;
    private String mDescription;
    private String mLocation;
    private String mCategory;
    private String mDateFound;
    private String mtype;

    /**
     * Constructor to initilaise objects of the class object found /lost
     * @param LDAPID LDAP id of the user
     * @param title  title of the post
     * @param imageUrl image url of the object as stored in firebase storage
     * @param location location of the object
     * @param desc description of the object
     * @param category Category of the object
     * @param datefound Date when object was lost/found
     * @param type type of item -> lost/found
     */
    public CreateFoundObject(String LDAPID,String title ,String imageUrl, String location, String desc,
                             String category, String datefound,String type) {
        if (LDAPID.trim().equals("")) {
            mLDAP = "No Name";
        }

        mImageUrl = imageUrl;
        mLDAP = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDescription = desc;
        mLocation = location;
        mDateFound = datefound;
        mCategory = category;
        mTitle = title;
        mtype = type;

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        mDate = df.format(date);



    }

    /**
     * Defined a no object constructor for empty values
     */
    public CreateFoundObject() {

    }

    /**
     * Sets image url
     * @param imageUrl image uri
     */
    public void setImageUrl(String imageUrl)
    {
        mImageUrl = imageUrl;
    }

    /**
     * Get LDAP id
     * @return LDAP of user
     */
    public String getLDAP() {
        return mLDAP;
    }

    /**
     * Setting LDAP credentials
     * @param LDAP LDAP id of user
     */
    public void setLDAP(String LDAP) { mLDAP= LDAP; }

    /**
     * Set the title of post
     * @return title of post
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     * get the image url
     * @return image url
     */
    public String getImageUrl()
    {
        return mImageUrl;
    }

    /**
     * get category of post
     * @return category of post
     */
    public String getmCategory()
    {
        return mCategory;
    }

    /**
     * get date when object was lost/found
     * @return date of lost/found
     */
    public String getmDateFound()
    {
        return mDateFound;
    }

    /**
     * description of post
     * @return description
     */

    public void setDate(String Date)
    { mDate = Date ; }

    public String getmDescription()
    { return mDescription; }

    /**
     * Get the location of object
     * @return location coordinates
     */
    public String getmLocation()
    {return mLocation;}


    @Exclude
    public String getKey()
    {
        return mKey;
    }
    @Exclude
    public void setKey(String key)
    {
        mKey = key;
    }

    /**
     * Get date of item lost/found
     * @return date item
     */
    public String getDate()
    {
        return mDate;
    }

}
