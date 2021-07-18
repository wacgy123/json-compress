import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class JsonCompressUtil {

    public static JSONArray compress(Object o){
        JSONArray result = new JSONArray();
        if (o instanceof JSONArray){
            JSONArray jsonArray=(JSONArray) o;
            return hpack(jsonArray,result);
        }
        return null;
    }

    private static JSONArray hpack(JSONArray jsonArray,JSONArray result){
        int max=0,index=0;
        Set<String> headers=null;
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item=jsonArray.get(i);
            if (item instanceof Integer||item instanceof Float||item instanceof String){
                result.add(item);
            }
            else if (item instanceof JSONObject){
                JSONObject jsonObject = (JSONObject) item;
                Set<String> keySet = jsonObject.keySet();
                if (headers!=null&&headers.containsAll(keySet)){
                    Collection<Object> values = jsonObject.values();
                    ((JSONArray)result.get(0)).add(values);
                    continue;
                }
                if (keySet.size()>max){
                    max= keySet.size();
//                    JSONObject object = (JSONObject) jsonArray.get(i);
                    headers = jsonObject.keySet();
                    Collection<Object> values = jsonObject.values();
                    JSONArray compressed=new JSONArray();
                    JSONArray array=new JSONArray(new ArrayList<>(values));
                    JSONArray compress = hpack(array,new JSONArray());
                    compressed.add(compress);

                    compressed.add(new ArrayList<>(headers));
//                    compressed.add(values);
                    result.add(compressed);

//                    result.add(new ArrayList<>(headers));
//                    result.add(values);
                }
            }else if (item instanceof JSONArray){
                JSONArray item1 = (JSONArray) item;
                JSONArray compress = hpack(item1,new JSONArray());
//                System.out.println(i);
//                System.out.println(compress.toJSONString());
                result.add(compress);
            }
        }
        return result;

//        if (jsonArray.get(index) instanceof JSONObject) {
//            JSONObject jsonObject = (JSONObject) jsonArray.get(index);
//            Set<String> headers = jsonObject.keySet();
//            Collection<Object> values = jsonObject.values();
//            JSONArray compressed=new JSONArray();
//            compressed.add(headers);
//            compressed.addAll(values);
//            result.add(compressed);
//            return result;
//        } else {
//            JSONArray compressed=new JSONArray();
//            compressed.add(jsonArray.get(index));
//            result.add(compressed);
//            return result;
//        }
    }
    private Set<String> getHeaders(JSONArray jsonArray){
        return null;
    }

    public static void main(String[] args) {
        JSONArray jsonArray=JSON.parseArray("[{\"a\":5,\"b\":{\"c\":\"d\"}},{\"a\":4,\"b\":{\"d\":\"e\"}},{\"a\":3,\"b\":{\"h\":\"g\"}},[6],5,\"z\",[{\"f\":4,\"g\":{\"j\":\"h\"}}]]");
        System.out.println(jsonArray.toJSONString());
        System.out.println(compress(jsonArray).toJSONString());
    }
}
