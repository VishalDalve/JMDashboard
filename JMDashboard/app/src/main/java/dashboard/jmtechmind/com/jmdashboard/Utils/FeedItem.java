package dashboard.jmtechmind.com.jmdashboard.Utils;

/**
 * Created by jmtec on 12/8/2017.
 */

public class FeedItem {
    public String paid;
    public String unpaid;

    public String date;
    public String total;
    public String paid_un_paid;
    public String is_viewed;
    public String proprietor_name1;
    public String admin_name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaid_un_paid() {
        return paid_un_paid;
    }

    public void setPaid_un_paid(String paid_un_paid) {
        this.paid_un_paid = paid_un_paid;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }

    public String getProprietor_name1() {
        return proprietor_name1;
    }

    public void setProprietor_name1(String proprietor_name1) {
        this.proprietor_name1 = proprietor_name1;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }


    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(String unpaid) {
        this.unpaid = unpaid;
    }


}
