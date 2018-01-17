package dashboard.jmtechmind.com.jmdashboard.Utils;

/**
 * Created by Vishal on 4/01/2018.
 */
public class Webservices {

    /*this is host url from where all webservice call*/

    public static String WEB_HOST_URL = "http://10.10.10.53";

    public static String WEBSERVICE_BILL_SALE_GST = WEB_HOST_URL + "/jm_bill_sales_gst.php";

    public static String WEBSERVICE_CUST_BILL = WEB_HOST_URL + "/jm_bill_cusotmerlist.php";

    public static String WEBSERVICE_CUST_SALES = WEB_HOST_URL + "/jm_bill_cusotmer_sales_list.php";

    public static String WEBSERVICE_TENANT_STATUS = WEB_HOST_URL + "/tenantname.php?status=unpaid";

    public static String WEBSERVICE_VENDOR_LIST = WEB_HOST_URL + "/jmvendorstat.php";

    public static String WEBSERVICE_BILL_PURCHASE_GST = WEB_HOST_URL + "/jm_bill_purchase_gst.php";

    public static String WEBSERVICE_TENANT_PAID_UNPAIS = WEB_HOST_URL + "/tenant_paid_unpaid.php";


}