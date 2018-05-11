package com.example.appointment_management.Thesaurus;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER on 4/10/2018.
 */

public class XMLParser {

    static final String KEY_LIST = "list";
    static final String KEY_CATEGORY = "category";
    static final String KEY_SYNONYMS = "synonyms";

    public static List<Synonym> getSynonyms(Context ctx)
    {

        List<Synonym> synonymList;
        synonymList = new ArrayList<Synonym>();

        Synonym currentSynonym = null;

        String currentText = "";

        try
        {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();

            FileInputStream fileInputStream= ctx.openFileInput("synonyms.xml");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            xmlPullParser.setInput(bufferedReader);

            int type = xmlPullParser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT)
            {

                String currentTag = xmlPullParser.getName();

                switch(type)
                {

                    case XmlPullParser.START_TAG:
                        if(currentTag.equalsIgnoreCase(KEY_LIST))
                            currentSynonym = new Synonym();
                        break;
                    case XmlPullParser.TEXT:
                        currentText = xmlPullParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(currentTag.equalsIgnoreCase(KEY_LIST))
                                synonymList.add(currentSynonym);
                        else if(currentTag.equalsIgnoreCase(KEY_CATEGORY))
                            currentSynonym.setCategory(currentText);
                        else if(currentTag.equalsIgnoreCase(KEY_SYNONYMS))
                            currentSynonym.setSynonyms(currentText);
                        break;
                    default:
                        break;
                }

                type = xmlPullParser.next();

            }

        }

        catch (Exception e)
        {

            e.printStackTrace();

        }

        return synonymList;

    }


}
