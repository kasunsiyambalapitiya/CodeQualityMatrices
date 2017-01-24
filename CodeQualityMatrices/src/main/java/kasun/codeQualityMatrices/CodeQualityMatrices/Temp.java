/*
 *  Copyright (c) Jan 18, 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */



package kasun.codeQualityMatrices.CodeQualityMatrices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Temp {


    private String token;
    private String patchId;
    private String URL;




    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getPatchId() {
        return patchId;
    }
    public void setPatchId(String patchId) {
        this.patchId = patchId;
    }
    public String getURL() {
        return URL;
    }
    public void setURL(String uRL) {
        URL = uRL;
    }


    public void setData(){
        Scanner user_input= new Scanner(System.in);
        System.out.println("Enter the token");

        setToken(user_input.next());

        System.out.println("Enter the patch id");

        setPatchId(user_input.next());

        setURL("http://umt.private.wso2.com:9765/resource/1.0.0//properties?path=/_system/governance/patchs/"+getPatchId());



    }

    public void closeResources() throws ClientProtocolException, IOException{



        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet= new HttpGet(URL);
        httpGet.addHeader("Authorization","Bearer "+getToken());

        CloseableHttpResponse httpResponse=httpclient.execute(httpGet);
        int responseCode= httpResponse.getStatusLine().getStatusCode();
        System.out.println(responseCode);

//
//        try(BufferedReader bufferedReader= new BufferedReader(new InputStreamReader (httpResponse.getEntity().getContent()))){
//            StringBuilder stringBuilder= new StringBuilder();
//            String line;
//            while((line=bufferedReader.readLine())!=null){
//                stringBuilder.append(line);
//
//            }
//
//            System.out.println(stringBuilder.toString()); 
//
//
//        } catch (UnsupportedOperationException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
//
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }




    }

}
