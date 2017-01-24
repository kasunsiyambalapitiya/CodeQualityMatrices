/*
 *  Copyright (c) Jan 17, 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.lang3.*;

public class GettingTheCommitIDFromPatchID {

    private String token;
    private String patchId;
    private String urlForObtainingCommitHashes,urlForObtainingPRs;

    protected final String location=System.getProperty("user.dir")+"/";           // to save the json output of the API call
    private String jsonOutPutFileOfCommits= "jsonOutPutFileCommits.json";
    private String jsonOutPutFileOfPRs= "jsonOutPutFilePRs.json";
    private String prHtmlUrlDetails;
    protected JSONParser parser= new JSONParser();
    private String patchInformation_svnRevisionpublic[];        // for saving the commit id of the patch
    protected String productID; 
    protected long prNumber;



    Scanner user_input= new Scanner(System.in);


    //----- modified for testing purposes------------------

    public String getToken() {
        return token;
    }

    //    public void setToken(String tokenName) {
    //        System.out.println("Enter the token for "+tokenName);
    //
    //        this.token = user_input.next();
    //    }


    //----------------------------------------------------------
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
        return urlForObtainingCommitHashes;
    }
    public void setURL(String uRL) {
        urlForObtainingCommitHashes = uRL;
    }

    public String getUrlForObtainingPRs() {
        return urlForObtainingPRs;
    }
    public void setUrlForObtainingPRs(String commitHash) {
        this.urlForObtainingPRs = "https://api.github.com/search/issues?q="+commitHash;
    }


    public void setData() throws IOException{




        System.out.println("Enter the patch id");

        setPatchId(user_input.next());

        setURL("http://umt.private.wso2.com:9765/codequalitymatricesapi/1.0.0//properties?path=/_system/governance/patchs/"+getPatchId());

        callingTheAPI(urlForObtainingCommitHashes,jsonOutPutFileOfCommits,true,false);

    }


    //=========== calling the relevant API and saving the output to a file===============================================

    public void  callingTheAPI(String URL, String file,boolean requireToken,boolean requireReview) throws IOException{

        BufferedReader bufferedReader= null;
        CloseableHttpClient httpclient= null;
        CloseableHttpResponse httpResponse= null;
        BufferedWriter bufferedWriter= null;


        //================ To do: 
        //                try(BufferedReader bufferedReader= new BufferedReader(new InputStreamReader (httpResponse.getEntity().getContent()))){
        //                    StringBuilder stringBuilder= new StringBuilder();
        //                    String line;
        //                    while((line=bufferedReader.readLine())!=null){
        //                        stringBuilder.append(line);
        //        
        //                    }
        //        
        //                    System.out.println(stringBuilder.toString()); 
        //        
        //        
        //                }


        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpGet= new HttpGet(URL);

            if(requireToken==true){

                httpGet.addHeader("Authorization","Bearer "+getToken());        // passing the token
            }

            //as the accept header is needed for the review API since it is still in preview mode   
            if(requireReview==true){
                httpGet.addHeader("Accept","application/vnd.github.black-cat-preview+json");

            }

            httpResponse=httpclient.execute(httpGet);
            int responseCode= httpResponse.getStatusLine().getStatusCode();
            System.out.println(responseCode);

            bufferedReader= new BufferedReader(new InputStreamReader (httpResponse.getEntity().getContent()));

            StringBuilder stringBuilder= new StringBuilder();
            String line;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);

            }

            System.out.println(stringBuilder.toString());

            bufferedWriter= new BufferedWriter(new FileWriter (location+file));
            bufferedWriter.write(stringBuilder.toString());
        } 

        catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally{
            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if (bufferedReader != null){
                bufferedReader.close();
            }

            if(httpResponse != null){
                httpResponse.close();
            }
            if (httpclient != null){
                httpclient.close();
            }


        }
    }

    //  ===================== getting the commit IDs from the above saved file ============================================

    public void getThePublicGitCommitId(){
        try{
            JSONArray jsonArray= (JSONArray)parser.parse(new FileReader(location+jsonOutPutFileOfCommits));

            for(int i=0; i<jsonArray.size();i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String tempName= (String)jsonObject.get("name");

                if(tempName.equals("patchInformation_svnRevisionpublic")){
                    JSONArray tempJSONArray= (JSONArray)jsonObject.get("value");

                    patchInformation_svnRevisionpublic= new String [tempJSONArray.size()];

                    for(int j =0; j< tempJSONArray.size();j++){
                        //initializing the patchInformation_svnRevisionpublic array


                        patchInformation_svnRevisionpublic[j]=(String)tempJSONArray.get(j);


                    }

                    break;
                }

            }

            System.out.println("The commit Ids are");


            //            for printing all the commits ID associated with a patch
            for (String tmp: patchInformation_svnRevisionpublic){
                System.out.println(tmp);
            }
            System.out.println();

        }
        catch(FileNotFoundException e){
            System.out.println("JSON file is not found");
            e. printStackTrace();


        }
        catch (ParseException e){
            System.out.println("Parse Execption occured");
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    public void obtainingThePRS() throws IOException{


        //        // setting the github token
        //        setToken("Github");



        for(String commitHash: patchInformation_svnRevisionpublic){

            setUrlForObtainingPRs(commitHash);

            //            setUrlForObtainingPRs("ada1ef315be4a53759ccc0d8efd61ac1346c673a");

            //calling the API calling method
            callingTheAPI(getUrlForObtainingPRs(),jsonOutPutFileOfPRs,true,false);
            savePRSInArray();
        }


    }

    //================================= saving the  PRs in the array===============================

    public void savePRSInArray(){
        try{
            JSONObject parentJsonObject= (JSONObject) parser.parse(new FileReader (location+jsonOutPutFileOfPRs));
            //            long x=(long)parentJsonObject.get("total_count");
            //            PRNo= new String [1];        // setting the length of the array
            JSONArray jsonArray= (JSONArray)parentJsonObject.get("items");

            for(int i =0 ;i< jsonArray.size();i++){

                JSONObject  prNoForCommitID=(JSONObject)jsonArray.get(i);
                String state=(String)prNoForCommitID.get("state");
                //                JSONObject assignee=(JSONObject)prNoForCommitID.get("assignee");
                //
                //
                //                String checkItIsAssigned= (prNoForCommitID.get("assignee")== null) ? "null": "notnull";
                //
                //                // this is because if it is not assigned it means that the PR is merged, if it is notnull then it is assigned to another for further modifications
                //
                //               if(state.equals("closed") & checkItIsAssigned.equals("null") ){

                if(state.equals("closed") ){

                    JSONObject prDetails=(JSONObject)prNoForCommitID.get("pull_request");
                    prHtmlUrlDetails= (String)prDetails.get("html_url");

                    // for debuging purposes to print the URL
                    //                    System.out.println(prHtmlUrlDetails);

                    prNumber=(long) prNoForCommitID.get("number");


                    printTheResults();

                }


            }
        }


        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public void printTheResults(){
        String part1= StringUtils.substringAfter(prHtmlUrlDetails,"/wso2/");
        System.out.println(part1);
        productID=StringUtils.substringBefore(part1, "/pull/");
        System.out.println("Product ID "+productID+" \t PR no "+ prNumber );



    }



}
