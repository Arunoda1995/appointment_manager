package com.example.appointment_management.Thesaurus;

/**
 * Created by ACER on 4/10/2018.
 */

public class Synonym {


    private String category;
    private String synonyms;


   public void setCategory(String category)
   {

       this.category = category;

   }

    public String getCategory()
    {

        return category;
    }

    public void setSynonyms(String synonyms)
    {

        this.synonyms = synonyms;

    }

    public String getSynonyms()
    {

        return synonyms;

    }

    public String toString()
    {

        return "Synonym [Category = " +category+ ", Synonym = " +synonyms+ "] ";

    }


}
