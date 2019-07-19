package net.net.response;

public class BaseResponse
{
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String CreateResponse(int status, String message) {
//        StringBuilder response = new StringBuilder();
//        response.append("{");
//        response.append("\n");
//        response.append("\t");
//        response.append("\"status\" : ");
//        response.append(String.valueOf(status));
//        response.append(",");
//        response.append("\n");
//        response.append("\t");
//        response.append("\"message\" : \"");
//        response.append(message);
//        response.append("\"");
//        response.append("\n");
//        response.append("}");
//        return response.toString();
//    }

//    public String CreateFileResponse(String filename, String fileUrl, String fileType) {
//        StringBuilder response = new StringBuilder();
//        response.append("{");
//        response.append("\n");
//        response.append("\t");
//        response.append("\"fileName\" : \"");
//        response.append(filename);
//        response.append("\",");
//        response.append("\n");
//        response.append("\t");
//        response.append("\"fileUrl\" : \"");
//        response.append(fileUrl);
//        response.append("\",");
//        response.append("\n");
//        response.append("\t");
//        response.append("\"fileType\" : \"");
//        response.append(fileType);
//        response.append("\"");
//        response.append("\n");
//        response.append("}");
//        return response.toString();
//    }
}
