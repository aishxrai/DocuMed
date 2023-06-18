package in.aishxrai.documed.model;

public class Document
{

    String documentType;
    String hospitalName;
    String date;
    String pdfUrl;


    public Document(String documentType, String hospitalName, String date, String pdfUrl) {
        this.documentType = documentType;
        this.hospitalName = hospitalName;
        this.date = date;
        this.pdfUrl = pdfUrl;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public String getDate() {
        return date;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }


}
