package com.ra.demo.config;

import org.apache.plc4x.java.eip.readwrite.types.CIPDataTypeCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlcTag {
    private static final String REGEX ="\\[(\\d+)\\]";
    private String name;
    private int length;
    private String datatype;

    private Map<String,String> influxTags = new HashMap<String,String>();

    public PlcTag(String name, int length,String datatype,Map influxTags) {
        this.name = name;
        this.length = length;
        this.datatype = datatype;
        this.influxTags = influxTags;
    }

    public String getItemName() {
        return "%" + name + validDataType(datatype)+ ":" + length;}

    public String getItemName(boolean debug) {
        if (debug) {
            return "%" + name + validDataType(datatype) + ":1";
        } else {
            return getItemName();
        }
    }
    public String getName() {
        return name;
    }

    public int getLength() {
	    return length;
    }
    public Map<String, String> getInfluxTags() {
        return influxTags;
    }
    List<PlcTag> flat(){
        ArrayList<PlcTag> newPlcTags = new ArrayList<PlcTag>();
        for(int i = 0;i < length;i++) {
            newPlcTags.add(new PlcTag(changeArrayIndex(name,i),1,datatype,influxTags));
        }
        return newPlcTags;
    }
    private String changeArrayIndex(String tagName,int index){
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(tagName); // 获取 matcher 对象
        if (m.find( )) {
            // group 1 will be the original index
            int replace = Integer.parseInt(m.group(1)) + index;
            StringBuffer str = new StringBuffer();
            m.appendReplacement(str,"[" + String.valueOf(replace) + "]");
            return m.appendTail(str).toString();
        } else {
            return null;
        }
    }

    private String validDataType(String datatype){
        if (CIPDataTypeCode.valueOf(datatype) != null){
            return ":" + datatype;
        }
        return "";
    }

    @Override
    public String toString() {
        return "\n" + this.getItemName() + ",influxTags = " + influxTags.toString();
    }
}
