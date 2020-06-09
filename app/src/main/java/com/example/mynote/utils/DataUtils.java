package com.example.mynote.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Xml;
import android.widget.Filterable;

import com.example.mynote.model.NoteItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DataUtils {
    private final String DATA = "note.xml";

    public static String dateFromLong(long time)
    {
        DateFormat format=new SimpleDateFormat("EEE, dd MM yyyy 'at' hh:mm aaa");
        return format.format(new Date(time));
    }

    public ArrayList<NoteItem> getData(Context context) {
        if (FileUtils.checkFileExists(context, DATA)) {
            Log.d("NOTE_TEST", "File exists");
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = fac.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            FileInputStream fIn = null;
            try {
                fIn = context.openFileInput(DATA);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Document doc = null;
            try {
                doc = builder.parse(fIn);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            Element root = doc.getDocumentElement();
            NodeList list = root.getChildNodes();

            ArrayList<NoteItem> noteItemArrayList = new ArrayList<>();

            for (int i = 0; i < list.getLength(); i++) {
                Node node =list.item(i);
                NodeList nodeList = node.getChildNodes();

                String title = "";
                String timeAndID = "";
                String content = "";
                String color= Color.BLACK+"";
                String size =12+"";
                NoteItem.TYPE type = NoteItem.TYPE.NormalNote;

                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node noteCon =nodeList.item(j);
                    if (noteCon.getNodeName().equals("title")) {
                        title = noteCon.getTextContent();
                    }

                    if (noteCon.getNodeName().equals("time")) {
                        timeAndID = noteCon.getTextContent();
                    }

                    if (noteCon.getNodeName().equals("content")) {
                        content = noteCon.getTextContent();
                    }
                    if (noteCon.getNodeName().equals("color")) {
                        color = noteCon.getTextContent();
                    }
                    if (noteCon.getNodeName().equals("size")) {
                        size = noteCon.getTextContent();
                    }
                    if (noteCon.getNodeName().equals("type")) {
                        type = NoteItem.TYPE.valueOf(noteCon.getTextContent());
                    }
                }
                NoteItem item = new NoteItem(Long.parseLong(timeAndID), title, content,Integer.parseInt(color),Float.parseFloat(size),type);

                noteItemArrayList.add(item);
            }

            for (NoteItem noteItem : noteItemArrayList) {
                Log.d("NOTE_TEST", noteItem.getContent() + "/" + noteItem.getTitle() + "/" + noteItem.getTimeAndId());
            }

            return noteItemArrayList;
        }

        return null;
    }

    public boolean saveData(ArrayList<NoteItem> noteItemArrayList, Context context) {
        if (FileUtils.checkFileExists(context, DATA)) {
            Log.d("NOTE_TEST", "File exists");
        } else {
            Log.d("NOTE_TEST", "File not exists");
            if (FileUtils.createFile(context, DATA)) {
                Log.d("NOTE_TEST", "File created");
            } else {
                Log.d("NOTE_TEST", "File not created");
            }
        }
        try {
            FileOutputStream fileos= context.openFileOutput(DATA, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "userData");

            for (NoteItem item : noteItemArrayList) {
                xmlSerializer.startTag(null, "note");

                xmlSerializer.startTag(null, "title");
                xmlSerializer.text(item.getTitle());
                xmlSerializer.endTag(null, "title");

                xmlSerializer.startTag(null,"time");
                xmlSerializer.text(item.getTimeAndId() + "");
                xmlSerializer.endTag(null, "time");

                xmlSerializer.startTag(null,"content");
                xmlSerializer.text(item.getContent());
                xmlSerializer.endTag(null, "content");

                xmlSerializer.startTag(null,"color");
                xmlSerializer.text(item.getContentTextColor()+"");
                xmlSerializer.endTag(null, "color");

                xmlSerializer.startTag(null,"size");
                xmlSerializer.text(item.getContentTextSize()+"");
                xmlSerializer.endTag(null, "size");

                xmlSerializer.startTag(null,"type");
                xmlSerializer.text(item.getType().toString());
                xmlSerializer.endTag(null, "type");

                xmlSerializer.endTag(null, "note");
            }

            xmlSerializer.endTag(null, "userData");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
