package kasun.codeQualityMatrices.CodeQualityMatrices;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        GettingTheInfoOnPrReview object= new GettingTheInfoOnPrReview();
//       object.setToken("PMT");
        object.setToken("tQU5vxzrGeBpLMQuwOsJW_fyYLYa");
       object.setData();
       
       object.getThePublicGitCommitId();
       object.setToken("8ab9c39691a9b85aecc43d0106e9684ad7437ac6");
       object.obtainingThePRS();
       
       
       System.out.println("second part");
//       GettingTheInfoOnPrReview objectReview = new GettingTheInfoOnPrReview();
       object.setURLs();
       object.getReviewsForPR();
       object.getAuthorOfPR();
       object.printResults();
       object.checkPRMergedOrNot();
    }
}
