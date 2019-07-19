package net.net.response;

import net.net.entity.File;

public class FileResponse extends BaseResponse {
    private File file;

    public FileResponse(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
